// src/components/ComprobanteButton.js
import React from "react";
import { Button } from "@mui/material";
import { generarPdfComprobante } from "../utils/pdfUtils";

const ComprobanteButton = ({ comprobante, reservaId, reservaInfo }) => {
  if (!comprobante || !reservaId || !reservaInfo) return null;

  return (
    <Button
      variant="contained"
      color="success"
      sx={{ marginTop: 2 }}
      onClick={() => generarPdfComprobante(comprobante, reservaId, reservaInfo)}
    >
      Descargar comprobante (PDF)
    </Button>
  );
};

export default ComprobanteButton;