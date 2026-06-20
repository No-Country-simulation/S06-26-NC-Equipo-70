"use client";

import { cn } from "@/lib/utils";
import type { Region, IndicatorKey } from "@/lib/data";
import { severityStyles } from "@/lib/severity";

const severityHeat: Record<Region["severity"], string> = {
  critical: "var(--color-critical)",
  warning: "var(--color-warning)",
  moderate: "var(--color-info)",
  healthy: "var(--color-healthy)",
};

function indicatorValue(r: Region, key: IndicatorKey): number {
  switch (key) {
    case "connectivity":
      return r.networkCoverage;
    case "employment":
      return r.employmentRate;
    case "training":
      return r.trainingPrograms;
    case "mentorship":
      return r.mentorshipCoverage;
    case "mentalHealth":
      return r.mentalHealthAccess;
    default:
      return r.digitalAccess;
  }
}

export function MapCanvas({
  regions,
  activeId,
  onSelect,
  showHeatmap,
}: {
  regions: Region[];
  activeId?: string | null;
  onSelect: (id: string) => void;
  showHeatmap: boolean;
}) {
  return (
    <div className="relative h-full w-full overflow-hidden rounded-2xl border border-border bg-muted/30">
      {/* grid backdrop */}
      <svg className="absolute inset-0 size-full text-border/60" aria-hidden="true">
        <defs>
          <pattern id="map-grid" width="40" height="40" patternUnits="userSpaceOnUse">
            <path d="M 40 0 L 0 0 0 40" fill="none" stroke="currentColor" strokeWidth="1" />
          </pattern>
        </defs>
        <rect width="100%" height="100%" fill="url(#map-grid)" />
      </svg>

      {/* heatmap blobs */}
      {showHeatmap &&
        regions.map((r) => (
          <span
            key={`heat-${r.id}`}
            className="pointer-events-none absolute -translate-x-1/2 -translate-y-1/2 rounded-full blur-2xl"
            style={{
              left: `${r.x}%`,
              top: `${r.y}%`,
              width: `${28 + (r.riskScore / 100) * 22}%`,
              height: `${28 + (r.riskScore / 100) * 22}%`,
              background: severityHeat[r.severity],
              opacity: 0.16 + (r.riskScore / 100) * 0.24,
            }}
          />
        ))}

      {/* markers */}
      {regions.map((r) => {
        const active = r.id === activeId;
        return (
          <button
            key={r.id}
            onClick={() => onSelect(r.id)}
            aria-label={`${r.name}, ${r.severity} severity`}
            className="group absolute -translate-x-1/2 -translate-y-1/2"
            style={{ left: `${r.x}%`, top: `${r.y}%` }}
          >
            <span className="relative flex items-center justify-center">
              {(r.severity === "critical" || active) && (
                <span
                  className={cn(
                    "absolute inline-flex size-7 animate-ping rounded-full opacity-60",
                    severityStyles[r.severity].dot,
                  )}
                />
              )}
              <span
                className={cn(
                  "relative flex size-4 items-center justify-center rounded-full border-2 border-background shadow-md transition-transform group-hover:scale-125",
                  severityStyles[r.severity].dot,
                  active && "scale-125 ring-2 ring-offset-2 ring-offset-background",
                  active && severityStyles[r.severity].ring,
                )}
              />
            </span>
            <span
              className={cn(
                "pointer-events-none absolute left-1/2 top-5 -translate-x-1/2 whitespace-nowrap rounded-md bg-popover px-1.5 py-0.5 text-[10px] font-medium text-popover-foreground opacity-0 shadow-sm transition-opacity group-hover:opacity-100",
                active && "opacity-100",
              )}
            >
              {r.name}
            </span>
          </button>
        );
      })}
    </div>
  );
}

export { indicatorValue };
