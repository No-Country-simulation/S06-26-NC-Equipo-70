import { cn } from "@/lib/utils";
import { SEVERITY_META, type Severity } from "@/lib/data";
import { severityStyles } from "@/lib/severity";

export function SeverityBadge({
  severity,
  showDot = true,
  className,
}: {
  severity: Severity;
  showDot?: boolean;
  className?: string;
}) {
  const meta = SEVERITY_META[severity];
  const styles = severityStyles[severity];
  return (
    <span
      className={cn(
        "inline-flex items-center gap-1.5 rounded-full border px-2.5 py-0.5 text-[11px] font-medium",
        styles.badge,
        className,
      )}
    >
      {showDot && <span className={cn("size-1.5 rounded-full", styles.dot)} />}
      {meta.label}
    </span>
  );
}
