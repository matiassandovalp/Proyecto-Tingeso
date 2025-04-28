import axios from "axios";

const BASE_URL = "http://localhost:8090/api/comprobantes";

export const getComprobantes = async () => {
    try {
        const response = await axios.get(`${BASE_URL}`);
        console.log("Comprobantes obtenidos:", response.data);
        return response.data;
    } catch (error) {
        console.error("Error al obtener comprobantes:", error);
        return [];
    }
};

export const getComprobanteByReservaId = async (reservaId) => {
    try {
        const response = await axios.get(`${BASE_URL}?reservaId=${reservaId}`, {
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        });
        console.log(`Comprobante obtenido para reserva ${reservaId}:`, response.data);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener comprobante para reserva ${reservaId}:`, error);
        return null;
    }
};