package kartbackend.services;

import org.springframework.stereotype.Service;

@Service
public class TarifasService {

    public int obtenerTarifa(String tarifaSeleccionada) {
        return switch (tarifaSeleccionada) {
            case "10_vueltas_10_min" -> 15000;
            case "15_vueltas_15_min" -> 20000;
            case "20_vueltas_20_min" -> 25000;
            default -> throw new IllegalArgumentException("Tarifa inválida: " + tarifaSeleccionada);
        };
    }

    public double calcularDescuentoGrupo(int cantPersonas) {
        if (cantPersonas >= 11) return 0.30;
        else if (cantPersonas >= 6) return 0.20;
        else if (cantPersonas >= 3) return 0.10;
        return 0.00;
    }

    public double calcularDescuentoFrecuencia(int visitasMensuales) {
        if (visitasMensuales >= 7) return 0.30;
        else if (visitasMensuales >= 5) return 0.20;
        else if (visitasMensuales >= 2) return 0.10;
        return 0.00;
    }

    public int calcularPrecioFinal(String tarifaSeleccionada, int cantPersonas, int visitasMensuales, boolean esCumpleaños) {
        int precioBase = obtenerTarifa(tarifaSeleccionada);
        double descuentoGrupo = calcularDescuentoGrupo(cantPersonas);
        double descuentoFrecuencia = calcularDescuentoFrecuencia(visitasMensuales);

        double descuentoTotal = descuentoGrupo + descuentoFrecuencia;
        if (esCumpleaños) descuentoTotal += 0.50;

        int precioDescontado = (int) (precioBase * (1 - Math.min(descuentoTotal, 0.50)));
        int valorIVA = (int) (precioDescontado * 0.19);

        return precioDescontado + valorIVA;
    }
}