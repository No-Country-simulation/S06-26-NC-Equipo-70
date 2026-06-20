"use client";

import { useEffect } from "react";
import {
  X,
  Users,
  Wifi,
  Briefcase,
  GraduationCap,
  HeartHandshake,
  Brain,
  MapPin,
  Gauge,
} from "lucide-react";
import type { LucideIcon } from "lucide-react";
import { cn } from "@/lib/utils";
import { getRegion, formatPopulation, type IndicatorKey } from "@/lib/data";
import { severityStyles, valueTone } from "@/lib/severity";
import { SeverityBadge } from "@/components/ui/severity-badge";
import { ProgressBar } from "@/components/ui/progress-bar";
import { Button } from "@/components/ui/button";

const indicatorIcons: Record<IndicatorKey, LucideIcon> = {
  digitalInequality: Wifi,
  employment: Briefcase,
  connectivity: Wifi,
  training: GraduationCap,
  mentorship: HeartHandshake,
  mentalHealth: Brain,
};

export function RegionDetailDrawer({
  regionId,
  onClose,
}: {
  regionId: string | null;
  onClose: () => void;
}) {
  const region = regionId ? getRegion(regionId) : undefined;
  const open = Boolean(region);

  useEffect(() => {
    if (!open) return;
    const onKey = (e: KeyboardEvent) => e.key === "Escape" && onClose();
    window.addEventListener("keydown", onKey);
    document.body.style.overflow = "hidden";
    return () => {
      window.removeEventListener("keydown", onKey);
      document.body.style.overflow = "";
    };
  }, [open, onClose]);

  return (
    <div
      className={cn("fixed inset-0 z-60", open ? "pointer-events-auto" : "pointer-events-none")}
      aria-hidden={!open}
    >
      {/* Scrim */}
      <div
        className={cn(
          "absolute inset-0 bg-background/70 backdrop-blur-sm transition-opacity duration-300",
          open ? "opacity-100" : "opacity-0",
        )}
        onClick={onClose}
      />

      {/* Panel */}
      <aside
        role="dialog"
        aria-modal="true"
        aria-label={region ? `${region.name} details` : "Region details"}
        className={cn(
          "absolute right-0 top-0 flex h-full w-full max-w-md flex-col border-l border-border bg-card shadow-2xl transition-transform duration-300 ease-out sm:w-md",
          open ? "translate-x-0" : "translate-x-full",
        )}
      >
        {region && (
          <>
            <div className="flex items-start justify-between gap-3 border-b border-border p-5">
              <div>
                <div className="flex items-center gap-2">
                  <h2 className="text-lg font-semibold tracking-tight">{region.name}</h2>
                  <SeverityBadge severity={region.severity} />
                </div>
                <p className="mt-1 flex items-center gap-1 text-sm text-muted-foreground">
                  <MapPin className="size-3.5" />
                  {region.district}
                </p>
              </div>
              <Button variant="ghost" size="icon" aria-label="Close drawer" onClick={onClose}>
                <X />
              </Button>
            </div>

            <div className="flex-1 overflow-y-auto scrollbar-thin p-5">
              {/* Risk score hero */}
              <div
                className={cn(
                  "flex items-center gap-4 rounded-2xl border p-4",
                  "border-border bg-muted/40",
                )}
              >
                <div
                  className={cn(
                    "flex size-16 shrink-0 items-center justify-center rounded-2xl",
                    severityStyles[region.severity].badge,
                  )}
                >
                  <Gauge className="size-7" />
                </div>
                <div>
                  <p className="text-xs uppercase tracking-wide text-muted-foreground">
                    Composite risk score
                  </p>
                  <p
                    className={cn(
                      "text-3xl font-semibold tabular-nums",
                      severityStyles[region.severity].text,
                    )}
                  >
                    {region.riskScore}
                    <span className="text-base text-muted-foreground">/100</span>
                  </p>
                </div>
              </div>

              {/* Population concentration */}
              <div className="mt-4 grid grid-cols-2 gap-3">
                <div className="rounded-2xl border border-border p-4">
                  <p className="flex items-center gap-1.5 text-xs text-muted-foreground">
                    <Users className="size-3.5" />
                    Population
                  </p>
                  <p className="mt-1 text-xl font-semibold tabular-nums">
                    {formatPopulation(region.population)}
                  </p>
                  <p className="mt-0.5 text-[11px] text-muted-foreground">residents concentrated</p>
                </div>
                <div className="rounded-2xl border border-border p-4">
                  <p className="flex items-center gap-1.5 text-xs text-muted-foreground">
                    <Wifi className="size-3.5" />
                    Network coverage
                  </p>
                  <p className="mt-1 text-xl font-semibold tabular-nums">
                    {region.networkCoverage}%
                  </p>
                  <p className="mt-0.5 text-[11px] text-muted-foreground">households connected</p>
                </div>
              </div>

              {/* Indicators */}
              <p className="mb-3 mt-6 text-xs font-semibold uppercase tracking-wide text-muted-foreground">
                Indicator breakdown
              </p>
              <div className="flex flex-col gap-4">
                {region.indicators.map((ind) => {
                  const Icon = indicatorIcons[ind.key];
                  const tone = valueTone(ind.value);
                  return (
                    <div key={ind.key}>
                      <div className="mb-1.5 flex items-center justify-between">
                        <span className="flex items-center gap-2 text-sm">
                          <Icon className="size-4 text-muted-foreground" />
                          {ind.label}
                        </span>
                        <span className={cn("text-sm font-semibold tabular-nums", tone.text)}>
                          {ind.value}%
                        </span>
                      </div>
                      <ProgressBar value={ind.value} barClassName={tone.bar} />
                    </div>
                  );
                })}
              </div>
            </div>

            <div className="flex gap-2 border-t border-border p-4">
              <Button variant="outline" className="flex-1">
                Export report
              </Button>
              <Button className="flex-1">Create action plan</Button>
            </div>
          </>
        )}
      </aside>
    </div>
  );
}
