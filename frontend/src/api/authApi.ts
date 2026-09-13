import api from './axios';
import type { ApiResponse, AuthResponse, CompanyOption, DepartmentOption, LoginRequest } from '../types/auth';

export async function login(request: LoginRequest): Promise<AuthResponse> {
  const res = await api.post<ApiResponse<AuthResponse>>('/auth/login', request);
  return res.data.data;
}

export async function register(data: {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  employeeId: string;
  phone?: string;
  organizationId: number;
  departmentId?: number;
}): Promise<AuthResponse> {
  const res = await api.post<ApiResponse<AuthResponse>>('/auth/register', data);
  return res.data.data;
}

export async function listCompanies(): Promise<CompanyOption[]> {
  const res = await api.get<ApiResponse<CompanyOption[]>>('/auth/companies');
  return res.data.data;
}

export async function listDepartments(organizationId: number): Promise<DepartmentOption[]> {
  const res = await api.get<ApiResponse<DepartmentOption[]>>(`/auth/companies/${organizationId}/departments`);
  return res.data.data;
}

export async function verifyOtp(email: string, otp: string): Promise<void> {
  await api.post('/auth/verify-otp', { email, otp });
}

export async function resendOtp(email: string): Promise<void> {
  await api.post('/auth/resend-otp', { email });
}

export async function logout(refreshToken: string | null): Promise<void> {
  await api.post('/auth/logout', { refreshToken });
}

export async function getMe() {
  const res = await api.get<ApiResponse<Record<string, unknown>>>('/auth/me');
  return res.data.data;
}
