import { Link } from "react-router-dom";
import "../styles/Navbar.css";
const Navbar = () => {
  return (
    <nav className="navbar">
      <h1>KartingRM</h1>
      <ul className="nav-links">
        <li><Link to="/">Inicio</Link></li>
        <li><Link to="/dashboard">Dashboard</Link></li>
        <li><Link to="/cliente">Clientes</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;