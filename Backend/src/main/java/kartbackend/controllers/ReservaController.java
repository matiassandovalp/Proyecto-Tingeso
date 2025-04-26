package kartbackend.controllers;

import kartbackend.entities.ReservaEntity;
import kartbackend.entities.ClientEntity;
import kartbackend.entities.ComprobanteEntity;
import kartbackend.repositories.ReservaRepository;
import kartbackend.repositories.ComprobanteRepository;
import kartbackend.repositories.ClientRepository;
import kartbackend.services.TarifasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final ClientRepository clientRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final TarifasService tarifasService;

    public ReservaController(ReservaRepository reservaRepository,
                             ClientRepository clientRepository,
                             ComprobanteRepository comprobanteRepository,
                             TarifasService tarifasService) {
        this.reservaRepository = reservaRepository;
        this.clientRepository = clientRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.tarifasService = tarifasService;
    }

    // Obtener todas las reservas
    @GetMapping
    public List<ReservaEntity> obtenerReservas() {
        return reservaRepository.findAll();
    }

    // Insertar reserva con comprobante generado automáticamente
    @PostMapping
    public ResponseEntity<?> agregarReserva(@RequestBody ReservaEntity reserva) {
        if (reserva.getCliente() == null || reserva.getCliente().getClientId() == null) {
            return ResponseEntity.badRequest().body("Error: Cliente no válido");
        }

        Optional<ClientEntity> clienteExistente = clientRepository.findById(reserva.getCliente().getClientId());
        if (!clienteExistente.isPresent()) {
            return ResponseEntity.badRequest().body("Error: El cliente no existe en la base de datos");
        }

        reserva.setCliente(clienteExistente.get());

        // Guardamos primero la reserva para que tenga un ID válido
        ReservaEntity nuevaReserva = reservaRepository.save(reserva);

        // Generacion del comprobante con la reserva ya persistida
        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setReserva(nuevaReserva);
        comprobante.setFechaEmision(new Date());
        comprobante.setNumeroVueltas(10);

        int precioEstandar = tarifasService.calcularTarifaBase(comprobante.getNumeroVueltas());
        comprobante.setPrecioEstandar(precioEstandar);

        int descuentoPersonas = (int) (precioEstandar * tarifasService.calcularDescuentoGrupo(nuevaReserva.getCantPersonas()));
        comprobante.setDescuentoPersonas(descuentoPersonas);

        int descuentoFrecuencia = (int) (precioEstandar * tarifasService.calcularDescuentoFrecuencia(nuevaReserva.getCliente().getVisitasMensuales()));
        comprobante.setDescuentoPersonal(descuentoFrecuencia);

        comprobante.setPrecioAjustado(precioEstandar - descuentoPersonas - descuentoFrecuencia);
        comprobante.setValorIVA((int) (comprobante.getPrecioAjustado() * 0.19));

        int precioFinal = tarifasService.calcularPrecioFinal(
                comprobante.getNumeroVueltas(),
                nuevaReserva.getCantPersonas(),
                nuevaReserva.getCliente().getVisitasMensuales(),
                nuevaReserva.isEsCumpleaños());

        comprobante.setPrecioFinal(precioFinal);

        // Guardamos el comprobante en la base de datos
        comprobanteRepository.save(comprobante);

        // Asociamos el comprobante a la reserva ya guardada y actualizamos la reserva
        nuevaReserva.setComprobante(comprobante);
        reservaRepository.save(nuevaReserva);

        return ResponseEntity.ok(nuevaReserva);
    }

    @Transactional
    @PutMapping("/generar-comprobantes")
    public ResponseEntity<?> generarComprobantesParaReservas() {
        List<ReservaEntity> reservas = reservaRepository.findAll();

        for (ReservaEntity reserva : reservas) {
            // Si la reserva no tiene comprobante, generamos uno
            if (reserva.getComprobante() == null) {
                ComprobanteEntity comprobante = new ComprobanteEntity();
                comprobante.setReserva(reserva);
                comprobante.setFechaEmision(new Date());
                comprobante.setNumeroVueltas(10);
                comprobante.setPrecioFinal(tarifasService.calcularPrecioFinal(
                        comprobante.getNumeroVueltas(),
                        reserva.getCantPersonas(),
                        reserva.getCliente().getVisitasMensuales(),
                        reserva.isEsCumpleaños()));

                comprobanteRepository.save(comprobante);
                reserva.setComprobante(comprobante);
                reservaRepository.save(reserva);
            }
        }

        return ResponseEntity.ok("Comprobantes generados correctamente para reservas sin comprobante.");
    }

    // Eliminar reserva por ID
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.getKartIds().clear();
        reserva.setComprobante(null);

        reservaRepository.delete(reserva);
        return ResponseEntity.noContent().build();
    }

    // Eliminar todas las reservas sin errores de restricciones de clave foránea
    @Transactional
    @DeleteMapping("/eliminar-todos")
    public ResponseEntity<Void> eliminarTodasLasReservas() {
        List<ReservaEntity> reservas = reservaRepository.findAll();

        for (ReservaEntity reserva : reservas) {
            reserva.getKartIds().clear();
            reserva.setComprobante(null);
        }

        reservaRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}