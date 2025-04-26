package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;

@Entity
@Table(name = "comprobante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comprobante_id", unique = true, nullable = false)
    private int comprobanteId;

    private int numeroVueltas;
    private int precioEstandar;
    private int descuentoPersonas;
    private int descuentoPersonal;
    private int precioAjustado;
    private int valorIVA;
    private int precioFinal;
    private Date fechaEmision;

    @OneToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonManagedReference(value = "reserva-comprobante") // Usa el mismo identificador único
    private ReservaEntity reserva;


    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setNumeroVueltas(int numeroVueltas) {
        this.numeroVueltas = numeroVueltas;
    }

    public int getNumeroVueltas() {
        return numeroVueltas;
    }

    public void setPrecioFinal(int precioFinal) {
        this.precioFinal = precioFinal;
    }

    public int getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioEstandar(int precioEstandar) {
        this.precioEstandar = precioEstandar;
    }

    public int getPrecioEstandar() {
        return precioEstandar;
    }

    public void setDescuentoPersonas(int descuentoPersonas) {
        this.descuentoPersonas = descuentoPersonas;
    }

    public int getDescuentoPersonas() {
        return descuentoPersonas;
    }

    public void setDescuentoPersonal(int descuentoPersonal) {
        this.descuentoPersonal = descuentoPersonal;
    }

    public int getDescuentoPersonal() {
        return descuentoPersonal;
    }

    public void setPrecioAjustado(int precioAjustado) {
        this.precioAjustado = precioAjustado;
    }

    public int getPrecioAjustado() {
        return precioAjustado;
    }

    public void setValorIVA(int valorIVA) {
        this.valorIVA = valorIVA;
    }

    public int getValorIVA() {
        return valorIVA;
    }

    public void setReserva(ReservaEntity reserva) {
        this.reserva = reserva;
    }
}