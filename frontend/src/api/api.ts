import axios from "axios";
import { useAuthStore } from "../store/useAuthStore";

const baseURL = import.meta.env.DEV
  ? import.meta.env.VITE_API_BASE_URL || "http://localhost:4004"
  : "";
const api = axios.create({ baseURL });

api.interceptors.request.use(
  (config) => {
    const token = useAuthStore.getState().token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
