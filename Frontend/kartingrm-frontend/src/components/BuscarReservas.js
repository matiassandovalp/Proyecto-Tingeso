import React, { useState } from "react";
import { Grid, TextField, Button, Paper, Typography, Alert } from "@mui/material";
import axios from "axios";
import ReservaCard from "./ReservaCard";

const BASE_URL = "http://localhost:8090/api";

const BuscarReservas = () => {
  const [clienteIdBuscar, setClienteIdBuscar] = useState("");
  const [reservas, setReservas] = useState([]);
  const [mensaje, setMensaje] = useState("");
  const [tipoMensaje, setTipoMensaje] = useState("info");

  const buscarReservas = async () => {
    setMensaje("");
    setTipoMensaje("info");

    if (!clienteIdBuscar) {
      setMensaje("⚠️ Debes ingresar un ID de cliente.");
      setTipoMensaje("warning");
      return;
    }

    try {
      console.log("Buscando reservas para el cliente:", clienteIdBuscar);
      const response = await axios.get(`${BASE_URL}/reservas`);
      if (response.status === 200) {
        const filtradas = response.data.filter(
          (reserva) => reserva.clienteId === clienteIdBuscar
        );
        setReservas(filtradas);

        if (filtradas.length === 0) {
          setMensaje("No se encontraron reservas para este cliente.");
          setTipoMensaje("error");
        }
      } else {
        setMensaje("Error al consultar reservas.");
        setTipoMensaje("warning");
      }
    } catch (error) {
      console.error("Error al consultar reservas:", error);
      setMensaje("Error al conectar con el servidor.");
      setTipoMensaje("error");
    }
  };

  return (
    <Paper sx={{ padding: "20px" }}>
      <Typography variant="h5">Consultar Reservas</Typography>
      <TextField
        label="ID Cliente"
        variant="outlined"
        value={clienteIdBuscar}
        onChange={(e) => setClienteIdBuscar(e.target.value)}
        fullWidth
        sx={{ marginTop: "10px" }}
      />
      <Button
        variant="contained"
        color="primary"
        onClick={buscarReservas}
        sx={{ marginTop: "10px" }}
      >
        Buscar
      </Button>
      {mensaje && (
        <Alert severity={tipoMensaje} sx={{ marginTop: "10px" }}>
          {mensaje}
        </Alert>
      )}
      <Typography variant="h6" sx={{ marginTop: "20px" }}>
        Reservas Encontradas
      </Typography>
      <Grid container spacing={2}>
        {reservas.length > 0 ? (
          reservas.map((reserva) => (
            <Grid item xs={12} md={6} key={reserva.reservaId}>
              <ReservaCard reserva={reserva} />
            </Grid>
          ))
        ) : (
          <Typography sx={{ margin: "10px" }}>
            No hay reservas para mostrar.
          </Typography>
        )}
      </Grid>
    </Paper>
  );
};

export default BuscarReservas;