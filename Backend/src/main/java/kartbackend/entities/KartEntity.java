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

    @Temporal(TemporalType.DATE)
    private Date ultimoMantenimiento;

    private boolean estado;

    @ElementCollection
    private List<String> reservasIds;

    public String getModelo() {
        return modelo;
    }

    public Date getUltimoMantenimiento() {
        return ultimoMantenimiento;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setUltimoMantenimiento(Date ultimoMantenimiento) {
        this.ultimoMantenimiento = ultimoMantenimiento;
    }
}
