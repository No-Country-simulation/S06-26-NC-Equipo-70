import { cn } from "@/lib/utils";
import { SEVERITY_META, type Severity } from "@/lib/data";
import { severityStyles } from "@/lib/severity";

const order: Severity[] = ["critical", "warning", "moderate", "healthy"];

export function MapLegend({ className }: { className?: string }) {
  return (
    <div
      className={cn("rounded-xl border border-border bg-card/90 p-3 backdrop-blur-sm", className)}
    >
      <p className="mb-2 text-[11px] font-semibold uppercase tracking-wide text-muted-foreground">
        Severity
      </p>
      <ul className="flex flex-col gap-1.5">
        {order.map((s) => (
          <li key={s} className="flex items-center gap-2 text-xs">
            <span className={cn("size-2.5 rounded-full", severityStyles[s].dot)} />
            <span className="font-medium">{SEVERITY_META[s].label}</span>
            <span className="ml-auto text-muted-foreground">{SEVERITY_META[s].range}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
