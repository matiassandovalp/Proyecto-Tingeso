package kartbackend.controllers;

import kartbackend.entities.ReservaEntity;
import kartbackend.entities.ClientEntity;
import kartbackend.repositories.ClientRepository;
import kartbackend.services.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ClientRepository clientRepository;

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

            // Cantidad de personas
            Integer cantPersonas = (Integer) request.get("cantPersonas");
            if (cantPersonas == null || cantPersonas <= 0) {
                throw new IllegalArgumentException("La cantidad de personas es obligatoria y debe ser mayor que cero.");
            }
            reserva.setCantPersonas(cantPersonas);

            // 2) Nombres de personas: se espera un array de strings y su tamaño debe coincidir con la cantidad
            List<String> nombresPersonas = (List<String>) request.get("nombresPersonas");
            if (nombresPersonas == null || nombresPersonas.size() != cantPersonas) {
                throw new IllegalArgumentException("La cantidad de nombres proporcionados debe coincidir con la cantidad de personas.");
            }
            reserva.setNombresPersonas(nombresPersonas);

            // Fecha y hora de la reserva:
            // Se espera el string en formato ISO: "yyyy-MM-dd'T'HH:mm" (por ejemplo, "2025-04-29T10:00")
            // ReservaController: parseo de fecha
            String fechaStr = (String) request.get("fecha");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("America/Santiago")); // Fuerza la zona horaria
            Date fecha = sdf.parse(fechaStr);
            reserva.setFecha(fecha);

            //  Tarifa seleccionada
            String tarifaSeleccionada = (String) request.get("tarifaSeleccionada");
            if (tarifaSeleccionada == null || tarifaSeleccionada.isBlank()) {
                throw new IllegalArgumentException("La tarifa seleccionada es obligatoria.");
            }
            reserva.setTarifaSeleccionada(tarifaSeleccionada);

            //Indicar si es cumpleaños
            Boolean esCumple = (Boolean) request.get("esCumpleaños");
            reserva.setEsCumpleaños(esCumple != null ? esCumple : false);

            // Buscar cliente (se envía como "clienteId")
            String clienteId = (String) request.get("clienteId");
            if (clienteId == null || clienteId.isBlank()) {
                throw new IllegalArgumentException("El ID del cliente es obligatorio.");
            }
            ClientEntity cliente = clientRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            reserva.setCliente(cliente);

            // Lista de karts asignados
            List<String> kartIds = (List<String>) request.get("kartIds");
            if (kartIds == null || kartIds.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar al menos un kart.");
            }
            reserva.setKartIds(kartIds);

            // Crear la reserva a través del servicio (que ya incorpora la lógica del rack, comprobante, etc.)
            ReservaEntity nuevaReserva = reservaService.crearReserva(reserva);

            return ResponseEntity.ok(nuevaReserva);

        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Fecha en formato incorrecto, se espera: yyyy-MM-dd'T'HH:mm");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}