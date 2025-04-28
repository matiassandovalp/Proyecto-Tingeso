package kartbackend.controllers;

import kartbackend.entities.ComprobanteEntity;
import kartbackend.repositories.ComprobanteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comprobantes")
public class ComprobanteController {

    private final ComprobanteRepository comprobanteRepository;

    public ComprobanteController(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    // Obtener todos los comprobantes
    @GetMapping
    public List<ComprobanteEntity> obtenerComprobantes() {
        return comprobanteRepository.findAll();
    }

    // Obtener un comprobante por ID
    @GetMapping("/{id}")
    public ResponseEntity<ComprobanteEntity> obtenerComprobantePorId(@PathVariable int id) {
        Optional<ComprobanteEntity> comprobante = comprobanteRepository.findById(id);

        if (comprobante.isPresent()) {
            return ResponseEntity.ok(comprobante.get()); // Devuelve el comprobante correctamente
        } else {
            return ResponseEntity.notFound().build(); // Devuelve un 404 sin texto para mantener el tipo correcto
        }
    }
}