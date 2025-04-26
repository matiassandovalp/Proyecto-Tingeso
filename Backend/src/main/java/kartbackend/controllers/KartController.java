package kartbackend.controllers;

import kartbackend.entities.KartEntity;
import kartbackend.repositories.KartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/karts")
public class KartController {

    private final KartRepository kartRepository;

    public KartController(KartRepository kartRepository) {
        this.kartRepository = kartRepository;
    }

    // Obtener todos los karts
    @GetMapping
    public List<KartEntity> obtenerKarts() {
        return kartRepository.findAll();
    }

    // Obtener un kart especifico
    @GetMapping("/{kartId}")
    public ResponseEntity<KartEntity> obtenerKartPorId(@PathVariable String kartId) {
        return kartRepository.findById(kartId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Insertar un kart desde Postman (archivo JSON)
    @PostMapping
    public ResponseEntity<List<KartEntity>> agregarKarts(@RequestBody List<KartEntity> karts) {
        List<KartEntity> nuevosKarts = kartRepository.saveAll(karts);
        return ResponseEntity.ok(nuevosKarts);
    }

    // Eliminar todos los contenidos de la tabla
    @DeleteMapping("/eliminar-todos")
    public ResponseEntity<Void> eliminarTodosLosKarts() {
        kartRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    // Eliminar un kart por ID
    @DeleteMapping("/{kartId}")
    public ResponseEntity<Void> eliminarKart(@PathVariable String kartId) {
        kartRepository.deleteById(kartId);
        return ResponseEntity.noContent().build();
    }

    //Actualizar Cliente
    @PutMapping("/{kartId}")
    public ResponseEntity<KartEntity> actualizarKart(@PathVariable String kartId, @RequestBody KartEntity nuevoKart) {
        return kartRepository.findById(kartId)
                .map(kart -> {
                    kart.setModelo(nuevoKart.getModelo());
                    kart.setUltimoMantenimiento(nuevoKart.getUltimoMantenimiento());
                    return ResponseEntity.ok(kartRepository.save(kart));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}