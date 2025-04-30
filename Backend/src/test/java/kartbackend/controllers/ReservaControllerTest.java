package kartbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kartbackend.entities.ClientEntity;
import kartbackend.entities.ReservaEntity;
import kartbackend.repositories.ClientRepository;
import kartbackend.services.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private ClientRepository clientRepository;

    private ClientEntity client;

    @BeforeEach
    public void setup() {
        client = new ClientEntity();
        client.setClientId("C001");
        // Se pueden asignar más propiedades al cliente si es necesario
    }

    // Test para el endpoint GET /api/reservas
    @Test
    public void testObtenerReservas() throws Exception {
        // Preparamos una lista de reservas simulada
        ReservaEntity reserva = new ReservaEntity();
        reserva.setReservaId(1);
        reserva.setCantPersonas(3);
        List<ReservaEntity> reservaList = Collections.singletonList(reserva);
        when(reservaService.obtenerTodasLasReservas()).thenReturn(reservaList);

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservaId").value(1))
                .andExpect(jsonPath("$[0].cantPersonas").value(3));
    }

    // Test para una solicitud POST válida a /api/reservas
    @Test
    public void testAgregarReserva_Success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("cantPersonas", 3);
        request.put("nombresPersonas", Arrays.asList("Juan", "Maria", "Pedro"));
        request.put("fecha", "2025-04-29T10:00");
        request.put("tarifaSeleccionada", "15_vueltas_15_min");
        request.put("esCumpleaños", false);
        request.put("clienteId", "C001");
        request.put("kartIds", Arrays.asList("K001", "K002"));

        // Configuramos el stub para que el cliente se encuentre
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        // Simulamos que el servicio crea la reserva
        ReservaEntity createdReserva = new ReservaEntity();
        createdReserva.setReservaId(1);
        createdReserva.setCantPersonas(3);
        when(reservaService.crearReserva(any(ReservaEntity.class))).thenReturn(createdReserva);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservaId").value(1))
                .andExpect(jsonPath("$.cantPersonas").value(3));
    }

    // Test para error de formato de fecha incorrecto
    @Test
    public void testAgregarReserva_InvalidDateFormat() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("cantPersonas", 3);
        request.put("nombresPersonas", Arrays.asList("Juan", "Maria", "Pedro"));
        // Fecha mal formateada (no sigue el patrón "yyyy-MM-dd'T'HH:mm")
        request.put("fecha", "2025-04-29 10:00");
        request.put("tarifaSeleccionada", "15_vueltas_15_min");
        request.put("esCumpleaños", false);
        request.put("clienteId", "C001");
        request.put("kartIds", Arrays.asList("K001", "K002"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fecha en formato incorrecto, se espera: yyyy-MM-dd'T'HH:mm"));
    }

    // Test para cuando falta un campo obligatorio (ej., falta 'cantPersonas')
    @Test
    public void testAgregarReserva_MissingRequiredField() throws Exception {
        Map<String, Object> request = new HashMap<>();
        // No se asigna "cantPersonas"
        request.put("nombresPersonas", Arrays.asList("Juan"));
        request.put("fecha", "2025-04-29T10:00");
        request.put("tarifaSeleccionada", "15_vueltas_15_min");
        request.put("esCumpleaños", false);
        request.put("clienteId", "C001");
        request.put("kartIds", Arrays.asList("K001"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La cantidad de personas es obligatoria y debe ser mayor que cero."));
    }

    // Test para cuando no se encuentra el cliente (retornando error 404 y mensaje "Cliente no encontrado")
    @Test
    public void testAgregarReserva_InvalidClient() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("cantPersonas", 3);
        request.put("nombresPersonas", Arrays.asList("Juan", "Maria", "Pedro"));
        request.put("fecha", "2025-04-29T10:00");
        request.put("tarifaSeleccionada", "15_vueltas_15_min");
        request.put("esCumpleaños", false);
        request.put("clienteId", "C999");
        request.put("kartIds", Arrays.asList("K001", "K002"));

        // Simulamos que el cliente no se encuentra
        when(clientRepository.findById("C999")).thenReturn(Optional.empty());

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado"));
    }
}