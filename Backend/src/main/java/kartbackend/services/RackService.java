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
        // Ajustamos la fechaFin para incluir todo el día: 23:59:59.999
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaFin);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date fechaFinConHora = cal.getTime();

        // Obtenemos los bloques en el rango extendido
        List<BloqueHorarioEntity> bloques = bloqueHorarioRepository.findByFechaBetween(fechaInicio, fechaFinConHora);
        System.out.println("Buscando bloques entre: " + fechaInicio + " y " + fechaFinConHora);
        List<Map<String, Object>> eventos = new ArrayList<>();

        // Formato para la fecha en "yyyy-MM-dd"
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        sdfDate.setTimeZone(TimeZone.getTimeZone("America/Santiago")); // Mismo uso de zona horaria

        for (BloqueHorarioEntity bloque : bloques) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", bloque.getId());

            // Formatear la fecha base
            String dateStr = sdfDate.format(bloque.getFecha());

            // Concatenar la fecha con las horas de inicio y fin para formar cadenas ISO (compatibles con FullCalendar)
            String startDateTime = dateStr + "T" + bloque.getHoraInicio();
            String endDateTime = dateStr + "T" + bloque.getHoraFin();

            map.put("start", startDateTime);
            map.put("end", endDateTime);
            // El título muestra la lista de karts ocupados
            map.put("title", "Kart: " + String.join(", ", bloque.getKartsOcupados()));

            eventos.add(map);
        }
        return eventos;
    }
}