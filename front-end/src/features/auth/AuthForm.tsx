import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { motion } from "framer-motion";
import type { ReactNode } from "react";
import type { SubmitHandler } from "react-hook-form";

type Props = {
  onSubmit: (values: any) => void | Promise<void>;
  children: ReactNode;
  title: string;
  submitLabel: string;
  footer?: ReactNode;
  isLoading?: boolean;
};

export default function AuthForm({
  onSubmit,
  children,
  title,
  submitLabel,
  footer,
  isLoading,
}: Props) {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-[#2e026d] to-[#15162c]">
      <motion.div
        initial={{ opacity: 0, y: 32 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, type: "spring" }}
        className="w-[350px] xs:w-[90%] max-w-sm"
      >
        <Card>
          <CardHeader>
            <CardTitle className="text-center text-3xl font-bold">{title}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={onSubmit} className="space-y-4">{children}
              <Button type="submit" className="w-full mt-2" disabled={isLoading}>
                {isLoading ? "Loading..." : submitLabel}
              </Button>
            </form>
            {footer}
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}