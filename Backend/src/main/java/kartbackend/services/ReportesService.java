package kartbackend.services;

import kartbackend.entities.ComprobanteEntity;
import kartbackend.entities.ReservaEntity;
import kartbackend.repositories.ComprobanteRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportesService {

    private final ComprobanteRepository comprobanteRepository;

    public ReportesService(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    // Método existente para reporte de ingresos
    public int obtenerReporteIngresos(Date fechaInicio, Date fechaFin) {
        Integer total = comprobanteRepository.sumPrecioFinalByFechaEmisionBetween(fechaInicio, fechaFin);
        return total != null ? total : 0;
    }

    // Método existente para reporte detallado de ingresos (por tarifa y por grupo)
    public Map<String, Object> obtenerReporteIngresosDetallado(Date fechaInicio, Date fechaFin) {
        List<ComprobanteEntity> comprobantes = comprobanteRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);

        Map<String, Map<String, Object>> ingresosPorTarifa = new HashMap<>();
        Map<Integer, Map<String, Object>> ingresosPorGrupo = new TreeMap<>();

        for (ComprobanteEntity comp : comprobantes) {
            ReservaEntity reserva = comp.getReserva();
            if (reserva == null) continue;

            String tarifa = reserva.getTarifaSeleccionada();
            int precioFinal = comp.getPrecioFinal();

            ingresosPorTarifa.compute(tarifa, (k, v) -> {
                if (v == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tarifa", tarifa);
                    map.put("totalIngreso", precioFinal);
                    map.put("reservas", 1);
                    return map;
                } else {
                    int totalActual = (int) v.get("totalIngreso");
                    int cantidad = (int) v.get("reservas");
                    v.put("totalIngreso", totalActual + precioFinal);
                    v.put("reservas", cantidad + 1);
                    return v;
                }
            });

            int cantPersonas = reserva.getCantPersonas();
            ingresosPorGrupo.compute(cantPersonas, (k, v) -> {
                if (v == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cantPersonas", cantPersonas);
                    map.put("totalIngreso", precioFinal);
                    map.put("reservas", 1);
                    return map;
                } else {
                    int totalActual = (int) v.get("totalIngreso");
                    int cantidad = (int) v.get("reservas");
                    v.put("totalIngreso", totalActual + precioFinal);
                    v.put("reservas", cantidad + 1);
                    return v;
                }
            });
        }

        int totalIngreso = comprobantes.stream()
                .mapToInt(ComprobanteEntity::getPrecioFinal)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("ingresosPorTarifa", ingresosPorTarifa.values());
        response.put("ingresosPorGrupo", ingresosPorGrupo.values());
        response.put("totalIngreso", totalIngreso);

        return response;
    }

    // Nuevo método para obtener un reporte detallado de reservas
    public Map<String, Object> obtenerReporteReservasDetallado(Date fechaInicio, Date fechaFin) {
        // Se filtran los comprobantes por fecha de emisión (facturación)
        List<ComprobanteEntity> comprobantes = comprobanteRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);

        // Se extraen los datos relevantes de la reserva
        List<Map<String, Object>> reservasList = new ArrayList<>();
        for (ComprobanteEntity comp : comprobantes) {
            ReservaEntity reserva = comp.getReserva();
            if (reserva == null)
                continue;

            Map<String, Object> reservaData = new HashMap<>();
            reservaData.put("reservaId", reserva.getReservaId());
            // Se asume que 'reserva.getFecha()' representa la fecha operativa de la reserva.
            reservaData.put("fechaReserva", reserva.getFecha());
            reservaData.put("tarifaSeleccionada", reserva.getTarifaSeleccionada());
            reservaData.put("cantPersonas", reserva.getCantPersonas());
            // Si la entidad Cliente está definida, se puede incluir el ID del cliente.
            if(reserva.getCliente() != null){
                reservaData.put("clienteId", reserva.getCliente().getClientId());
            }
            reservasList.add(reservaData);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalReservas", reservasList.size());
        result.put("reservas", reservasList);
        return result;
    }
}