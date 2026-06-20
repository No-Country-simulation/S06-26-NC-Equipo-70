"use client";

import { useEffect, useState } from "react";
import { Globe, AlertTriangle, Briefcase, Wifi, RefreshCw, Calendar, Download } from "lucide-react";
import { kpis, formatPopulation } from "@/lib/data";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { MetricCard } from "@/components/cards/metric-card";
import { LoadingSkeleton } from "@/components/states/loading-skeleton";
import { ErrorState } from "@/components/states/error-state";
import { Page, PageActions, PageDescription, PageHeader, PageTitle } from "@/components/ui/page";

type Status = "loading" | "ready" | "error";

export function DashboardClient() {
  const [status, setStatus] = useState<Status>("loading");

  // Carga inicial (solo al montar el componente)
  useEffect(() => {
    const timer = setTimeout(() => {
      setStatus("ready");
    }, 750);

    return () => clearTimeout(timer);
  }, []);

  // Recarga manual (botón refresh / retry)
  const load = () => {
    setStatus("loading");

    setTimeout(() => {
      setStatus("ready");
    }, 750);
  };

  return (
    <Page>
      <PageHeader>
        <div>
          <PageTitle>Territorial overview</PageTitle>
          <PageDescription>
            Monitoring digital, economic and social indicators across {kpis.monitoredRegions}{" "}
            regions.
          </PageDescription>
        </div>
        <PageActions>
          <Button variant="outline" size="sm">
            <Calendar />
            Last 6 quarters
          </Button>
          <Button variant="outline" size="sm">
            <Download />
            Export
          </Button>
          <Button variant="ghost" size="icon-sm" aria-label="Refresh data" onClick={load}>
            <RefreshCw className={cn(status === "loading" && "animate-spin")} />
          </Button>
        </PageActions>
      </PageHeader>

      {status === "error" ? (
        <ErrorState className="mt-8" onRetry={load} />
      ) : (
        <>
          {/* KPI cards */}
          <section className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
            {status === "loading" ? (
              Array.from({ length: 4 }).map((_, i) => <LoadingSkeleton key={i} variant="metric" />)
            ) : (
              <>
                <MetricCard
                  label="Monitored regions"
                  value={`${kpis.monitoredRegions}`}
                  icon={Globe}
                  hint={`${formatPopulation(kpis.population)} residents covered`}
                  accent="primary"
                />
                <MetricCard
                  label="Critical regions"
                  value={`${kpis.criticalRegions}`}
                  icon={AlertTriangle}
                  delta={1}
                  invertDelta
                  hint={`${kpis.atRiskRegions} regions at risk overall`}
                  accent="critical"
                />
                <MetricCard
                  label="Avg. connectivity"
                  value={`${kpis.avgConnectivity}%`}
                  icon={Wifi}
                  delta={3.4}
                  hint="Households with broadband"
                  accent="healthy"
                />
                <MetricCard
                  label="Avg. employment"
                  value={`${kpis.avgEmployment}%`}
                  icon={Briefcase}
                  delta={2.1}
                  hint="Working-age population"
                  accent="warning"
                />
              </>
            )}
          </section>
        </>
      )}
    </Page>
  );
}
