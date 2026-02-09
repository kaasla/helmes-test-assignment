import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import Toast from "./Toast";

describe("Toast", () => {
  it("renders message when visible", () => {
    render(<Toast message="Saved successfully" type="success" visible={true} />);

    expect(screen.getByText("Saved successfully")).toBeInTheDocument();
  });

  it("renders nothing when not visible and no message", () => {
    const { container } = render(<Toast message="" type="success" visible={false} />);

    expect(container.firstChild).toBeNull();
  });

  it("applies success styling", () => {
    render(<Toast message="Done" type="success" visible={true} />);

    const toast = screen.getByText("Done");
    expect(toast.className).toContain("bg-emerald-500");
  });

  it("applies error styling", () => {
    render(<Toast message="Failed" type="error" visible={true} />);

    const toast = screen.getByText("Failed");
    expect(toast.className).toContain("bg-red-500");
  });
});