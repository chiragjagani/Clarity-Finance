import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/state/authStore";
import { login } from "@/api/auth";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";

export default function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [error, setError] = React.useState("");
  const [loading, setLoading] = React.useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      const data = await login(email, password);
      useAuthStore.getState().setAuth(data.token, { id: data.id, email: data.email, role: data.role });
      navigate("/dashboard");
    } catch (err: unknown) {
      let msg = "Login failed";
      if (err && typeof err === "object" && "message" in err && typeof (err as { message?: string }).message === "string") {
        msg = (err as { message?: string }).message!;
      }
      setError(msg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-zinc-900">
      <div className="w-full max-w-md mx-auto p-6">
        <Card className="p-8 rounded-2xl shadow-lg border border-gray-200 dark:border-zinc-800 bg-white dark:bg-zinc-900">
          <div className="flex flex-col items-center mb-6">
            {/* Simple SVG Icon */}
            <svg width="48" height="48" fill="none" viewBox="0 0 48 48" className="mb-2 text-indigo-500"><circle cx="24" cy="24" r="22" stroke="currentColor" strokeWidth="3" fill="#eef2ff" /><path d="M16 28c0-4 8-4 8 0v2" stroke="#6366f1" strokeWidth="2" strokeLinecap="round" /><circle cx="24" cy="20" r="4" stroke="#6366f1" strokeWidth="2" /></svg>
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-1">Sign in to your account</h1>
            <p className="text-gray-500 text-sm">Welcome back! Please enter your details.</p>
          </div>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Email</label>
              <Input
                id="email"
                type="email"
                autoComplete="email"
                required
                value={email}
                onChange={e => setEmail(e.target.value)}
                className="w-full border border-gray-300 dark:border-zinc-700 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-transparent"
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Password</label>
              <Input
                id="password"
                type="password"
                autoComplete="current-password"
                required
                value={password}
                onChange={e => setPassword(e.target.value)}
                className="w-full border border-gray-300 dark:border-zinc-700 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-transparent"
              />
            </div>
            {error && <div className="text-red-500 text-sm text-center">{error}</div>}
            <button
              type="submit"
              className="w-full py-2 mt-2 rounded-lg bg-indigo-500 hover:bg-indigo-600 text-white font-semibold text-lg transition disabled:opacity-60"
              disabled={loading}
            >
              {loading ? "Signing in..." : "Sign In"}
            </button>
          </form>
          <div className="mt-6 text-center">
            <span className="text-gray-500 text-sm">Don't have an account? </span>
            <button
              type="button"
              className="text-indigo-600 hover:underline font-medium text-sm"
              onClick={() => navigate("/register")}
            >
              Register
            </button>
          </div>
        </Card>
      </div>
    </div>
  );
}