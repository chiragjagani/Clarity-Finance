import React from "react";
import { HandCoinsIcon } from "lucide-react";
export default function BorrowedPage() {
  return (
    <div className="flex flex-col items-center justify-center h-full">
      <HandCoinsIcon className="w-12 h-12 text-indigo-500 mb-4" />
      <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">Borrowed</h1>
      <p className="text-gray-500 dark:text-gray-400">This is the Borrowed page.</p>
    </div>
  );
} 