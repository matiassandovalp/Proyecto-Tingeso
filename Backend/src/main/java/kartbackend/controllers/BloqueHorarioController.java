package kartbackend.controllers;

import kartbackend.services.RackService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/rack")
public class BloqueHorarioController {

    private final RackService rackService;

    public BloqueHorarioController(RackService rackService) {
        this.rackService = rackService;
    }

    // Endpoint para obtener la disponibilidad del rack en el rango solicitado.
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<Map<String, Object>>> obtenerDisponibilidad(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin) {
        List<Map<String, Object>> eventos = rackService.obtenerRackPorRango(fechaInicio, fechaFin);
        return ResponseEntity.ok(eventos);
    }
}