package kartbackend.repositories;

import kartbackend.entities.ComprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity, Integer> {

    // Método derivado para obtener la lista de comprobantes en el rango dado.
    List<ComprobanteEntity> findByFechaEmisionBetween(Date fechaInicio, Date fechaFin);

    // Método por defecto para sumar el precioFinal usando la lista derivada.
    default Integer sumPrecioFinalByFechaEmisionBetween(Date fechaInicio, Date fechaFin) {
        List<ComprobanteEntity> comprobantes = findByFechaEmisionBetween(fechaInicio, fechaFin);
        return comprobantes.stream()
                .mapToInt(ComprobanteEntity::getPrecioFinal)
                .sum();
    }
}