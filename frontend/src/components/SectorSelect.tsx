import { useCallback, useMemo } from "react";
import type { SectorNode } from "@/lib/types";

interface SectorSelectProps {
  readonly sectors: readonly SectorNode[];
  readonly selected: ReadonlySet<number>;
  readonly onChange: (selected: ReadonlySet<number>) => void;
  readonly error?: string;
}

interface FlatOption {
  readonly id: number;
  readonly name: string;
  readonly depth: number;
  readonly hasChildren: boolean;
}

const flattenSectors = (nodes: readonly SectorNode[], depth = 0): FlatOption[] =>
  nodes.flatMap((node) => [
    { id: node.id, name: node.name, depth, hasChildren: node.children.length > 0 },
    ...flattenSectors(node.children, depth + 1),
  ]);

const SectorSelect = ({ sectors, selected, onChange, error }: SectorSelectProps) => {
  const options = useMemo(() => flattenSectors(sectors), [sectors]);

  const handleToggle = useCallback(
    (id: number) => {
      const next = new Set(selected);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      onChange(next);
    },
    [selected, onChange],
  );

  return (
    <div>
      <div
        className={`max-h-72 overflow-y-auto rounded-lg border bg-white shadow-sm transition-colors ${
          error ? "border-red-400" : "border-slate-200 focus-within:border-indigo-400"
        }`}
      >
        {options.map((option) => (
          <label
            key={option.id}
            className="flex cursor-pointer items-center gap-2 px-3 py-1.5 transition-colors hover:bg-indigo-50"
            style={{ paddingLeft: `${option.depth * 20 + 12}px` }}
          >
            <input
              type="checkbox"
              checked={selected.has(option.id)}
              onChange={() => {
                handleToggle(option.id);
              }}
              className="size-4 rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
            />
            <span
              className={`text-sm select-none ${
                option.hasChildren ? "font-semibold text-slate-800" : "text-slate-600"
              }`}
            >
              {option.name}
            </span>
          </label>
        ))}
      </div>
      {error && <p className="mt-1.5 text-sm text-red-500">{error}</p>}
    </div>
  );
};

export default SectorSelect;
