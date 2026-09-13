import api from './axios';
import type { ApiResponse } from '../types/auth';

export interface Company {
  id: number;
  name: string;
  code: string;
  registrationNumber?: string;
  taxNumber?: string;
  email?: string;
  phone?: string;
  domainName?: string;
  addressLine1?: string;
  city?: string;
  state?: string;
  country?: string;
  currencyCode?: string;
  timezone?: string;
  annualTravelBudget?: number;
  active?: boolean;
  employeeCount?: number;
  departmentCount?: number;
  createdAt?: string;
}

export interface SystemStats {
  totalCompanies: number;
  activeCompanies: number;
  totalUsers: number;
  totalDepartments: number;
  totalTravelRequests: number;
  totalBookings: number;
}

export interface CreateCompanyRequest {
  name: string;
  code: string;
  registrationNumber?: string;
  taxNumber?: string;
  email?: string;
  phone?: string;
  domainName?: string;
  addressLine1?: string;
  city?: string;
  state?: string;
  country: string;
  currencyCode: string;
  timezone?: string;
  annualTravelBudget?: number;
}

export async function getSystemStats(): Promise<SystemStats> {
  const res = await api.get<ApiResponse<SystemStats>>('/companies/stats');
  return res.data.data;
}

export async function listCompanies(): Promise<Company[]> {
  const res = await api.get<ApiResponse<Company[]>>('/companies');
  return res.data.data;
}

export async function createCompany(data: CreateCompanyRequest): Promise<Company> {
  const res = await api.post<ApiResponse<Company>>('/companies', data);
  return res.data.data;
}

export async function activateCompany(id: number): Promise<Company> {
  const res = await api.patch<ApiResponse<Company>>(`/companies/${id}/activate`);
  return res.data.data;
}

export async function deactivateCompany(id: number): Promise<Company> {
  const res = await api.patch<ApiResponse<Company>>(`/companies/${id}/deactivate`);
  return res.data.data;
}
