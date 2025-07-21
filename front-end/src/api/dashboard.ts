import axios from "axios";
const API = axios.create({ baseURL: import.meta.env.VITE_API_URL });

export const getDashboardSummary = async () => {
  const token = localStorage.getItem("token");
  const resp = await API.get("/dashboard/summary", {
    headers: { Authorization: token ? `Bearer ${token}` : undefined },
  });
  return resp.data;
};

// Simulated endpoint. Replace this with your real API when available.
export const getCategoryBreakdown = async () => [
  { category: "Food", value: 1200 },
  { category: "Travel", value: 700 },
  { category: "Shopping", value: 400 },
  { category: "Bills", value: 300 }
];

// Simulated AI tips. Replace with `/api/ai/tips` when backend supports it.
export const getAiTips = async () => [
  "You spent 46% of your budget on Food.",
  "Consider reducing restaurant expenses.",
  "You are on track to stay within your budget this month."
];