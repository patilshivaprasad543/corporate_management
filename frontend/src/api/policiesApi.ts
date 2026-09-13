import api from './axios';
import type { ApiResponse } from '../types/auth';

export interface TravelPolicy {
  id: number;
  name: string;
  description?: string;
  organizationId: number;
  organizationName?: string;
  maxDomesticFlightPrice?: number;
  maxInternationalFlightPrice?: number;
  maxHotelPricePerNight?: number;
  hotelRoomLimit?: number;
  dailyMealAllowance?: number;
  dailyTaxiAllowance?: number;
  maxTransportAmount?: number;
  advanceBookingDays?: number;
  allowedFlightClass?: string;
  allowedHotelCategory?: string;
  financeApprovalThreshold?: number;
  adminApprovalThreshold?: number;
  internationalRequiresFinance?: boolean;
  active?: boolean;
}

export interface PolicyEvaluation {
  status: string;
  summary?: string;
  violations: Array<{
    violationType: string;
    ruleName: string;
    requestedAmount?: number;
    allowedAmount?: number;
    differenceAmount?: number;
    severity: string;
    explanation: string;
  }>;
  warnings: string[];
  requiresFinanceApproval: boolean;
  requiresAdminApproval: boolean;
  requiresManagerApproval: boolean;
}

export async function listPolicies(): Promise<TravelPolicy[]> {
  const res = await api.get<ApiResponse<TravelPolicy[]>>('/policies');
  return res.data.data;
}

export async function evaluatePolicy(data: Record<string, unknown>): Promise<PolicyEvaluation> {
  const res = await api.post<ApiResponse<PolicyEvaluation>>('/policies/evaluate', data);
  return res.data.data;
}

export async function updatePolicy(id: number, data: Partial<TravelPolicy>): Promise<TravelPolicy> {
  const res = await api.put<ApiResponse<TravelPolicy>>(`/policies/${id}`, data);
  return res.data.data;
}

export async function listViolations(): Promise<unknown[]> {
  const res = await api.get<ApiResponse<unknown[]>>('/policies/violations');
  return res.data.data;
}
