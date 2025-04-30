import { Box, Button, Typography, Container } from "@mui/material";

const Home = () => {
  return (
    <Container maxWidth="md">
      <Box
        sx={{
          minHeight: "80vh",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          textAlign: "center",
          gap: 4,
        }}
      >
        <Typography variant="h3" color="primary" fontWeight={700}>
          Bienvenido a KartingRM
        </Typography>
        <Typography variant="h6" color="textSecondary">
          Administra tus reservas de karting de forma rápida y sencilla.
        </Typography>
        <Button variant="contained" color="secondary" size="large">
          Ver Reservas
        </Button>
      </Box>
    </Container>
  );
};

export default Home;
