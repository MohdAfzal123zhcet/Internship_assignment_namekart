import axios from "axios";

const baseURL =import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";


export const http = axios.create({ baseURL });

// Attach Authorization: Bearer <token>
http.interceptors.request.use((cfg) => {
  const token = localStorage.getItem("token");
  if (token) {
    cfg.headers = cfg.headers || {};
    cfg.headers.Authorization = `Bearer ${token}`;
  }
  return cfg;
});
