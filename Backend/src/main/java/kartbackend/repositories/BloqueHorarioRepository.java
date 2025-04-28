package kartbackend.repositories;

import kartbackend.entities.BloqueHorarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BloqueHorarioRepository extends JpaRepository<BloqueHorarioEntity, Integer> {

    // Para obtener los bloques de un rango (útil para el endpoint de rack)
    List<BloqueHorarioEntity> findByFechaBetween(Date fechaInicio, Date fechaFin);

    // Método para detectar solapamientos:
    // Se buscan bloques en la misma fecha cuyo horaInicio sea menor que el nuevo bloque’s horaFin
    // y cuyo horaFin sea mayor que el nuevo bloque’s horaInicio.
    List<BloqueHorarioEntity> findByFechaAndHoraInicioLessThanAndHoraFinGreaterThan(Date fecha, String nuevaHoraFin, String nuevaHoraInicio);
}