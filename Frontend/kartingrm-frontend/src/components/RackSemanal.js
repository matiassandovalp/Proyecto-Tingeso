import React, { useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import axios from 'axios';

const RackSemanal = () => {
  const [eventos, setEventos] = useState([]);

  // Función para formatear la fecha en "yyyy-MM-dd"
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  // Función para consultar eventos en el rango dado
  const fetchEventos = (start, end) => {
    // Convertir el final para que incluya hasta el último momento del día
    let fechaInicio = formatDate(start);
    let fechaFin = formatDate(end);

    // Construimos la URL (asegúrate de que tu backend esté esperando estas fechas)
    const url = `http://localhost:8090/api/rack/disponibilidad?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    console.log("Consultando URL:", url);

    axios
      .get(url)
      .then((response) => {
        console.log("Respuesta recibida:", response.data);
        setEventos(response.data);
      })
      .catch((error) => {
        console.error('Error al cargar la disponibilidad del rack:', error);
      });
  };

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
        slotMinTime="08:00:00"
        slotMaxTime="20:00:00"
        allDaySlot={false}
        events={eventos}
        eventColor="#ff5722"
        eventTextColor="white"
        eventDidMount={(info) => {
          info.el.style.padding = "4px 6px";
          info.el.style.borderRadius = "4px";
          info.el.style.fontSize = "12px";
        }}
        eventContent={(args) => {
          return {
            html: `<div style="text-align:left; font-weight:bold; padding:2px 4px;">${args.event.title}</div>`
          };
        }}
        // La callback que se llama cada vez que cambia el rango visible.
        datesSet={(dateInfo) => {
          // dateInfo contiene .start y .end (objetos Date)
          console.log("Nuevo rango visible:", dateInfo.start, dateInfo.end);
          // Aquí se actualizan los eventos consultando el backend (nota: "end" normalmente es EXCLUSIVO)
          // Por ello, podrías restar un milisegundo a "end" si tu backend espera un rango inclusivo.
          const adjustedEnd = new Date(dateInfo.end.getTime() - 1);
          fetchEventos(dateInfo.start, adjustedEnd);
        }}
      />
    </div>
  );
};

export default RackSemanal;