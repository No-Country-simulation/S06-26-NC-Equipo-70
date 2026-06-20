import { cn } from "@/lib/utils";

export function ProgressBar({
  value,
  barClassName,
  trackClassName,
  target,
}: {
  value: number;
  barClassName?: string;
  trackClassName?: string;
  target?: number;
}) {
  const clamped = Math.max(0, Math.min(100, value));
  return (
    <div
      className={cn("relative h-2 w-full overflow-hidden rounded-full bg-muted", trackClassName)}
      role="progressbar"
      aria-valuenow={clamped}
      aria-valuemin={0}
      aria-valuemax={100}
    >
      <div
        className={cn("h-full rounded-full bg-primary transition-all", barClassName)}
        style={{ width: `${clamped}%` }}
      />
      {typeof target === "number" && (
        <span
          className="absolute top-0 h-full w-0.5 bg-foreground/40"
          style={{ left: `${Math.min(100, target)}%` }}
          aria-label={`Target ${target}%`}
        />
      )}
    </div>
  );
}
