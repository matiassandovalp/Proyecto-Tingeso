import React from 'react';
import { Container, Grid, Typography, Divider, Box } from '@mui/material';
import ReservaForm from "../components/ReservaForm";
import BuscarReservas from "../components/BuscarReservas";

const Cliente = () => {
  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Título alineado a la izquierda */}
      <Typography variant="h4" align="left" gutterBottom>
        Gestión de Reservas
      </Typography>

      <Grid container spacing={4}>
        {/* Columna izquierda: Consultar Reservas */}
        <Grid item xs={12} md={5}>
          <Box sx={{ height: '100%' }}>
            <BuscarReservas />
          </Box>
        </Grid>

        {/* Divisor vertical solo visible en pantallas md+ */}
        <Grid item md={1} sx={{ display: { xs: 'none', md: 'flex' }, justifyContent: 'center' }}>
          <Divider orientation="vertical" flexItem />
        </Grid>

        {/* Columna derecha: Crear Reserva */}
        <Grid item xs={12} md={6}>
          <Box sx={{ height: '100%' }}>
            <ReservaForm />
          </Box>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Cliente;
