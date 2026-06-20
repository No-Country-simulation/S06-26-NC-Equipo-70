"use client";

import { SlidersHorizontal, RotateCcw } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  DISTRICT_OPTIONS,
  SERVICE_OPTIONS,
  INDICATOR_OPTIONS,
  SEVERITY_META,
  type IndicatorKey,
  type Severity,
} from "@/lib/data";
import { severityStyles } from "@/lib/severity";
import { SelectField } from "../ui/select-field";

export interface MapFilters {
  district: string;
  service: string;
  indicator: IndicatorKey;
  severities: Severity[];
}

export const defaultFilters: MapFilters = {
  district: "All regions",
  service: "All services",
  indicator: "digitalInequality",
  severities: ["critical", "warning", "moderate", "healthy"],
};

export function FilterPanel({
  filters,
  onChange,
  className,
}: {
  filters: MapFilters;
  onChange: (f: MapFilters) => void;
  className?: string;
}) {
  const toggleSeverity = (s: Severity) => {
    const has = filters.severities.includes(s);
    const next = has ? filters.severities.filter((x) => x !== s) : [...filters.severities, s];
    onChange({ ...filters, severities: next });
  };

  const indicatorLabel = INDICATOR_OPTIONS.find((i) => i.key === filters.indicator)?.label ?? "";

  return (
    <div
      className={cn("flex flex-col gap-5 rounded-2xl border border-border bg-card p-5", className)}
    >
      <div className="flex items-center justify-between">
        <span className="flex items-center gap-2 text-sm font-semibold">
          <SlidersHorizontal className="size-4 text-primary" />
          Filters
        </span>
        <Button variant="ghost" size="xs" onClick={() => onChange(defaultFilters)}>
          <RotateCcw />
          Reset
        </Button>
      </div>
      <SelectField
        label="Region"
        value={filters.district}
        options={DISTRICT_OPTIONS}
        onChange={(v) => onChange({ ...filters, district: v })}
      />
      <SelectField
        label="Service domain"
        value={filters.service}
        options={SERVICE_OPTIONS}
        onChange={(v) => onChange({ ...filters, service: v })}
      />

      <div>
        <span className="mb-1.5 block text-xs font-medium text-muted-foreground">
          Indicator overlay
        </span>
        <div className="flex flex-wrap gap-1.5">
          {INDICATOR_OPTIONS.map((opt) => {
            const active = filters.indicator === opt.key;
            return (
              <button
                key={opt.key}
                onClick={() => onChange({ ...filters, indicator: opt.key })}
                className={cn(
                  "rounded-lg border px-2.5 py-1 text-xs font-medium transition-colors",
                  active
                    ? "border-primary bg-primary/12 text-primary"
                    : "border-border text-muted-foreground hover:bg-muted",
                )}
              >
                {opt.label}
              </button>
            );
          })}
        </div>
      </div>

      <div>
        <span className="mb-1.5 block text-xs font-medium text-muted-foreground">Severity</span>
        <div className="flex flex-col gap-1.5">
          {(["critical", "warning", "moderate", "healthy"] as Severity[]).map((s) => {
            const active = filters.severities.includes(s);
            return (
              <button
                key={s}
                onClick={() => toggleSeverity(s)}
                className={cn(
                  "flex items-center gap-2 rounded-lg border px-2.5 py-1.5 text-xs transition-colors",
                  active
                    ? "border-border bg-muted/60"
                    : "border-border opacity-50 hover:opacity-100",
                )}
              >
                <span className={cn("size-2.5 rounded-full", severityStyles[s].dot)} />
                <span className="font-medium">{SEVERITY_META[s].label}</span>
                <span
                  className={cn(
                    "ml-auto size-3.5 rounded-md border",
                    active ? "border-primary bg-primary" : "border-border",
                  )}
                />
              </button>
            );
          })}
        </div>
      </div>

      <p className="rounded-xl bg-muted/50 px-3 py-2 text-[11px] text-muted-foreground">
        Showing <span className="font-medium text-foreground">{indicatorLabel}</span> across{" "}
        {filters.district.toLowerCase()}.
      </p>
    </div>
  );
}
