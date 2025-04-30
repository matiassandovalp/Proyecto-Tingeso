package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false)
    private String tarifaSeleccionada;

    @Column(nullable = false)
    private boolean esCumpleaños;

    @ElementCollection
    @CollectionTable(name = "reserva_nombres_personas", joinColumns = @JoinColumn(name = "reserva_id"))
    @Column(name = "nombre")
    private List<String> nombresPersonas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private ClientEntity cliente;

    @ElementCollection
    @CollectionTable(name = "reserva_karts", joinColumns = @JoinColumn(name = "reserva_id"))
    @Column(name = "kart_id")
    private List<String> kartIds;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "reserva-comprobante")
    private ComprobanteEntity comprobante;

    @JsonProperty("clienteId")
    public String getClienteId() {
        return cliente != null ? cliente.getClientId() : null;
    }

    public int getReservaId() {
        return reservaId;
    }

    public int getCantPersonas() {
        return cantPersonas;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTarifaSeleccionada() {
        return tarifaSeleccionada;
    }

    public boolean isEsCumpleaños() {
        return esCumpleaños;
    }

    public List<String> getNombresPersonas() {
        return nombresPersonas;
    }

    public ClientEntity getCliente() {
        return cliente;
    }

    public List<String> getKartIds() {
        return kartIds;
    }

    public ComprobanteEntity getComprobante() {
        return comprobante;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public void setCantPersonas(int cantPersonas) {
        this.cantPersonas = cantPersonas;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTarifaSeleccionada(String tarifaSeleccionada) {
        this.tarifaSeleccionada = tarifaSeleccionada;
    }

    public void setNombresPersonas(List<String> nombresPersonas) {
        this.nombresPersonas = nombresPersonas;
    }

    public void setEsCumpleaños(boolean esCumpleaños) {
        this.esCumpleaños = esCumpleaños;
    }

    public void setKartIds(List<String> kartIds) {
        this.kartIds = kartIds;
    }

    public void setCliente(ClientEntity cliente) {
        this.cliente = cliente;
    }

    public void setComprobante(ComprobanteEntity comprobante) {
        this.comprobante = comprobante;
    }
}