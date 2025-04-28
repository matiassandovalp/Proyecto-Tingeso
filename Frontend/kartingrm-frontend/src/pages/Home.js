import { Button, Typography } from "@mui/material";

const Home = () => {
  return (
    <div>
      <Typography variant="h1" color="primary">
        Bienvenido a KartingRM
      </Typography>
      <Button variant="contained" color="secondary">
        Ver Reservas
      </Button>
    </div>
  );
};

export default Home;