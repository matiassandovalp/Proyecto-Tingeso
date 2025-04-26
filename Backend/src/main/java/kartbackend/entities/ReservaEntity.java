package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import java.time.LocalTime;
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

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private LocalTime duracion;

    @Column(nullable = false)
    private boolean esCumpleaños;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private ClientEntity cliente;

    @ElementCollection
    @CollectionTable(name = "reserva_karts", joinColumns = @JoinColumn(name = "reserva_id"))
    @Column(name = "kart_id")
    private List<String> kartIds;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "reserva-comprobante") // Identificador unico
    private ComprobanteEntity comprobante;

    //Getters y Setters
    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public boolean isEsCumpleaños() {
        return esCumpleaños;
    }

    public void setEsCumpleaños(boolean esCumpleaños) {
        this.esCumpleaños = esCumpleaños;
    }

    public ClientEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientEntity cliente) {
        this.cliente = cliente;
    }

    public String getClientId() {
        return (cliente != null) ? cliente.getClientId() : null;
    }

    public List<String> getKartIds() {
        return kartIds;
    }

    public void setKartIds(List<String> kartIds) {
        this.kartIds = kartIds;
    }

    public ComprobanteEntity getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteEntity comprobante) {
        this.comprobante = comprobante;
    }
}