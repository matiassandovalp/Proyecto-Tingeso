import { jsPDF } from "jspdf";

// Función para formatear fechas
export const formatoFecha = (fechaISO) => {
  return new Date(fechaISO).toLocaleDateString("es-ES", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit"
  });
};

// Función que genera el PDF del comprobante
export const generarPdfComprobante = (comprobante = {}, reservaId, reservaInfo = {}) => {
  if (!reservaId || !comprobante || !reservaInfo) {
    console.error("Error: Datos de la reserva o comprobante inválidos.");
    return;
  }

  const doc = new jsPDF();
  let y = 10;

  doc.setFontSize(16);
  doc.text("Comprobante de Reserva de Karting", 10, y);
  y += 10;

  doc.setFontSize(12);
  const fechaEmision = formatoFecha(reservaInfo.fecha ?? new Date());
  doc.text(`Reserva ID: ${reservaId}`, 10, y); y += 8;
  doc.text(`Cliente ID: ${reservaInfo.clienteId ?? "N/D"}`, 10, y); y += 8;
  doc.text(`Fecha de la reserva: ${fechaEmision}`, 10, y); y += 8;
  doc.text(`Tarifa seleccionada: ${reservaInfo.tarifaSeleccionada ?? "N/D"}`, 10, y); y += 10;

  const nombres = reservaInfo.nombresPersonas ?? [];
  const karts = reservaInfo.kartIds ?? [];

  doc.setFont("helvetica", "bold");
  doc.text("Participantes:", 10, y); y += 8;
  doc.setFont("helvetica", "normal");
  nombres.forEach((nombre) => {
    doc.text(`- ${nombre}`, 12, y);
    y += 6;
  });

  y += 4;
  doc.setFont("helvetica", "bold");
  doc.text("Karts asignados:", 10, y); y += 8;
  doc.setFont("helvetica", "normal");
  karts.forEach((kart) => {
    doc.text(`- ${kart}`, 12, y);
    y += 6;
  });

  y += 4;
  doc.setFont("helvetica", "bold");
  doc.text("Detalles del Pago:", 10, y); y += 8;
  doc.setFont("helvetica", "normal");
  doc.text(`- Precio base total: $${comprobante.precioEstandar ?? "N/D"}`, 10, y); y += 8;
  doc.text(`- Descuento por grupo: -$${comprobante.descuentoPersonas ?? 0}`, 10, y); y += 8;
  doc.text(`- Descuento por frecuencia: -$${comprobante.descuentoPersonal ?? 0}`, 10, y); y += 8;
  doc.text(`- Precio ajustado (sin IVA): $${comprobante.precioAjustado ?? "N/D"}`, 10, y); y += 8;
  doc.text(`- IVA (19%): $${comprobante.valorIVA ?? "N/D"}`, 10, y); y += 8;
  doc.text(`----------------------------------------------`, 10, y); y += 8;
  doc.text(`Total a pagar: $${comprobante.precioFinal ?? "N/D"}`, 10, y); y += 10;

  doc.setFont("helvetica", "bold");
  doc.text("Detalles de la experiencia:", 10, y); y += 8;
  doc.setFont("helvetica", "normal");
  doc.text(`- Número de vueltas: ${comprobante.numeroVueltas ?? "N/D"}`, 10, y); y += 8;

  if (reservaInfo.esCumpleaños) {
    y += 4;
    doc.setTextColor(200, 0, 0);
    doc.text("¡Reserva marcada como cumpleaños!", 10, y);
    doc.setTextColor(0, 0, 0);
  }

  doc.save(`comprobante_${reservaId}.pdf`);
};