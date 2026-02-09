import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { beforeEach, describe, expect, it, vi } from "vitest";
import type { SectorNode, UserSelectionResponse } from "@/lib/types";
import App from "./App";

const mockSectors: SectorNode[] = [
  {
    id: 1,
    name: "Manufacturing",
    children: [{ id: 19, name: "Construction materials", children: [] }],
  },
  { id: 2, name: "Service", children: [] },
];

const mockSavedSelection: UserSelectionResponse = {
  id: 1,
  name: "John",
  sectorIds: [1, 19],
  agreeToTerms: true,
  createdAt: "2026-01-01T00:00:00",
  updatedAt: "2026-01-01T00:00:00",
};

vi.mock("@/lib/api", () => ({
  fetchSectors: vi.fn(),
  fetchMySelection: vi.fn(),
  createSelection: vi.fn(),
  updateSelection: vi.fn(),
  ApiRequestError: class extends Error {
    readonly status: number;
    readonly body: unknown;
    constructor(status: number, body: unknown) {
      super("API error");
      this.status = status;
      this.body = body;
    }
  },
}));

const api = await import("@/lib/api");
const { fetchSectors, fetchMySelection, createSelection } = api as {
  fetchSectors: ReturnType<typeof vi.fn>;
  fetchMySelection: ReturnType<typeof vi.fn>;
  createSelection: ReturnType<typeof vi.fn>;
};

beforeEach(() => {
  vi.clearAllMocks();
});

/* ── Tests ────────────────────────────────────────────────────────── */

describe("App", () => {
  it("shows loading state initially", () => {
    fetchSectors.mockReturnValue(new Promise(vi.fn()));
    fetchMySelection.mockReturnValue(new Promise(vi.fn()));

    render(<App />);

    expect(screen.getByText("Loading...")).toBeInTheDocument();
  });

  it("renders form after sectors load", async () => {
    fetchSectors.mockResolvedValue(mockSectors);
    fetchMySelection.mockResolvedValue(undefined);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText("Sector Selection")).toBeInTheDocument();
    });

    expect(screen.getByPlaceholderText("Your name")).toBeInTheDocument();
    expect(screen.getByText("Manufacturing")).toBeInTheDocument();
    expect(screen.getByText("Service")).toBeInTheDocument();
    expect(screen.getByText("Agree to terms")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Save" })).toBeInTheDocument();
  });

  it("shows error when sectors fail to load", async () => {
    fetchSectors.mockRejectedValue(new Error("Network error"));
    fetchMySelection.mockResolvedValue(undefined);

    render(<App />);

    await waitFor(() => {
      expect(
        screen.getByText("Failed to load sectors. Please try again later."),
      ).toBeInTheDocument();
    });
  });

  it("refills form from existing session data", async () => {
    fetchSectors.mockResolvedValue(mockSectors);
    fetchMySelection.mockResolvedValue(mockSavedSelection);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByPlaceholderText("Your name")).toHaveValue("John");
    });

    const checkboxes = screen.getAllByRole("checkbox");
    // Manufacturing (1) and Construction materials (19) should be checked
    expect(checkboxes[0]).toBeChecked();
    expect(checkboxes[1]).toBeChecked();
    // Service (2) should not be checked
    expect(checkboxes[2]).not.toBeChecked();
    // Agree to terms should be checked
    expect(checkboxes[3]).toBeChecked();
  });

  it("submits form successfully", async () => {
    const user = userEvent.setup();

    fetchSectors.mockResolvedValue(mockSectors);
    fetchMySelection.mockResolvedValue(undefined);
    createSelection.mockResolvedValue(mockSavedSelection);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByPlaceholderText("Your name")).toBeInTheDocument();
    });

    await user.type(screen.getByPlaceholderText("Your name"), "John");
    await user.click(screen.getByText("Manufacturing"));
    await user.click(screen.getByText("Agree to terms"));
    await user.click(screen.getByRole("button", { name: "Save" }));

    await waitFor(() => {
      expect(screen.getByText("Selection saved successfully")).toBeInTheDocument();
    });
  });

  it("shows selected count badge", async () => {
    const user = userEvent.setup();

    fetchSectors.mockResolvedValue(mockSectors);
    fetchMySelection.mockResolvedValue(undefined);

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText("Manufacturing")).toBeInTheDocument();
    });

    await user.click(screen.getByText("Manufacturing"));
    await user.click(screen.getByText("Service"));

    expect(screen.getByText("(2 selected)")).toBeInTheDocument();
  });
});