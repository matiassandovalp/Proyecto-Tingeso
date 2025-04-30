import React from "react";
import { Card, CardContent, Typography, Box } from "@mui/material";
import ComprobanteButton from "./ComprobanteButton";

// Función para formatear fechas (se podría importar desde pdfUtils si se prefiere)
const formatDate = (fechaISO) =>
  new Date(fechaISO).toLocaleDateString("es-ES", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });

const ReservaCard = ({ reserva }) => {
  return (
    <Card sx={{ maxWidth: 400, m: "10px", p: "10px", boxShadow: 3 }}>
      <CardContent>
        <Typography variant="h6">Reserva #{reserva.reservaId}</Typography>
        <Typography>Cliente: {reserva.clienteId}</Typography>
        <Typography>Fecha: {formatDate(reserva.fecha)}</Typography>
        <Typography>Tarifa: {reserva.tarifaSeleccionada}</Typography>
        <Typography>Cantidad de personas: {reserva.cantPersonas}</Typography>
        {reserva.comprobante ? (
          <Box sx={{ mt: 1 }}>
            <ComprobanteButton
              comprobante={reserva.comprobante}
              reservaId={reserva.reservaId}
              reservaInfo={reserva}
            />
          </Box>
        ) : (
          <Typography variant="body2" sx={{ mt: 1 }}>
            No hay comprobante disponible.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
};

export default ReservaCard;