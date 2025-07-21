import { ReloadIcon } from "@radix-ui/react-icons";

export default function Loader({ className = "" }: { className?: string }) {
  return (
    <div className={`flex items-center justify-center ${className}`}>
      <ReloadIcon className="animate-spin h-6 w-6 text-primary" />
    </div>
  );
}