import type {
  ApiError,
  SectorNode,
  UserSelectionRequest,
  UserSelectionResponse,
} from "@/lib/types";

const BASE_URL = "/api/v1";

export class ApiRequestError extends Error {
  readonly status: number;
  readonly body: ApiError;

  constructor(status: number, body: ApiError) {
    super(body.detail);
    this.name = "ApiRequestError";
    this.status = status;
    this.body = body;
  }
}

const buildUrl = (path: string): string => `${BASE_URL}${path}`;

const handleResponse = async <T>(response: Response): Promise<T> => {
  if (!response.ok) {
    const body = (await response.json()) as ApiError;
    throw new ApiRequestError(response.status, body);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
};

const get = async <T>(path: string): Promise<T> => {
  const response = await fetch(buildUrl(path), { credentials: "include" });
  return handleResponse<T>(response);
};

const post = async <T>(path: string, body: unknown): Promise<T> => {
  const response = await fetch(buildUrl(path), {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return handleResponse<T>(response);
};

const put = async <T>(path: string, body: unknown): Promise<T> => {
  const response = await fetch(buildUrl(path), {
    method: "PUT",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return handleResponse<T>(response);
};

export const fetchSectors = (): Promise<SectorNode[]> => get<SectorNode[]>("/sectors");

export const fetchMySelection = (): Promise<UserSelectionResponse | undefined> =>
  get<UserSelectionResponse | undefined>("/user-selections/me");

export const createSelection = (request: UserSelectionRequest): Promise<UserSelectionResponse> =>
  post<UserSelectionResponse>("/user-selections", request);

export const updateSelection = (request: UserSelectionRequest): Promise<UserSelectionResponse> =>
  put<UserSelectionResponse>("/user-selections/me", request);
