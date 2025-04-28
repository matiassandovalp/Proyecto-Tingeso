import { useState } from "react";
import { Card, CardContent, Typography, Button, Paper, Box } from "@mui/material";

// Función para formatear la fecha en `DD/MM/YYYY HH:mm:ss`
const formatoFecha = (fechaISO) => {
  return new Date(fechaISO).toLocaleDateString("es-ES", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit"
  });
};

const ReservaCard = ({ reserva }) => {
  const [mostrarDetalles, setMostrarDetalles] = useState(false);
  const [mostrarComprobante, setMostrarComprobante] = useState(false);

  return (
    <Card sx={{ maxWidth: 400, margin: "10px", padding: "10px", boxShadow: 3 }}>
      <CardContent>
        <Typography variant="h6">Reserva #{reserva.reservaId}</Typography>
        <Typography>Cliente: {reserva.clientId}</Typography>
        <Typography>Fecha: {formatoFecha(reserva.fecha)}</Typography>
        <Typography>Duración: {reserva.duracion}</Typography>

        <Box sx={{ display: "flex", gap: 1, marginTop: "10px" }}>
          <Button
            variant="contained"
            color="secondary"
            onClick={() => setMostrarDetalles(!mostrarDetalles)}
          >
            {mostrarDetalles ? "Ocultar detalles" : "Ver detalles"}
          </Button>

          <Button
            variant="contained"
            color="primary"
            onClick={() => setMostrarComprobante(!mostrarComprobante)}
          >
            {mostrarComprobante ? "Ocultar comprobante" : "Ver comprobante"}
          </Button>
        </Box>

        {mostrarDetalles && (
          <Paper elevation={3} sx={{ padding: "10px", marginTop: "10px", backgroundColor: "#f5f5f5" }}>
            <Typography>Cantidad de personas: {reserva.cantPersonas}</Typography>
            <Typography>Cumpleaños: {reserva.esCumpleaños ? "Sí" : "No"}</Typography>
            <Typography>Karts asignados: {reserva.kartIds.length > 0 ? reserva.kartIds.join(", ") : "Ninguno"}</Typography>
          </Paper>
        )}

        {mostrarComprobante && (
          <Paper elevation={3} sx={{ padding: "10px", marginTop: "10px", backgroundColor: "#e3f2fd" }}>
            {reserva.comprobante ? (
              <>
                <Typography variant="h6">Comprobante de Pago</Typography>
                <Typography><strong>Fecha de emisión:</strong> {formatoFecha(reserva.comprobante.fechaEmision)}</Typography>
                <Typography><strong>Precio final:</strong> ${reserva.comprobante.precioFinal ?? "No disponible"}</Typography>
                <Typography><strong>Número de vueltas:</strong> {reserva.comprobante.numeroVueltas ?? "No disponible"}</Typography>
                <Typography><strong>Descuento aplicado:</strong> ${reserva.comprobante.descuentoPersonas + reserva.comprobante.descuentoPersonal ?? "No disponible"}</Typography>
              </>
            ) : (
              <Typography>No se ha generado un comprobante para esta reserva.</Typography>
            )}
          </Paper>
        )}
      </CardContent>
    </Card>
  );
};

export default ReservaCard;