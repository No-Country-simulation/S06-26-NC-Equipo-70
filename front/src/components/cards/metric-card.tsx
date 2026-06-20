import type { LucideIcon } from "lucide-react";
import { ArrowDownRight, ArrowUpRight } from "lucide-react";
import { cn } from "@/lib/utils";

export interface MetricCardProps {
  label: string;
  value: string;
  icon: LucideIcon;
  /** signed percentage change, e.g. -3.2 */
  delta?: number;
  /** whether a downward delta is good (e.g. risk going down) */
  invertDelta?: boolean;
  hint?: string;
  accent?: "primary" | "critical" | "warning" | "healthy";
  selected?: boolean;
  onClick?: () => void;
}

const accentMap = {
  primary: "bg-primary/12 text-primary",
  critical: "bg-critical/12 text-critical",
  warning: "bg-warning/15 text-warning",
  healthy: "bg-healthy/12 text-healthy",
};

export function MetricCard({
  label,
  value,
  icon: Icon,
  delta,
  invertDelta,
  hint,
  accent = "primary",
  selected,
  onClick,
}: MetricCardProps) {
  const positive = delta != null && delta >= 0;
  const good = invertDelta ? !positive : positive;
  const Interactive = onClick ? "button" : "div";

  return (
    <Interactive
      onClick={onClick}
      className={cn(
        "group relative flex w-full flex-col rounded-2xl border bg-card p-5 text-left transition-all",
        onClick && "hover:border-primary/40 hover:shadow-sm",
        selected ? "border-primary ring-2 ring-primary/30" : "border-border",
      )}
    >
      <div className="flex items-start justify-between">
        <span className="text-sm font-medium text-muted-foreground">{label}</span>
        <span
          className={cn("flex size-9 items-center justify-center rounded-xl", accentMap[accent])}
        >
          <Icon className="size-4.5" />
        </span>
      </div>

      <div className="mt-4 flex items-end gap-2">
        <span className="text-3xl font-semibold tracking-tight tabular-nums">{value}</span>
        {delta != null && (
          <span
            className={cn(
              "mb-1 inline-flex items-center gap-0.5 text-xs font-medium",
              good ? "text-healthy" : "text-critical",
            )}
          >
            {positive ? (
              <ArrowUpRight className="size-3.5" />
            ) : (
              <ArrowDownRight className="size-3.5" />
            )}
            {Math.abs(delta)}%
          </span>
        )}
      </div>

      {hint && <p className="mt-1.5 text-xs text-muted-foreground">{hint}</p>}
    </Interactive>
  );
}
