import axios from "axios";

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

// Optionally set up interceptors for auth headers if needed
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const login = async (email: string, password: string) => {
  const res = await API.post("/auth/login", { email, password });
  return res.data;
};

export const register = async (name: string, email: string, password: string) => {
  const res = await API.post("/auth/register", { name, email, password });
  return res.data;
};