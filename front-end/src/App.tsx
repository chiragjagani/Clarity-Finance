import { Navigate, Route, Routes, useLocation } from "react-router-dom";
import LoginPage from "./features/auth/LoginPage";
import RegisterPage from "./features/auth/RegisterPage";
import DashboardPage from "./features/dashboard/DashboardPage";
import ExpensesPage from "./features/expenses/ExpensesPage";
import BorrowedPage from "./features/borrowed/BorrowedPage";
import LentPage from "./features/lent/LentPage";
import StockPage from "./features/stock/StockPage";
import { AnimatePresence, motion } from "framer-motion";
import Layout from "./components/Layout";

export default function App() {
  const location = useLocation();
  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        {/* Auth pages (no layout) */}
        <Route
          path="/login"
          element={
            <motion.div initial={{ opacity: 0, y: 32 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -32 }} transition={{ duration: 0.35, ease: "easeInOut" }}>
              <LoginPage />
            </motion.div>
          }
        />
        <Route
          path="/register"
          element={
            <motion.div initial={{ opacity: 0, y: 32 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -32 }} transition={{ duration: 0.35, ease: "easeInOut" }}>
              <RegisterPage />
            </motion.div>
          }
        />
        {/* Main app layout */}
        <Route element={<Layout />}> 
          <Route
            path="/dashboard"
            element={<DashboardPage />}
          />
          <Route
            path="/expenses"
            element={<ExpensesPage />}
          />
          <Route
            path="/borrowed"
            element={<BorrowedPage />}
          />
          <Route
            path="/lent"
            element={<LentPage />}
          />
          <Route
            path="/stock"
            element={<StockPage />}
          />
          <Route path="/" element={<Navigate to="/dashboard" />} />
        </Route>
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </AnimatePresence>
  );
}