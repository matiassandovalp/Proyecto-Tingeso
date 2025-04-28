package kartbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cliente")
public class ClientEntity {

    @Id
    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    private String nombreCliente;
    private int visitasMensuales;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReservaEntity> reservas;

    // Getters y Setters
    public String getClientId() {
        return clientId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public int getVisitasMensuales() {
        return visitasMensuales;
    }

    public List<ReservaEntity> getReservas() {
        return reservas;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setVisitasMensuales(int visitasMensuales) {
        this.visitasMensuales = visitasMensuales;
    }

    public void setReservas(List<ReservaEntity> reservas) {
        this.reservas = reservas;
    }
}