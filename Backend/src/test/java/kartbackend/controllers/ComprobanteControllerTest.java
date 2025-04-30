package kartbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kartbackend.entities.ComprobanteEntity;
import kartbackend.repositories.ComprobanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ComprobanteController.class)
public class ComprobanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComprobanteRepository comprobanteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObtenerComprobantes() throws Exception {
        ComprobanteEntity comp = new ComprobanteEntity();
        comp.setPrecioFinal(76160);
        List<ComprobanteEntity> list = Collections.singletonList(comp);
        when(comprobanteRepository.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/comprobantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precioFinal").value(76160));
    }

    @Test
    public void testObtenerComprobantePorId_Found() throws Exception {
        ComprobanteEntity comp = new ComprobanteEntity();
        comp.setPrecioFinal(76160);
        when(comprobanteRepository.findById(1)).thenReturn(Optional.of(comp));

        mockMvc.perform(get("/api/comprobantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precioFinal").value(76160));
    }

    @Test
    public void testObtenerComprobantePorId_NotFound() throws Exception {
        when(comprobanteRepository.findById(999)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/comprobantes/999"))
                .andExpect(status().isNotFound());
    }
}