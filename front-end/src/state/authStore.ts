import { create } from "zustand";

interface UserState {
  token: string | null;
  user: { id: number; email: string; role: string } | null;
  setAuth: (token: string, user: UserState["user"]) => void;
  logout: () => void;
}

export const useAuthStore = create<UserState>((set) => ({
  token: localStorage.getItem("token"),
  user: JSON.parse(localStorage.getItem("user") ?? "null"),
  setAuth: (token, user) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(user));
    set({ token, user });
  },
  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    set({ token: null, user: null });
  },
}));