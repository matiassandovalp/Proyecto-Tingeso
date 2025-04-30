import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar"; 
import Home from "./pages/Home";
import Reservas from "./pages/Reservas";
import Reportes from "./pages/Reportes";
import Error404 from "./pages/Error404";
import Dashboard from "./pages/Dashboard";
import Cliente from "./pages/Cliente";
import RackSemanal from "./components/RackSemanal";
import { ThemeProvider } from "@mui/material/styles";
import theme from "./theme";

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Navigate to="/home" />} />
          <Route path="/home" element={<Home />} />
          <Route path="/rack" element={<RackSemanal />} />
          <Route path="/reservas" element={<Reservas />} />
          <Route path="/cliente" element={<Cliente />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/reportes" element={<Reportes />} />
          <Route path="*" element={<Error404 />} />   {/* Página 404 */}
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
