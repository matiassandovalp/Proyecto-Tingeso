package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

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
    private int valorFinal;

    @OneToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaEntity reserva;
}
