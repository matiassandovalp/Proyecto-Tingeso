// Dashboard.js
import React, { useState } from "react";
import { AppBar, Tabs, Tab, Box, Toolbar, Typography } from "@mui/material";
import Reservas from "./Reservas";        // Este componente exhibe la lista de reservas
import RackSemanal from "../components/RackSemanal";    // Componente que muestra el calendario de ocupación
import Reportes from "./Reportes";          // Componente para los reportes

const Dashboard = () => {
  const [tabValue, setTabValue] = useState(0);

  const handleTabChange = (event, newValue) => {
    console.log("Dashboard: Cambiando pestaña a", newValue);
    setTabValue(newValue);
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      {/* Barra superior */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Panel Administrativo - KartingRM
          </Typography>
        </Toolbar>
      </AppBar>
      
      {/* Pestañas de navegación */}
      <Tabs
        value={tabValue}
        onChange={handleTabChange}
        variant="fullWidth"
        indicatorColor="primary"
        textColor="primary"
      >
        <Tab label="Reservas" />
        <Tab label="Rack Semanal" />
        <Tab label="Reportes" />
      </Tabs>
      
      {/* Contenido de las pestañas */}
      <Box sx={{ p: 3 }}>
        {tabValue === 0 && (
          <>
            <Typography variant="h5" gutterBottom>
              Lista de Reservas
            </Typography>
            <Reservas />
          </>
        )}
        {tabValue === 1 && (
          <>
            <Typography variant="h5" gutterBottom>
              Rack Semanal
            </Typography>
            <RackSemanal />
          </>
        )}
        {tabValue === 2 && (
          <>
            <Typography variant="h5" gutterBottom>
              Reportes de Ingresos
            </Typography>
            <Reportes />
          </>
        )}
      </Box>
    </Box>
  );
};

export default Dashboard;