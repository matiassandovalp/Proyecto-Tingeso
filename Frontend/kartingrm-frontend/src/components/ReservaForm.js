import React, { useState } from "react";
import {
  Button,
  TextField,
  Typography,
  Alert,
  Paper,
  Box,
  FormControlLabel,
  Checkbox,
  MenuItem,
} from "@mui/material";
import axios from "axios";
import ComprobanteButton from "./ComprobanteButton"; // Componente para descargar el comprobante

const BASE_URL = "http://localhost:8090/api";

const ReservaForm = () => {
  // Estados para el formulario de reserva
  const [clienteIdFormulario, setClienteIdFormulario] = useState("");
  const [cantPersonas, setCantPersonas] = useState("");
  const [nombresPersonas, setNombresPersonas] = useState([]);
  const [fecha, setFecha] = useState("");
  const [tarifa, setTarifa] = useState("");
  const [kartIds, setKartIds] = useState([]);
  const [esCumpleaños, setEsCumpleaños] = useState(false);
  const [mensajeReserva, setMensajeReserva] = useState("");
  const [tipoMensajeReserva, setTipoMensajeReserva] = useState("info");
  const [reservaCreada, setReservaCreada] = useState(null);
  const [availableKarts, setAvailableKarts] = useState([]);
  const [mostrarLista, setMostrarLista] = useState(false); // Estado para alternar visualización

  const kartsDisponibles = [
    "K001", "K002", "K003", "K004", "K005",
    "K006", "K007", "K008", "K009", "K010",
    "K011", "K012", "K013", "K014", "K015",
  ];

  const toggleKartSeleccion = (kart) => {
    setKartIds((prev) =>
      prev.includes(kart) ? prev.filter((k) => k !== kart) : [...prev, kart]
    );
  };

  const handleCambiarCantPersonas = (e) => {
    const value = e.target.value;
    setCantPersonas(value);
    const num = parseInt(value, 10) || 0;
    setNombresPersonas(Array(num).fill(""));
  };

  const handleNombreChange = (index, e) => {
    const newNames = [...nombresPersonas];
    newNames[index] = e.target.value;
    setNombresPersonas(newNames);
  };

  const crearReserva = async (e) => {
    e.preventDefault();
    setMensajeReserva("");
    setTipoMensajeReserva("info");

    if (!clienteIdFormulario || !cantPersonas || !fecha || !tarifa || availableKarts.length === 0) {
      setMensajeReserva("Todos los campos son obligatorios.");
      setTipoMensajeReserva("warning");
      return;
    }

    const cantidadPersonas = parseInt(cantPersonas, 10);
    if (isNaN(cantidadPersonas)) {
      setMensajeReserva("La cantidad de personas debe ser un número válido.");
      setTipoMensajeReserva("warning");
      return;
    }

    if (
      nombresPersonas.length !== cantidadPersonas ||
      nombresPersonas.some((name) => name.trim() === "")
    ) {
      setMensajeReserva("Debes ingresar un nombre para cada persona.");
      setTipoMensajeReserva("warning");
      return;
    }

    const nuevaReserva = {
      clienteId: clienteIdFormulario,
      cantPersonas: cantidadPersonas,
      nombresPersonas,
      fecha,
      tarifaSeleccionada: tarifa,
      esCumpleaños,
      kartIds,
    };

    try {
      const response = await axios.post(`${BASE_URL}/reservas`, nuevaReserva, {
        headers: { "Content-Type": "application/json" },
      });

      setReservaCreada(response.data);
      setMensajeReserva(`Reserva creada con éxito! ID: ${response.data.reservaId}`);
      setTipoMensajeReserva("success");

      // Reinicia los campos del formulario
      setKartIds([]);
      setFecha("");
      setTarifa("");
      setCantPersonas("");
      setNombresPersonas([]);
      setEsCumpleaños(false);
    } catch (error) {
      console.error("Error al crear reserva:", error);
      setMensajeReserva("Error al crear la reserva. Verifica los datos ingresados.");
      setTipoMensajeReserva("error");
    }
  };

  return (
    <Paper sx={{ p: 3, m: 3 }}>
      <Typography variant="h5">Crear Reserva</Typography>
      <Box
        component="form"
        onSubmit={crearReserva}
        sx={{ mt: 2, display: "flex", flexDirection: "column", gap: 2 }}
      >
        <TextField
          label="Cantidad de Personas"
          type="number"
          variant="outlined"
          value={cantPersonas}
          onChange={handleCambiarCantPersonas}
          required
        />
        {nombresPersonas.map((nombre, index) => (
          <TextField
            key={index}
            label={`Nombre de Persona ${index + 1}`}
            variant="outlined"
            value={nombre}
            onChange={(e) => handleNombreChange(index, e)}
            required
          />
        ))}
        <TextField
          label="Fecha y Hora"
          type="datetime-local"
          variant="outlined"
          value={fecha}
          onChange={(e) => setFecha(e.target.value)}
          InputLabelProps={{ shrink: true }}
          required
        />
        <Button
          variant="contained"
          color="secondary"
          onClick={() => setAvailableKarts(kartsDisponibles)}
        >
          Consultar Disponibilidad de Karts
        </Button>
        {availableKarts.length > 0 && (
          <Box>
            <Typography variant="body1" sx={{ mb: 1 }}>
              Karts disponibles
            </Typography>
            <Button
              variant="outlined"
              size="small"
              onClick={() => setMostrarLista((prev) => !prev)}
              sx={{ mb: 1 }}
            >
              {mostrarLista ? "Ocultar lista completa" : "Mostrar lista completa"}
            </Button>
            {mostrarLista ? (
              <Box
                sx={{
                  display: "grid",
                  gridTemplateColumns: "repeat(auto-fit, minmax(100px, 1fr))",
                  gap: 1,
                  maxWidth: 400,
                  overflowX: "auto",
                }}
              >
                {availableKarts.map((kart) => (
                  <Button
                    key={kart}
                    variant={kartIds.includes(kart) ? "contained" : "outlined"}
                    onClick={() => toggleKartSeleccion(kart)}
                  >
                    {kart}
                  </Button>
                ))}
              </Box>
            ) : (
              <Typography variant="body2">
                {kartIds.length > 0
                  ? `Karts seleccionados: ${kartIds.join(", ")}`
                  : "No se ha seleccionado ningún kart."}
              </Typography>
            )}
          </Box>
        )}
        <TextField
          label="ID Cliente"
          variant="outlined"
          value={clienteIdFormulario}
          onChange={(e) => setClienteIdFormulario(e.target.value)}
          required
        />
        <TextField
          select
          label="Tarifa (Vueltas o Tiempo)"
          variant="outlined"
          value={tarifa}
          onChange={(e) => setTarifa(e.target.value)}
          required
        >
          <MenuItem value="10_vueltas_10_min">
            10 vueltas o máx 10 min - 15.000 CLP
          </MenuItem>
          <MenuItem value="15_vueltas_15_min">
            15 vueltas o máx 15 min - 20.000 CLP
          </MenuItem>
          <MenuItem value="20_vueltas_20_min">
            20 vueltas o máx 20 min - 25.000 CLP
          </MenuItem>
        </TextField>
        <FormControlLabel
          control={
            <Checkbox
              checked={esCumpleaños}
              onChange={(e) => setEsCumpleaños(e.target.checked)}
            />
          }
          label="¿Es cumpleaños?"
        />
        <Button variant="contained" color="primary" type="submit">
          Crear Reserva
        </Button>
        {mensajeReserva && (
          <Alert severity={tipoMensajeReserva}>{mensajeReserva}</Alert>
        )}
        {reservaCreada && reservaCreada.comprobante && (
          <ComprobanteButton
            comprobante={reservaCreada.comprobante}
            reservaId={reservaCreada.reservaId}
            reservaInfo={reservaCreada}
          />
        )}
      </Box>
    </Paper>
  );
};

export default ReservaForm;