package kartbackend.services;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class TarifasService {

    public int calcularTarifaBase(int numeroVueltas) {
        return switch (numeroVueltas) {
            case 10 -> 15000;
            case 15 -> 20000;
            case 20 -> 25000;
            default -> throw new IllegalArgumentException("Número de vueltas inválido");
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

    public int calcularPrecioFinal(int numeroVueltas, int cantPersonas, int visitasMensuales, boolean esCumpleaños) {
        int precioBase = calcularTarifaBase(numeroVueltas);
        double descuentoGrupo = calcularDescuentoGrupo(cantPersonas);
        double descuentoFrecuencia = calcularDescuentoFrecuencia(visitasMensuales);

        double descuentoTotal = descuentoGrupo + descuentoFrecuencia;
        if (esCumpleaños) descuentoTotal += 0.50;  // Descuento especial por cumpleaños

        int precioDescontado = (int) (precioBase * (1 - Math.min(descuentoTotal, 0.50)));  // Máximo 50% de descuento
        int valorIVA = (int) (precioDescontado * 0.19);

        return precioDescontado + valorIVA;
    }
}