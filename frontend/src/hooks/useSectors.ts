import { useCallback, useEffect, useState } from "react";
import { fetchSectors } from "@/lib/api";
import type { SectorNode } from "@/lib/types";

interface UseSectorsResult {
  readonly sectors: readonly SectorNode[];
  readonly loading: boolean;
  readonly error: string | null;
}

export const useSectors = (): UseSectorsResult => {
  const [sectors, setSectors] = useState<readonly SectorNode[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async () => {
    try {
      setLoading(true);
      const data = await fetchSectors();
      setSectors(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load sectors");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  return { sectors, loading, error };
};
