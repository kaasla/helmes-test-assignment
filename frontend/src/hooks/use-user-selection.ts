import { useCallback, useEffect, useState } from "react";
import { ApiRequestError, createSelection, fetchMySelection, updateSelection } from "@/lib/api";
import type { UserSelectionRequest, UserSelectionResponse } from "@/lib/types";

interface UseUserSelectionResult {
  readonly selection: UserSelectionResponse | null;
  readonly loading: boolean;
  readonly saving: boolean;
  readonly fieldErrors: Readonly<Record<string, string>>;
  readonly error: string | null;
  readonly save: (request: UserSelectionRequest) => Promise<boolean>;
  readonly clearErrors: () => void;
}

export const useUserSelection = (): UseUserSelectionResult => {
  const [selection, setSelection] = useState<UserSelectionResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [fieldErrors, setFieldErrors] = useState<Readonly<Record<string, string>>>({});
  const [error, setError] = useState<string | null>(null);

  const clearErrors = useCallback(() => {
    setFieldErrors({});
    setError(null);
  }, []);

  const load = useCallback(async () => {
    try {
      setLoading(true);
      const data = await fetchMySelection();
      setSelection(data ?? null);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load selection");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  const save = useCallback(
    async (request: UserSelectionRequest): Promise<boolean> => {
      try {
        clearErrors();
        setSaving(true);

        const saveFn = selection ? updateSelection : createSelection;
        const result = await saveFn(request);
        setSelection(result);
        return true;
      } catch (err) {
        if (err instanceof ApiRequestError) {
          if (err.body.errors) {
            setFieldErrors(err.body.errors);
          }
          setError(err.body.detail);
        } else {
          setError(err instanceof Error ? err.message : "An unexpected error occurred");
        }
        return false;
      } finally {
        setSaving(false);
      }
    },
    [selection, clearErrors],
  );

  return { selection, loading, saving, fieldErrors, error, save, clearErrors };
};
