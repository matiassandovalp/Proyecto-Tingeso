import { useState } from "react";
import { Button, TextField, Typography, Alert, Paper, Box, Grid, Divider, Accordion, AccordionSummary, AccordionDetails, FormGroup, FormControlLabel, Checkbox, MenuItem } from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import axios from "axios";

const BASE_URL = "http://localhost:8090/api";

const Cliente = () => {
  const [clienteIdBuscar, setClienteIdBuscar] = useState("");
  const [clienteIdFormulario, setClienteIdFormulario] = useState("");
  const [reservas, setReservas] = useState([]);
  const [mensaje, setMensaje] = useState("");
  const [tipoMensaje, setTipoMensaje] = useState("info");
  const [kartIds, setKartIds] = useState([]);
  const [fecha, setFecha] = useState("");
  const [tarifa, setTarifa] = useState("");
  const [cantPersonas, setCantPersonas] = useState("");
  const [esCumpleaños, setEsCumpleaños] = useState(false);
  const [mensajeReserva, setMensajeReserva] = useState("");
  const [tipoMensajeReserva, setTipoMensajeReserva] = useState("info");

  const kartsDisponibles = ["K001", "K002", "K003", "K004", "K005", "K006", "K007", "K008", "K009", "K010", "K011", "K012", "K013", "K014", "K015"];

  const toggleKartSeleccion = (kart) => {
    setKartIds(prevKartIds =>
      prevKartIds.includes(kart) ? prevKartIds.filter(k => k !== kart) : [...prevKartIds, kart]
    );
  };

  const buscarReservas = async () => {
    setMensaje("");
    setTipoMensaje("info");
  
    if (!clienteIdBuscar) {
      setMensaje("⚠️ Debes ingresar un ID de cliente.");
      setTipoMensaje("warning");
      console.warn("⚠️ Falta el ID de cliente para buscar reservas.");
      return;
    }
  
    try {
      console.log("🔍 Consultando reservas para el cliente ID:", clienteIdBuscar);
      const response = await fetch(`${BASE_URL}/reservas`);
      if (response.ok) {
        let data = await response.json();
        console.log("Reservas recibidas:", data);
  
        data = data.filter(reserva => reserva.clienteId === clienteIdBuscar);
        setReservas(data);
  
        if (data.length === 0) {
          setMensaje("No se encontraron reservas para este cliente.");
          setTipoMensaje("error");
          console.warn("No se encontraron reservas para el cliente ID:", clienteIdBuscar);
        }
      } else {
        setMensaje("Error al consultar reservas.");
        setTipoMensaje("warning");
        console.error("Error en la respuesta del servidor al obtener reservas.");
      }
    } catch (error) {
      console.error("Error al obtener reservas:", error);
      setMensaje("Error al conectar con el servidor.");
      setTipoMensaje("error");
    }
  };
  
  const crearReserva = async (event) => {
    event.preventDefault();
    setMensajeReserva("");
    setTipoMensajeReserva("info");
  
    if (!clienteIdFormulario || kartIds.length === 0 || !fecha || !tarifa || !cantPersonas) {
      setMensajeReserva("Todos los campos son obligatorios.");
      setTipoMensajeReserva("warning");
      console.warn("Faltan datos obligatorios para crear la reserva.");
      return;
    }
  
    const cantidadPersonas = parseInt(cantPersonas);
    if (isNaN(cantidadPersonas)) {
      setMensajeReserva("Cantidad de personas debe ser un número válido.");
      setTipoMensajeReserva("warning");
      console.warn("Cantidad de personas inválida:", cantPersonas);
      return;
    }
  
    const nuevaReserva = {
      clienteId: clienteIdFormulario,
      kartIds,
      fecha,
      tarifaSeleccionada: tarifa,
      cantPersonas: cantidadPersonas,
      esCumpleaños
    };
  
    try {
      console.log("📤 Enviando nueva reserva:", nuevaReserva);
      const response = await axios.post(`${BASE_URL}/reservas`, nuevaReserva, {
        headers: { "Content-Type": "application/json" }
      });
  
      console.log("Reserva creada con éxito! ID:", response.data.reservaId);
      setMensajeReserva(`Reserva creada con éxito! ID: ${response.data.reservaId}`);
      setTipoMensajeReserva("success");
  
      setKartIds([]);
      setFecha("");
      setTarifa("");
      setCantPersonas("");
      setEsCumpleaños(false);
  
      if (clienteIdBuscar === clienteIdFormulario) {
        buscarReservas();
      }
    } catch (error) {
      console.error("Error al crear reserva:", error);
      setMensajeReserva("Error al crear la reserva. Verifica los datos ingresados.");
      setTipoMensajeReserva("error");
    }
  };

  const formatearFecha = (fechaIso) => {
    try {
      const opciones = { day: 'numeric', month: 'long', year: 'numeric' };
      return new Date(fechaIso).toLocaleDateString('es-ES', opciones);
    } catch (error) {
      console.error("Error formateando fecha:", error);
      return fechaIso;
    }
  };

  return (
    <Grid container spacing={3} sx={{ padding: "20px" }}>
      {/* 🔹 Buscar Reservas */}
      <Grid item xs={12}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={4}>
            <Paper elevation={3} sx={{ padding: "20px", height: "100%" }}>
              <Typography variant="h5">Consultar Reservas</Typography>
              <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}>
                <TextField label="ID Cliente" variant="outlined" value={clienteIdBuscar} onChange={(e) => setClienteIdBuscar(e.target.value)} />
                <Button variant="contained" color="primary" onClick={buscarReservas}>Buscar</Button>
                {mensaje && <Alert severity={tipoMensaje}>{mensaje}</Alert>}
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={8}>
            <Paper elevation={3} sx={{ padding: "20px", minHeight: "300px" }}>
              <Typography variant="h5">Reservas Encontradas</Typography>
              {reservas.length > 0 ? (
                <Grid container spacing={2} sx={{ mt: 1 }}>
                  {reservas.map((reserva) => (
                    <Grid item xs={12} sm={6} md={4} key={reserva.reservaId}>
                      <Paper elevation={2} sx={{ padding: "10px" }}>
                        <Typography variant="subtitle1">Reserva #{reserva.reservaId}</Typography>
                        <Typography>Fecha: {formatearFecha(reserva.fecha)}</Typography>
                        <Typography>Tarifa: {reserva.tarifaSeleccionada}</Typography>
                        <Typography>Personas: {reserva.cantPersonas}</Typography>
                        <Typography>Karts: {reserva.kartIds.join(", ")}</Typography>
                      </Paper>
                    </Grid>
                  ))}
                </Grid>
              ) : (
                <Typography sx={{ mt: 2 }}>No hay reservas para mostrar.</Typography>
              )}
            </Paper>
          </Grid>
        </Grid>
      </Grid>

      {/* 🔹 Separador */}
      <Grid item xs={12}>
        <Divider sx={{ marginY: "20px" }} />
      </Grid>

      {/* 🔹 Crear Nueva Reserva */}
      <Grid item xs={12}>
        <Paper elevation={3} sx={{ padding: "20px", backgroundColor: "#f5f5f5" }}>
          <Typography variant="h5" gutterBottom>Crear Nueva Reserva</Typography>
          <Box component="form" onSubmit={crearReserva} sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
            
            <TextField
              label="ID Cliente"
              variant="outlined"
              value={clienteIdFormulario}
              onChange={(e) => setClienteIdFormulario(e.target.value)}
              required
            />

            {/* Tarifa unificada */}
            <TextField
              select
              label="Tarifa (Vueltas o Tiempo)"
              variant="outlined"
              value={tarifa}
              onChange={(e) => setTarifa(e.target.value)}
              required
            >
              <MenuItem value="10_vueltas_10_min">10 vueltas o máx 10 min - 15.000 CLP</MenuItem>
              <MenuItem value="15_vueltas_15_min">15 vueltas o máx 15 min - 20.000 CLP</MenuItem>
              <MenuItem value="20_vueltas_20_min">20 vueltas o máx 20 min - 25.000 CLP</MenuItem>
            </TextField>

            {/* Karts disponibles */}
            <Accordion>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Typography>Seleccionar Karts</Typography>
              </AccordionSummary>
              <AccordionDetails>
                <FormGroup>
                  {kartsDisponibles.map((kart) => (
                    <FormControlLabel
                      key={kart}
                      control={<Checkbox checked={kartIds.includes(kart)} onChange={() => toggleKartSeleccion(kart)} />}
                      label={kart}
                    />
                  ))}
                </FormGroup>
              </AccordionDetails>
            </Accordion>

            <TextField
              label="Fecha"
              type="date"
              variant="outlined"
              value={fecha}
              onChange={(e) => setFecha(e.target.value)}
              InputLabelProps={{ shrink: true }}
              required
            />

            <TextField
              label="Cantidad de Personas"
              type="number"
              variant="outlined"
              value={cantPersonas}
              onChange={(e) => setCantPersonas(e.target.value)}
              required
            />

            <FormControlLabel
              control={<Checkbox checked={esCumpleaños} onChange={(e) => setEsCumpleaños(e.target.checked)} />}
              label="¿Es cumpleaños?"
            />

            <Button variant="contained" color="primary" type="submit">Crear Reserva</Button>

            {mensajeReserva && <Alert severity={tipoMensajeReserva}>{mensajeReserva}</Alert>}
          </Box>
        </Paper>
      </Grid>
    </Grid>
  );
};

export default Cliente;
