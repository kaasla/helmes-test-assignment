export interface SectorNode {
  readonly id: number;
  readonly name: string;
  readonly children: readonly SectorNode[];
}

export interface UserSelectionRequest {
  readonly name: string;
  readonly sectorIds: readonly number[];
  readonly agreeToTerms: boolean;
}

export interface UserSelectionResponse {
  readonly id: number;
  readonly name: string;
  readonly sectorIds: readonly number[];
  readonly agreeToTerms: boolean;
  readonly createdAt: string;
  readonly updatedAt: string;
}

export interface ApiError {
  readonly type: string;
  readonly title: string;
  readonly status: number;
  readonly detail: string;
  readonly instance: string;
  readonly errors?: Readonly<Record<string, string>>;
}
