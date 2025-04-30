package kartbackend.controllers;

import kartbackend.services.RackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BloqueHorarioController.class)
public class BloqueHorarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RackService rackService;

    @Test
    public void testObtenerDisponibilidad() throws Exception {
        // Preparamos datos ficticios para simular la respuesta del servicio
        Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("id", 1);
        dummyMap.put("start", "2022-06-15T10:00");
        dummyMap.put("end", "2022-06-15T11:00");
        dummyMap.put("title", "Kart: K001");

        List<Map<String, Object>> dummyList = Collections.singletonList(dummyMap);

        when(rackService.obtenerRackPorRango(any(Date.class), any(Date.class))).thenReturn(dummyList);

        mockMvc.perform(get("/api/rack/disponibilidad")
                        .param("fechaInicio", "2022-06-15")
                        .param("fechaFin", "2022-06-16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].start").value("2022-06-15T10:00"))
                .andExpect(jsonPath("$[0].end").value("2022-06-15T11:00"))
                .andExpect(jsonPath("$[0].title").value("Kart: K001"));
    }
}