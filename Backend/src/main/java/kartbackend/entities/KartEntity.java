package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "kart")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class KartEntity {
    @Id
    @Column(name = "kart_id", unique = true, nullable = false)
    private String kartId;

    private String modelo;
    private Date ultimoMantenimiento;

    @ManyToMany(mappedBy = "karts")
    private List<ReservaEntity> reservas;
}
