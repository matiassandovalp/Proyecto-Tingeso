package kartbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kartbackend.entities.ClientEntity;
import kartbackend.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObtenerClientes() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setClientId("C001");
        List<ClientEntity> clients = Collections.singletonList(client);
        when(clientRepository.findAll()).thenReturn(clients);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value("C001"));
    }

    @Test
    public void testObtenerClientePorId_Found() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setClientId("C001");
        when(clientRepository.findById("C001")).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clientes/C001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("C001"));
    }

    @Test
    public void testObtenerClientePorId_NotFound() throws Exception {
        when(clientRepository.findById("C002")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/C002"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAgregarClientes_InvalidEmptyList() throws Exception {
        mockMvc.perform(post("/api/clientes")
                        .content("[]")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La lista de clientes no puede estar vacía"));
    }

    @Test
    public void testAgregarClientes_Valid() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setClientId("C001");

        List<ClientEntity> clients = Collections.singletonList(client);
        when(clientRepository.saveAll(any(List.class))).thenReturn(clients);

        String json = objectMapper.writeValueAsString(clients);
        mockMvc.perform(post("/api/clientes")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value("C001"));
    }

    @Test
    public void testEliminarTodosLosClientes() throws Exception {
        doNothing().when(clientRepository).deleteAll();
        mockMvc.perform(delete("/api/clientes/eliminar-todos"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarCliente() throws Exception {
        doNothing().when(clientRepository).deleteById("C001");
        mockMvc.perform(delete("/api/clientes/C001"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testActualizarCliente_Found() throws Exception {
        ClientEntity existingClient = new ClientEntity();
        existingClient.setClientId("C001");
        existingClient.setNombreCliente("Old Name");
        existingClient.setVisitasMensuales(2);

        ClientEntity updatedClient = new ClientEntity();
        updatedClient.setClientId("C001");
        updatedClient.setNombreCliente("New Name");
        updatedClient.setVisitasMensuales(5);

        when(clientRepository.findById("C001")).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(updatedClient);

        String json = objectMapper.writeValueAsString(updatedClient);
        mockMvc.perform(put("/api/clientes/C001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCliente").value("New Name"))
                .andExpect(jsonPath("$.visitasMensuales").value(5));
    }

    @Test
    public void testActualizarCliente_NotFound() throws Exception {
        ClientEntity updatedClient = new ClientEntity();
        updatedClient.setClientId("C002");
        updatedClient.setNombreCliente("New Name");
        updatedClient.setVisitasMensuales(5);

        when(clientRepository.findById("C002")).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(updatedClient);
        mockMvc.perform(put("/api/clientes/C002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}