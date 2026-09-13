import type { PortalType } from '../types/auth';
import { } from '../types/auth';

export interface PortalConfig {
  portal: PortalType;
  loginPath: string;
  dashboardPath: string;
  title: string;
  subtitle: string;
  accentColor: string;
  requiredRole: string;
  showRegister: boolean;
}

export const PORTALS: Record<PortalType, PortalConfig> = {
  EMPLOYEE: {
    portal: 'EMPLOYEE',
    loginPath: '/employee/login',
    dashboardPath: '/employee/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Employee Portal',
    accentColor: 'indigo',
    requiredRole: 'ROLE_EMPLOYEE',
    showRegister: true,
  },
  MANAGER: {
    portal: 'MANAGER',
    loginPath: '/manager/login',
    dashboardPath: '/manager/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Manager Portal',
    accentColor: 'blue',
    requiredRole: 'ROLE_APPROVER',
    showRegister: false,
  },
  TRAVEL_ADMIN: {
    portal: 'TRAVEL_ADMIN',
    loginPath: '/travel-admin/login',
    dashboardPath: '/travel-admin/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Travel Administration Portal',
    accentColor: 'violet',
    requiredRole: 'ROLE_TRAVEL_MANAGER',
    showRegister: false,
  },
  FINANCE: {
    portal: 'FINANCE',
    loginPath: '/finance/login',
    dashboardPath: '/finance/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Finance Portal',
    accentColor: 'emerald',
    requiredRole: 'ROLE_FINANCE',
    showRegister: false,
  },
  TRAVEL_AGENT: {
    portal: 'TRAVEL_AGENT',
    loginPath: '/travel-agent/login',
    dashboardPath: '/travel-agent/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Travel Agent / TMC Portal',
    accentColor: 'cyan',
    requiredRole: 'ROLE_SUPPORT',
    showRegister: false,
  },
  VENDOR: {
    portal: 'VENDOR',
    loginPath: '/vendor/login',
    dashboardPath: '/vendor/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Vendor Portal',
    accentColor: 'amber',
    requiredRole: 'ROLE_VENDOR',
    showRegister: false,
  },
  SUPER_ADMIN: {
    portal: 'SUPER_ADMIN',
    loginPath: '/super-admin/login',
    dashboardPath: '/super-admin/dashboard',
    title: 'Corporate Travel',
    subtitle: 'Super Administration Portal',
    accentColor: 'rose',
    requiredRole: 'ROLE_SUPER_ADMIN',
    showRegister: false,
  },
};

export const EMPLOYEE_NAV = [
  'Dashboard', 'Profile', 'Travel Requests', 'Bookings', 'Trips',
  'Expenses', 'Reimbursements', 'Documents', 'Notifications', 'Support', 'AI Assistant',
];

export const MANAGER_NAV = [
  'Dashboard', 'Approvals', 'Team Travel', 'Reports', 'Notifications', 'Profile',
];
