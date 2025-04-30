package kartbackend.services;

import kartbackend.entities.BloqueHorarioEntity;
import kartbackend.repositories.BloqueHorarioRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RackService {

    private final BloqueHorarioRepository bloqueHorarioRepository;

    public RackService(BloqueHorarioRepository bloqueHorarioRepository) {
        this.bloqueHorarioRepository = bloqueHorarioRepository;
    }

    public List<Map<String, Object>> obtenerRackPorRango(Date fechaInicio, Date fechaFin) {
        List<BloqueHorarioEntity> bloques = bloqueHorarioRepository.findByFechaBetween(fechaInicio, fechaFin);
        List<Map<String, Object>> eventos = new ArrayList<>();
        // Formato para extraer solamente la fecha en formato "yyyy-MM-dd"
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        for (BloqueHorarioEntity bloque : bloques) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", bloque.getId());

            // Obtener la fecha base
            String dateStr = sdfDate.format(bloque.getFecha());
            // Concatenar la fecha con las horas de inicio y fin para formar cadenas ISO (compatible con FullCalendar)
            String startDateTime = dateStr + "T" + bloque.getHoraInicio();
            String endDateTime = dateStr + "T" + bloque.getHoraFin();

            map.put("start", startDateTime);
            map.put("end", endDateTime);
            // Por ejemplo, el título puede contener la lista de karts ocupados
            map.put("title", "Kart: " + String.join(", ", bloque.getKartsOcupados()));

            eventos.add(map);
        }
        return eventos;
    }
}