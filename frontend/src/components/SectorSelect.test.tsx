import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import type { SectorNode } from "@/lib/types";
import SectorSelect from "./SectorSelect";

const sectors: SectorNode[] = [
  {
    id: 1,
    name: "Manufacturing",
    children: [
      { id: 19, name: "Construction materials", children: [] },
      { id: 18, name: "Electronics and Optics", children: [] },
    ],
  },
  { id: 2, name: "Service", children: [] },
];

describe("SectorSelect", () => {
  it("renders all sectors including nested children", () => {
    render(<SectorSelect sectors={sectors} selected={new Set()} onChange={vi.fn()} />);

    expect(screen.getByText("Manufacturing")).toBeInTheDocument();
    expect(screen.getByText("Construction materials")).toBeInTheDocument();
    expect(screen.getByText("Electronics and Optics")).toBeInTheDocument();
    expect(screen.getByText("Service")).toBeInTheDocument();
  });

  it("checks selected sectors", () => {
    render(<SectorSelect sectors={sectors} selected={new Set([1, 18])} onChange={vi.fn()} />);

    const checkboxes = screen.getAllByRole("checkbox");
    // Manufacturing (1) is checked
    expect(checkboxes[0]).toBeChecked();
    // Construction materials (19) is not checked
    expect(checkboxes[1]).not.toBeChecked();
    // Electronics and Optics (18) is checked
    expect(checkboxes[2]).toBeChecked();
  });

  it("calls onChange when a sector is toggled", async () => {
    const user = userEvent.setup();
    const onChange = vi.fn();

    render(<SectorSelect sectors={sectors} selected={new Set()} onChange={onChange} />);

    await user.click(screen.getByText("Service"));

    expect(onChange).toHaveBeenCalledOnce();
    const newSet = onChange.mock.calls[0][0] as Set<number>;
    expect(newSet.has(2)).toBe(true);
  });

  it("removes sector from selection when unchecked", async () => {
    const user = userEvent.setup();
    const onChange = vi.fn();

    render(<SectorSelect sectors={sectors} selected={new Set([2])} onChange={onChange} />);

    await user.click(screen.getByText("Service"));

    expect(onChange).toHaveBeenCalledOnce();
    const newSet = onChange.mock.calls[0][0] as Set<number>;
    expect(newSet.has(2)).toBe(false);
  });

  it("displays error message when provided", () => {
    render(
      <SectorSelect
        sectors={sectors}
        selected={new Set()}
        onChange={vi.fn()}
        error="At least one sector must be selected"
      />,
    );

    expect(screen.getByText("At least one sector must be selected")).toBeInTheDocument();
  });

  it("renders parent sectors with bold styling", () => {
    render(<SectorSelect sectors={sectors} selected={new Set()} onChange={vi.fn()} />);

    const manufacturing = screen.getByText("Manufacturing");
    expect(manufacturing.className).toContain("font-semibold");

    const service = screen.getByText("Service");
    expect(service.className).not.toContain("font-semibold");
  });
});