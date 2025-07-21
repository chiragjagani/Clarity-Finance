import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/state/authStore";
import { register } from "@/api/auth";
import { Input } from "@/components/ui/input";
import { Card } from "@/components/ui/card";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [confirm, setConfirm] = React.useState("");
  const [error, setError] = React.useState("");
  const [loading, setLoading] = React.useState(false);
  const [name, setName] = React.useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    if (password !== confirm) {
      setError("Passwords do not match");
      return;
    }
    setLoading(true);
    try {
      const data = await register(name, email, password);
      useAuthStore.getState().setAuth(data.token, { id: data.id, email: data.email, role: data.role });
      navigate("/dashboard");
    } catch (err: unknown) {
      let msg = "Registration failed";
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
            <svg width="48" height="48" fill="none" viewBox="0 0 48 48" className="mb-2 text-indigo-500"><rect x="6" y="10" width="36" height="28" rx="6" fill="#eef2ff" stroke="currentColor" strokeWidth="3" /><path d="M24 20v8" stroke="#6366f1" strokeWidth="2" strokeLinecap="round" /><circle cx="24" cy="18" r="3" stroke="#6366f1" strokeWidth="2" /></svg>
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-1">Create your account</h1>
            <p className="text-gray-500 text-sm">Sign up to get started.</p>
          </div>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Name</label>
              <Input
                id="name"
                type="text"
                autoComplete="name"
                required
                value={name}
                onChange={e => setName(e.target.value)}
                className="w-full border border-gray-300 dark:border-zinc-700 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-transparent"
              />
            </div>
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
                autoComplete="new-password"
                required
                value={password}
                onChange={e => setPassword(e.target.value)}
                className="w-full border border-gray-300 dark:border-zinc-700 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-transparent"
              />
            </div>
            <div>
              <label htmlFor="confirm" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">Confirm Password</label>
              <Input
                id="confirm"
                type="password"
                autoComplete="new-password"
                required
                value={confirm}
                onChange={e => setConfirm(e.target.value)}
                className="w-full border border-gray-300 dark:border-zinc-700 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-transparent"
              />
            </div>
            {error && <div className="text-red-500 text-sm text-center">{error}</div>}
            <button
              type="submit"
              className="w-full py-2 mt-2 rounded-lg bg-indigo-500 hover:bg-indigo-600 text-white font-semibold text-lg transition disabled:opacity-60"
              disabled={loading}
            >
              {loading ? "Registering..." : "Register"}
            </button>
          </form>
          <div className="mt-6 text-center">
            <span className="text-gray-500 text-sm">Already have an account? </span>
            <button
              type="button"
              className="text-indigo-600 hover:underline font-medium text-sm"
              onClick={() => navigate("/login")}
            >
              Sign in
            </button>
          </div>
        </Card>
      </div>
    </div>
  );
}