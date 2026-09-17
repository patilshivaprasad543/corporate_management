/*
 * CorporateTravel360 portal policy.
 *
 * The backend keeps legacy authority names for workflow compatibility, while
 * the user-facing application exposes exactly six portals:
 * Employee, Manager, Admin, Finance, HR and Support.
 *
 * This file intentionally does not contain passwords. Authentication remains
 * database/JWT based; users can change their password through /api/auth/change-password.
 */
(function () {
  'use strict';

  if (typeof DEMO_USERS !== 'undefined') {
    // Keep only the six user-facing personas. Legacy authorities remain
    // available to backend authorization but are not exposed as portals.
    delete DEMO_USERS.ROLE_TRAVEL_MANAGER;
    delete DEMO_USERS.ROLE_SUPER_ADMIN;
    delete DEMO_USERS.ROLE_VENDOR;

    DEMO_USERS.ROLE_APPROVER.portalLabel = 'Manager';
    DEMO_USERS.ROLE_COMPANY_ADMIN.portalLabel = 'Admin';
    DEMO_USERS.ROLE_EMPLOYEE.portalLabel = 'Employee';
    DEMO_USERS.ROLE_FINANCE.portalLabel = 'Finance';
    DEMO_USERS.ROLE_HR.portalLabel = 'HR';
    DEMO_USERS.ROLE_SUPPORT.portalLabel = 'Support';
  }

  if (typeof ROLE_NAVS !== 'undefined') {
    // Manager portal combines the existing manager/approver workflow without
    // creating another visible portal.
    ROLE_NAVS.ROLE_APPROVER = [
      { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
      { id: 'dashboard', label: 'Manager Dashboard', icon: 'layout-dashboard' },
      { id: 'approvals', label: 'Pending Approvals', icon: 'check-square', badge: '1' },
      { id: 'requests', label: 'Team Travel Requests', icon: 'file-text' },
      { id: 'itinerary', label: 'Team Trips', icon: 'calendar-days' },
      { id: 'expenses', label: 'Team Expenses', icon: 'receipt' },
      { id: 'analytics', label: 'Team Budget Analytics', icon: 'bar-chart-3' }
    ];

    ROLE_NAVS.ROLE_COMPANY_ADMIN = [
      { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
      { id: 'dashboard', label: 'Admin Command Center', icon: 'layout-dashboard' },
      { id: 'requests', label: 'All Company Requests', icon: 'file-text' },
      { id: 'approvals', label: 'Executive Approvals', icon: 'check-square' },
      { id: 'analytics', label: 'Executive Analytics', icon: 'trending-up' },
      { id: 'settings', label: 'Policies & Cost Centers', icon: 'sliders' },
      { id: 'audit', label: 'Security & Audit Logs', icon: 'shield-check' }
    ];
  }

  // Canonical portal metadata for UI components that need a six-portal list.
  window.CORPORATE_PORTALS = Object.freeze([
    { key: 'ROLE_COMPANY_ADMIN', label: 'Admin', description: 'Company administration, policies, approvals, budgets and audit.' },
    { key: 'ROLE_EMPLOYEE', label: 'Employee', description: 'Travel requests, bookings, itineraries, expenses and personal travel assistance.' },
    { key: 'ROLE_APPROVER', label: 'Manager', description: 'Team travel approvals, team requests, expenses and budgets.' },
    { key: 'ROLE_HR', label: 'HR', description: 'Employee travel records, people operations and duty of care.' },
    { key: 'ROLE_FINANCE', label: 'Finance', description: 'Expense verification, reimbursements, budgets and financial reporting.' },
    { key: 'ROLE_SUPPORT', label: 'Support', description: 'Traveler assistance, itinerary support, incidents and alerts.' }
  ]);
})();
