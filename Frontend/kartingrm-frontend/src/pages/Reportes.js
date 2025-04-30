import React, { useEffect, useState } from "react";
import {
  Paper,
  Typography,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Tabs,
  Tab,
  Box,
  Alert,
} from "@mui/material";
import axios from "axios";

const Reportes = () => {
  const [tabValue, setTabValue] = useState(0);
  const [reportVueltas, setReportVueltas] = useState([]);
  const [reportPersonas, setReportPersonas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    console.log("Reportes: Iniciando consulta de reservas.");
    setLoading(true);
    axios
      .get("http://localhost:8090/api/reservas")
      .then((res) => {
        console.log("Reportes: Reservas recibidas:", res.data);
        const { reportVueltas, reportPersonas } = computeReports(res.data);
        console.log("Reportes: Reporte de Vueltas:", reportVueltas);
        console.log("Reportes: Reporte de Personas:", reportPersonas);
        setReportVueltas(reportVueltas);
        setReportPersonas(reportPersonas);
      })
      .catch((e) => {
        console.error("Reportes: Error al consultar reservas", e);
        setErrorMsg("Error al cargar las reservas.");
      })
      .finally(() => {
        console.log("Reportes: Finalizando consulta de reservas.");
        setLoading(false);
      });
  }, []);

  // Función para transformar y agrupar los datos de las reservas en reportes
  const computeReports = (reservas) => {
    const reportVueltasObj = {};
    const reportPersonasObj = {};

    // Función para extraer el mes (solo enero, febrero o marzo) a partir de la fecha de la reserva
    const getMonth = (dateStr) => {
      const date = new Date(dateStr);
      const month = date.getMonth(); // 0=enero, 1=febrero, 2=marzo, etc.
      if (month === 0) return "enero";
      if (month === 1) return "febrero";
      if (month === 2) return "marzo";
      return null; // Se ignoran reservas fuera de estos meses
    };

    // Función para agrupar según la cantidad de personas
    const groupByPersonas = (cantidad) => {
      if (cantidad <= 2) return "1-2 personas";
      else if (cantidad <= 5) return "3-5 personas";
      else if (cantidad <= 10) return "6-10 personas";
      else return "11-15 personas";
    };

    reservas.forEach((reserva) => {
      // Verificamos que exista comprobante para extraer el precio final
      if (!reserva.comprobante) return;
      const price = reserva.comprobante.precioFinal || 0;
      const month = getMonth(reserva.fecha);
      if (!month) return; // Ignoramos si no es enero, febrero o marzo

      // Agrupar por tarifa (vueltas/tiempo) a partir del campo "tarifaSeleccionada"
      const tarifa = reserva.tarifaSeleccionada;
      if (!reportVueltasObj[tarifa]) {
        reportVueltasObj[tarifa] = { tarifa, enero: 0, febrero: 0, marzo: 0, total: 0 };
      }
      reportVueltasObj[tarifa][month] += price;
      reportVueltasObj[tarifa].total += price;

      // Agrupar por cantidad de personas usando el campo "cantPersonas"
      const group = groupByPersonas(reserva.cantPersonas);
      if (!reportPersonasObj[group]) {
        reportPersonasObj[group] = { rango: group, enero: 0, febrero: 0, marzo: 0, total: 0 };
      }
      reportPersonasObj[group][month] += price;
      reportPersonasObj[group].total += price;
    });

    return {
      reportVueltas: Object.values(reportVueltasObj),
      reportPersonas: Object.values(reportPersonasObj),
    };
  };

  const handleTabChange = (event, newValue) => {
    console.log("Reportes: Cambiando pestaña a:", newValue);
    setTabValue(newValue);
  };

  return (
    <Paper sx={{ p: 2, m: 2 }}>
      <Typography variant="h5" gutterBottom>
        Reportes de Ingresos
      </Typography>
      {errorMsg && <Alert severity="error">{errorMsg}</Alert>}
      <Tabs value={tabValue} onChange={handleTabChange}>
        <Tab label="Por Vueltas/tiempo" />
        <Tab label="Por Número de Personas" />
      </Tabs>
      <Box sx={{ mt: 2 }}>
        {tabValue === 0 && (
          <ReportTable
            title="Reporte por Vueltas/tiempo"
            data={reportVueltas}
            headers={["Tarifa", "Enero", "Febrero", "Marzo", "Total"]}
            keys={["tarifa", "enero", "febrero", "marzo", "total"]}
            loading={loading}
          />
        )}
        {tabValue === 1 && (
          <ReportTable
            title="Reporte por Número de Personas"
            data={reportPersonas}
            headers={["Grupo de Personas", "Enero", "Febrero", "Marzo", "Total"]}
            keys={["rango", "enero", "febrero", "marzo", "total"]}
            loading={loading}
          />
        )}
      </Box>
    </Paper>
  );
};

const ReportTable = ({ title, data, headers, keys, loading }) => {
  console.log(`ReportTable (${title}): Renderizando con data:`, data, "y loading =", loading);
  if (loading) return <Typography>Cargando reporte...</Typography>;
  if (!data || data.length === 0)
    return <Typography>No se encontraron datos para {title.toLowerCase()}.</Typography>;

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {title}
      </Typography>
      <Table>
        <TableHead>
          <TableRow>
            {headers.map((head, i) => (
              <TableCell key={i}>{head}</TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((row, i) => (
            <TableRow key={i}>
              {keys.map((key, j) => (
                <TableCell key={j}>{row[key]}</TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </>
  );
};

export default Reportes;