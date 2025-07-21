import React from "react";
import { useAuthStore } from "@/state/authStore";
import { MenuIcon } from "lucide-react";

export default function Header({ onMenuClick }: { onMenuClick?: () => void }) {
  const user = useAuthStore((s) => s.user);
  return (
    <header className="fixed top-0 left-0 right-0 h-16 bg-white/90 dark:bg-zinc-950/90 border-b border-gray-200 dark:border-zinc-800 flex items-center px-4 md:px-8 z-40 shadow-sm backdrop-blur-md">
      {/* Hamburger for mobile */}
      <button
        className="md:hidden mr-3 p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-zinc-800 transition"
        onClick={onMenuClick}
        aria-label="Open sidebar"
        type="button"
      >
        <MenuIcon className="w-6 h-6 text-gray-900 dark:text-white" />
      </button>
      <div className="flex items-center gap-3">
        {/* Logo (simple SVG or text) */}
        <div className="w-8 h-8 rounded-lg bg-indigo-600 flex items-center justify-center text-white font-bold text-xl shadow-sm select-none">
          F
        </div>
        <span className="font-black text-xl tracking-tight text-gray-900 dark:text-white select-none">FinApp</span>
      </div>
      <div className="flex-1" />
      <div className="flex items-center gap-4">
        {/* User avatar */}
        <div className="w-10 h-10 rounded-full bg-indigo-100 dark:bg-zinc-800 flex items-center justify-center text-indigo-700 dark:text-indigo-200 font-bold text-lg uppercase border border-indigo-200 dark:border-zinc-700">
          {user?.email?.[0] || "U"}
        </div>
      </div>
    </header>
  );
} 