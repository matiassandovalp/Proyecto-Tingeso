import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#1976d2",  // Azul principal
    },
    secondary: {
      main: "#ffcc00",  // Amarillo secundario
    },
    background: {
      default: "#f4f4f4",  // Fondo gris claro
    },
  },
  typography: {
    fontFamily: "Roboto, Arial, sans-serif",
    h1: {
      fontSize: "2rem",
      fontWeight: "bold",
    },
    button: {
      textTransform: "none",
    },
  },
});

export default theme;