import axios from "axios";

const baseURL = "http://localhost:8080/api/v1" || "https://internship-assignment-namekart-1.onrender.com";

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
