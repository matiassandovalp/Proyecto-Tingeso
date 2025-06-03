import { useEffect, useState } from "react";
import { getReservas } from "../services/reservasService";
import { getComprobantes } from "../services/comprobantesService";
import { Typography, Grid } from "@mui/material";
import ReservaCard from "../components/ReservaCard";

const Reservas = () => {
  const [reservas, setReservas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchReservasConComprobantes = async () => {
      try {
        const reservasData = await getReservas();
        console.log("Reservas obtenidas desde backend:", reservasData);

        const comprobantesData = await getComprobantes();
        console.log("Todos los comprobantes obtenidos:", comprobantesData);

        //Asociamos cada reserva con su comprobante correctamente por `reservaId`
        const reservasConComprobantes = reservasData.map(reserva => {
          const comprobanteEncontrado = comprobantesData.find(c => c.reservaId === reserva.reservaId);
          console.log(`Vinculando reserva ${reserva.reservaId} con comprobante:`, comprobanteEncontrado);
          return { ...reserva, comprobante: comprobanteEncontrado ?? null };
        });

        setReservas(reservasConComprobantes);
      } catch (error) {
        console.error("Error al cargar reservas o comprobantes:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchReservasConComprobantes();
  }, []);

  return (
    <div>
      <Typography variant="h4" gutterBottom>
        Lista de Reservas
      </Typography>
      {loading ? (
        <Typography>Cargando reservas...</Typography>
      ) : (
        reservas.length > 0 ? (
          <Grid container spacing={2}>
            {reservas.map(reserva => (
              <Grid item xs={12} sm={6} md={4} key={reserva.reservaId}>
                <ReservaCard reserva={reserva} />
              </Grid>
            ))}
          </Grid>
        ) : (
          <Typography>No hay reservas disponibles.</Typography>
        )
      )}
    </div>
  );
};

export default Reservas;