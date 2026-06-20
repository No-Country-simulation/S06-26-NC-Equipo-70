"use client";

import { createContext, useCallback, useContext, useMemo, useState } from "react";
import { RegionDetailDrawer } from "./region-detail-drawer";

interface RegionDrawerContextValue {
  openRegion: (id: string) => void;
  closeRegion: () => void;
  activeRegionId: string | null;
}

const RegionDrawerContext = createContext<RegionDrawerContextValue | null>(null);

export function useRegionDrawer() {
  const ctx = useContext(RegionDrawerContext);
  if (!ctx) throw new Error("useRegionDrawer must be used within RegionDrawerProvider");
  return ctx;
}

export function RegionDrawerProvider({ children }: { children: React.ReactNode }) {
  const [activeRegionId, setActiveRegionId] = useState<string | null>(null);

  const openRegion = useCallback((id: string) => setActiveRegionId(id), []);
  const closeRegion = useCallback(() => setActiveRegionId(null), []);

  const value = useMemo(
    () => ({ openRegion, closeRegion, activeRegionId }),
    [openRegion, closeRegion, activeRegionId],
  );

  return (
    <RegionDrawerContext.Provider value={value}>
      {children}
      <RegionDetailDrawer regionId={activeRegionId} onClose={closeRegion} />
    </RegionDrawerContext.Provider>
  );
}
