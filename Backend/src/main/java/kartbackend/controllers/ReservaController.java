package kartbackend.controllers;

import kartbackend.entities.ReservaEntity;
import kartbackend.entities.ClientEntity;
import kartbackend.repositories.ClientRepository;
import kartbackend.services.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ClientRepository clientRepository; // 👉 Necesitabas agregarlo aquí

    public ReservaController(ReservaService reservaService, ClientRepository clientRepository) {
        this.reservaService = reservaService;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<ReservaEntity> obtenerReservas() {
        return reservaService.obtenerTodasLasReservas();
    }

    @PostMapping
    public ResponseEntity<?> agregarReserva(@RequestBody Map<String, Object> request) {
        try {
            ReservaEntity reserva = new ReservaEntity();

            reserva.setCantPersonas((Integer) request.get("cantPersonas"));
            reserva.setFecha(Date.valueOf((String) request.get("fecha")));
            reserva.setTarifaSeleccionada((String) request.get("tarifaSeleccionada"));
            reserva.setEsCumpleaños((Boolean) request.get("esCumpleaños"));

            // Buscar cliente manualmente
            String clienteId = (String) request.get("clienteId");
            ClientEntity cliente = clientRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            reserva.setCliente(cliente);

            // Lista de karts
            List<String> kartIds = (List<String>) request.get("kartIds");
            reserva.setKartIds(kartIds);

            // Crear reserva
            ReservaEntity nuevaReserva = reservaService.crearReserva(reserva);
            return ResponseEntity.ok(nuevaReserva);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
