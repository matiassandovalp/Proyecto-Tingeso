package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id", unique = true, nullable = false)
    private int reservaId;

    private int cantPersonas;
    private Date fecha;
    private Time duracion;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClientEntity cliente;

    @ManyToMany
    @JoinTable(
            name = "reserva_kart",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "kart_id")
    )
    private List<KartEntity> karts;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private ComprobanteEntity comprobante;
}
