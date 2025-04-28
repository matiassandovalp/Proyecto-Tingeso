package kartbackend.services;

import kartbackend.entities.ClientEntity;
import kartbackend.entities.ComprobanteEntity;
import kartbackend.entities.ReservaEntity;
import kartbackend.entities.BloqueHorarioEntity;
import kartbackend.repositories.ComprobanteRepository;
import kartbackend.repositories.KartRepository;
import kartbackend.repositories.ClientRepository;
import kartbackend.repositories.ReservaRepository;
import kartbackend.repositories.BloqueHorarioRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final TarifasService tarifasService;
    private final KartRepository kartRepository;
    private final ClientRepository clientRepository;
    private final BloqueHorarioRepository bloqueHorarioRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ComprobanteRepository comprobanteRepository,
                          TarifasService tarifasService,
                          KartRepository kartRepository,
                          ClientRepository clientRepository,
                          BloqueHorarioRepository bloqueHorarioRepository) {
        this.reservaRepository = reservaRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.tarifasService = tarifasService;
        this.kartRepository = kartRepository;
        this.clientRepository = clientRepository;
        this.bloqueHorarioRepository = bloqueHorarioRepository;
    }

    public ReservaEntity crearReserva(ReservaEntity reserva) {
        // Validación del cliente
        if (reserva.getCliente() == null || reserva.getCliente().getClientId() == null) {
            throw new IllegalArgumentException("Cliente inválido");
        }
        ClientEntity cliente = clientRepository.findById(reserva.getCliente().getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        reserva.setCliente(cliente);

        // --- Lógica del rack: Determinar el bloque de ocupación ---
        // Se obtiene la duración del bloque de acuerdo a la tarifa
        int duracionReserva = obtenerDuracionReserva(reserva.getTarifaSeleccionada());
        if (duracionReserva == 0) throw new IllegalArgumentException("Tarifa inválida");

        // Suponemos que reserva.getFecha() incluye la hora de inicio (por ejemplo, "2025-04-29 10:00")
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reserva.getFecha());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaInicio = sdf.format(reserva.getFecha());
        calendar.add(Calendar.MINUTE, duracionReserva);
        String horaFin = sdf.format(calendar.getTime());

        // Verificar que ninguno de los karts solicitados ya esté reservado en un bloque que solape.
        // La condición de solapamiento se determina buscando bloques en la misma fecha cuyo
        // horaInicio es menor que el nuevo bloque’s horaFin y cuyo horaFin es mayor que el nuevo bloque’s horaInicio.
        List<BloqueHorarioEntity> bloquesExistentes =
                bloqueHorarioRepository.findByFechaAndHoraInicioLessThanAndHoraFinGreaterThan(reserva.getFecha(), horaFin, horaInicio);
        for (BloqueHorarioEntity bloque : bloquesExistentes) {
            for (String kartId : reserva.getKartIds()) {
                if (bloque.getKartsOcupados() != null && bloque.getKartsOcupados().contains(kartId)) {
                    throw new IllegalArgumentException("El Kart " + kartId + " ya está ocupado en ese horario.");
                }
            }
        }
        // --- Fin de la validación del rack ---

        // Guardar la reserva
        ReservaEntity nuevaReserva = reservaRepository.save(reserva);

        // Registrar bloques de ocupación para cada kart de la reserva
        for (String kartId : reserva.getKartIds()) {
            BloqueHorarioEntity bloque = new BloqueHorarioEntity();
            bloque.setFecha(reserva.getFecha());
            bloque.setHoraInicio(horaInicio);
            bloque.setHoraFin(horaFin);
            // Cada bloque registra la ocupación de un kart
            bloque.setKartsOcupados(List.of(kartId));
            bloque.setReservaAsociada(nuevaReserva);
            bloqueHorarioRepository.save(bloque);
        }

        // --- Creación del comprobante (lógica ya existente) ---
        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setReserva(nuevaReserva);
        comprobante.setFechaEmision(new Date());

        int precioBase = tarifasService.obtenerTarifa(nuevaReserva.getTarifaSeleccionada());
        double descuentoGrupo = tarifasService.calcularDescuentoGrupo(nuevaReserva.getCantPersonas());
        double descuentoFrecuencia = tarifasService.calcularDescuentoFrecuencia(nuevaReserva.getCliente().getVisitasMensuales());

        double descuentoTotal = descuentoGrupo + descuentoFrecuencia;
        if (nuevaReserva.isEsCumpleaños()) descuentoTotal += 0.50;
        descuentoTotal = Math.min(descuentoTotal, 0.50);

        int precioAjustado = (int) (precioBase * (1 - descuentoTotal));
        int valorIVA = (int) (precioAjustado * 0.19);
        int precioFinal = precioAjustado + valorIVA;

        comprobante.setPrecioEstandar(precioBase);
        comprobante.setDescuentoPersonas((int) (precioBase * descuentoGrupo));
        comprobante.setDescuentoPersonal((int) (precioBase * descuentoFrecuencia));
        comprobante.setPrecioAjustado(precioAjustado);
        comprobante.setValorIVA(valorIVA);
        comprobante.setPrecioFinal(precioFinal);

        // Extra: calcular número de vueltas a partir del nombre de la tarifa
        int numeroVueltas = obtenerNumeroVueltas(nuevaReserva.getTarifaSeleccionada());
        comprobante.setNumeroVueltas(numeroVueltas);

        comprobanteRepository.save(comprobante);

        nuevaReserva.setComprobante(comprobante);
        return reservaRepository.save(nuevaReserva);
    }

    // Método auxiliar para parsear número de vueltas
    private int obtenerNumeroVueltas(String tarifaSeleccionada) {
        if (tarifaSeleccionada == null) return 0;
        if (tarifaSeleccionada.startsWith("10_vueltas")) return 10;
        if (tarifaSeleccionada.startsWith("15_vueltas")) return 15;
        if (tarifaSeleccionada.startsWith("20_vueltas")) return 20;
        return 0; // Valor por defecto
    }

    // Método auxiliar para determinar la duración de la reserva (en minutos) según la tarifa
    private int obtenerDuracionReserva(String tarifaSeleccionada) {
        if (tarifaSeleccionada == null) return 0;
        switch (tarifaSeleccionada) {
            case "10_vueltas_10_min":
                return 30;
            case "15_vueltas_15_min":
                return 35;
            case "20_vueltas_20_min":
                return 40;
            default:
                return 0;
        }
    }

    public List<ReservaEntity> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }
}