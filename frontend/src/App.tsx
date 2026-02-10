import { useCallback, useMemo, useState } from "react";
import SectorSelect from "@/components/SectorSelect";
import Toast from "@/components/Toast";
import { useSectors } from "@/hooks/useSectors";
import { useToast } from "@/hooks/useToast";
import { useUserSelection } from "@/hooks/useUserSelection";

const App = () => {
  const { sectors, loading: sectorsLoading, error: sectorsError } = useSectors();
  const {
    selection,
    loading: selectionLoading,
    saving,
    fieldErrors,
    save,
    clearErrors,
  } = useUserSelection();
  const { toast, show: showToast } = useToast();

  const [name, setName] = useState("");
  const [selectedSectors, setSelectedSectors] = useState<ReadonlySet<number>>(new Set());
  const [agreeToTerms, setAgreeToTerms] = useState(false);
  const [initialized, setInitialized] = useState(false);

  if (selection && !initialized) {
    setName(selection.name);
    setSelectedSectors(new Set(selection.sectorIds));
    setAgreeToTerms(selection.agreeToTerms);
    setInitialized(true);
  }

  const handleSubmit = useCallback(
    async (e: React.SyntheticEvent) => {
      e.preventDefault();
      clearErrors();

      const success = await save({
        name: name.trim(),
        sectorIds: [...selectedSectors],
        agreeToTerms,
      });

      if (success) {
        showToast(selection ? "Selection updated successfully" : "Selection saved successfully");
      }
    },
    [name, selectedSectors, agreeToTerms, save, clearErrors, showToast, selection],
  );

  const loading = sectorsLoading || selectionLoading;

  const selectedCount = useMemo(() => selectedSectors.size, [selectedSectors]);

  if (loading) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-indigo-50">
        <div className="flex items-center gap-3 text-slate-500">
          <svg className="size-5 animate-spin" viewBox="0 0 24 24" fill="none">
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            />
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
            />
          </svg>
          <span className="text-sm font-medium">Loading...</span>
        </div>
      </main>
    );
  }

  if (sectorsError) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-indigo-50">
        <div className="rounded-xl bg-white p-8 text-center shadow-lg">
          <p className="text-sm text-red-500">Failed to load sectors. Please try again later.</p>
        </div>
      </main>
    );
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-gradient-to-br from-slate-50 to-indigo-50 px-4 py-12">
      <div className="w-full max-w-lg">
        <div className="mb-8 text-center">
          <h1 className="text-2xl font-bold tracking-tight text-slate-900">Sector Selection</h1>
          <p className="mt-2 text-sm text-slate-500">
            Please enter your name and pick the sectors you are currently involved in.
          </p>
        </div>

        <form
          onSubmit={(e) => {
            void handleSubmit(e);
          }}
          className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm sm:p-8"
          noValidate
        >
          <div className="mb-5">
            <label htmlFor="name" className="mb-1.5 block text-sm font-medium text-slate-700">
              Name
            </label>
            <input
              id="name"
              type="text"
              value={name}
              onChange={(e) => {
                setName(e.target.value);
              }}
              placeholder="Your name"
              className={`w-full rounded-lg border bg-white px-3.5 py-2.5 text-sm text-slate-900 shadow-sm transition-colors outline-none placeholder:text-slate-400 ${
                fieldErrors.name
                  ? "border-red-400 focus:border-red-400 focus:ring-1 focus:ring-red-400"
                  : "border-slate-200 focus:border-indigo-400 focus:ring-1 focus:ring-indigo-400"
              }`}
            />
            {fieldErrors.name && <p className="mt-1.5 text-sm text-red-500">{fieldErrors.name}</p>}
          </div>

          <div className="mb-5">
            <label className="mb-1.5 block text-sm font-medium text-slate-700">
              Sectors
              {selectedCount > 0 && (
                <span className="ml-1.5 text-xs font-normal text-slate-400">
                  ({selectedCount} selected)
                </span>
              )}
            </label>
            <SectorSelect
              sectors={sectors}
              selected={selectedSectors}
              onChange={setSelectedSectors}
              error={fieldErrors.sectorIds}
            />
          </div>

          <div className="mb-6">
            <label className="flex cursor-pointer items-start gap-2.5">
              <input
                type="checkbox"
                checked={agreeToTerms}
                onChange={(e) => {
                  setAgreeToTerms(e.target.checked);
                }}
                className="mt-0.5 size-4 rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
              />
              <span className="text-sm text-slate-600">Agree to terms</span>
            </label>
            {fieldErrors.agreeToTerms && (
              <p className="mt-1.5 text-sm text-red-500">{fieldErrors.agreeToTerms}</p>
            )}
          </div>

          <button
            type="submit"
            disabled={saving}
            className="w-full cursor-pointer rounded-lg bg-indigo-600 px-4 py-2.5 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
          >
            {saving ? "Saving..." : "Save"}
          </button>
        </form>
      </div>

      <Toast message={toast.message} type={toast.type} visible={toast.visible} />
    </main>
  );
};

export default App;
