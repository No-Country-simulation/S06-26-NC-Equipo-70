"use client";

import { MapPin, Maximize2, Users } from "lucide-react";
import { formatPopulation, type Region } from "@/lib/data";
import { valueTone } from "@/lib/severity";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { SeverityBadge } from "@/components/ui/severity-badge";
import { ProgressBar } from "@/components/ui/progress-bar";
import { EmptyState } from "@/components/states/empty-state";

export function MapDetailPanel({
  region,
  onExpand,
}: {
  region: Region | null;
  onExpand: (id: string) => void;
}) {
  if (!region) {
    return (
      <EmptyState
        icon={MapPin}
        title="No region selected"
        description="Select a marker on the map to inspect its indicators and risk profile."
        className="h-full"
      />
    );
  }

  return (
    <div className="flex h-full flex-col rounded-2xl border border-border bg-card">
      <div className="border-b border-border p-4">
        <div className="flex items-start justify-between gap-2">
          <div>
            <p className="text-base font-semibold">{region.name}</p>
            <p className="mt-0.5 flex items-center gap-1 text-xs text-muted-foreground">
              <MapPin className="size-3" />
              {region.district}
            </p>
          </div>
          <SeverityBadge severity={region.severity} />
        </div>
        <div className="mt-3 flex items-center gap-4 text-xs text-muted-foreground">
          <span className="flex items-center gap-1">
            <Users className="size-3.5" />
            {formatPopulation(region.population)}
          </span>
          <span>
            Risk{" "}
            <span
              className={cn("font-semibold tabular-nums", valueTone(100 - region.riskScore).text)}
            >
              {region.riskScore}
            </span>
          </span>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto scrollbar-thin p-4">
        <div className="flex flex-col gap-3.5">
          {region.indicators.map((ind) => {
            const tone = valueTone(ind.value);
            return (
              <div key={ind.key}>
                <div className="mb-1 flex items-center justify-between text-xs">
                  <span className="text-muted-foreground">{ind.label}</span>
                  <span className={cn("font-semibold tabular-nums", tone.text)}>{ind.value}%</span>
                </div>
                <ProgressBar value={ind.value} barClassName={tone.bar} />
              </div>
            );
          })}
        </div>
      </div>

      <div className="border-t border-border p-4">
        <Button className="w-full" onClick={() => onExpand(region.id)}>
          <Maximize2 />
          Open full detail
        </Button>
      </div>
    </div>
  );
}
