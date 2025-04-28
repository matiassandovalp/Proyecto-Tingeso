import axios from "axios";

const BASE_URL = "http://localhost:8090/api/reservas";

export const getReservas = async () => {
    try {
        const response = await axios.get(BASE_URL);
        console.log("Respuesta completa del backend:", response);
        return response.data;
    } catch (error) {
        console.error("Error al obtener reservas:", error);
        return [];
    }
};