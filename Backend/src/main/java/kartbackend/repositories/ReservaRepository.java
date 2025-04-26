package kartbackend.repositories;

import kartbackend.entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Integer> {
}
