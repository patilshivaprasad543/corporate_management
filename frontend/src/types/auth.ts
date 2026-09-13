export type PortalType =
  | 'EMPLOYEE'
  | 'MANAGER'
  | 'TRAVEL_ADMIN'
  | 'FINANCE'
  | 'TRAVEL_AGENT'
  | 'VENDOR'
  | 'SUPER_ADMIN';

export interface AuthUser {
  userId: number;
  username: string;
  email: string;
  fullName: string;
  organizationId?: number;
  organizationName?: string;
  roles: string[];
  permissions?: string[];
  portal: PortalType;
  emailVerified?: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
  portal: PortalType;
  organizationId?: number;
}

export interface CompanyOption {
  id: number;
  name: string;
  code: string;
  country?: string;
  currencyCode?: string;
}

export interface DepartmentOption {
  id: number;
  name: string;
  code?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  username: string;
  email: string;
  fullName: string;
  organizationId?: number;
  organizationName?: string;
  roles: string[];
  portal?: string;
  emailVerified?: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
