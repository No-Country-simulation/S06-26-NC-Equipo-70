import { cn } from "@/lib/utils";

export function Skeleton({ className }: { className?: string }) {
  return <div className={cn("animate-pulse rounded-lg bg-muted", className)} aria-hidden="true" />;
}

/** Card-shaped loading skeleton used across screens. */
export function LoadingSkeleton({
  variant = "card",
  rows = 3,
  className,
}: {
  variant?: "card" | "metric" | "list" | "chart";
  rows?: number;
  className?: string;
}) {
  if (variant === "metric") {
    return (
      <div
        className={cn("rounded-2xl border border-border bg-card p-5", className)}
        role="status"
        aria-label="Loading metric"
      >
        <Skeleton className="h-3.5 w-24" />
        <Skeleton className="mt-4 h-8 w-20" />
        <Skeleton className="mt-3 h-3 w-32" />
      </div>
    );
  }

  if (variant === "chart") {
    return (
      <div
        className={cn("rounded-2xl border border-border bg-card p-5", className)}
        role="status"
        aria-label="Loading chart"
      >
        <Skeleton className="h-4 w-40" />
        <div className="mt-6 flex h-32 items-end gap-2">
          {["h-2/5", "h-3/4", "h-1/2", "h-full", "h-3/5", "h-4/5", "h-1/2"].map((h, i) => (
            <Skeleton key={i} className={cn("w-full rounded-md", h)} />
          ))}
        </div>
        <Skeleton className="mt-4 h-3 w-full" />
      </div>
    );
  }

  if (variant === "list") {
    return (
      <div className={cn("flex flex-col gap-3", className)} role="status" aria-label="Loading list">
        {Array.from({ length: rows }).map((_, i) => (
          <div
            key={i}
            className="flex items-center gap-3 rounded-xl border border-border bg-card p-3"
          >
            <Skeleton className="size-9 rounded-lg" />
            <div className="flex-1">
              <Skeleton className="h-3.5 w-32" />
              <Skeleton className="mt-2 h-3 w-20" />
            </div>
            <Skeleton className="h-6 w-12" />
          </div>
        ))}
      </div>
    );
  }

  return (
    <div
      className={cn("rounded-2xl border border-border bg-card p-5", className)}
      role="status"
      aria-label="Loading"
    >
      <Skeleton className="h-4 w-40" />
      <div className="mt-4 flex flex-col gap-3">
        {Array.from({ length: rows }).map((_, i) => (
          <Skeleton key={i} className="h-3.5 w-full" />
        ))}
      </div>
    </div>
  );
}
