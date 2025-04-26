package kartbackend.repositories;

import kartbackend.entities.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KartRepository extends JpaRepository<KartEntity, String> {

    // Buscar un kart por su estado (disponible/no disponible)
    List<KartEntity> findByEstado(boolean estado);

    // Buscar karts que estén asociados a una reserva específica
    List<KartEntity> findByReservasIdsContaining(String reservaId);

    // Contar cuántos karts hay en la base de datos
    long count();

}