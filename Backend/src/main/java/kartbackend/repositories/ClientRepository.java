package kartbackend.repositories;

import kartbackend.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

    //Buscar un cliente por su nombre
    List<ClientEntity> findByNombreCliente(String nombreCliente);
    

    // Obtener todos los clientes que han visitado más de X veces en el mes
    List<ClientEntity> findByVisitasMensualesGreaterThan(int visitas);

    // Contar cuántos clientes hay en la base de datos
    long count();

}