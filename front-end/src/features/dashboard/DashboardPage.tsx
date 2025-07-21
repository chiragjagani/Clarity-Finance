import { Card } from "@/components/ui/card";
import Loader from "@/components/Loader";
import { useAuthStore } from "@/state/authStore";
import { useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { getDashboardSummary, getCategoryBreakdown, getAiTips } from "@/api/dashboard";
import SpendingsPie from "@/components/charts/SpendingsPie";
import { formatMoney } from "@/utils/formatMoney";
import React, { useRef } from "react";
import { motion } from "framer-motion";
import {
  WalletIcon,
  CreditCardIcon,
  PiggyBankIcon,
  PieChartIcon,
  BotIcon,
  TagIcon,
  GemIcon,
  ArrowLeftRightIcon
} from "lucide-react";

export default function DashboardPage() {
  const { token } = useAuthStore();
  const navigate = useNavigate();

  const { data, isLoading, isError } = useQuery({
    queryKey: ["dashboard-summary"],
    queryFn: getDashboardSummary,
    staleTime: 60_000,
  });
  const { data: catData } = useQuery({
    queryKey: ["category-breakdown"],
    queryFn: getCategoryBreakdown,
    staleTime: 120_000,
  });
  const { data: tips } = useQuery({
    queryKey: ["ai-tips"],
    queryFn: getAiTips,
    staleTime: 120_000,
  });

  if (!token) {
    navigate("/login");
    return null;
  }
  if (isLoading) return <Loader className="min-h-screen" />;
  if (isError || !data) return <div className="text-center py-16 text-xl">Could not load dashboard data.</div>;

  return (
    <div className="w-full max-w-7xl mx-auto flex flex-col flex-1 h-full min-h-0 max-h-none justify-center items-center px-0 md:px-2 py-0 md:py-2 overflow-hidden">
      {/* Stat Cards Row */}
      <section className="w-full grid grid-cols-3 gap-4 mb-2">
        <AnimatedStatCard icon={<WalletIcon className="w-6 h-6 text-[#3f51b5]" />} iconBg="bg-[#e3f2fd]" label="Total Budget" value={formatMoney(data.totalBudget ?? 0)} accent="text-[#3f51b5]" delay={0} compact />
        <AnimatedStatCard icon={<CreditCardIcon className="w-6 h-6 text-[#e81e63]" />} iconBg="bg-[#fce4ec]" label="Total Spent" value={formatMoney(data.totalSpent ?? 0)} accent="text-[#e81e63]" delay={0.08} compact />
        <AnimatedStatCard icon={<PiggyBankIcon className="w-6 h-6 text-[#4caf50]" />} iconBg="bg-[#e8f5e9]" label="Remaining After Rent" value={formatMoney(data.remainingAfterRent ?? 0)} accent="text-[#4caf50]" delay={0.16} compact />
      </section>
      {/* Chart + AI Tips Row */}
      <motion.section className="w-full grid grid-cols-2 gap-4 mb-2" initial="hidden" animate="visible" variants={{ visible: { transition: { staggerChildren: 0.12 } } }}>
        <motion.div variants={{ hidden: { opacity: 0, y: 32 }, visible: { opacity: 1, y: 0 } }}>
          <Card className="p-4 rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-zinc-900 shadow-sm min-h-[180px] flex flex-col justify-between">
            <div className="flex items-center gap-2 mb-2">
              <span className="inline-flex items-center justify-center w-8 h-8 rounded-full bg-[#e3f2fd] mr-2"><PieChartIcon className="w-5 h-5 text-[#2196f3]" /></span>
              <h2 className="text-lg font-bold text-[#2196f3] dark:text-[#e3f2fd] tracking-tight leading-tight">Spending by Category</h2>
            </div>
            <motion.div initial={{ scale: 0.97, opacity: 0 }} animate={{ scale: 1, opacity: 1 }} transition={{ duration: 0.6, delay: 0.1, ease: "easeOut" }}>
              <SpendingsPie data={catData ?? []} />
            </motion.div>
            {!catData?.length && <div className="text-xs text-gray-500 dark:text-gray-400 pt-4 text-center">No category data yet</div>}
          </Card>
        </motion.div>
        <motion.div variants={{ hidden: { opacity: 0, y: 32 }, visible: { opacity: 1, y: 0 } }}>
          <Card className="p-4 rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-zinc-900 shadow-sm min-h-[180px] flex flex-col justify-between">
            <div className="flex items-center gap-2 mb-2">
              <span className="inline-flex items-center justify-center w-8 h-8 rounded-full bg-[#e0f7fa] mr-2"><BotIcon className="w-5 h-5 text-[#00bcd4]" /></span>
              <h2 className="text-lg font-bold text-[#00bcd4] dark:text-[#e0f7fa] tracking-tight leading-tight">AI Finance Insights</h2>
            </div>
            {tips?.length ? (
              <ul className="list-disc pl-5 space-y-1 text-gray-700 dark:text-gray-200 text-sm">
                {tips.map((t: string) => (
                  <li key={t}>{t}</li>
                ))}
              </ul>
            ) : (
              <div className="text-xs text-gray-500 dark:text-gray-400 py-4 text-center">No AI tips yet</div>
            )}
          </Card>
        </motion.div>
      </motion.section>
      {/* Details Row */}
      <section className="w-full grid grid-cols-3 gap-4">
        <AnimatedStatCard icon={<TagIcon className="w-6 h-6 text-[#ffc107]" />} iconBg="bg-[#fff8e1]" label="Most Spent Category" value={data.mostSpentCategory || 'N/A'} accent="text-[#ffc107]" delay={0} compact />
        <AnimatedStatCard icon={<GemIcon className="w-6 h-6 text-[#9c27b0]" />} iconBg="bg-[#f3e5f5]" label="Most Expensive Item" value={data.mostExpensiveItem || 'N/A'} accent="text-[#9c27b0]" delay={0.08} compact />
        <AnimatedStatCard icon={<ArrowLeftRightIcon className="w-6 h-6 text-[#009688]" />} iconBg="bg-[#e0f2f1]" label="Borrowed vs Lent" value={<BorrowedLentValue borrowed={data.totalBorrowed} lent={data.totalLent} compact />} accent="text-[#009688]" delay={0.16} compact />
      </section>
    </div>
  );
}

function AnimatedStatCard({ icon, iconBg, label, value, accent, delay, compact }: { icon: React.ReactNode; iconBg: string; label: string; value: React.ReactNode; accent: string; delay: number; compact?: boolean }) {
  const ref = useRef<HTMLDivElement>(null);
  const [rotate, setRotate] = React.useState({ x: 0, y: 0 });

  function handleMouseMove(e: React.MouseEvent<HTMLDivElement, MouseEvent>) {
    const card = ref.current;
    if (!card) return;
    const rect = card.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    const centerX = rect.width / 2;
    const centerY = rect.height / 2;
    // Max tilt: 10deg
    const rotateY = ((x - centerX) / centerX) * 10;
    const rotateX = ((centerY - y) / centerY) * 10;
    setRotate({ x: rotateX, y: rotateY });
  }

  function handleMouseLeave() {
    setRotate({ x: 0, y: 0 });
  }

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 24, scale: 0.97 }}
      animate={{ opacity: 1, y: 0, scale: 1, rotateX: 0, rotateY: 0 }}
      whileHover={{
        scale: 1.035,
        boxShadow: "0 4px 16px 0 rgba(31, 38, 135, 0.12)",
        filter: "brightness(1.06)",
        rotateX: rotate.x,
        rotateY: rotate.y,
        transition: { type: "spring", stiffness: 120, damping: 18 },
      }}
      transition={{ duration: 0.5, delay, ease: "easeOut" }}
      style={{ perspective: 1000, transformStyle: "preserve-3d" }}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      className={`rounded-xl border border-gray-200 dark:border-zinc-800 bg-white dark:bg-zinc-900 shadow-md flex flex-row items-center gap-3 cursor-pointer select-none transition-all min-h-[80px] justify-start p-3 ${compact ? 'h-[90px]' : 'p-5 md:p-6 min-h-[100px]'}`}
    >
      <span className={`inline-flex items-center justify-center w-10 h-10 rounded-full ${iconBg} mr-2`}>{icon}</span>
      <div className="flex flex-col gap-0.5">
        <span className={`text-xs md:text-sm text-gray-600 dark:text-gray-300 font-semibold mb-0.5 tracking-tight`}>{label}</span>
        <span className={`text-xl md:text-2xl font-extrabold ${accent}`}>{value}</span>
      </div>
    </motion.div>
  );
}

function BorrowedLentValue({ borrowed, lent, compact }: { borrowed: number; lent: number; compact?: boolean }) {
  return (
    <div className="flex flex-col gap-0.5">
      <span className={`text-pink-600 ${compact ? 'text-base md:text-lg' : 'text-lg md:text-2xl'} font-bold leading-tight`}>Borrowed: {formatMoney(borrowed ?? 0)}</span>
      <span className={`text-green-600 ${compact ? 'text-base md:text-lg' : 'text-lg md:text-2xl'} font-bold leading-tight`}>Lent: {formatMoney(lent ?? 0)}</span>
    </div>
  );
}