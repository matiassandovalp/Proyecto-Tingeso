package kartbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kartbackend.entities.KartEntity;
import kartbackend.repositories.KartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KartController.class)
public class KartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KartRepository kartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObtenerKarts() throws Exception {
        KartEntity kart = new KartEntity();
        kart.setKartId("K001");
        List<KartEntity> list = Collections.singletonList(kart);
        when(kartRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/karts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].kartId").value("K001"));
    }

    @Test
    public void testObtenerKartPorId_Found() throws Exception {
        KartEntity kart = new KartEntity();
        kart.setKartId("K001");
        when(kartRepository.findById("K001")).thenReturn(Optional.of(kart));

        mockMvc.perform(get("/api/karts/K001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kartId").value("K001"));
    }

    @Test
    public void testObtenerKartPorId_NotFound() throws Exception {
        when(kartRepository.findById("K002")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/karts/K002"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAgregarKarts() throws Exception {
        KartEntity kart = new KartEntity();
        kart.setKartId("K001");
        List<KartEntity> list = Collections.singletonList(kart);
        when(kartRepository.saveAll(any(List.class))).thenReturn(list);

        String json = objectMapper.writeValueAsString(list);
        mockMvc.perform(post("/api/karts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].kartId").value("K001"));
    }

    @Test
    public void testEliminarTodosLosKarts() throws Exception {
        doNothing().when(kartRepository).deleteAll();
        mockMvc.perform(delete("/api/karts/eliminar-todos"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarKart() throws Exception {
        doNothing().when(kartRepository).deleteById("K001");
        mockMvc.perform(delete("/api/karts/K001"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testActualizarKart_Found() throws Exception {
        KartEntity existingKart = new KartEntity();
        existingKart.setKartId("K001");
        existingKart.setModelo("ModeloViejo");

        KartEntity updatedKart = new KartEntity();
        updatedKart.setKartId("K001");
        updatedKart.setModelo("ModeloNuevo");

        when(kartRepository.findById("K001")).thenReturn(Optional.of(existingKart));
        when(kartRepository.save(any(KartEntity.class))).thenReturn(updatedKart);

        String json = objectMapper.writeValueAsString(updatedKart);
        mockMvc.perform(put("/api/karts/K001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelo").value("ModeloNuevo"));
    }

    @Test
    public void testActualizarKart_NotFound() throws Exception {
        KartEntity updatedKart = new KartEntity();
        updatedKart.setModelo("ModeloNuevo");

        when(kartRepository.findById("K002")).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(updatedKart);
        mockMvc.perform(put("/api/karts/K002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}