/* CorporateTravel360: exactly six user-facing portals. */
(function () {
  'use strict';

  const visibleRoles = new Set([
    'ROLE_COMPANY_ADMIN',
    'ROLE_EMPLOYEE',
    'ROLE_APPROVER',
    'ROLE_HR',
    'ROLE_FINANCE',
    'ROLE_SUPPORT'
  ]);

  if (typeof DEMO_USERS !== 'undefined') {
    Object.keys(DEMO_USERS).forEach(function (role) {
      if (!visibleRoles.has(role)) delete DEMO_USERS[role];
    });
    DEMO_USERS.ROLE_COMPANY_ADMIN.portalLabel = 'Admin';
    DEMO_USERS.ROLE_EMPLOYEE.portalLabel = 'Employee';
    DEMO_USERS.ROLE_APPROVER.portalLabel = 'Manager';
    DEMO_USERS.ROLE_HR.portalLabel = 'HR';
    DEMO_USERS.ROLE_FINANCE.portalLabel = 'Finance';
    DEMO_USERS.ROLE_SUPPORT.portalLabel = 'Support';
  }

  if (typeof ROLE_NAVS !== 'undefined') {
    delete ROLE_NAVS.ROLE_TRAVEL_MANAGER;
    delete ROLE_NAVS.ROLE_SUPER_ADMIN;
    delete ROLE_NAVS.ROLE_VENDOR;

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

  // The role selector is a presentation control; keep it aligned with the
  // six portal contract even though the backend retains legacy authorities.
  const selector = document.getElementById('roleSelector');
  if (selector) {
    Array.from(selector.options).forEach(function (option) {
      if (!visibleRoles.has(option.value)) option.remove();
    });
    const labels = {
      ROLE_COMPANY_ADMIN: 'Admin',
      ROLE_EMPLOYEE: 'Employee',
      ROLE_APPROVER: 'Manager',
      ROLE_HR: 'HR',
      ROLE_FINANCE: 'Finance',
      ROLE_SUPPORT: 'Support'
    };
    Array.from(selector.options).forEach(function (option) {
      if (labels[option.value]) option.textContent = labels[option.value];
    });
  }

  window.CORPORATE_PORTALS = Object.freeze([
    { key: 'ROLE_COMPANY_ADMIN', label: 'Admin', description: 'Company administration, policies, approvals, budgets and audit.' },
    { key: 'ROLE_EMPLOYEE', label: 'Employee', description: 'Travel requests, bookings, itineraries, expenses and personal travel assistance.' },
    { key: 'ROLE_APPROVER', label: 'Manager', description: 'Team travel approvals, team requests, expenses and budgets.' },
    { key: 'ROLE_HR', label: 'HR', description: 'Employee travel records, people operations and duty of care.' },
    { key: 'ROLE_FINANCE', label: 'Finance', description: 'Expense verification, reimbursements, budgets and financial reporting.' },
    { key: 'ROLE_SUPPORT', label: 'Support', description: 'Traveler assistance, itinerary support, incidents and alerts.' }
  ]);
})();
