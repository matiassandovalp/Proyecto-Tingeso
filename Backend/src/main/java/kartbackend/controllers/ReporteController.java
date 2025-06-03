package kartbackend.controllers;

import kartbackend.services.ReportesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReportesService reportesService;

    public ReporteController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @GetMapping("/ingresos")
    public ResponseEntity<Map<String, Object>> obtenerReporteIngresos(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam("fechaFin")   @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {

        int totalIngresos = reportesService.obtenerReporteIngresos(fechaInicio, fechaFin);
        return ResponseEntity.ok(Map.of(
                "totalIngresos", totalIngresos,
                "fechaInicio", fechaInicio,
                "fechaFin", fechaFin
        ));
    }

    @GetMapping("/detallado")
    public ResponseEntity<Map<String, Object>> obtenerReporteIngresosDetallado(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam("fechaFin")   @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {

        Map<String, Object> reporteDetallado = reportesService.obtenerReporteIngresosDetallado(fechaInicio, fechaFin);
        return ResponseEntity.ok(reporteDetallado);
    }

    // Nuevo endpoint para el reporte detallado de reservas
    @GetMapping("/reservas-detallado")
    public ResponseEntity<Map<String, Object>> obtenerReporteReservasDetallado(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam("fechaFin")   @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {

        Map<String, Object> reporteReservas = reportesService.obtenerReporteReservasDetallado(fechaInicio, fechaFin);
        return ResponseEntity.ok(reporteReservas);
    }
}