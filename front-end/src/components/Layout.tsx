import React from "react";
import { NavLink, Outlet, useNavigate, useLocation } from "react-router-dom";
import { useAuthStore } from "@/state/authStore";
import { AnimatePresence, motion } from "framer-motion";
import Header from "./Header";
import {
  HomeIcon,
  ReceiptTextIcon,
  HandCoinsIcon,
  HandshakeIcon,
  LineChartIcon,
  LogOutIcon
} from "lucide-react";

const navItems = [
  { to: "/dashboard", label: "Home", icon: <HomeIcon className="w-5 h-5 mr-3 text-[#3f51b5]" /> },
  { to: "/expenses", label: "Expenses", icon: <ReceiptTextIcon className="w-5 h-5 mr-3 text-[#e81e63]" /> },
  { to: "/borrowed", label: "Borrowed", icon: <HandCoinsIcon className="w-5 h-5 mr-3 text-[#4caf50]" /> },
  { to: "/lent", label: "Lent", icon: <HandshakeIcon className="w-5 h-5 mr-3 text-[#ffc107]" /> },
  { to: "/stock", label: "Stock", icon: <LineChartIcon className="w-5 h-5 mr-3 text-[#00bcd4]" /> },
];

export default function Layout() {
  const navigate = useNavigate();
  const location = useLocation();
  const logout = useAuthStore((s) => s.logout);
  const user = useAuthStore((s) => s.user);
  const [sidebarOpen, setSidebarOpen] = React.useState(false);

  function handleLogout() {
    logout();
    navigate("/login");
  }

  function handleNav() {
    setSidebarOpen(false);
  }

  React.useEffect(() => {
    setSidebarOpen(false);
  }, [location.pathname]);

  // Sidebar content as a component for reuse
  const SidebarContent = (
    <>
      {/* Visually connect to header with border and glass effect */}
      <div className="h-16 border-b border-r border-gray-200 dark:border-zinc-800 bg-white/80 dark:bg-zinc-950/80 backdrop-blur-xl" />
      <nav className="flex-1 py-8 flex flex-col gap-1">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `px-7 py-3 text-base font-medium flex items-center transition rounded-xl group relative overflow-hidden outline-none
              focus-visible:ring-2 focus-visible:ring-[#3f51b5] focus-visible:ring-offset-2 focus-visible:z-20
              ${isActive ? "bg-[#e3f2fd] text-[#3f51b5]" : "text-gray-700 dark:text-gray-300 hover:bg-[#fce4ec] hover:text-[#e81e63] dark:hover:bg-zinc-800/80 hover:scale-[1.04] hover:brightness-105"}
              `
            }
            end={item.to === "/dashboard"}
            onClick={handleNav}
            tabIndex={0}
          >
            <motion.span
              layoutId="sidebar-active"
              className="absolute left-0 top-1/2 -translate-y-1/2 h-8 w-1.5 bg-[#3f51b5] rounded-full shadow-lg"
              style={{ opacity: location.pathname.startsWith(item.to) ? 1 : 0 }}
              transition={{ type: "spring", stiffness: 500, damping: 30 }}
            />
            <span className="relative z-10 flex items-center">{item.icon}{item.label}</span>
          </NavLink>
        ))}
        <button
          onClick={() => { handleLogout(); setSidebarOpen(false); }}
          className="mt-auto px-7 py-3 text-base font-medium flex items-center text-[#f44336] hover:bg-[#ffebee] dark:hover:bg-zinc-800/80 transition rounded-xl focus-visible:ring-2 focus-visible:ring-[#f44336] focus-visible:ring-offset-2 focus-visible:z-20 hover:scale-[1.04] hover:brightness-105 outline-none"
          tabIndex={0}
        >
          <LogOutIcon className="w-5 h-5 mr-3 text-[#f44336]" /> Logout
        </button>
      </nav>
      <div className="p-6 border-t border-gray-200 dark:border-zinc-800 text-xs text-gray-400 dark:text-gray-600">
        {user?.email}
      </div>
    </>
  );

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-zinc-900">
      {/* Sidebar for desktop */}
      <aside className="hidden md:flex fixed left-0 top-0 h-full w-64 bg-[#f5f5f5] dark:bg-zinc-950 border-r border-gray-200 dark:border-zinc-800 flex-col z-30 shadow-2xl backdrop-blur-xl transition-all duration-300">
        {SidebarContent}
      </aside>
      {/* Sidebar drawer for mobile */}
      <AnimatePresence>
        {sidebarOpen && (
          <>
            {/* Overlay */}
            <motion.div
              className="fixed inset-0 bg-black/40 z-40 md:hidden"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              transition={{ duration: 0.2 }}
              onClick={() => setSidebarOpen(false)}
            />
            {/* Drawer */}
            <motion.aside
              className="fixed left-0 top-0 h-full w-64 bg-white/90 dark:bg-zinc-950/90 border-r border-gray-200 dark:border-zinc-800 flex flex-col z-50 shadow-2xl backdrop-blur-xl md:hidden"
              initial={{ x: -280 }}
              animate={{ x: 0 }}
              exit={{ x: -280 }}
              transition={{ type: "spring", stiffness: 500, damping: 40 }}
            >
              {SidebarContent}
            </motion.aside>
          </>
        )}
      </AnimatePresence>
      {/* Main area with global header */}
      <div className="flex-1 flex flex-col min-h-0 md:ml-64">
        <Header onMenuClick={() => setSidebarOpen(true)} />
        {/* Main content with animation and balanced spacing */}
        <main className="flex-1 h-full min-h-0 px-2 md:px-8 overflow-hidden max-w-screen-2xl mx-auto w-full">
          <AnimatePresence mode="wait" initial={false}>
            <motion.div
              key={location.pathname}
              initial={{ opacity: 0, y: 32, scale: 0.98, backgroundColor: "#F3F4F6" }}
              animate={{ opacity: 1, y: 0, scale: 1, backgroundColor: "transparent" }}
              exit={{ opacity: 0, y: -32, scale: 0.98, backgroundColor: "#F3F4F6" }}
              transition={{ duration: 0.45, ease: "easeInOut" }}
              className="h-full"
              style={{ borderRadius: 18 }}
            >
              <Outlet />
            </motion.div>
          </AnimatePresence>
        </main>
      </div>
    </div>
  );
} 