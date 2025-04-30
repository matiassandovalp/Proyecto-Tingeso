import React, { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import axios from 'axios';

const RackSemanal = () => {
  const [eventos, setEventos] = useState([]);

  useEffect(() => {
    console.log("RackSemanal: useEffect iniciado");

    // Determinar el rango de fechas para la semana actual.
    const now = new Date();
    console.log("Fecha actual:", now);
    const day = now.getDay();
    console.log("Día actual (0=Dom, 1=Lun, ...):", day);

    // En JavaScript, el domingo es 0 y el lunes 1.
    const diffToMonday = day === 0 ? -6 : 1 - day;
    const startDate = new Date(now);
    startDate.setDate(now.getDate() + diffToMonday);
    const endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + 6);

    console.log("Fecha de inicio de la semana:", startDate);
    console.log("Fecha de fin de la semana:", endDate);

    // Función para formatear la fecha en "yyyy-MM-dd"
    const formatDate = (date) => {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    };

    const fechaInicio = formatDate(startDate);
    const fechaFin = formatDate(endDate);
    console.log(`Fecha formateada -> inicio: ${fechaInicio}, fin: ${fechaFin}`);

    const url = `http://localhost:8090/api/rack/disponibilidad?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    console.log("Consultando URL:", url);

    // Realizar la consulta al endpoint del backend.
    axios
      .get(url)
      .then((response) => {
        console.log("Respuesta recibida:", response.data);
        setEventos(response.data);
      })
      .catch((error) => {
        console.error('Error al cargar la disponibilidad del rack:', error);
      });
  }, []);

  return (
    <div style={{ padding: '20px' }}>
      <h4>Rack Semanal</h4>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView="timeGridWeek"
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay',
        }}
        // Limitar la visualización a un rango horario (p. ej. operativo de 08:00 a 20:00)
        slotMinTime="08:00:00"
        slotMaxTime="20:00:00"
        allDaySlot={false}
        // Pasamos los eventos obtenidos del backend
        events={eventos}
        // Establece un color de fondo para los bloques reservados
        eventColor="#ff5722"
        eventTextColor="white"
        // Modificación de estilos en cada evento al montarlo
        eventDidMount={(info) => {
          info.el.style.padding = "4px 6px";
          info.el.style.borderRadius = "4px";
          info.el.style.fontSize = "12px";
        }}
        // Renderización personalizada: alineamos el texto a la izquierda para mostrar los karts.
        eventContent={(args) => {
          return {
            html: `<div style="text-align:left; font-weight:bold; padding:2px 4px;">${args.event.title}</div>`
          };
        }}
      />
    </div>
  );
};

export default RackSemanal;