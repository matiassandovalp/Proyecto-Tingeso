import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";

const Reportes = () => {
  // Configuración del rango (por defecto, mes actual)
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth();
  
  const getFormattedDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const defaultStartDate = new Date(currentYear, currentMonth, 1);
  const defaultEndDate = new Date(currentYear, currentMonth + 1, 0);
  
  const [fechaInicio, setFechaInicio] = useState(getFormattedDate(defaultStartDate));
  const [fechaFin, setFechaFin] = useState(getFormattedDate(defaultEndDate));
  
  const [reporteIngresos, setReporteIngresos] = useState(null);
  const [reporteReservas, setReporteReservas] = useState(null);

  const fetchReporteIngresos = useCallback(() => {
    const url = `http://localhost:8090/api/reportes/detallado?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    axios.get(url)
      .then(response => {
        setReporteIngresos(response.data);
      })
      .catch(error => {
        console.error("Error al obtener reporte detallado de ingresos:", error);
      });
  }, [fechaInicio, fechaFin]);

  const fetchReporteReservas = useCallback(() => {
    const url = `http://localhost:8090/api/reportes/reservas-detallado?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    axios.get(url)
      .then(response => {
        setReporteReservas(response.data);
      })
      .catch(error => {
        console.error("Error al obtener reporte detallado de reservas:", error);
      });
  }, [fechaInicio, fechaFin]);

  useEffect(() => {
    fetchReporteIngresos();
    fetchReporteReservas();
  }, [fetchReporteIngresos, fetchReporteReservas]);

  // Funciones para navegar entre meses (como antes)
  const handlePrevMonth = () => {
    const start = new Date(fechaInicio + "T00:00:00");
    start.setMonth(start.getMonth() - 1);
    const newStart = new Date(start.getFullYear(), start.getMonth(), 1);
    const newEnd = new Date(start.getFullYear(), start.getMonth() + 1, 0);
    setFechaInicio(getFormattedDate(newStart));
    setFechaFin(getFormattedDate(newEnd));
  };

  const handleNextMonth = () => {
    const start = new Date(fechaInicio + "T00:00:00");
    start.setMonth(start.getMonth() + 1);
    const newStart = new Date(start.getFullYear(), start.getMonth(), 1);
    const newEnd = new Date(start.getFullYear(), start.getMonth() + 1, 0);
    setFechaInicio(getFormattedDate(newStart));
    setFechaFin(getFormattedDate(newEnd));
  };

  return (
    <div style={{ padding: "20px" }}>
      <h4>Reporte Detallado de Ingresos</h4>
      <div
        className="notification-message"
        style={{
          backgroundColor: "#e0f7fa",
          border: "1px solid #4dd0e1",
          padding: "10px",
          borderRadius: "5px",
          marginBottom: "20px",
          fontSize: "0.9rem"
        }}
      >
        <strong>Nota:</strong> El reporte se calcula en función de la{" "}
        <em>fecha de emisión</em> del comprobante (la fecha en que se facturó la reserva).
      </div>
      <p>
        <strong>Período:</strong> {fechaInicio} a {fechaFin}
      </p>
      <div style={{ marginBottom: "10px" }}>
        <label>
          Fecha Inicio:{" "}
          <input
            type="date"
            value={fechaInicio}
            onChange={(e) => setFechaInicio(e.target.value)}
          />
        </label>
        <label style={{ marginLeft: "20px" }}>
          Fecha Fin:{" "}
          <input
            type="date"
            value={fechaFin}
            onChange={(e) => setFechaFin(e.target.value)}
          />
        </label>
      </div>
      <div style={{ marginBottom: "20px" }}>
        <button onClick={handlePrevMonth}>Mes Anterior</button>
        <button onClick={handleNextMonth} style={{ marginLeft: "20px" }}>
          Mes Siguiente
        </button>
      </div>

      {reporteIngresos ? (
        <div>
          <p>
            <strong>Total de Ingresos:</strong> ${reporteIngresos.totalIngreso}
          </p>
          <h5>Ingresos por Tarifa</h5>
          <ul>
            {reporteIngresos.ingresosPorTarifa &&
              Array.from(reporteIngresos.ingresosPorTarifa).map((item, index) => (
                <li key={index}>
                  <strong>Tarifa:</strong> {item.tarifa} -{" "}
                  <strong>Reservas:</strong> {item.reservas} -{" "}
                  <strong>Ingreso:</strong> ${item.totalIngreso}
                </li>
              ))}
          </ul>
          <h5>Ingresos por Grupo de Personas</h5>
          <ul>
            {reporteIngresos.ingresosPorGrupo &&
              Array.from(reporteIngresos.ingresosPorGrupo).map((item, index) => (
                <li key={index}>
                  <strong>Grupo de {item.cantPersonas} personas</strong> -{" "}
                  <strong>Reservas:</strong> {item.reservas} -{" "}
                  <strong>Ingreso:</strong> ${item.totalIngreso}
                </li>
              ))}
          </ul>
        </div>
      ) : (
        <p>Cargando reporte detallado de ingresos...</p>
      )}

      <hr style={{ margin: "40px 0" }} />

      <h4>Reporte Detallado de Reservas</h4>
      {reporteReservas ? (
        <div>
          <p>
            <strong>Total de Reservas:</strong> {reporteReservas.totalReservas}
          </p>
          <ul>
            {reporteReservas.reservas &&
              reporteReservas.reservas.map((reserva, index) => (
                <li key={index}>
                  <strong>Reserva ID:</strong> {reserva.reservaId} |{" "}
                  <strong>Fecha Reserva:</strong>{" "}
                  {new Date(reserva.fechaReserva).toLocaleDateString()} |{" "}
                  <strong>Tarifa:</strong> {reserva.tarifaSeleccionada} |{" "}
                  <strong>Cantidad de Personas:</strong> {reserva.cantPersonas}{" "}
                  {reserva.clienteId && (
                    <>| <strong>Cliente ID:</strong> {reserva.clienteId}</>
                  )}
                </li>
              ))}
          </ul>
        </div>
      ) : (
        <p>Cargando reporte detallado de reservas...</p>
      )}
    </div>
  );
};

export default Reportes;