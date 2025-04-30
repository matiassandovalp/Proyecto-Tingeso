package kartbackend.services;

import kartbackend.entities.BloqueHorarioEntity;
import kartbackend.repositories.BloqueHorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RackServiceTest {

    @Mock
    private BloqueHorarioRepository bloqueHorarioRepository;

    @InjectMocks
    private RackService rackService;

    private SimpleDateFormat sdf;

    @BeforeEach
    public void setup() {
        // Usamos el formato de fecha "yyyy-MM-dd" para las pruebas
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    public void testObtenerRackPorRango_Empty() {
        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        when(bloqueHorarioRepository.findByFechaBetween(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        List<Map<String, Object>> eventos = rackService.obtenerRackPorRango(fechaInicio, fechaFin);
        assertNotNull(eventos, "La lista de eventos no debe ser nula");
        assertTrue(eventos.isEmpty(), "La lista de eventos debe estar vacía cuando no se encuentran bloques");
    }

    @Test
    public void testObtenerRackPorRango_WithBloques() throws ParseException {
        Date fecha = sdf.parse("2022-06-15");

        BloqueHorarioEntity bloque = new BloqueHorarioEntity();
        bloque.setId(100);  // Usar 100 en lugar de 100L, ya que setId espera int
        bloque.setFecha(fecha);
        bloque.setHoraInicio("10:00");
        bloque.setHoraFin("11:00");
        bloque.setKartsOcupados(Arrays.asList("K001", "K002"));

        List<BloqueHorarioEntity> bloques = Arrays.asList(bloque);
        when(bloqueHorarioRepository.findByFechaBetween(any(Date.class), any(Date.class)))
                .thenReturn(bloques);

        List<Map<String, Object>> eventos = rackService.obtenerRackPorRango(fecha, fecha);
        assertNotNull(eventos, "La lista de eventos no debe ser nula");
        assertEquals(1, eventos.size(), "Debe retornar un evento");

        Map<String, Object> evento = eventos.get(0);
        assertEquals(100, evento.get("id"), "El ID del evento debe ser 100");
        assertEquals("2022-06-15T10:00", evento.get("start"), "El campo 'start' no coincide con el esperado");
        assertEquals("2022-06-15T11:00", evento.get("end"), "El campo 'end' no coincide con el esperado");
        assertEquals("Kart: K001, K002", evento.get("title"), "El título no se generó correctamente");
    }
}