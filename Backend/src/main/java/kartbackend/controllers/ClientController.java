package kartbackend.controllers;

import kartbackend.entities.ClientEntity;
import kartbackend.entities.KartEntity;
import kartbackend.repositories.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Obtener todos los clientes
    @GetMapping
    public List<ClientEntity> obtenerClientes() {
        return clientRepository.findAll();
    }

    // Obtener un cliente especifico
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientEntity> obtenerClientePorId(@PathVariable String clientId) {
        return clientRepository.findById(clientId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Insertar cliente desde Postman (archivo JSON)
    @PostMapping
    public ResponseEntity<?> agregarClientes(@RequestBody List<ClientEntity> clientes) {
        if (clientes.isEmpty()) {
            return ResponseEntity.badRequest().body("La lista de clientes no puede estar vacía");
        }

        for (ClientEntity cliente : clientes) {
            if (cliente.getClientId() == null || cliente.getClientId().isEmpty()) {
                return ResponseEntity.badRequest().body("Todos los clientes deben tener un 'clientId'");
            }
        }

        List<ClientEntity> nuevosClientes = clientRepository.saveAll(clientes);
        return ResponseEntity.ok(nuevosClientes);
    }

    // Eliminar todos los clientes
    @DeleteMapping("/eliminar-todos")
    public ResponseEntity<Void> eliminarTodosLosClientes() {
        clientRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    // Eliminar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String id) {
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar cliente
    @PutMapping("/{clientId}")
    public ResponseEntity<ClientEntity> actualizarCliente(@PathVariable String clientId, @RequestBody ClientEntity nuevoCliente) {
        return clientRepository.findById(clientId)
                .map(cliente -> {
                    cliente.setNombreCliente(nuevoCliente.getNombreCliente());
                    cliente.setVisitasMensuales(nuevoCliente.getVisitasMensuales());
                    return ResponseEntity.ok(clientRepository.save(cliente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}