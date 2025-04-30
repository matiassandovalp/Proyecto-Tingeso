package kartbackend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kartbackend.entities.BloqueHorarioEntity;
import kartbackend.entities.ClientEntity;
import kartbackend.entities.ComprobanteEntity;
import kartbackend.entities.ReservaEntity;
import kartbackend.repositories.BloqueHorarioRepository;
import kartbackend.repositories.ClientRepository;
import kartbackend.repositories.ComprobanteRepository;
import kartbackend.repositories.KartRepository;
import kartbackend.repositories.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ComprobanteRepository comprobanteRepository;
    @Mock
    private TarifasService tarifasService;
    @Mock
    private KartRepository kartRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private BloqueHorarioRepository bloqueHorarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    private ClientEntity client;

    @BeforeEach
    public void setUp() {
        client = new ClientEntity();
        client.setClientId("C001");
        client.setVisitasMensuales(3);
    }

    // Test para cliente nulo.
    @Test
    public void testCrearReserva_InvalidClient() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCliente(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });
        assertEquals("Cliente inválido", exception.getMessage());
    }

    // Test para cantidad de nombres inválida.
    @Test
    public void testCrearReserva_InvalidNombres() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCliente(client);
        reserva.setCantPersonas(3);
        // Se proporcionan solo 2 nombres en vez de 3.
        reserva.setNombresPersonas(Arrays.asList("Juan", "Maria"));

        // Aseguramos que el cliente se encuentre.
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });
        assertEquals("La cantidad de nombres debe coincidir con la cantidad de personas.", exception.getMessage());
    }

    // Test para tarifa inválida (que provoca duración 0).
    @Test
    public void testCrearReserva_InvalidTarifa() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCliente(client);
        reserva.setCantPersonas(1);
        reserva.setNombresPersonas(Arrays.asList("Juan"));
        reserva.setFecha(new Date());
        reserva.setTarifaSeleccionada("INVALID_TARIFF");  // No coincide con ningún caso.
        reserva.setKartIds(Arrays.asList("K001"));

        // Stub para el cliente.
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });
        assertEquals("Tarifa inválida", exception.getMessage());
    }

    // Test para conflicto: el kart ya está ocupado en el horario.
    @Test
    public void testCrearReserva_KartAlreadyOccupied() throws Exception {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCliente(client);
        reserva.setCantPersonas(1);
        reserva.setNombresPersonas(Arrays.asList("Juan"));
        Date fecha = new Date();
        reserva.setFecha(fecha);
        reserva.setTarifaSeleccionada("10_vueltas_10_min"); // Tarifa válida: duración = 30 minutos.
        reserva.setKartIds(Arrays.asList("K001"));

        // Stub para que el cliente se encuentre.
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        // Calcular hora de inicio y hora de fin.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaInicio = sdf.format(fecha);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.MINUTE, 30);
        String horaFin = sdf.format(cal.getTime());

        // Simulamos que ya existe un bloqueo con el kart "K001" ocupado.
        BloqueHorarioEntity bloque = new BloqueHorarioEntity();
        bloque.setKartsOcupados(Arrays.asList("K001"));
        when(bloqueHorarioRepository.findByFechaAndHoraInicioLessThanAndHoraFinGreaterThan(
                eq(fecha), eq(horaFin), eq(horaInicio)))
                .thenReturn(Collections.singletonList(bloque));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva(reserva);
        });
        assertEquals("El Kart K001 ya está ocupado en ese horario.", exception.getMessage());
    }

    // Test para creación exitosa de reserva y cálculo del comprobante.
    @Test
    public void testCrearReserva_Success() {
        ReservaEntity reserva = new ReservaEntity();
        reserva.setCantPersonas(4);
        reserva.setNombresPersonas(Arrays.asList("Ana", "Luis", "Carlos", "Marta"));
        Date fecha = new Date();
        reserva.setFecha(fecha);
        reserva.setTarifaSeleccionada("15_vueltas_15_min");
        reserva.setEsCumpleaños(false);
        reserva.setKartIds(Arrays.asList("K001", "K002"));
        reserva.setCliente(client);

        // Stub: el cliente se encuentra.
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        // Simulamos que no hay bloqueos conflictivos.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaInicio = sdf.format(fecha);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.MINUTE, 35); // Para "15_vueltas_15_min".
        String horaFin = sdf.format(cal.getTime());
        when(bloqueHorarioRepository.findByFechaAndHoraInicioLessThanAndHoraFinGreaterThan(
                eq(fecha), eq(horaFin), eq(horaInicio)))
                .thenReturn(Collections.emptyList());

        // Stub para tarifas.
        when(tarifasService.obtenerTarifa("15_vueltas_15_min")).thenReturn(20000);
        when(tarifasService.calcularDescuentoGrupo(4)).thenReturn(0.10);
        when(tarifasService.calcularDescuentoFrecuencia(client.getVisitasMensuales())).thenReturn(0.10);

        // Simulamos que al guardar la reserva se asigna un ID.
        ReservaEntity reservaSaved = new ReservaEntity();
        reservaSaved.setReservaId(1);
        reservaSaved.setCantPersonas(reserva.getCantPersonas());
        reservaSaved.setNombresPersonas(reserva.getNombresPersonas());
        reservaSaved.setFecha(fecha);
        reservaSaved.setTarifaSeleccionada(reserva.getTarifaSeleccionada());
        reservaSaved.setEsCumpleaños(reserva.isEsCumpleaños());
        reservaSaved.setKartIds(reserva.getKartIds());
        reservaSaved.setCliente(reserva.getCliente());
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaSaved);

        // Calcular valores del comprobante:
        int precioTotalBase = 4 * 20000; // 80000
        double descuentoGrupo = 0.10;
        double descuentoFrecuencia = 0.10;
        double descuentoTotal = descuentoGrupo + descuentoFrecuencia; // 0.20 (sin adicional de cumpleaños)
        descuentoTotal = Math.min(descuentoTotal, 0.50);
        int precioAjustado = (int) (precioTotalBase * (1 - descuentoTotal)); // 80000 * 0.80 = 64000
        int valorIVA = (int) (precioAjustado * 0.19); // 12160
        int precioFinal = precioAjustado + valorIVA; // 64000 + 12160 = 76160

        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setPrecioEstandar(precioTotalBase);
        comprobante.setDescuentoPersonas((int) (precioTotalBase * descuentoGrupo));
        comprobante.setDescuentoPersonal((int) (precioTotalBase * descuentoFrecuencia));
        comprobante.setPrecioAjustado(precioAjustado);
        comprobante.setValorIVA(valorIVA);
        comprobante.setPrecioFinal(precioFinal);
        comprobante.setNumeroVueltas(15); // Según la lógica: tarifa "15_vueltas_15_min"

        when(comprobanteRepository.save(any(ComprobanteEntity.class))).thenReturn(comprobante);

        // Ejecutar el método a probar.
        ReservaEntity result = reservaService.crearReserva(reserva);

        // Verificar resultados.
        assertNotNull(result, "La reserva creada no debe ser nula");
        assertEquals(1, result.getReservaId(), "El ID de la reserva debe ser 1");
        assertNotNull(result.getComprobante(), "El comprobante debe estar asignado en la reserva");
        assertEquals(precioFinal, result.getComprobante().getPrecioFinal(), "El precio final del comprobante no es correcto");
        assertEquals(15, result.getComprobante().getNumeroVueltas(), "El número de vueltas no es correcto");

        // Verificar que se haya buscado al cliente exactamente una vez.
        verify(clientRepository, times(1)).findById("C001");

        // Verificar que se crearon bloques de ocupación para cada kart (2 en este caso).
        verify(bloqueHorarioRepository, times(2)).save(any(BloqueHorarioEntity.class));
    }
}