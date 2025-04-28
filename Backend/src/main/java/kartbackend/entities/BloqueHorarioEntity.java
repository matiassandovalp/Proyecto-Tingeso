package kartbackend.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bloque_horario")
public class BloqueHorarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private String horaInicio;

    @Column(nullable = false)
    private String horaFin;

    @ElementCollection
    private List<String> kartsOcupados;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaEntity reservaAsociada;

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public List<String> getKartsOcupados() {
        return kartsOcupados;
    }

    public ReservaEntity getReservaAsociada() {
        return reservaAsociada;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setKartsOcupados(List<String> kartsOcupados) {
        this.kartsOcupados = kartsOcupados;
    }

    public void setReservaAsociada(ReservaEntity reservaAsociada) {
        this.reservaAsociada = reservaAsociada;
    }
}