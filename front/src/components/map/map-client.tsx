"use client";

import { useMemo, useState } from "react";
import { Flame, Layers, SlidersHorizontal, X } from "lucide-react";
import { regions as allRegions, getRegion } from "@/lib/data";
import { Button } from "@/components/ui/button";
import { FilterPanel, defaultFilters, type MapFilters } from "@/components/filters/filter-panel";
import { MapCanvas } from "@/components/map/map-canvas";
import { MapLegend } from "@/components/map/map-legend";
import { MapDetailPanel } from "@/components/map/map-detail-panel";
import { EmptyState } from "@/components/states/empty-state";
import { useRegionDrawer } from "@/components/region/region-drawer-provider";
import { Page, PageActions, PageDescription, PageHeader, PageTitle } from "@/components/ui/page";

export function MapClient() {
  const { openRegion } = useRegionDrawer();
  const [filters, setFilters] = useState<MapFilters>(defaultFilters);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [showHeatmap, setShowHeatmap] = useState(true);
  const [mobileFilters, setMobileFilters] = useState(false);

  const filtered = useMemo(() => {
    return allRegions.filter((r) => {
      if (filters.district !== "All regions" && r.district !== filters.district) return false;
      if (!filters.severities.includes(r.severity)) return false;
      return true;
    });
  }, [filters]);

  const selected = selectedId ? (getRegion(selectedId) ?? null) : null;

  return (
    <Page>
      <PageHeader>
        <div>
          <PageTitle>Territory map</PageTitle>
          <PageDescription>Spatial view of regional risk and service coverage.</PageDescription>
        </div>
        <PageActions>
          <Button
            variant={showHeatmap ? "default" : "outline"}
            size="sm"
            onClick={() => setShowHeatmap((v) => !v)}
          >
            <Flame />
            Heatmap
          </Button>
          <Button variant="outline" size="sm">
            <Layers />
            Layers
          </Button>
          <Button
            variant="outline"
            size="sm"
            className="lg:hidden"
            onClick={() => setMobileFilters(true)}
          >
            <SlidersHorizontal />
            Filters
          </Button>
        </PageActions>
      </PageHeader>
      <div className="mt-6 grid grid-cols-1 gap-4 lg:grid-cols-[260px_1fr_320px]">
        {/* Filters (desktop) */}
        <div className="hidden lg:block">
          <FilterPanel filters={filters} onChange={setFilters} />
        </div>

        {/* Map */}
        <div className="relative h-120 lg:h-160">
          {filtered.length === 0 ? (
            <EmptyState
              icon={Layers}
              title="No regions match your filters"
              description="Try enabling more severity levels or selecting a different district."
              actionLabel="Reset filters"
              onAction={() => setFilters(defaultFilters)}
              className="h-full"
            />
          ) : (
            <>
              <MapCanvas
                regions={filtered}
                activeId={selectedId}
                onSelect={setSelectedId}
                showHeatmap={showHeatmap}
              />
              <MapLegend className="absolute bottom-4 left-4 w-56" />
              <div className="absolute right-4 top-4 rounded-xl border border-border bg-card/90 px-3 py-1.5 text-xs backdrop-blur-sm">
                <span className="font-medium tabular-nums">{filtered.length}</span>{" "}
                <span className="text-muted-foreground">regions shown</span>
              </div>
            </>
          )}
        </div>

        {/* Detail panel */}
        <div className="h-120 lg:h-160">
          <MapDetailPanel region={selected} onExpand={openRegion} />
        </div>
      </div>

      {/* Mobile filter sheet */}
      {mobileFilters && (
        <div className="fixed inset-0 z-50 lg:hidden">
          <button
            aria-label="Close filters"
            className="absolute inset-0 bg-background/70 backdrop-blur-sm"
            onClick={() => setMobileFilters(false)}
          />
          <div className="absolute inset-x-0 bottom-0 max-h-[85vh] overflow-y-auto scrollbar-thin rounded-t-2xl border-t border-border bg-background p-4">
            <div className="mb-3 flex items-center justify-between">
              <p className="text-sm font-semibold">Filters</p>
              <Button
                variant="ghost"
                size="icon-sm"
                aria-label="Close filters"
                onClick={() => setMobileFilters(false)}
              >
                <X />
              </Button>
            </div>
            <FilterPanel filters={filters} onChange={setFilters} />
          </div>
        </div>
      )}
    </Page>
  );
}
