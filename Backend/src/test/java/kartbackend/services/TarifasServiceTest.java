// src/test/java/kartbackend/services/TarifasServiceTest.java
package kartbackend.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarifasServiceTest {

    private final TarifasService service = new TarifasService();

    @Test
    public void testObtenerTarifa_10_vueltas() {
        int tarifa = service.obtenerTarifa("10_vueltas_10_min");
        assertEquals(15000, tarifa, "La tarifa para 10 vueltas debe ser 15000");
    }

    @Test
    public void testObtenerTarifa_15_vueltas() {
        int tarifa = service.obtenerTarifa("15_vueltas_15_min");
        assertEquals(20000, tarifa, "La tarifa para 15 vueltas debe ser 20000");
    }

    @Test
    public void testObtenerTarifa_20_vueltas() {
        int tarifa = service.obtenerTarifa("20_vueltas_20_min");
        assertEquals(25000, tarifa, "La tarifa para 20 vueltas debe ser 25000");
    }

    @Test
    public void testObtenerTarifa_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.obtenerTarifa("30_vueltas");
        }, "Debe lanzar excepción para tarifa inválida");
    }

    @Test
    public void testCalcularDescuentoGrupo() {
        assertEquals(0.00, service.calcularDescuentoGrupo(1), 0.0001, "Para 1 persona el descuento debe ser 0%");
        assertEquals(0.10, service.calcularDescuentoGrupo(3), 0.0001, "Para 3 personas el descuento debe ser 10%");
        assertEquals(0.10, service.calcularDescuentoGrupo(5), 0.0001, "Para 5 personas el descuento debe ser 10%");
        assertEquals(0.20, service.calcularDescuentoGrupo(6), 0.0001, "Para 6 personas el descuento debe ser 20%");
        assertEquals(0.30, service.calcularDescuentoGrupo(11), 0.0001, "Para 11 personas el descuento debe ser 30%");
    }

    @Test
    public void testCalcularDescuentoFrecuencia() {
        assertEquals(0.00, service.calcularDescuentoFrecuencia(0), 0.0001, "Para 0 visitas no hay descuento");
        assertEquals(0.10, service.calcularDescuentoFrecuencia(2), 0.0001, "Para 2 visitas el descuento debe ser 10%");
        assertEquals(0.10, service.calcularDescuentoFrecuencia(4), 0.0001, "Para 4 visitas el descuento debe ser 10%");
        assertEquals(0.20, service.calcularDescuentoFrecuencia(5), 0.0001, "Para 5 visitas el descuento debe ser 20%");
        assertEquals(0.30, service.calcularDescuentoFrecuencia(7), 0.0001, "Para 7 visitas el descuento debe ser 30%");
    }

    @Test
    public void testCalcularPrecioFinal_SinCumple() {
        // Para tarifa "15_vueltas_15_min" con 4 personas y 3 visitas:
        // Precio base = 4 * 20000 = 80000
        // Descuento grupo: 10% y descuento frecuencia: 10% = total 20%
        // Precio ajustado = 80000 * (1 - 0.20) = 64000
        // IVA = 64000 * 0.19 = 12160
        // Precio final = 64000 + 12160 = 76160
        int precioFinal = service.calcularPrecioFinal("15_vueltas_15_min", 4, 3, false);
        assertEquals(76160, precioFinal, "El precio final sin cumpleaños debe ser 76160");
    }

    @Test
    public void testCalcularPrecioFinal_ConCumple() {
        // Con cumpleaños, se suma un 50% pero se limita al 50% total:
        // Precio base = 80000 y se aplica descuento máximo del 50%
        // Precio ajustado = 80000 * 0.50 = 40000
        // IVA = 40000 * 0.19 = 7600
        // Precio final = 40000 + 7600 = 47600
        int precioFinal = service.calcularPrecioFinal("15_vueltas_15_min", 4, 3, true);
        assertEquals(47600, precioFinal, "El precio final con cumpleaños debe ser 47600");
    }
}