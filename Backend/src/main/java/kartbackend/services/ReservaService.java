package kartbackend.services;

import kartbackend.entities.ClientEntity;
import kartbackend.repositories.ComprobanteRepository;
import kartbackend.repositories.KartRepository;
import kartbackend.entities.ComprobanteEntity;
import kartbackend.entities.ReservaEntity;
import kartbackend.entities.KartEntity;
import kartbackend.repositories.ClientRepository;
import kartbackend.repositories.ReservaRepository;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final TarifasService tarifasService;
    private final KartRepository kartRepository;
    private final ClientRepository clientRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ComprobanteRepository comprobanteRepository,
                          TarifasService tarifasService,
                          KartRepository kartRepository,
                          ClientRepository clientRepository) {
        this.reservaRepository = reservaRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.tarifasService = tarifasService;
        this.kartRepository = kartRepository;
        this.clientRepository = clientRepository;
    }

    public ReservaEntity crearReserva(ReservaEntity reserva) {
        String clientId = reserva.getCliente().getClientId();

        //Buscar el cliente en la base de datos
        ClientEntity cliente = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + clientId + " no encontrado"));

        // Asignar el cliente a la reserva antes de guardarla
        reserva.setCliente(cliente);

        return reservaRepository.save(reserva);
    }

    public ComprobanteEntity generarComprobante(Integer reservaId) {
        ReservaEntity reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Recuperar karts desde la base de datos usando los IDs almacenados en la reserva
        List<KartEntity> karts = kartRepository.findAllById(reserva.getKartIds());

        ComprobanteEntity comprobante = new ComprobanteEntity();
        comprobante.setNumeroVueltas(karts.size());

        int precioFinal = tarifasService.calcularPrecioFinal(
                karts.size(),
                reserva.getCantPersonas(),
                reserva.getCliente().getVisitasMensuales(),
                reserva.isEsCumpleaños()
        );

        comprobante.setPrecioFinal(precioFinal);
        comprobante.setReserva(reserva);

        // Actualiza la reserva con el comprobante antes de guardarlo
        reserva.setComprobante(comprobante);

        // Guarda los cambios en ambos
        reservaRepository.save(reserva);
        return comprobanteRepository.save(comprobante);
    }

}