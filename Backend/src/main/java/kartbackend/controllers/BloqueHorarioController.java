package kartbackend.controllers;


import kartbackend.entities.BloqueHorarioEntity;
import kartbackend.repositories.BloqueHorarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rack")
public class BloqueHorarioController {

    private final BloqueHorarioRepository bloqueHorarioRepository;

    public BloqueHorarioController(BloqueHorarioRepository bloqueHorarioRepository) {
        this.bloqueHorarioRepository = bloqueHorarioRepository;
    }

    // Consultar el estado del rack semanal
    @GetMapping
    public List<BloqueHorarioEntity> obtenerRackSemanal(@RequestParam("fechaInicio") Date fechaInicio, @RequestParam("fechaFin") Date fechaFin) {
        return bloqueHorarioRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    // Registrar un nuevo bloque de ocupación cuando se crea una reserva
    @PostMapping
    public ResponseEntity<?> agregarBloque(@RequestBody BloqueHorarioEntity bloque) {
        try {
            BloqueHorarioEntity nuevoBloque = bloqueHorarioRepository.save(bloque);
            return ResponseEntity.ok(nuevoBloque);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar el bloque de ocupación.");
        }
    }

    // Eliminar un bloque cuando se cancela la reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarBloque(@PathVariable int id) {
        if (!bloqueHorarioRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("No existe un bloque con ese ID.");
        }
        bloqueHorarioRepository.deleteById(id);
        return ResponseEntity.ok("Bloque eliminado correctamente.");
    }
}