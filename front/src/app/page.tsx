"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Map } from "lucide-react";
import { Button } from "@/components/ui/button";
import { SelectField } from "@/components/ui/select-field";
import { Page, PageContent, PageDescription, PageHeader, PageTitle } from "@/components/ui/page";

const regions = ["All regions", "North", "South", "East", "West"];

const indicators = ["Connectivity", "Employment", "Risk", "Population"];

export default function HomePage() {
  const router = useRouter();

  const [region, setRegion] = useState("All regions");
  const [indicator, setIndicator] = useState("Connectivity");

  const openMap = () => {
    router.push(
      `/map?region=${encodeURIComponent(region)}&indicator=${encodeURIComponent(indicator)}`,
    );
  };
  return (
    <Page>
      <PageHeader>
        <div>
          <PageTitle>Initial Dashboard</PageTitle>
          <PageDescription>
            Select region and indicator to explore the territory map
          </PageDescription>
        </div>
      </PageHeader>
      <PageContent className="flex justify-center items-center">
        <div className="flex flex-col gap-5 rounded-2xl border border-border bg-card p-5 lg:min-w-90">
          <SelectField
            label="Region"
            value={region}
            options={regions}
            onChange={(value) => setRegion(value ?? "All regions")}
          />
          <SelectField
            label="Indicator"
            value={indicator}
            options={indicators}
            onChange={(value) => setIndicator(value ?? "Connectivity")}
          />
          <Button className="w-full" onClick={openMap}>
            <Map className="mr-2 size-4" />
            Open map
          </Button>
        </div>
      </PageContent>
    </Page>
  );
}
