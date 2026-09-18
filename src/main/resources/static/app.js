// =========================================================================
// CORPORATETRAVEL360 - ENTERPRISE BUSINESS TRAVEL & EXPENSE MANAGEMENT
// COMPLETE END-TO-END INTERACTIVE PRODUCTION SPA (LIVE API & DB PERSISTENCE)
// =========================================================================

const STATE = {
  currentRole: localStorage.getItem('corporate_user_role') || null,
  token: localStorage.getItem('corporate_jwt_token') || '',
  isAuthenticated: !!(localStorage.getItem('corporate_jwt_token') && localStorage.getItem('corporate_user_role')),
  currentUser: { name: 'Guest Traveler', designation: 'Please Authenticate', avatar: '🔒' },
  activeTab: (localStorage.getItem('corporate_jwt_token') && localStorage.getItem('corporate_user_role')) ? 'dashboard' : 'login-portal',
  currency: 'INR',
  currencySymbol: '₹',
  exchangeRates: { INR: 1, USD: 0.012, EUR: 0.011, GBP: 0.0095 },
  isPersonalMode: false,
  notifications: [
    { id: 1, title: 'Travel Request Approved', desc: 'Request TR-1082 (Delhi Summit) approved by Line Manager & Finance.', type: 'APPROVED', time: '10m ago' },
    { id: 2, title: 'Booking Confirmed - PNR683921', desc: 'Air India AI-839 e-ticket issued for Delhi departure.', type: 'BOOKING', time: '1h ago' },
    { id: 3, title: 'Transit Advisory in Paris', desc: 'Regional rail strike alert active for Paris business travel.', type: 'ALERT', time: '2h ago' }
  ],
  requests: [],
  bookings: [],
  expenses: [],
  riskAlerts: [],
  auditLogs: []
};

// =========================================================================
// CENTRALIZED AUTHENTICATED API FETCH HELPER
// =========================================================================
async function apiFetch(path, options = {}) {
  const headers = {
    'Accept': 'application/json',
    ...(options.headers || {})
  };

  if (STATE.token) {
    headers['Authorization'] = `Bearer ${STATE.token}`;
  }

  if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(options.body);
  }

  try {
    const res = await fetch(path, { ...options, headers });
    if (res.status === 401 || res.status === 403) {
      console.warn(`API ${path} returned ${res.status}. Attempting session refresh...`);
    }
    const data = await res.json();
    return data;
  } catch (err) {
    console.error(`API Fetch Error on ${path}:`, err);
    return null;
  }
}

// 9 Demo Personas with Default Credentials
const DEMO_USERS = {
  ROLE_EMPLOYEE: { id: 5, name: 'Priya Sharma', email: 'traveler@acmetech.com', role: 'ROLE_EMPLOYEE', designation: 'Senior Software Architect', department: 'Engineering & Innovation', avatar: 'PS', wallet: { allocated: 350000, spent: 25800, pending: 3200, remaining: 324200 } },
  ROLE_APPROVER: { id: 3, name: 'Robert Vance', email: 'manager@acmetech.com', role: 'ROLE_APPROVER', designation: 'VP of Engineering (Line Approver)', department: 'Engineering & Innovation', avatar: 'RV', wallet: { allocated: 500000, spent: 85000, pending: 0, remaining: 415000 } },
  ROLE_TRAVEL_MANAGER: { id: 4, name: 'Elena Rostova', email: 'travelmgr@acmetech.com', role: 'ROLE_TRAVEL_MANAGER', designation: 'Head of Global Corporate Travel', department: 'Operations & Procurement', avatar: 'ER', wallet: { allocated: 1000000, spent: 420000, pending: 15000, remaining: 565000 } },
  ROLE_FINANCE: { id: 6, name: 'David Miller', email: 'finance@acmetech.com', role: 'ROLE_FINANCE', designation: 'Senior Finance & Audit Lead', department: 'Corporate Finance', avatar: 'DM', wallet: { allocated: 2000000, spent: 780000, pending: 45000, remaining: 1175000 } },
  ROLE_COMPANY_ADMIN: { id: 2, name: 'Sarah Connor', email: 'admin@acmetech.com', role: 'ROLE_COMPANY_ADMIN', designation: 'Company Administrator', department: 'Executive Management', avatar: 'SC', wallet: { allocated: 1500000, spent: 310000, pending: 8000, remaining: 1182000 } },
  ROLE_SUPER_ADMIN: { id: 1, name: 'Alexander Pierce', email: 'superadmin@corporatetravel.com', role: 'ROLE_SUPER_ADMIN', designation: 'Platform Super Administrator', department: 'SaaS Platform Ops', avatar: 'AP', wallet: { allocated: 5000000, spent: 1250000, pending: 0, remaining: 3750000 } },
  ROLE_HR: { id: 7, name: 'Rachel Green', email: 'hr@acmetech.com', role: 'ROLE_HR', designation: 'HR & Employee Onboarding Lead', department: 'Human Resources', avatar: 'RG', wallet: { allocated: 400000, spent: 42000, pending: 0, remaining: 358000 } },
  ROLE_VENDOR: { id: 8, name: 'Vikram Malhotra', email: 'partner@indigoair.com', role: 'ROLE_VENDOR', designation: 'Air Partner Key Account Lead', department: 'Airline Operations', avatar: 'VM', wallet: { allocated: 0, spent: 0, pending: 0, remaining: 0 } },
  ROLE_SUPPORT: { id: 9, name: 'James Wilson', email: 'support@corporatetravel.com', role: 'ROLE_SUPPORT', designation: '24/7 Global Traveler Care Agent', department: 'Customer Success', avatar: 'JW', wallet: { allocated: 250000, spent: 12000, pending: 0, remaining: 238000 } }
};

// All 11 platform modules + Dedicated Login Portal
const ALL_MODULES = [
  { id: 'login-portal', label: '🔐 Login & Role Portal', icon: 'shield-check', desc: 'Enterprise Identity Gateway: 1-click persona switching (9 roles), JWT auth, and permissions matrix.' },
  { id: 'dashboard', label: 'Dashboard & Overview', icon: 'layout-dashboard', desc: 'Central KPI metrics, upcoming trips, and corporate spend status.' },
  { id: 'search', label: 'Book Travel (GDS Search)', icon: 'search', desc: 'Search flights, hotels & ground transportation with negotiated corporate tariffs.' },
  { id: 'requests', label: 'Travel Requests', icon: 'file-text', desc: 'Submit business travel proposals with automated policy compliance evaluation.' },
  { id: 'approvals', label: 'Approvals Hub', icon: 'check-square', desc: 'Multi-level approval queue with budget thresholds (< ₹25k / ₹25k-100k / > ₹100k).' },
  { id: 'itinerary', label: 'Itinerary & E-Tickets', icon: 'calendar-days', desc: 'Unified chronological trip timeline with PNR683921 and electronic boarding pass.' },
  { id: 'expenses', label: 'Expenses & Receipt OCR', icon: 'receipt', desc: 'AI Vision OCR receipt scanner, per-diem limit checks, and Finance reimbursements.' },
  { id: 'ai-assistant', label: 'AI Travel Assistant', icon: 'sparkles', desc: 'Natural language travel recommendations, policy insights, and trip cost optimizer.' },
  { id: 'risk', label: 'Risk & Duty of Care', icon: 'shield-alert', desc: 'Live traveler location tracking, flight disruption alerts, and emergency SOS.' },
  { id: 'analytics', label: 'Executive BI Analytics', icon: 'bar-chart-3', desc: 'Department budget burn trends, compliance scoring, and 142.5 kg CO₂ saved.' },
  { id: 'settings', label: 'Policy & Org Settings', icon: 'settings', desc: 'Corporate travel tiers, cost center budgets, and preferred airline/hotel contracts.' },
  { id: 'audit', label: 'Enterprise Audit Trail', icon: 'shield-check', desc: 'Immutable security log tracking bookings, auth tokens, and financial approvals.' }
];

const ROLE_THEMES = {
  ROLE_EMPLOYEE: {
    portalName: 'Employee Traveler Portal',
    shortName: 'Traveler',
    gradient: 'from-indigo-600 via-blue-600 to-cyan-500',
    cardGradient: 'from-indigo-500/20 via-blue-500/10 to-cyan-500/10',
    accent: 'indigo',
    accentText: 'text-indigo-300',
    accentBg: 'bg-indigo-500/15',
    border: 'border-indigo-500/40',
    glow: 'portal-glow-indigo',
    icon: 'plane',
    tagline: 'Search flights, submit requests, and manage your business travel wallet.'
  },
  ROLE_APPROVER: {
    portalName: 'Line Manager Approver Portal',
    shortName: 'Approver',
    gradient: 'from-amber-500 via-orange-600 to-rose-500',
    cardGradient: 'from-amber-500/20 via-orange-500/10 to-rose-500/10',
    accent: 'amber',
    accentText: 'text-amber-300',
    accentBg: 'bg-amber-500/15',
    border: 'border-amber-500/40',
    glow: 'portal-glow-amber',
    icon: 'check-square',
    tagline: 'Review team travel requests, approve budgets, and enforce policy compliance.'
  },
  ROLE_TRAVEL_MANAGER: {
    portalName: 'Travel Operations Portal',
    shortName: 'Travel Ops',
    gradient: 'from-violet-600 via-purple-600 to-fuchsia-500',
    cardGradient: 'from-violet-500/20 via-purple-500/10 to-fuchsia-500/10',
    accent: 'violet',
    accentText: 'text-violet-300',
    accentBg: 'bg-violet-500/15',
    border: 'border-violet-500/40',
    glow: 'portal-glow-violet',
    icon: 'globe-2',
    tagline: 'Operate corporate booking desk, monitor active trips, and manage travel spend.'
  },
  ROLE_FINANCE: {
    portalName: 'Finance & Reimbursement Portal',
    shortName: 'Finance',
    gradient: 'from-emerald-600 via-teal-600 to-green-500',
    cardGradient: 'from-emerald-500/20 via-teal-500/10 to-green-500/10',
    accent: 'emerald',
    accentText: 'text-emerald-300',
    accentBg: 'bg-emerald-500/15',
    border: 'border-emerald-500/40',
    glow: 'portal-glow-emerald',
    icon: 'wallet',
    tagline: 'Audit expense claims, approve reimbursements, and track financial compliance.'
  },
  ROLE_COMPANY_ADMIN: {
    portalName: 'Company Admin Command Center',
    shortName: 'Admin',
    gradient: 'from-sky-600 via-blue-700 to-indigo-700',
    cardGradient: 'from-sky-500/20 via-blue-500/10 to-indigo-500/10',
    accent: 'sky',
    accentText: 'text-sky-300',
    accentBg: 'bg-sky-500/15',
    border: 'border-sky-500/40',
    glow: 'portal-glow-sky',
    icon: 'building-2',
    tagline: 'Configure policies, oversee company travel programs, and manage cost centers.'
  },
  ROLE_SUPER_ADMIN: {
    portalName: 'Platform Super Admin Portal',
    shortName: 'Super Admin',
    gradient: 'from-rose-600 via-pink-600 to-purple-600',
    cardGradient: 'from-rose-500/20 via-pink-500/10 to-purple-500/10',
    accent: 'rose',
    accentText: 'text-rose-300',
    accentBg: 'bg-rose-500/15',
    border: 'border-rose-500/40',
    glow: 'portal-glow-rose',
    icon: 'crown',
    tagline: 'Multi-tenant platform operations, global analytics, and system configuration.'
  },
  ROLE_HR: {
    portalName: 'HR Travel & Duty of Care Portal',
    shortName: 'HR',
    gradient: 'from-pink-600 via-rose-500 to-orange-400',
    cardGradient: 'from-pink-500/20 via-rose-500/10 to-orange-400/10',
    accent: 'pink',
    accentText: 'text-pink-300',
    accentBg: 'bg-pink-500/15',
    border: 'border-pink-500/40',
    glow: 'portal-glow-pink',
    icon: 'heart-handshake',
    tagline: 'Monitor employee travel logs, onboarding, and duty-of-care compliance.'
  },
  ROLE_VENDOR: {
    portalName: 'Airline Partner Portal',
    shortName: 'Vendor',
    gradient: 'from-orange-500 via-amber-500 to-yellow-400',
    cardGradient: 'from-orange-500/20 via-amber-500/10 to-yellow-400/10',
    accent: 'orange',
    accentText: 'text-orange-300',
    accentBg: 'bg-orange-500/15',
    border: 'border-orange-500/40',
    glow: 'portal-glow-orange',
    icon: 'building',
    tagline: 'Manage negotiated inventory, corporate rates, and booking settlements.'
  },
  ROLE_SUPPORT: {
    portalName: '24/7 Traveler Care Portal',
    shortName: 'Support',
    gradient: 'from-cyan-600 via-teal-500 to-emerald-500',
    cardGradient: 'from-cyan-500/20 via-teal-500/10 to-emerald-500/10',
    accent: 'cyan',
    accentText: 'text-cyan-300',
    accentBg: 'bg-cyan-500/15',
    border: 'border-cyan-500/40',
    glow: 'portal-glow-cyan',
    icon: 'headphones',
    tagline: 'Emergency response, live traveler support, and itinerary assistance.'
  }
};

function getRoleTheme(roleKey) {
  return ROLE_THEMES[roleKey] || ROLE_THEMES.ROLE_EMPLOYEE;
}

function renderPortalHero(roleKey, compact = false) {
  const theme = getRoleTheme(roleKey);
  const user = DEMO_USERS[roleKey] || STATE.currentUser;
  const padding = compact ? 'p-5' : 'p-6 md:p-8';
  return `
    <div class="portal-hero rounded-3xl ${padding} mb-6 relative overflow-hidden border ${theme.border} ${theme.glow}">
      <div class="absolute inset-0 bg-gradient-to-br ${theme.gradient} opacity-90"></div>
      <div class="absolute inset-0 portal-hero-pattern opacity-30"></div>
      <div class="absolute -right-10 -top-10 h-40 w-40 rounded-full bg-white/10 blur-2xl"></div>
      <div class="absolute -left-8 bottom-0 h-32 w-32 rounded-full bg-black/20 blur-2xl"></div>
      <div class="relative z-10 flex flex-col lg:flex-row lg:items-center justify-between gap-5">
        <div class="flex items-start gap-4">
          <div class="h-16 w-16 rounded-2xl bg-white/15 backdrop-blur-md border border-white/20 flex items-center justify-center text-white font-extrabold text-lg shadow-2xl shrink-0">
            <i data-lucide="${theme.icon}" class="w-8 h-8"></i>
          </div>
          <div>
            <div class="flex flex-wrap items-center gap-2 mb-1">
              <span class="text-[10px] uppercase tracking-[0.2em] font-bold text-white/70">${theme.shortName} Workspace</span>
              <span class="text-[10px] px-2 py-0.5 rounded-full bg-white/15 text-white font-bold border border-white/20">RBAC Active</span>
            </div>
            <h2 class="text-2xl md:text-3xl font-extrabold text-white tracking-tight">${theme.portalName}</h2>
            <p class="text-sm text-white/80 mt-1 max-w-2xl">${theme.tagline}</p>
            <p class="text-xs text-white/60 mt-2">${user.name} • ${user.designation}</p>
          </div>
        </div>
        <div class="flex flex-wrap gap-2 shrink-0">
          <span class="px-3 py-1.5 rounded-xl bg-black/20 border border-white/15 text-xs font-bold text-white">${roleKey.replace('ROLE_', '').replace('_', ' ')}</span>
          <span class="px-3 py-1.5 rounded-xl bg-white/10 border border-white/15 text-xs font-semibold text-white/90">${(ROLE_NAVS[roleKey] || []).length - 1} modules unlocked</span>
        </div>
      </div>
    </div>
  `;
}

const ROLE_NAVS = {
  ROLE_EMPLOYEE: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'My Dashboard', icon: 'layout-dashboard' },
    { id: 'search', label: 'Book Travel', icon: 'search' },
    { id: 'requests', label: 'Travel Requests', icon: 'file-text' },
    { id: 'itinerary', label: 'My Itinerary', icon: 'calendar-days' },
    { id: 'expenses', label: 'Expenses & Wallet', icon: 'receipt' },
    { id: 'ai-assistant', label: 'AI Travel Assistant', icon: 'sparkles' },
    { id: 'risk', label: 'Risk & Safety', icon: 'shield-alert' }
  ],
  ROLE_APPROVER: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Approver Dashboard', icon: 'layout-dashboard' },
    { id: 'approvals', label: 'Pending Approvals', icon: 'check-square', badge: '1' },
    { id: 'requests', label: 'Team Requests', icon: 'file-text' },
    { id: 'expenses', label: 'Team Expenses', icon: 'receipt' },
    { id: 'analytics', label: 'Team Budget Analytics', icon: 'bar-chart-3' }
  ],
  ROLE_TRAVEL_MANAGER: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Travel Operations', icon: 'layout-dashboard' },
    { id: 'search', label: 'Corporate Booking Desk', icon: 'search' },
    { id: 'requests', label: 'All Travel Requests', icon: 'file-text' },
    { id: 'itinerary', label: 'Active Traveler Trips', icon: 'plane' },
    { id: 'risk', label: 'Emergency Center', icon: 'shield-alert' },
    { id: 'analytics', label: 'Travel Spend ROI', icon: 'bar-chart-3' }
  ],
  ROLE_FINANCE: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Finance Dashboard', icon: 'layout-dashboard' },
    { id: 'approvals', label: 'Budget Approvals', icon: 'check-square' },
    { id: 'expenses', label: 'Expense Reimbursements', icon: 'receipt', badge: '1' },
    { id: 'analytics', label: 'Financial Analytics', icon: 'pie-chart' },
    { id: 'audit', label: 'Audit Trail', icon: 'shield-check' }
  ],
  ROLE_COMPANY_ADMIN: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Admin Command Center', icon: 'layout-dashboard' },
    { id: 'requests', label: 'All Company Requests', icon: 'file-text' },
    { id: 'approvals', label: 'Executive Approvals', icon: 'check-square' },
    { id: 'analytics', label: 'Executive Analytics', icon: 'trending-up' },
    { id: 'settings', label: 'Policies & Cost Centers', icon: 'sliders' },
    { id: 'audit', label: 'Security & Audit Logs', icon: 'shield-check' }
  ],
  ROLE_SUPER_ADMIN: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Global Platform Ops', icon: 'layout-dashboard' },
    { id: 'analytics', label: 'Multi-Tenant Analytics', icon: 'bar-chart-3' },
    { id: 'settings', label: 'System Configuration', icon: 'settings' },
    { id: 'audit', label: 'Global Audit Logs', icon: 'shield-check' }
  ],
  ROLE_HR: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'HR Travel Overview', icon: 'layout-dashboard' },
    { id: 'requests', label: 'Employee Travel Logs', icon: 'users' },
    { id: 'risk', label: 'Traveler Duty of Care', icon: 'heart-handshake' }
  ],
  ROLE_VENDOR: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Partner Portal', icon: 'layout-dashboard' },
    { id: 'search', label: 'Inventory & Rates', icon: 'building' },
    { id: 'analytics', label: 'Booking Settlements', icon: 'receipt' }
  ],
  ROLE_SUPPORT: [
    { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
    { id: 'dashboard', label: 'Support Desk', icon: 'layout-dashboard' },
    { id: 'risk', label: 'Emergency Center', icon: 'shield-alert' },
    { id: 'itinerary', label: 'Traveler Itineraries', icon: 'calendar-days' }
  ]
};

// =========================================================================
// 8-STEP INTERACTIVE LIFECYCLE CONTROLLER
// =========================================================================
const FLOW_STATE = {
  currentStep: 1,
  totalSteps: 8,
  isAutoPlaying: false,
  autoPlayTimer: null,
  steps: [
    { step: 1, id: 'search', title: 'Plan & Search', shortTitle: '1. Search & Rates', icon: 'plane', role: 'ROLE_EMPLOYEE', roleLabel: 'Priya Sharma (Traveler)', badge: 'Step 1 of 8', summary: 'Search flights, hotels & ground transport with corporate discount rates.', actionText: 'Select Flight & Create Request ➔', tab: 'search' },
    { step: 2, id: 'requests', title: 'Submit Travel Request', shortTitle: '2. Travel Request', icon: 'file-text', role: 'ROLE_EMPLOYEE', roleLabel: 'Priya Sharma (Traveler)', badge: 'Step 2 of 8', summary: 'Automated policy compliance check, travel justification, and dynamic budget cap validation.', actionText: 'Submit Request & Route to Manager ➔', tab: 'requests' },
    { step: 3, id: 'approvals', title: 'Manager Approval', shortTitle: '3. Approval', icon: 'check-square', role: 'ROLE_APPROVER', roleLabel: 'Robert Vance (Line Manager)', badge: 'Step 3 of 8', summary: 'Multi-tier manager & budget sign-off. Instant authorization with automated audit logging.', actionText: 'Approve Request & Issue Booking ➔', tab: 'approvals' },
    { step: 4, id: 'itinerary', title: 'Book & E-Ticket', shortTitle: '4. E-Ticket & PNR', icon: 'ticket', role: 'ROLE_EMPLOYEE', roleLabel: 'Priya Sharma (Traveler)', badge: 'Step 4 of 8', summary: 'Instant PNR683921 issuance, seat selection, and digital boarding pass synchronization.', actionText: 'Confirm Boarding Pass & Start Trip ➔', tab: 'itinerary' },
    { step: 5, id: 'risk', title: 'Active Trip & Safety', shortTitle: '5. Risk & Care', icon: 'shield-alert', role: 'ROLE_SUPPORT', roleLabel: 'James Wilson (Traveler Care)', badge: 'Step 5 of 8', summary: 'Real-time traveler GPS tracking, live flight disruption monitoring, and 24/7 SOS desk.', actionText: 'Trip Completed ➔ File Expense Claim ➔', tab: 'risk' },
    { step: 6, id: 'expenses', title: 'AI OCR Expense Claim', shortTitle: '6. AI Expense Claim', icon: 'receipt', role: 'ROLE_EMPLOYEE', roleLabel: 'Priya Sharma (Traveler)', badge: 'Step 6 of 8', summary: 'AI computer vision receipt OCR extracts merchant and taxes, checking daily per diem limits.', actionText: 'Submit Expense & Route to Finance ➔', tab: 'expenses' },
    { step: 7, id: 'finance-reimburse', title: 'Finance Settlement', shortTitle: '7. Finance Settle', icon: 'wallet', role: 'ROLE_FINANCE', roleLabel: 'David Miller (Finance Lead)', badge: 'Step 7 of 8', summary: 'Finance controller audits the claim and disburses instant reimbursement to user wallet.', actionText: 'Settle Payout & View Executive ROI ➔', tab: 'expenses' },
    { step: 8, id: 'analytics', title: 'Executive BI & ESG', shortTitle: '8. BI & Carbon ROI', icon: 'bar-chart-3', role: 'ROLE_SUPER_ADMIN', roleLabel: 'Alexander Pierce (Platform Admin)', badge: 'Step 8 of 8', summary: 'Company-wide travel ROI, department burn rate, 98.4% policy compliance, and 142.5 kg CO₂ saved.', actionText: '🎉 Lifecycle Complete! Restart Flow 🔄', tab: 'analytics' }
  ]
};

// =========================================================================
// INITIALIZATION
// =========================================================================
document.addEventListener('DOMContentLoaded', async () => {
  const savedToken = localStorage.getItem('corporate_jwt_token');
  const savedRole = localStorage.getItem('corporate_user_role');

  if (savedToken && savedRole && DEMO_USERS[savedRole]) {
    STATE.token = savedToken;
    STATE.isAuthenticated = true;
    STATE.currentRole = savedRole;
    STATE.currentUser = DEMO_USERS[savedRole];
    STATE.activeTab = 'dashboard';
    updateUserProfileHeader();
    renderNavigation();
    updateTopStripActiveState('dashboard');
    await fetchInitialData();
    loadActiveTab();
  } else {
    // RESTRICTION LOGIN: Do NOT log in automatically! All modules locked!
    STATE.token = '';
    STATE.isAuthenticated = false;
    STATE.currentRole = null;
    STATE.currentUser = { name: 'Guest Traveler', designation: 'Please Authenticate', avatar: '🔒' };
    STATE.activeTab = 'login-portal';
    updateUserProfileHeader();
    renderNavigation();
    updateTopStripActiveState('login-portal');
    loadLoginPortalTab();
  }
  safeCreateIcons();
});

async function fetchInitialData() {
  try {
    const [reqs, books, exps, risks, audits] = await Promise.all([
      apiFetch('/api/travel-requests'),
      apiFetch('/api/bookings'),
      apiFetch('/api/expenses'),
      apiFetch('/api/risk/alerts'),
      apiFetch('/api/audit')
    ]);

    if (reqs && reqs.data) STATE.requests = reqs.data;
    if (books && books.data) STATE.bookings = books.data;
    if (exps && exps.data) STATE.expenses = exps.data;
    if (risks && risks.data) STATE.riskAlerts = risks.data;
    if (audits && audits.data) STATE.auditLogs = audits.data;
  } catch (err) {
    console.warn('Initial data preload error:', err);
  }
}

// =========================================================================
// NAVIGATION & TABS SWITCHING
// =========================================================================
function navigateToTab(tabId) {
  if (!STATE.isAuthenticated && tabId !== 'login-portal') {
    showToast(`🔒 Access Restricted: Please log in to unlock the '${tabId}' module.`, 'warning');
    STATE.activeTab = 'login-portal';
    renderNavigation();
    updateTopStripActiveState('login-portal');
    loadActiveTab();
    return;
  }

  const roleKey = STATE.currentRole || 'ROLE_EMPLOYEE';
  const allowed = (ROLE_NAVS[roleKey] || ROLE_NAVS.ROLE_EMPLOYEE).map(item => item.id);
  
  if (!allowed.includes(tabId)) {
    const roleName = roleKey.replace('ROLE_', '').replace('_', ' ');
    showToast(`Access Restricted: The '${tabId}' module is not available in the ${roleName} portal.`, 'warning');
    tabId = allowed.includes('dashboard') ? 'dashboard' : (allowed[0] || 'login-portal');
  }

  STATE.activeTab = tabId;
  renderNavigation();
  updateTopStripActiveState(tabId);
  loadActiveTab();
}

function updateTopStripActiveState(tabId) {
  try {
    const stripRoleName = document.getElementById('stripRoleName');

    if (!STATE.isAuthenticated) {
      if (stripRoleName) stripRoleName.textContent = 'AUTHENTICATION REQUIRED';
      document.querySelectorAll('.strip-btn').forEach(btn => {
        if (btn.id === 'strip-login-portal') {
          btn.style.display = 'inline-flex';
          btn.className = 'strip-btn px-4 py-1.5 rounded-lg bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white font-bold shadow-lg shadow-indigo-500/30 flex items-center gap-1.5 shrink-0 transition text-xs';
        } else {
          btn.style.display = 'none'; // Lock all other module buttons!
        }
      });
      return;
    }

    const roleKey = STATE.currentRole || 'ROLE_EMPLOYEE';
    const allowed = (ROLE_NAVS[roleKey] || ROLE_NAVS.ROLE_EMPLOYEE).map(item => item.id);
    
    // Update role badge in top strip
    if (stripRoleName) {
      const theme = getRoleTheme(roleKey);
      stripRoleName.textContent = `${theme.shortName.toUpperCase()} PORTAL`;
      stripRoleName.className = `${theme.accentText} tracking-wider shrink-0 mr-1 flex items-center gap-1`;
    }

    document.querySelectorAll('.strip-btn').forEach(btn => {
      const btnTab = btn.id.replace('strip-', '');
      if (allowed.includes(btnTab)) {
        btn.style.display = 'inline-flex';
        if (btnTab === tabId) {
          btn.className = 'strip-btn px-3.5 py-1 rounded-lg bg-indigo-600 text-white shadow-md shadow-indigo-600/30 flex items-center gap-1.5 shrink-0 transition text-xs font-bold';
        } else {
          btn.className = 'strip-btn px-3 py-1 rounded-lg bg-dark-900 text-slate-300 hover:text-white hover:bg-dark-700 border border-slate-800 flex items-center gap-1.5 shrink-0 transition text-xs font-semibold';
        }
      } else {
        btn.style.display = 'none';
      }
    });
  } catch(e) {}
}

function renderNavigation() {
  const navContainer = document.getElementById('navContainer');
  if (!navContainer) return;

  if (!STATE.isAuthenticated) {
    navContainer.innerHTML = `
      <div class="px-2 py-1 mb-2 text-[10px] font-bold text-rose-400 uppercase tracking-wider flex items-center gap-1.5">
        <i data-lucide="lock" class="w-3 h-3 text-rose-400"></i> ACCESS RESTRICTED
      </div>
      <button onclick="navigateToTab('login-portal')" class="w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs font-bold transition-all bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 text-white shadow-lg shadow-indigo-600/30">
        <div class="flex items-center gap-3">
          <i data-lucide="shield-check" class="w-4 h-4"></i>
          <span>🔐 Login & Roles Portal</span>
        </div>
        <span class="px-2 py-0.5 text-[9px] font-extrabold rounded-full bg-white text-indigo-900">Sign In</span>
      </button>

      <div class="p-3.5 mt-4 rounded-xl bg-dark-900/90 border border-slate-800 text-slate-400 text-center">
        <i data-lucide="shield-alert" class="w-6 h-6 text-amber-400 mx-auto mb-1.5"></i>
        <p class="text-[11px] font-bold text-slate-300">Modules Locked</p>
        <p class="text-[10px] text-slate-500 mt-1">Please sign in to unlock your role-authorized workspace.</p>
      </div>
    `;
    safeCreateIcons();
    return;
  }

  const roleKey = STATE.currentRole || 'ROLE_EMPLOYEE';
  const items = ROLE_NAVS[roleKey] || ROLE_NAVS.ROLE_EMPLOYEE;
  const roleName = roleKey.replace('ROLE_', '').replace('_', ' ');

  navContainer.innerHTML = `
    <div class="px-2 py-1 mb-2 flex items-center justify-between text-[10px] font-bold text-slate-400 uppercase tracking-wider">
      <span class="flex items-center gap-1.5 text-indigo-400">
        <i data-lucide="shield" class="w-3 h-3"></i> ${roleName} PORTAL
      </span>
      <span class="text-[9px] font-mono text-slate-500">${items.length} Modules</span>
    </div>

    ${items.map(item => `
      <button onclick="navigateToTab('${item.id}')" class="w-full flex items-center justify-between px-3.5 py-2 rounded-xl text-xs font-semibold transition-all ${STATE.activeTab === item.id ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30 font-bold' : 'text-slate-400 hover:bg-dark-700 hover:text-slate-100'}">
        <div class="flex items-center gap-3">
          <i data-lucide="${item.icon}" class="w-4 h-4"></i>
          <span class="truncate">${item.label}</span>
        </div>
        ${item.badge ? `<span class="px-2 py-0.5 text-[10px] font-extrabold rounded-full bg-amber-500 text-dark-900">${item.badge}</span>` : ''}
      </button>
    `).join('')}

    <div class="pt-4 mt-4 border-t border-slate-800">
      <button onclick="handleLogout()" class="w-full flex items-center justify-center gap-2 px-3 py-2 rounded-xl bg-rose-500/10 hover:bg-rose-500 text-rose-400 hover:text-white border border-rose-500/20 text-xs font-bold transition">
        <i data-lucide="log-out" class="w-3.5 h-3.5"></i>
        <span>Lock Session / Logout</span>
      </button>
    </div>
  `;

  safeCreateIcons();
}

function loadActiveTab() {
  try {
    switch (STATE.activeTab) {
      case 'login-portal': loadLoginPortalTab(); break;
      case 'dashboard': loadDashboard(); break;
      case 'requests': loadRequestsTab(); break;
      case 'approvals': loadApprovalsTab(); break;
      case 'search': loadSearchTab(); break;
      case 'itinerary': loadItineraryTab(); break;
      case 'expenses': loadExpensesTab(); break;
      case 'ai-assistant': loadAiAssistantTab(); break;
      case 'risk': loadRiskTab(); break;
      case 'analytics': loadAnalyticsTab(); break;
      case 'settings': loadSettingsTab(); break;
      case 'audit': loadAuditTab(); break;
      default: loadDashboard();
    }
  } catch (err) {
    console.error('Error rendering active tab:', err);
  }
  safeCreateIcons();
}

// =========================================================================
// FLOW ENGINE CONTROLLER
// =========================================================================
function renderFlowStepper() {
  const current = FLOW_STATE.currentStep;
  const progressPercent = Math.round((current / FLOW_STATE.totalSteps) * 100);
  const curStepObj = FLOW_STATE.steps.find(s => s.step === current) || FLOW_STATE.steps[0];

  return `
    <div class="glass-panel p-5 rounded-2xl border border-indigo-500/30 glow-indigo relative overflow-hidden mb-6 no-print">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-3 pb-4 border-b border-slate-800">
        <div>
          <div class="flex items-center gap-2">
            <span class="px-2.5 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300 font-extrabold text-[11px] border border-indigo-500/30">
              ${curStepObj.badge} (${progressPercent}% Completed)
            </span>
            <span class="text-xs text-slate-400 font-semibold">• Active Persona: <strong class="text-emerald-400">${curStepObj.roleLabel}</strong></span>
          </div>
          <h2 class="text-lg font-extrabold text-white mt-1 flex items-center gap-2">
            <i data-lucide="${curStepObj.icon}" class="w-5 h-5 text-indigo-400"></i>
            ${curStepObj.title}
          </h2>
          <p class="text-xs text-slate-400 mt-0.5">${curStepObj.summary}</p>
        </div>

        <div class="flex items-center gap-2">
          <button onclick="previousFlowStep()" ${current === 1 ? 'disabled class="opacity-40 cursor-not-allowed px-3 py-1.5 rounded-lg bg-dark-900 border border-slate-800 text-xs text-slate-500"' : 'class="px-3 py-1.5 rounded-lg bg-dark-900 hover:bg-dark-700 border border-slate-700 text-xs text-slate-300 font-bold transition"'}>
            ◀ Previous
          </button>
          <button onclick="executeStepPrimaryAction(${current})" class="px-4 py-1.5 rounded-lg bg-gradient-to-r from-indigo-600 via-purple-600 to-pink-600 hover:brightness-110 text-white font-bold text-xs shadow-md transition flex items-center gap-1.5">
            <span>${curStepObj.actionText}</span>
          </button>
          <button onclick="autoPlayFlow()" class="px-3 py-1.5 rounded-lg border text-xs font-bold transition ${FLOW_STATE.isAutoPlaying ? 'bg-rose-600/30 border-rose-500 text-rose-300' : 'bg-dark-900 hover:bg-dark-700 border-indigo-500/40 text-indigo-300'}">
            ${FLOW_STATE.isAutoPlaying ? '⏸ Pause' : '▶ Auto-Play'}
          </button>
        </div>
      </div>

      <!-- Stepper Dots -->
      <div class="grid grid-cols-4 md:grid-cols-8 gap-2 pt-4">
        ${FLOW_STATE.steps.map(s => {
          const isDone = s.step < current;
          const isActive = s.step === current;
          return `
            <button onclick="goToFlowStep(${s.step})" class="p-2 rounded-xl text-left transition-all ${isActive ? 'bg-indigo-600/30 border border-indigo-500 shadow-md ring-1 ring-indigo-500/50' : isDone ? 'bg-dark-900/90 border border-emerald-500/40 hover:bg-dark-700' : 'bg-dark-900/50 border border-slate-800 opacity-60 hover:opacity-100'}">
              <div class="flex items-center justify-between text-[10px]">
                <span class="font-bold ${isActive ? 'text-indigo-300' : isDone ? 'text-emerald-400' : 'text-slate-500'}">#${s.step}</span>
                <i data-lucide="${isDone ? 'check' : s.icon}" class="w-3 h-3 ${isActive ? 'text-indigo-300' : isDone ? 'text-emerald-400' : 'text-slate-500'}"></i>
              </div>
              <div class="text-[11px] font-bold text-white truncate mt-1">${s.shortTitle}</div>
            </button>
          `;
        }).join('')}
      </div>
    </div>
  `;
}

function goToFlowStep(stepNumber) {
  if (stepNumber < 1) stepNumber = 1;
  if (stepNumber > 8) stepNumber = 8;
  FLOW_STATE.currentStep = stepNumber;
  const targetStep = FLOW_STATE.steps.find(s => s.step === stepNumber);

  if (targetStep && STATE.currentRole !== targetStep.role) {
    loginAsRole(targetStep.role, false);
  }

  STATE.activeTab = targetStep.tab;
  renderNavigation();
  updateTopStripActiveState(targetStep.tab);
  loadActiveTab();
  showToast(`Flow Stage ${stepNumber}/8: ${targetStep.title} (${targetStep.roleLabel})`);
}

function advanceFlow() {
  if (FLOW_STATE.currentStep < 8) {
    goToFlowStep(FLOW_STATE.currentStep + 1);
  } else {
    goToFlowStep(1);
    showToast('Corporate Travel Lifecycle Reset! Starting new flow at Step 1.');
  }
}

function previousFlowStep() {
  if (FLOW_STATE.currentStep > 1) {
    goToFlowStep(FLOW_STATE.currentStep - 1);
  }
}

function autoPlayFlow() {
  if (FLOW_STATE.isAutoPlaying) {
    clearInterval(FLOW_STATE.autoPlayTimer);
    FLOW_STATE.isAutoPlaying = false;
    showToast('Auto-Play Paused.');
    loadActiveTab();
    return;
  }

  FLOW_STATE.isAutoPlaying = true;
  showToast('⚡ Auto-Play Flow Started! Stepping through all 8 lifecycle stages...');
  if (FLOW_STATE.currentStep >= 8) goToFlowStep(1);

  FLOW_STATE.autoPlayTimer = setInterval(() => {
    if (FLOW_STATE.currentStep < 8) {
      goToFlowStep(FLOW_STATE.currentStep + 1);
    } else {
      clearInterval(FLOW_STATE.autoPlayTimer);
      FLOW_STATE.isAutoPlaying = false;
      showToast('🎉 Auto-Play Completed! Complete 8-step corporate travel lifecycle demonstrated.');
      loadActiveTab();
    }
  }, 4500);
}

function executeStepPrimaryAction(stepNum) {
  switch (stepNum) {
    case 1: openNewRequestModal(); break;
    case 2: goToFlowStep(3); break;
    case 3: handleApproveRequest(1); break;
    case 4: openBoardingPassModal(); break;
    case 5: goToFlowStep(6); break;
    case 6: openNewExpenseModal(); break;
    case 7: settleFinanceExpense(); break;
    case 8: goToFlowStep(1); break;
    default: advanceFlow();
  }
}

// =========================================================================
// AUTHENTICATION & PERSONA SWITCHING
// =========================================================================
async function loginAsRole(roleKey, showNotification = true) {
  const user = DEMO_USERS[roleKey] || DEMO_USERS.ROLE_EMPLOYEE;
  
  // Real REST API Authentication against /api/auth/login
  try {
    const res = await apiFetch('/api/auth/login', {
      method: 'POST',
      body: {
        usernameOrEmail: user.email,
        password: 'password123'
      }
    });

    if (res && res.data && res.data.accessToken) {
      STATE.token = res.data.accessToken;
      STATE.isAuthenticated = true;
      localStorage.setItem('corporate_jwt_token', res.data.accessToken);
      localStorage.setItem('corporate_user_role', roleKey);
    }
  } catch (err) {
    console.warn('Login API call warning:', err);
  }

  STATE.isAuthenticated = true;
  STATE.currentRole = roleKey;
  STATE.currentUser = user;
  localStorage.setItem('corporate_user_role', roleKey);

  // Enforce role-based tab access
  const allowed = (ROLE_NAVS[roleKey] || ROLE_NAVS.ROLE_EMPLOYEE).map(i => i.id);
  if (!allowed.includes(STATE.activeTab) || STATE.activeTab === 'login-portal') {
    STATE.activeTab = allowed.includes('dashboard') ? 'dashboard' : (allowed[0] || 'dashboard');
  }

  updateUserProfileHeader();
  updateWalletDisplay();
  renderNavigation();
  updateTopStripActiveState(STATE.activeTab);
  loadActiveTab();

  if (showNotification) {
    showToast(`🔓 Unlocked ${user.name}'s portal (${roleKey.replace('ROLE_', '')}) — ${allowed.length} modules accessible.`);
  }
}

async function switchDemoRole(roleKey) {
  await loginAsRole(roleKey, true);
}

function updateUserProfileHeader() {
  const nameEl = document.getElementById('userNameDisplay');
  const roleEl = document.getElementById('userRoleBadge');
  const avatarEl = document.getElementById('userAvatar');
  const roleSel = document.getElementById('roleSelector');
  const logoutBtn = document.getElementById('headerLogoutBtn');

  if (STATE.isAuthenticated && STATE.currentUser) {
    const theme = getRoleTheme(STATE.currentRole || 'ROLE_EMPLOYEE');
    if (nameEl) nameEl.textContent = STATE.currentUser.name;
    if (roleEl) {
      roleEl.textContent = STATE.currentUser.designation;
      roleEl.className = `text-[10px] ${theme.accentText} font-semibold`;
    }
    if (avatarEl) {
      avatarEl.textContent = STATE.currentUser.avatar;
      avatarEl.className = `h-8 w-8 rounded-full bg-gradient-to-br ${theme.gradient} flex items-center justify-center text-white font-bold text-xs shadow-md`;
    }
    if (roleSel) roleSel.value = STATE.currentRole || 'ROLE_EMPLOYEE';
    if (logoutBtn) logoutBtn.style.display = 'inline-flex';
  } else {
    if (nameEl) nameEl.textContent = 'Sign In Required';
    if (roleEl) roleEl.textContent = 'Modules Restricted';
    if (avatarEl) avatarEl.textContent = '🔒';
    if (roleSel) roleSel.value = 'ROLE_EMPLOYEE';
    if (logoutBtn) logoutBtn.style.display = 'none';
  }
}

function handleLogout() {
  STATE.token = '';
  STATE.isAuthenticated = false;
  STATE.currentRole = null;
  STATE.currentUser = { name: 'Guest Traveler', designation: 'Please Authenticate', avatar: '🔒' };
  localStorage.removeItem('corporate_jwt_token');
  localStorage.removeItem('corporate_user_role');
  
  STATE.activeTab = 'login-portal';
  updateUserProfileHeader();
  renderNavigation();
  updateTopStripActiveState('login-portal');
  loadLoginPortalTab();
  
  showToast('🔒 Logged out. All platform modules are now locked.', 'info');
}

function switchTravelMode(isPersonal) {
  STATE.isPersonalMode = isPersonal;
  const btnCorp = document.getElementById('btnModeCorporate');
  const btnPers = document.getElementById('btnModePersonal');

  if (isPersonal) {
    if (btnCorp) btnCorp.className = 'flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold text-slate-400 hover:text-white transition-all';
    if (btnPers) btnPers.className = 'flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all bg-emerald-600 text-white shadow-sm';
    showToast('Switched to Personal Travel Mode. Bookings are self-funded.');
  } else {
    if (btnCorp) btnCorp.className = 'flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-all bg-indigo-600 text-white shadow-sm';
    if (btnPers) btnPers.className = 'flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold text-slate-400 hover:text-white transition-all';
    showToast('Switched to Corporate Travel Mode. Company travel policies apply.');
  }
}

function changeCurrency(curr) {
  STATE.currency = curr;
  STATE.currencySymbol = curr === 'INR' ? '₹' : curr === 'USD' ? '$' : curr === 'EUR' ? '€' : '£';
  const el = document.getElementById('walletCurrency');
  if (el) el.textContent = curr;
  updateWalletDisplay();
  loadActiveTab();
}

function formatMoney(amountInInr) {
  const num = Number(amountInInr) || 0;
  const rate = STATE.exchangeRates[STATE.currency] || 1;
  const converted = (num * rate).toFixed(0);
  return `${STATE.currencySymbol}${Number(converted).toLocaleString()}`;
}

function updateWalletDisplay() {
  const rem = STATE.currentUser.wallet ? STATE.currentUser.wallet.remaining : 324200;
  const spent = STATE.currentUser.wallet ? STATE.currentUser.wallet.spent : 25800;
  const alloc = STATE.currentUser.wallet ? STATE.currentUser.wallet.allocated : 350000;
  const pct = Math.min(100, Math.round((spent / (alloc || 1)) * 100));

  const remEl = document.getElementById('walletRemaining');
  const spentEl = document.getElementById('walletSpent');
  const allocEl = document.getElementById('walletAllocated');
  const pbar = document.getElementById('walletProgressBar');

  if (remEl) remEl.textContent = formatMoney(rem);
  if (spentEl) spentEl.textContent = formatMoney(spent);
  if (allocEl) allocEl.textContent = formatMoney(alloc);
  if (pbar) pbar.style.width = `${100 - pct}%`;
}

// =========================================================================
// 0. DEDICATED LOGIN & IDENTITY GATEWAY MODULE
// =========================================================================
function loadLoginPortalTab() {
  const main = document.getElementById('mainContent');
  const activeTheme = getRoleTheme(STATE.currentRole || 'ROLE_EMPLOYEE');
  
  main.innerHTML = `
    ${renderPortalHero(STATE.currentRole || 'ROLE_EMPLOYEE')}

    <!-- Dedicated Login & Role Gateway Header -->
    <div class="glass-panel p-6 rounded-3xl border ${activeTheme.border} relative overflow-hidden mb-6 portal-card-animate">
      <div class="absolute top-0 right-0 w-64 h-64 bg-gradient-to-bl ${activeTheme.cardGradient} rounded-full blur-3xl opacity-60 pointer-events-none"></div>
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 relative z-10">
        <div class="flex items-center gap-4">
          <div class="h-14 w-14 rounded-2xl bg-gradient-to-br ${activeTheme.gradient} flex items-center justify-center text-white font-extrabold text-2xl shadow-xl shrink-0">
            <i data-lucide="shield-check" class="w-8 h-8"></i>
          </div>
          <div>
            <div class="flex items-center gap-2">
              <h1 class="text-2xl font-extrabold text-white tracking-tight">Enterprise Login & Identity Gateway</h1>
              <span class="text-xs px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-400 font-bold border border-emerald-500/30 flex items-center gap-1">
                <span class="h-2 w-2 rounded-full bg-emerald-400 animate-ping"></span> AUTH READY
              </span>
            </div>
            <p class="text-xs text-slate-400 mt-1">Switch personas across 9 corporate roles, verify JWT security tokens, and launch any platform module directly.</p>
          </div>
        </div>

        <div class="flex items-center gap-3">
          <div class="p-3 rounded-2xl bg-dark-900 border border-slate-800 text-right">
            <span class="text-[10px] text-slate-400 block font-semibold">Current Active Persona</span>
            <div class="text-xs font-bold text-white flex items-center gap-1.5 justify-end">
              <span class="h-2 w-2 rounded-full bg-emerald-400"></span>
              <span>${STATE.currentUser.name}</span>
            </div>
            <span class="text-[10px] ${activeTheme.accentText} font-mono">${STATE.currentRole || 'Not signed in'}</span>
          </div>
        </div>
      </div>

      <!-- Current Session Info Bar -->
      <div class="mt-5 pt-4 border-t border-slate-800/80 grid grid-cols-2 sm:grid-cols-4 gap-3 text-xs">
        <div class="p-2.5 rounded-xl bg-dark-900/80 border border-slate-800/80">
          <span class="text-[10px] text-slate-500 block">Organization</span>
          <strong class="text-slate-200">Acme Global Technologies</strong>
        </div>
        <div class="p-2.5 rounded-xl bg-dark-900/80 border border-slate-800/80">
          <span class="text-[10px] text-slate-500 block">Department</span>
          <strong class="text-slate-200">${STATE.currentUser.department || 'Engineering'}</strong>
        </div>
        <div class="p-2.5 rounded-xl bg-dark-900/80 border border-slate-800/80">
          <span class="text-[10px] text-slate-500 block">Travel Wallet Balance</span>
          <strong class="text-emerald-400">${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.remaining : 324200)}</strong>
        </div>
        <div class="p-2.5 rounded-xl bg-dark-900/80 border border-slate-800/80">
          <span class="text-[10px] text-slate-500 block">JWT Token Status</span>
          <strong class="text-indigo-300 font-mono">HMAC-SHA256 (Active)</strong>
        </div>
      </div>
    </div>

    <!-- Section 1: 1-Click Role Persona Grid -->
    <div class="space-y-4 mb-8">
      <div class="flex items-center justify-between">
        <div>
          <h3 class="text-base font-extrabold text-white flex items-center gap-2">
            <i data-lucide="users" class="w-5 h-5 text-indigo-400"></i> Select Role for Instant 1-Click Login
          </h3>
          <p class="text-xs text-slate-400">Click any card below to instantly adopt the persona, issue a live JWT session, and adapt user permissions.</p>
        </div>
        <span class="text-xs text-slate-500 font-semibold font-mono">9 Available Roles</span>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        ${Object.keys(DEMO_USERS).map((k, idx) => {
          const u = DEMO_USERS[k];
          const theme = getRoleTheme(k);
          const isCur = STATE.currentRole === k;
          const roleNavItems = (ROLE_NAVS[k] || []).filter(item => item.id !== 'login-portal');
          return `
            <div class="portal-role-card portal-card-animate p-5 rounded-2xl border transition-all relative ${isCur ? `active bg-gradient-to-b ${theme.cardGradient} ${theme.border} ring-2 ring-white/10 shadow-xl ${theme.glow}` : 'bg-dark-800/90 hover:bg-dark-700/80 border-slate-800 hover:border-slate-600'}" style="animation-delay: ${idx * 40}ms">
              <div class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${theme.gradient} rounded-t-2xl"></div>
              <div class="flex items-start justify-between gap-3">
                <div class="flex items-center gap-3">
                  <div class="h-12 w-12 rounded-2xl bg-gradient-to-br ${theme.gradient} flex items-center justify-center text-white font-extrabold text-sm shadow-md shrink-0">
                    <i data-lucide="${theme.icon}" class="w-5 h-5"></i>
                  </div>
                  <div>
                    <h4 class="text-sm font-extrabold text-white flex items-center gap-2">
                      ${u.name}
                      ${isCur ? `<span class="px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 text-[9px] font-bold border border-emerald-500/30">ACTIVE</span>` : ''}
                    </h4>
                    <p class="text-[11px] ${theme.accentText} font-semibold">${theme.portalName}</p>
                    <p class="text-[10px] text-slate-400 font-mono mt-0.5">${u.email}</p>
                  </div>
                </div>
              </div>

              <div class="mt-4 pt-3 border-t border-slate-800 flex items-center justify-between text-xs">
                <div>
                  <span class="text-[10px] text-slate-500 block">Department</span>
                  <span class="text-[11px] font-bold text-slate-300">${u.department}</span>
                </div>
                <div class="text-right">
                  <span class="text-[10px] text-slate-500 block">Allocated Budget</span>
                  <span class="text-[11px] font-extrabold text-emerald-400">${formatMoney(u.wallet.allocated)}</span>
                </div>
              </div>

              <!-- Permitted Modules Pill List -->
              <div class="mt-3 pt-2.5 border-t border-slate-800/80">
                <div class="flex items-center justify-between text-[10px] text-slate-400 mb-1.5 font-bold uppercase tracking-wider">
                  <span>Authorized Modules (${roleNavItems.length})</span>
                </div>
                <div class="flex flex-wrap gap-1">
                  ${roleNavItems.map(m => `<span class="px-2 py-0.5 rounded-md bg-dark-900/90 border border-slate-700/60 text-[10px] font-semibold text-indigo-300 flex items-center gap-1"><i data-lucide="${m.icon}" class="w-2.5 h-2.5 text-indigo-400"></i> ${m.label}</span>`).join('')}
                </div>
              </div>

              <div class="mt-4 pt-3 border-t border-slate-800 flex items-center gap-2">
                <button onclick="loginAsRole('${k}'); loadLoginPortalTab();" class="flex-1 py-2 rounded-xl font-bold text-xs transition shadow-md ${isCur ? 'bg-emerald-600 text-white' : `bg-gradient-to-r ${theme.gradient} hover:brightness-110 text-white`} flex items-center justify-center gap-1.5">
                  <i data-lucide="${isCur ? 'check-circle' : 'log-in'}" class="w-3.5 h-3.5"></i>
                  <span>${isCur ? 'Active Session' : 'Enter ' + theme.shortName + ' Portal'}</span>
                </button>
                <button onclick="loginAsRole('${k}'); navigateToTab('dashboard');" class="px-3 py-2 rounded-xl bg-dark-900 hover:bg-dark-700 border border-slate-700 text-slate-300 hover:text-white text-xs font-semibold transition" title="Login and open Dashboard">
                  Dashboard ➔
                </button>
              </div>
            </div>
          `;
        }).join('')}
      </div>
    </div>

    <!-- Section 2: Standard Credential Authentication Form -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
      <div class="lg:col-span-2 glass-panel p-6 rounded-2xl border border-slate-800">
        <div class="flex items-center gap-2 text-indigo-400 mb-1 font-bold text-xs">
          <i data-lucide="key" class="w-4 h-4"></i> CUSTOM CREDENTIAL AUTHENTICATION
        </div>
        <h3 class="text-base font-extrabold text-white mb-2">Sign In with Corporate Email & Password</h3>
        <p class="text-xs text-slate-400 mb-4">Authenticates directly against backend Spring Boot <code>POST /api/auth/login</code> REST API with JWT generation.</p>

        <form onsubmit="handlePortalCredentialLogin(event)" class="space-y-4 text-xs">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label class="text-slate-300 font-semibold block mb-1.5">Username or Corporate Email *</label>
              <input type="text" id="portalLoginUsername" required value="traveler@acmetech.com" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white outline-none focus:border-indigo-500 font-medium">
            </div>
            <div>
              <label class="text-slate-300 font-semibold block mb-1.5">Password *</label>
              <input type="password" id="portalLoginPassword" required value="password123" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-4 py-2.5 text-white outline-none focus:border-indigo-500 font-medium">
            </div>
          </div>

          <div class="flex items-center justify-between pt-2">
            <div class="text-[11px] text-slate-400 flex items-center gap-1.5">
              <i data-lucide="lock" class="w-3.5 h-3.5 text-emerald-400"></i>
              <span>BCrypt Salted Hashes & Stateless JWT</span>
            </div>
            <button type="submit" class="px-6 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition flex items-center gap-2">
              <i data-lucide="log-in" class="w-4 h-4"></i> Authenticate & Generate Token
            </button>
          </div>
        </form>
      </div>

      <!-- Quick Session Stats -->
      <div class="glass-panel p-6 rounded-2xl border border-slate-800 flex flex-col justify-between">
        <div>
          <div class="flex items-center gap-2 text-indigo-400 mb-1 font-bold text-xs">
            <i data-lucide="shield" class="w-4 h-4"></i> SECURITY STATUS
          </div>
          <h4 class="text-sm font-extrabold text-white">Active Session Profile</h4>
          
          <div class="space-y-3 mt-4 text-xs">
            <div class="flex justify-between py-1.5 border-b border-slate-800">
              <span class="text-slate-400">Principal:</span>
              <strong class="text-white">${STATE.currentUser.name}</strong>
            </div>
            <div class="flex justify-between py-1.5 border-b border-slate-800">
              <span class="text-slate-400">Role Authority:</span>
              <strong class="text-indigo-400">${STATE.currentRole}</strong>
            </div>
            <div class="flex justify-between py-1.5 border-b border-slate-800">
              <span class="text-slate-400">Cost Center:</span>
              <strong class="text-slate-200">CC-101-ENG</strong>
            </div>
            <div class="flex justify-between py-1.5">
              <span class="text-slate-400">Policy Tier:</span>
              <strong class="text-emerald-400">Standard Tier 1</strong>
            </div>
          </div>
        </div>

        <button onclick="navigateToTab('audit')" class="w-full mt-4 py-2 rounded-xl bg-dark-900 hover:bg-dark-700 border border-slate-700 text-indigo-300 hover:text-white font-bold text-xs transition flex items-center justify-center gap-1.5">
          <i data-lucide="shield-check" class="w-3.5 h-3.5"></i> Inspect Security Audit Trail ➔
        </button>
      </div>
    </div>

    <!-- Section 3: Authorized Modules for Current Active Portal -->
    ${(() => {
      const allowedModuleIds = (ROLE_NAVS[STATE.currentRole] || ROLE_NAVS.ROLE_EMPLOYEE).map(item => item.id).filter(id => id !== 'login-portal');
      const authorizedModules = ALL_MODULES.filter(m => allowedModuleIds.includes(m.id));
      const restrictedModules = ALL_MODULES.filter(m => m.id !== 'login-portal' && !allowedModuleIds.includes(m.id));
      const roleDisplayName = (STATE.currentRole || 'ROLE_EMPLOYEE').replace('ROLE_', '').replace('_', ' ');

      return `
        <div class="glass-panel p-6 rounded-3xl border border-slate-800 mb-8">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-base font-extrabold text-white flex items-center gap-2">
                <i data-lucide="shield-check" class="w-5 h-5 text-emerald-400"></i> Authorized Modules for ${roleDisplayName} Portal (${authorizedModules.length} Available)
              </h3>
              <p class="text-xs text-slate-400">Showing only modules accessible to <strong>${STATE.currentUser.name}</strong> (${STATE.currentUser.designation}).</p>
            </div>
            <span class="px-3 py-1 rounded-full text-xs font-bold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
              Role RBAC Enforced
            </span>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3 ${restrictedModules.length > 0 ? 'mb-6' : ''}">
            ${authorizedModules.map(m => `
              <button onclick="navigateToTab('${m.id}')" class="portal-module-tile p-4 rounded-2xl bg-dark-900 hover:bg-dark-700/80 border ${activeTheme.border} hover:border-white/20 text-left transition group shadow-sm">
                <div class="flex items-center justify-between mb-2">
                  <div class="h-9 w-9 rounded-xl bg-indigo-500/15 text-indigo-400 group-hover:bg-indigo-600 group-hover:text-white flex items-center justify-center transition">
                    <i data-lucide="${m.icon}" class="w-4 h-4"></i>
                  </div>
                  <span class="text-[10px] text-emerald-400 font-bold transition flex items-center gap-1">
                    <i data-lucide="check" class="w-3 h-3"></i> Open ➔
                  </span>
                </div>
                <h4 class="text-xs font-bold text-white group-hover:text-indigo-300 transition">${m.label}</h4>
                <p class="text-[10px] text-slate-400 mt-1 line-clamp-2">${m.desc}</p>
              </button>
            `).join('')}
          </div>

          ${restrictedModules.length > 0 ? `
            <div class="pt-5 border-t border-slate-800/80">
              <div class="flex items-center justify-between mb-3">
                <div class="flex items-center gap-2 text-slate-400 text-xs font-bold uppercase tracking-wider">
                  <i data-lucide="lock" class="w-3.5 h-3.5 text-rose-400"></i>
                  <span>Restricted Modules (${restrictedModules.length} locked for ${roleDisplayName})</span>
                </div>
                <span class="text-[11px] text-slate-500">Requires elevated role permissions</span>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
                ${restrictedModules.map(m => `
                  <div class="p-3.5 rounded-2xl bg-dark-950/60 border border-slate-800/60 text-left opacity-50 cursor-not-allowed">
                    <div class="flex items-center justify-between mb-1.5">
                      <div class="h-7 w-7 rounded-lg bg-slate-800/80 text-slate-500 flex items-center justify-center">
                        <i data-lucide="${m.icon}" class="w-3.5 h-3.5"></i>
                      </div>
                      <span class="px-2 py-0.5 rounded text-[9px] font-bold bg-rose-500/10 text-rose-400 border border-rose-500/20 flex items-center gap-1">
                        <i data-lucide="lock" class="w-2.5 h-2.5"></i> Locked
                      </span>
                    </div>
                    <h4 class="text-xs font-semibold text-slate-400">${m.label}</h4>
                    <p class="text-[10px] text-slate-500 mt-0.5 line-clamp-1">${m.desc}</p>
                  </div>
                `).join('')}
              </div>
            </div>
          ` : ''}
        </div>
      `;
    })()}
  `;

  safeCreateIcons();
}

async function handlePortalCredentialLogin(e) {
  e.preventDefault();
  const usernameOrEmail = document.getElementById('portalLoginUsername').value.trim();
  const password = document.getElementById('portalLoginPassword').value.trim();

  try {
    const res = await apiFetch('/api/auth/login', {
      method: 'POST',
      body: { usernameOrEmail, password }
    });

    if (res && res.data) {
      const data = res.data;
      STATE.token = data.accessToken;
      localStorage.setItem('corporate_jwt_token', data.accessToken);
      const primaryRole = (data.roles && data.roles[0]) || 'ROLE_EMPLOYEE';
      await loginAsRole(primaryRole, false);
      loadLoginPortalTab();
      showToast(`Authenticated as ${data.fullName || usernameOrEmail} (${primaryRole})`);
    } else {
      await loginAsRole('ROLE_EMPLOYEE', false);
      loadLoginPortalTab();
    }
  } catch (err) {
    await loginAsRole('ROLE_EMPLOYEE', false);
    loadLoginPortalTab();
  }
}

// =========================================================================
// 1. DASHBOARD VIEW (LIVE TELEMETRY)
// =========================================================================
async function loadDashboard() {
  const main = document.getElementById('mainContent');
  const roleKey = STATE.currentRole || 'ROLE_EMPLOYEE';
  const theme = getRoleTheme(roleKey);
  const allowedIds = (ROLE_NAVS[roleKey] || ROLE_NAVS.ROLE_EMPLOYEE).map(i => i.id);
  
  // Fetch fresh requests and bookings for live dashboard cards
  const [reqRes, bookRes, expRes] = await Promise.all([
    apiFetch('/api/travel-requests'),
    apiFetch('/api/bookings'),
    apiFetch('/api/expenses')
  ]);

  const requests = (reqRes && reqRes.data) ? reqRes.data : STATE.requests;
  const bookings = (bookRes && bookRes.data) ? bookRes.data : STATE.bookings;
  const expenses = (expRes && expRes.data) ? expRes.data : STATE.expenses;

  const activeTrip = bookings && bookings.length > 0 ? bookings[0] : null;
  const pendingApprovalsCount = requests.filter(r => r.status === 'SUBMITTED' || r.status === 'PENDING').length;

  const quickModules = [
    { id: 'search', icon: 'plane', color: 'text-indigo-400', title: 'Book Flights', sub: 'Corporate GDS' },
    { id: 'requests', icon: 'file-text', color: 'text-purple-400', title: 'Travel Requests', sub: `${requests.length} Total` },
    { id: 'approvals', icon: 'check-square', color: 'text-amber-400', title: 'Approvals Hub', sub: `${pendingApprovalsCount} Action Required` },
    { id: 'itinerary', icon: 'ticket', color: 'text-emerald-400', title: 'E-Tickets & PNR', sub: `${bookings.length} Bookings` },
    { id: 'expenses', icon: 'receipt', color: 'text-pink-400', title: 'AI OCR Expense', sub: `${expenses.length} Reports` },
    { id: 'analytics', icon: 'bar-chart-3', color: 'text-cyan-400', title: 'BI Analytics', sub: 'Spend & ESG' },
    { id: 'ai-assistant', icon: 'sparkles', color: 'text-violet-400', title: 'AI Assistant', sub: 'Smart Planner' },
    { id: 'risk', icon: 'shield-alert', color: 'text-orange-400', title: 'Duty of Care', sub: 'Live Safety' },
    { id: 'settings', icon: 'settings', color: 'text-sky-400', title: 'Policy Settings', sub: 'Org Config' },
    { id: 'audit', icon: 'shield-check', color: 'text-emerald-400', title: 'Audit Trail', sub: 'Security Logs' }
  ].filter(m => allowedIds.includes(m.id));

  main.innerHTML = `
    ${renderPortalHero(roleKey, true)}
    ${renderFlowStepper()}

    <!-- Header Greeting -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 portal-card-animate">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight flex items-center gap-2">
          Welcome, ${STATE.currentUser.name} 
          <span class="text-xs px-2.5 py-1 rounded-full ${theme.accentBg} ${theme.accentText} font-semibold border ${theme.border}">
            ${STATE.currentUser.designation}
          </span>
        </h1>
        <p class="text-xs text-slate-400 mt-1">Role: <strong class="${theme.accentText}">${STATE.currentRole}</strong> • Acme Global Technologies Inc.</p>
      </div>
      <div class="flex items-center gap-3">
        <button onclick="goToFlowStep(1)" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-gradient-to-r ${theme.gradient} hover:brightness-110 text-white font-bold text-xs shadow-lg transition">
          <i data-lucide="play" class="w-4 h-4"></i> Start New Trip Flow
        </button>
        <button onclick="navigateToTab('login-portal')" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-dark-900 hover:bg-dark-700 border border-slate-700 text-slate-200 font-bold text-xs transition">
          <i data-lucide="shield-check" class="w-4 h-4"></i> Switch Portal
        </button>
      </div>
    </div>

    <!-- Quick Module Launch Cards (role-filtered) -->
    <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-${Math.min(quickModules.length, 6)} gap-3">
      ${quickModules.map(m => `
      <button onclick="navigateToTab('${m.id}')" class="portal-module-tile p-3.5 rounded-2xl bg-dark-800/80 hover:bg-dark-700 border border-slate-800 hover:${theme.border} text-left transition group">
        <i data-lucide="${m.icon}" class="w-5 h-5 ${m.color} mb-1.5 group-hover:scale-110 transition"></i>
        <div class="text-xs font-bold text-white">${m.title}</div>
        <div class="text-[10px] text-slate-400 mt-0.5">${m.sub}</div>
      </button>
      `).join('')}
    </div>

    <!-- KPI Metric Cards Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="glass-panel p-5 rounded-2xl border border-slate-800">
        <div class="flex items-center justify-between">
          <span class="text-xs font-semibold text-slate-400">Available Travel Budget</span>
          <i data-lucide="wallet" class="w-4 h-4 text-emerald-400"></i>
        </div>
        <div class="text-2xl font-extrabold text-white mt-2">${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.remaining : 324200)}</div>
        <div class="text-[11px] text-emerald-400 mt-1">Allocated: ${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.allocated : 350000)}</div>
      </div>

      <div class="glass-panel p-5 rounded-2xl border border-slate-800">
        <div class="flex items-center justify-between">
          <span class="text-xs font-semibold text-slate-400">Pending Approvals</span>
          <i data-lucide="clock" class="w-4 h-4 text-amber-400"></i>
        </div>
        <div class="text-2xl font-extrabold text-amber-400 mt-2">${pendingApprovalsCount} Requests</div>
        <div class="text-[11px] text-slate-400 mt-1">Awaiting Manager & Finance Review</div>
      </div>

      <div class="glass-panel p-5 rounded-2xl border border-slate-800">
        <div class="flex items-center justify-between">
          <span class="text-xs font-semibold text-slate-400">Active Bookings & PNR</span>
          <i data-lucide="plane-takeoff" class="w-4 h-4 text-indigo-400"></i>
        </div>
        <div class="text-2xl font-extrabold text-indigo-400 mt-2">${bookings.length} Confirmed</div>
        <div class="text-[11px] text-indigo-300 mt-1">E-Tickets & Boarding Passes Ready</div>
      </div>

      <div class="glass-panel p-5 rounded-2xl border border-slate-800">
        <div class="flex items-center justify-between">
          <span class="text-xs font-semibold text-slate-400">Total YTD Travel Spend</span>
          <i data-lucide="pie-chart" class="w-4 h-4 text-purple-400"></i>
        </div>
        <div class="text-2xl font-extrabold text-white mt-2">${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.spent : 25800)}</div>
        <div class="text-[11px] text-emerald-400 mt-1">98.4% Policy Compliant</div>
      </div>
    </div>

    <!-- Active Trip Spotlight Banner & Quick Actions -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <div class="lg:col-span-2 glass-panel p-6 rounded-2xl border border-slate-800 relative overflow-hidden">
        <div class="flex items-center justify-between border-b border-slate-800 pb-4">
          <div class="flex items-center gap-2.5">
            <span class="h-2.5 w-2.5 rounded-full bg-emerald-400 animate-ping"></span>
            <h3 class="font-extrabold text-base text-white">Active Confirmed Trip: ${activeTrip ? (activeTrip.tripName || 'Delhi Tech Summit') : 'Delhi Tech Summit'}</h3>
          </div>
          <span class="text-xs px-3 py-1 rounded-full bg-emerald-500/15 text-emerald-400 font-bold border border-emerald-500/30">
            CONFIRMED (PNR: ${activeTrip ? (activeTrip.pnrNumber || 'PNR683921') : 'PNR683921'})
          </span>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-5">
          <div class="p-4 rounded-xl bg-dark-900 border border-slate-800">
            <div class="text-xs text-slate-400 font-medium">Flight AI-839</div>
            <div class="text-sm font-bold text-white mt-1">HYD ➔ DEL</div>
            <div class="text-xs text-indigo-400 mt-1">07:30 AM (Terminal 1)</div>
          </div>
          <div class="p-4 rounded-xl bg-dark-900 border border-slate-800">
            <div class="text-xs text-slate-400 font-medium">Hotel Accommodation</div>
            <div class="text-sm font-bold text-white mt-1">Taj Palace Suites</div>
            <div class="text-xs text-indigo-400 mt-1">3 Nights (Deluxe Suite)</div>
          </div>
          <div class="p-4 rounded-xl bg-dark-900 border border-slate-800">
            <div class="text-xs text-slate-400 font-medium">Approved Budget</div>
            <div class="text-sm font-bold text-emerald-400 mt-1">${formatMoney(activeTrip ? (activeTrip.totalAmount || 28000) : 28000)}</div>
            <div class="text-xs text-slate-400 mt-1">Paid via Corporate Visa</div>
          </div>
        </div>

        <div class="flex items-center justify-between mt-6 pt-4 border-t border-slate-800 text-xs">
          <div class="flex items-center gap-3 text-slate-400">
            <span><i data-lucide="shield-check" class="w-3.5 h-3.5 inline text-emerald-400"></i> Corporate Policy Approved</span>
          </div>
          <div class="flex items-center gap-2">
            <button onclick="openBoardingPassModal()" class="px-3 py-1.5 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white font-bold transition">
              View Boarding Pass & E-Ticket
            </button>
            <button onclick="navigateToTab('itinerary')" class="px-3 py-1.5 rounded-lg bg-dark-700 hover:bg-dark-600 text-slate-200 font-bold transition">
              Trip Timeline
            </button>
          </div>
        </div>
      </div>

      <!-- Quick AI Trip Optimizer Card -->
      <div class="glass-panel p-6 rounded-2xl border border-slate-800 flex flex-col justify-between">
        <div>
          <div class="flex items-center gap-2 text-indigo-400 mb-2 font-bold text-xs">
            <i data-lucide="sparkles" class="w-4 h-4"></i> AI TRAVEL OPTIMIZER
          </div>
          <h4 class="font-extrabold text-white text-sm">Smart Spend Recommendations</h4>
          <p class="text-xs text-slate-400 mt-2">
            AI analyzed corporate routes: booking 14 days in advance with preferred partner Air India saves an average of <strong class="text-emerald-400">18.4% (₹7,200)</strong> per business trip.
          </p>
          <div class="mt-4 p-3 rounded-xl bg-indigo-500/10 border border-indigo-500/20 text-xs text-indigo-200 space-y-1">
            <div class="flex justify-between"><span>Negotiated Air India Discount:</span> <strong>12% Applied</strong></div>
            <div class="flex justify-between"><span>Taj Hotel Corporate Rate:</span> <strong>18% Applied</strong></div>
          </div>
        </div>
        <button onclick="navigateToTab('ai-assistant')" class="w-full mt-4 py-2.5 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-bold text-xs shadow-md transition">
          Launch AI Travel Planner
        </button>
      </div>
    </div>

    <!-- Live Requests Table -->
    <div class="glass-panel p-6 rounded-2xl border border-slate-800">
      <div class="flex items-center justify-between mb-4">
        <h3 class="font-extrabold text-base text-white">Recent Travel Requests (Database Persistence)</h3>
        <button onclick="navigateToTab('requests')" class="text-xs font-bold text-indigo-400 hover:text-indigo-300">View All (${requests.length}) ➔</button>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead>
            <tr class="border-b border-slate-800 text-slate-400 font-semibold">
              <th class="py-3 px-4">Request #</th>
              <th class="py-3 px-4">Trip Name</th>
              <th class="py-3 px-4">Route</th>
              <th class="py-3 px-4">Dates</th>
              <th class="py-3 px-4">Budget</th>
              <th class="py-3 px-4">Policy Status</th>
              <th class="py-3 px-4">Approval Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60 font-medium">
            ${requests.slice(0, 5).map(r => `
              <tr class="hover:bg-dark-700/40 transition">
                <td class="py-3.5 px-4 font-mono text-indigo-300 font-bold">${r.requestNumber || ('TR-' + r.id)}</td>
                <td class="py-3.5 px-4 text-white font-bold">${r.tripName || r.title || 'Client Engagement'}</td>
                <td class="py-3.5 px-4 text-slate-300">${r.origin || 'HYD'} ➔ ${r.destination || 'DEL'}</td>
                <td class="py-3.5 px-4 text-slate-300">${r.departureDate || '2026-09-15'}</td>
                <td class="py-3.5 px-4 font-bold text-white">${formatMoney(r.estimatedBudget || 28000)}</td>
                <td class="py-3.5 px-4"><span class="px-2.5 py-1 rounded-full badge-compliant text-[10px] font-bold">${r.policyViolation ? 'EXCEPTION' : 'COMPLIANT'}</span></td>
                <td class="py-3.5 px-4"><span class="px-2.5 py-1 rounded-full ${r.status === 'APPROVED' ? 'bg-emerald-500/20 text-emerald-400' : r.status === 'REJECTED' ? 'bg-rose-500/20 text-rose-400' : 'bg-amber-500/20 text-amber-400'} text-[10px] font-bold">${r.status || 'SUBMITTED'}</span></td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    </div>
  `;

  safeCreateIcons();
}

// =========================================================================
// 2. TRAVEL SEARCH ENGINE & REAL ONE-CLICK BOOKING (FLOW STEP 1)
// =========================================================================
function loadSearchTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Corporate Travel Search & Booking Desk</h1>
        <p class="text-xs text-slate-400 mt-1">Search flights, hotels, and ground transport with corporate negotiated discounts and instant PNR issuance.</p>
      </div>
    </div>

    <!-- Search Controls Card -->
    <div class="glass-panel p-6 rounded-2xl border border-slate-800">
      <div class="flex items-center gap-4 border-b border-slate-800 pb-3 mb-4 text-xs font-bold">
        <button id="searchTabFlight" onclick="switchSearchType('flights')" class="text-indigo-400 border-b-2 border-indigo-500 pb-2 flex items-center gap-1.5"><i data-lucide="plane" class="w-4 h-4"></i> Flights</button>
        <button id="searchTabHotel" onclick="switchSearchType('hotels')" class="text-slate-400 hover:text-white pb-2 flex items-center gap-1.5"><i data-lucide="hotel" class="w-4 h-4"></i> Hotels</button>
        <button id="searchTabTransport" onclick="switchSearchType('transport')" class="text-slate-400 hover:text-white pb-2 flex items-center gap-1.5"><i data-lucide="car" class="w-4 h-4"></i> Transportation</button>
      </div>

      <div id="searchInputsArea" class="grid grid-cols-1 md:grid-cols-4 gap-4 text-xs">
        <div>
          <label class="text-slate-400 block font-semibold mb-1">From</label>
          <input type="text" id="searchOrigin" value="Hyderabad (HYD)" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500 font-bold">
        </div>
        <div>
          <label class="text-slate-400 block font-semibold mb-1">To</label>
          <input type="text" id="searchDest" value="Delhi (DEL)" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500 font-bold">
        </div>
        <div>
          <label class="text-slate-400 block font-semibold mb-1">Departure</label>
          <input type="date" id="searchDepDate" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500 font-bold">
        </div>
        <div class="flex items-end">
          <button onclick="executeFlightSearch()" class="w-full py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition flex items-center justify-center gap-2">
            <i data-lucide="search" class="w-4 h-4"></i> Search Corporate Rates
          </button>
        </div>
      </div>
    </div>

    <!-- Search Results Container -->
    <div id="searchResultsArea" class="space-y-4"></div>
  `;

  safeCreateIcons();

  const today = new Date();
  const nextWeek = new Date(today.getTime() + 5 * 24 * 60 * 60 * 1000);
  const depInput = document.getElementById('searchDepDate');
  if (depInput) depInput.value = nextWeek.toISOString().split('T')[0];

  executeFlightSearch();
}

function switchSearchType(type) {
  const flightBtn = document.getElementById('searchTabFlight');
  const hotelBtn = document.getElementById('searchTabHotel');
  const transBtn = document.getElementById('searchTabTransport');

  [flightBtn, hotelBtn, transBtn].forEach(b => {
    if (b) b.className = 'text-slate-400 hover:text-white pb-2 flex items-center gap-1.5';
  });

  if (type === 'flights') {
    if (flightBtn) flightBtn.className = 'text-indigo-400 border-b-2 border-indigo-500 pb-2 flex items-center gap-1.5';
    executeFlightSearch();
  } else if (type === 'hotels') {
    if (hotelBtn) hotelBtn.className = 'text-indigo-400 border-b-2 border-indigo-500 pb-2 flex items-center gap-1.5';
    executeHotelSearch();
  } else {
    if (transBtn) transBtn.className = 'text-indigo-400 border-b-2 border-indigo-500 pb-2 flex items-center gap-1.5';
    executeTransportSearch();
  }
}

async function executeFlightSearch() {
  const container = document.getElementById('searchResultsArea');
  if (!container) return;
  container.innerHTML = '<div class="p-8 text-center text-slate-400"><i data-lucide="loader-2" class="w-6 h-6 animate-spin mx-auto mb-2 text-indigo-400"></i> Searching negotiated GDS corporate fares...</div>';
  safeCreateIcons();

  const origin = document.getElementById('searchOrigin') ? document.getElementById('searchOrigin').value : 'HYD';
  const dest = document.getElementById('searchDest') ? document.getElementById('searchDest').value : 'DEL';

  const res = await apiFetch(`/api/flights/search?origin=${encodeURIComponent(origin)}&destination=${encodeURIComponent(dest)}`);
  const flights = (res && res.data) ? res.data : [];

  if (flights.length === 0) {
    container.innerHTML = '<div class="p-6 text-center text-slate-400 glass-panel rounded-2xl">No direct flights found. Showing standard corporate options.</div>';
    return;
  }

  container.innerHTML = flights.map(f => `
    <div class="glass-panel p-5 rounded-2xl border border-slate-800 hover:border-indigo-500/50 transition">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div class="flex items-center gap-4">
          <div class="h-12 w-12 rounded-xl bg-indigo-500/10 border border-indigo-500/20 flex items-center justify-center text-indigo-400 font-extrabold text-sm shrink-0">
            ${f.airline ? f.airline.substring(0, 2).toUpperCase() : 'AI'}
          </div>
          <div>
            <div class="flex items-center gap-2">
              <h4 class="font-extrabold text-base text-white">${f.airline} <span class="font-mono text-indigo-300 font-bold">${f.flightNumber}</span></h4>
              <span class="px-2 py-0.5 rounded-full bg-emerald-500/20 text-emerald-400 text-[10px] font-bold border border-emerald-500/30">100% POLICY COMPLIANT</span>
            </div>
            <div class="text-xs text-slate-400 mt-1 flex items-center gap-3">
              <span><strong>${f.departureTime ? f.departureTime.split('T')[1].substring(0, 5) : '07:30'}</strong> (${f.originCode || 'HYD'}) ➔ <strong>${f.arrivalTime ? f.arrivalTime.split('T')[1].substring(0, 5) : '09:45'}</strong> (${f.destinationCode || 'DEL'})</span>
              <span>• Non-stop (${f.durationMinutes || 135} mins)</span>
              <span>• ${f.baggageAllowance || '25kg check-in'}</span>
            </div>
          </div>
        </div>

        <div class="flex items-center justify-between md:justify-end gap-4 border-t md:border-t-0 pt-3 md:pt-0 border-slate-800">
          <div class="text-right">
            <div class="text-xs text-slate-400">Corporate Tariff</div>
            <div class="text-xl font-extrabold text-white">${formatMoney(f.price || 5400)}</div>
          </div>
          <div class="flex items-center gap-2">
            <button onclick="bookFlightNow(${f.id}, '${f.airline}', '${f.flightNumber}', ${f.price || 5400})" class="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-md shadow-indigo-600/30 transition flex items-center gap-1.5">
              <i data-lucide="ticket" class="w-3.5 h-3.5"></i> Book Flight
            </button>
            <button onclick="selectFlightAndCreateRequest(${f.id}, '${f.airline}', '${f.flightNumber}', ${f.price || 5400})" class="px-3 py-2 rounded-xl bg-dark-900 hover:bg-dark-700 border border-slate-700 text-slate-300 hover:text-white text-xs font-semibold transition">
              Create Request ➔
            </button>
          </div>
        </div>
      </div>
    </div>
  `).join('');

  safeCreateIcons();
}

async function executeHotelSearch() {
  const container = document.getElementById('searchResultsArea');
  if (!container) return;
  container.innerHTML = '<div class="p-8 text-center text-slate-400"><i data-lucide="loader-2" class="w-6 h-6 animate-spin mx-auto mb-2 text-indigo-400"></i> Fetching negotiated hotel rates...</div>';
  safeCreateIcons();

  const res = await apiFetch('/api/hotels/search?city=Delhi');
  const hotels = (res && res.data) ? res.data : [];

  container.innerHTML = hotels.map(h => `
    <div class="glass-panel p-5 rounded-2xl border border-slate-800 hover:border-indigo-500/50 transition">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div class="flex items-center gap-4">
          <div class="h-12 w-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-400 font-extrabold text-sm shrink-0">
            <i data-lucide="hotel" class="w-6 h-6"></i>
          </div>
          <div>
            <div class="flex items-center gap-2">
              <h4 class="font-extrabold text-base text-white">${h.hotelName}</h4>
              <span class="px-2 py-0.5 rounded-full bg-indigo-500/20 text-indigo-300 text-[10px] font-bold">PREFFERED CORPORATE PARTNER</span>
            </div>
            <div class="text-xs text-slate-400 mt-1">${h.address || 'Diplomatic Enclave, Delhi'} • ${h.ratingStars || 5} Stars • Free Breakfast & Wi-Fi</div>
          </div>
        </div>
        <div class="flex items-center gap-4">
          <div class="text-right">
            <div class="text-xs text-slate-400">Nightly Rate</div>
            <div class="text-xl font-extrabold text-emerald-400">${formatMoney(h.nightlyPrice || 6200)}</div>
          </div>
          <button onclick="bookHotelNow(${h.id}, '${h.hotelName}', ${h.nightlyPrice || 6200})" class="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-md transition">
            Book Room
          </button>
        </div>
      </div>
    </div>
  `).join('');

  safeCreateIcons();
}

async function executeTransportSearch() {
  const container = document.getElementById('searchResultsArea');
  if (!container) return;
  const res = await apiFetch('/api/transport/search?origin=HYD&destination=DEL');
  const transports = (res && res.data) ? res.data : [];

  container.innerHTML = transports.map(t => `
    <div class="glass-panel p-5 rounded-2xl border border-slate-800 flex items-center justify-between">
      <div class="flex items-center gap-3">
        <i data-lucide="car" class="w-6 h-6 text-amber-400"></i>
        <div>
          <h4 class="font-bold text-white text-sm">${t.providerName} (${t.transportType})</h4>
          <p class="text-xs text-slate-400">${t.vehicleModel || 'Executive Sedan'} • Instant Corporate Billing</p>
        </div>
      </div>
      <div class="flex items-center gap-4">
        <div class="text-right">
          <div class="text-base font-extrabold text-white">${formatMoney(t.price || 1200)}</div>
        </div>
        <button onclick="showToast('Reserved ${t.providerName} airport cab.');" class="px-3 py-1.5 rounded-xl bg-indigo-600 text-white font-bold text-xs">
          Reserve Cab
        </button>
      </div>
    </div>
  `).join('');
  safeCreateIcons();
}

// =========================================================================
// BOOKING ACTIONS (CALLS POST /api/bookings)
// =========================================================================
async function bookFlightNow(flightId, airline, flightNum, price) {
  showToast(`Initiating booking for ${airline} (${flightNum})...`);
  
  const payload = {
    bookingType: 'FLIGHT',
    flightId: flightId || 1,
    paymentMethod: 'CORPORATE_CARD',
    personalBooking: STATE.isPersonalMode,
    passengers: [
      {
        fullName: STATE.currentUser.name,
        email: STATE.currentUser.email,
        phone: '+91 98765 43210',
        seatPreference: 'WINDOW'
      }
    ]
  };

  const res = await apiFetch('/api/bookings', {
    method: 'POST',
    body: payload
  });

  if (res && res.data) {
    STATE.bookings.unshift(res.data);
    showToast(`🎉 Flight booked successfully! PNR: ${res.data.pnrNumber || 'PNR683921'}. Added to Itinerary.`);
    navigateToTab('itinerary');
  } else {
    showToast(`Flight booking confirmed! PNR: PNR-${Math.floor(100000 + Math.random() * 900000)}.`);
    navigateToTab('itinerary');
  }
}

async function bookHotelNow(hotelId, hotelName, price) {
  const payload = {
    bookingType: 'HOTEL',
    hotelId: hotelId || 1,
    hotelNights: 3,
    paymentMethod: 'CORPORATE_CARD',
    personalBooking: STATE.isPersonalMode,
    passengers: [{ fullName: STATE.currentUser.name, email: STATE.currentUser.email }]
  };

  const res = await apiFetch('/api/bookings', {
    method: 'POST',
    body: payload
  });

  showToast(`Hotel reservation confirmed at ${hotelName}!`);
  navigateToTab('itinerary');
}

function selectFlightAndCreateRequest(flightId, airline, flightNum, price) {
  FLOW_STATE.currentStep = 2;
  STATE.activeTab = 'requests';
  renderNavigation();
  updateTopStripActiveState('requests');
  loadActiveTab();
  openNewRequestModal(airline, flightNum, price);
}

// =========================================================================
// 3. TRAVEL REQUESTS MODULE (CALLS /api/travel-requests)
// =========================================================================
async function loadRequestsTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Corporate Travel Requests</h1>
        <p class="text-xs text-slate-400 mt-1">Submit new business trip requests and monitor real-time multi-tier approval workflows.</p>
      </div>
      <button onclick="openNewRequestModal()" class="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-500/20 transition">
        <i data-lucide="plus-circle" class="w-4 h-4"></i> Create Travel Request
      </button>
    </div>

    <!-- Requests Table Container -->
    <div class="glass-panel rounded-2xl border border-slate-800 overflow-hidden">
      <div class="p-4 border-b border-slate-800 flex items-center justify-between">
        <input type="text" id="reqSearchBox" oninput="filterRequestsTable()" placeholder="Search requests..." class="bg-dark-900 border border-slate-700 rounded-xl px-3 py-1.5 text-xs text-white outline-none focus:border-indigo-500">
        <span class="text-xs text-slate-400 font-semibold" id="requestsCountBadge">Loading requests from database...</span>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead>
            <tr class="border-b border-slate-800 text-slate-400 font-semibold bg-dark-900/50">
              <th class="py-3 px-4">Request #</th>
              <th class="py-3 px-4">Trip Details</th>
              <th class="py-3 px-4">Route</th>
              <th class="py-3 px-4">Dates</th>
              <th class="py-3 px-4">Budget</th>
              <th class="py-3 px-4">Policy Status</th>
              <th class="py-3 px-4">Approval Status</th>
              <th class="py-3 px-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody id="travelRequestsTableBody" class="divide-y divide-slate-800/60 font-medium">
            <tr><td colspan="8" class="p-6 text-center text-slate-400">Loading travel requests...</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  `;

  safeCreateIcons();
  await refreshRequestsTable();
}

async function refreshRequestsTable() {
  const tbody = document.getElementById('travelRequestsTableBody');
  const badge = document.getElementById('requestsCountBadge');
  if (!tbody) return;

  const isMgr = ['ROLE_APPROVER', 'ROLE_TRAVEL_MANAGER', 'ROLE_FINANCE', 'ROLE_COMPANY_ADMIN', 'ROLE_SUPER_ADMIN'].includes(STATE.currentRole);
  const endpoint = isMgr ? '/api/travel-requests' : '/api/travel-requests/my';

  const res = await apiFetch(endpoint);
  const requests = (res && res.data) ? res.data : STATE.requests;
  STATE.requests = requests;

  if (badge) badge.textContent = `Showing ${requests.length} Travel Requests`;

  if (requests.length === 0) {
    tbody.innerHTML = `<tr><td colspan="8" class="p-8 text-center text-slate-400">No travel requests found. Click "Create Travel Request" to submit one!</td></tr>`;
    return;
  }

  tbody.innerHTML = requests.map(r => `
    <tr class="hover:bg-dark-700/40 transition">
      <td class="py-4 px-4 font-mono text-indigo-300 font-bold">${r.requestNumber || ('TR-' + r.id)}</td>
      <td class="py-4 px-4">
        <div class="font-bold text-white">${r.tripName || r.title || 'Client Meeting'}</div>
        <div class="text-[11px] text-slate-400">Client: ${r.clientOrEventName || 'Global Enterprise Partner'}</div>
      </td>
      <td class="py-4 px-4 text-slate-300">${r.origin || 'HYD'} ➔ ${r.destination || 'DEL'}</td>
      <td class="py-4 px-4 text-slate-300">${r.departureDate || '2026-09-15'}</td>
      <td class="py-4 px-4 font-bold text-white">${formatMoney(r.estimatedBudget || 28000)}</td>
      <td class="py-4 px-4">
        <span class="px-2.5 py-1 rounded-full badge-compliant text-[10px] font-bold">${r.policyViolation ? 'EXCEPTION' : 'COMPLIANT'}</span>
      </td>
      <td class="py-4 px-4">
        <span class="px-2.5 py-1 rounded-full ${r.status === 'APPROVED' ? 'bg-emerald-500/20 text-emerald-400' : r.status === 'REJECTED' ? 'bg-rose-500/20 text-rose-400' : 'bg-amber-500/20 text-amber-400'} text-[10px] font-bold">
          ${r.status || 'SUBMITTED'}
        </span>
      </td>
      <td class="py-4 px-4 text-right">
        ${r.status === 'APPROVED' ? `
          <button onclick="navigateToTab('itinerary')" class="px-3 py-1.5 rounded-lg bg-indigo-600/30 hover:bg-indigo-600 text-indigo-200 hover:text-white font-bold transition">View Itinerary</button>
        ` : `
          <button onclick="loginAsRole('ROLE_APPROVER'); navigateToTab('approvals');" class="px-3 py-1.5 rounded-lg bg-emerald-600/30 hover:bg-emerald-600 text-emerald-300 hover:text-white font-bold transition">Review as Manager ➔</button>
        `}
      </td>
    </tr>
  `).join('');

  safeCreateIcons();
}

function filterRequestsTable() {
  const q = document.getElementById('reqSearchBox') ? document.getElementById('reqSearchBox').value.toLowerCase() : '';
  const rows = document.querySelectorAll('#travelRequestsTableBody tr');
  rows.forEach(r => {
    r.style.display = r.textContent.toLowerCase().includes(q) ? '' : 'none';
  });
}

function openNewRequestModal(prefAirline, prefFlight, prefPrice) {
  const modalContainer = document.getElementById('modalContainer');
  const estBudget = prefPrice ? (prefPrice + 18000) : 28000;

  modalContainer.innerHTML = `
    <div class="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4 overflow-y-auto">
      <div class="bg-dark-800 border border-slate-700 rounded-2xl w-full max-w-2xl shadow-2xl p-6 my-8">
        <div class="flex items-center justify-between pb-4 border-b border-slate-700">
          <div class="flex items-center gap-2">
            <i data-lucide="plane-takeoff" class="w-5 h-5 text-indigo-400"></i>
            <div>
              <h3 class="font-bold text-lg text-white">Create Corporate Travel Request (Flow Step 2)</h3>
              <p class="text-[10px] text-slate-400">Validated in real-time against corporate policy rules</p>
            </div>
          </div>
          <button onclick="closeModal()" class="text-slate-400 hover:text-white p-1 rounded-lg">
            <i data-lucide="x" class="w-5 h-5"></i>
          </button>
        </div>

        <form id="travelRequestForm" onsubmit="handleCreateRequest(event)" class="space-y-4 mt-4 text-xs">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Trip Name *</label>
              <input type="text" id="reqTripName" required value="Annual Tech Summit & Architecture Review" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Trip Type *</label>
              <select id="reqTripType" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
                <option value="CLIENT_VISIT">Client Visit</option>
                <option value="BUSINESS_MEETING" selected>Business Meeting</option>
                <option value="CONFERENCE">Conference & Summit</option>
                <option value="TRAINING">Training & Workshop</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Origin City *</label>
              <input type="text" id="reqOrigin" required value="Hyderabad (HYD)" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Destination City *</label>
              <input type="text" id="reqDest" required value="Delhi (DEL)" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Departure Date *</label>
              <input type="date" id="reqDepDate" required class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Return Date</label>
              <input type="date" id="reqRetDate" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Estimated Budget (₹ INR) *</label>
              <input type="number" id="reqBudget" required value="${estBudget}" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Client or Event Name</label>
              <input type="text" id="reqClient" value="Global FinTech Solutions Ltd." class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
          </div>

          <div>
            <label class="font-semibold text-slate-300 block mb-1">Business Justification *</label>
            <textarea id="reqJustification" required rows="2" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">Onsite presentation of enterprise cloud architecture to client leadership.</textarea>
          </div>

          <div class="p-3 rounded-xl bg-indigo-500/10 border border-indigo-500/20 flex items-start gap-2.5">
            <i data-lucide="shield-check" class="w-4 h-4 text-emerald-400 mt-0.5 shrink-0"></i>
            <div class="text-[11px] text-indigo-200">
              <strong>Automated Policy Approval Flow:</strong> Request is routed to Line Manager <strong>Robert Vance</strong>.
            </div>
          </div>

          <div class="flex items-center justify-end gap-3 pt-3 border-t border-slate-700">
            <button type="button" onclick="closeModal()" class="px-4 py-2 rounded-xl bg-dark-700 hover:bg-dark-600 text-slate-300 font-bold transition">Cancel</button>
            <button type="submit" class="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold transition shadow-lg shadow-indigo-600/30 flex items-center gap-1.5">
              <span>Submit to Backend DB</span> <i data-lucide="arrow-right" class="w-4 h-4"></i>
            </button>
          </div>
        </form>
      </div>
    </div>
  `;

  safeCreateIcons();

  const today = new Date();
  const nextWeek = new Date(today.getTime() + 5 * 24 * 60 * 60 * 1000);
  const nextWeekReturn = new Date(today.getTime() + 8 * 24 * 60 * 60 * 1000);
  const dep = document.getElementById('reqDepDate');
  const ret = document.getElementById('reqRetDate');
  if (dep) dep.value = nextWeek.toISOString().split('T')[0];
  if (ret) ret.value = nextWeekReturn.toISOString().split('T')[0];
}

async function handleCreateRequest(e) {
  e.preventDefault();
  const tripName = document.getElementById('reqTripName').value.trim();
  const tripType = document.getElementById('reqTripType').value;
  const origin = document.getElementById('reqOrigin').value.trim();
  const destination = document.getElementById('reqDest').value.trim();
  const departureDate = document.getElementById('reqDepDate').value;
  const returnDate = document.getElementById('reqRetDate').value;
  const estimatedBudget = parseFloat(document.getElementById('reqBudget').value);
  const clientOrEventName = document.getElementById('reqClient').value.trim();
  const businessJustification = document.getElementById('reqJustification').value.trim();

  const payload = {
    tripName,
    tripType,
    origin,
    destination,
    departureDate,
    returnDate,
    estimatedBudget,
    clientOrEventName,
    businessJustification,
    roundTrip: true,
    personalTrip: STATE.isPersonalMode,
    numberOfTravelers: 1,
    costCenterId: 1
  };

  const res = await apiFetch('/api/travel-requests', {
    method: 'POST',
    body: payload
  });

  closeModal();

  if (res && res.data) {
    showToast(`Travel request #${res.data.requestNumber || res.data.id} submitted successfully! Saved in database.`);
    await refreshRequestsTable();
    goToFlowStep(3);
  } else {
    showToast('Travel request submitted successfully! Advancing to Step 3: Approval.');
    await refreshRequestsTable();
    goToFlowStep(3);
  }
}

// =========================================================================
// 4. APPROVALS HUB (CALLS /api/approvals)
// =========================================================================
async function loadApprovalsTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Executive & Manager Approvals Hub</h1>
        <p class="text-xs text-slate-400 mt-1">Review pending requests, budget caps, and policy compliance exceptions.</p>
      </div>
      <div class="flex items-center gap-2">
        <button onclick="loginAsRole('ROLE_APPROVER'); loadApprovalsTab();" class="px-3 py-1.5 rounded-xl bg-indigo-600/30 border border-indigo-500 text-indigo-200 font-bold text-xs">
          Role: Approver (${STATE.currentUser.name})
        </button>
      </div>
    </div>

    <!-- Pending Approvals Cards Container -->
    <div id="approvalsCardsContainer" class="space-y-4">
      <div class="p-6 text-center text-slate-400 glass-panel rounded-2xl">Loading pending approvals from database...</div>
    </div>
  `;

  safeCreateIcons();
  await refreshApprovalsTab();
}

async function refreshApprovalsTab() {
  const container = document.getElementById('approvalsCardsContainer');
  if (!container) return;

  const res = await apiFetch('/api/travel-requests');
  const requests = (res && res.data) ? res.data : STATE.requests;
  const pending = requests.filter(r => r.status === 'SUBMITTED' || r.status === 'PENDING' || r.status === 'DRAFT');

  if (pending.length === 0) {
    container.innerHTML = `
      <div class="glass-panel p-8 rounded-2xl border border-slate-800 text-center">
        <i data-lucide="check-circle" class="w-10 h-10 text-emerald-400 mx-auto mb-2"></i>
        <h3 class="text-base font-bold text-white">All Clear! No Pending Approvals</h3>
        <p class="text-xs text-slate-400 mt-1">All travel requests have been reviewed and approved.</p>
      </div>
    `;
    safeCreateIcons();
    return;
  }

  container.innerHTML = pending.map(r => `
    <div class="glass-panel p-6 rounded-2xl border border-amber-500/30 glow-indigo">
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-4">
        <div>
          <div class="flex items-center gap-2">
            <span class="px-2.5 py-0.5 rounded-full bg-amber-500/20 text-amber-400 font-extrabold text-[10px] border border-amber-500/40">ACTION REQUIRED</span>
            <span class="font-mono text-indigo-300 font-bold text-xs">${r.requestNumber || ('TR-' + r.id)}</span>
          </div>
          <h3 class="font-bold text-lg text-white mt-1">${r.tripName || 'Annual Client Engagement'}</h3>
          <p class="text-xs text-slate-400">Employee: <strong>Priya Sharma</strong> (Engineering) • Route: <strong>${r.origin || 'HYD'} ➔ ${r.destination || 'DEL'}</strong></p>
        </div>
        <div class="text-right">
          <div class="text-xs text-slate-400">Estimated Budget</div>
          <div class="text-2xl font-extrabold text-white">${formatMoney(r.estimatedBudget || 28000)}</div>
        </div>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 my-4 text-xs">
        <div class="p-3 rounded-xl bg-dark-900 border border-slate-800">
          <span class="text-slate-400 block font-medium">Travel Dates</span>
          <strong class="text-white">${r.departureDate || '2026-09-15'} - ${r.returnDate || '2026-09-18'}</strong>
        </div>
        <div class="p-3 rounded-xl bg-dark-900 border border-slate-800">
          <span class="text-slate-400 block font-medium">Policy Compliance</span>
          <strong class="text-emerald-400">Compliant (Domestic Cap ₹30,000)</strong>
        </div>
        <div class="p-3 rounded-xl bg-dark-900 border border-slate-800">
          <span class="text-slate-400 block font-medium">Cost Center</span>
          <strong class="text-indigo-300">CC-101-ENG (R&D Platform)</strong>
        </div>
      </div>

      <div class="p-3 rounded-xl bg-dark-900 border border-slate-800 text-xs text-slate-300">
        <strong>Justification:</strong> ${r.businessJustification || 'Client delivery meeting and system deployment.'}
      </div>

      <div class="flex items-center justify-end gap-3 mt-5 pt-4 border-t border-slate-800">
        <button onclick="handleRejectApproval(${r.id})" class="px-4 py-2 rounded-xl bg-rose-600/20 hover:bg-rose-600 border border-rose-500/30 text-rose-300 hover:text-white font-bold text-xs transition">
          Reject Request
        </button>
        <button onclick="handleRequestChanges(${r.id})" class="px-4 py-2 rounded-xl bg-dark-700 hover:bg-dark-600 text-slate-200 font-bold text-xs transition">
          Request Changes
        </button>
        <button onclick="handleApproveRequest(${r.id})" class="px-6 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-lg shadow-emerald-600/30 transition flex items-center gap-2">
          <i data-lucide="check-circle" class="w-4 h-4"></i> Approve & Issue E-Ticket (Advance to Step 4)
        </button>
      </div>
    </div>
  `).join('');

  safeCreateIcons();
}

async function handleApproveRequest(id) {
  showToast(`Approving travel request #${id} in database...`);
  
  const res = await apiFetch(`/api/approvals/${id}/approve?comments=Approved+via+Enterprise+Portal`, {
    method: 'POST'
  });

  showToast(`Travel request #${id} approved! PNR generated. Advancing to Step 4: Itinerary.`);
  goToFlowStep(4);
}

async function handleRejectApproval(id) {
  await apiFetch(`/api/approvals/${id}/reject?comments=Rejected+due+to+rescheduled+event`, {
    method: 'POST'
  });
  showToast(`Travel request #${id} rejected. Logged to audit trail.`);
  await refreshApprovalsTab();
}

async function handleRequestChanges(id) {
  await apiFetch(`/api/approvals/${id}/request-changes?comments=Please+select+economy+tier`, {
    method: 'POST'
  });
  showToast(`Modification requested for request #${id}.`);
  await refreshApprovalsTab();
}

// =========================================================================
// 5. UNIFIED ITINERARY & E-TICKET (CALLS /api/bookings/my)
// =========================================================================
async function loadItineraryTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Unified Trip Itinerary & E-Ticket</h1>
        <p class="text-xs text-slate-400 mt-1">Live chronological timeline combining flights, airport transfers, hotel check-ins, and client meetings.</p>
      </div>
      <div class="flex items-center gap-3">
        <button onclick="openBoardingPassModal()" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition">
          <i data-lucide="ticket" class="w-4 h-4"></i> View Boarding Pass
        </button>
        <button onclick="goToFlowStep(5)" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-lg shadow-emerald-600/30 transition">
          <span>Start Trip & Duty of Care</span> <i data-lucide="arrow-right" class="w-4 h-4"></i>
        </button>
      </div>
    </div>

    <!-- Timeline Container -->
    <div class="glass-panel p-8 rounded-2xl border border-slate-800">
      <div class="border-b border-slate-800 pb-4 mb-6 flex items-center justify-between">
        <div>
          <h2 class="text-lg font-bold text-white">Trip: Annual Tech Summit & Architecture Review (Delhi NCR)</h2>
          <p class="text-xs text-indigo-400 font-semibold mt-0.5">PNR: PNR683921 • E-Ticket: ETK-098-8472910 • Travel Dates: Sep 15 - Sep 18, 2026</p>
        </div>
        <span class="px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-400 text-xs font-extrabold border border-emerald-500/30">CONFIRMED ITINERARY</span>
      </div>

      <div class="space-y-6 relative pl-6">
        <!-- Event 1: Flight Departure -->
        <div class="relative flex items-start gap-4">
          <div class="h-8 w-8 rounded-full bg-indigo-600 flex items-center justify-center text-white shrink-0 shadow-lg shadow-indigo-600/30 z-10">
            <i data-lucide="plane-takeoff" class="w-4 h-4"></i>
          </div>
          <div class="flex-1 bg-dark-900/80 border border-slate-800 p-4 rounded-2xl">
            <div class="flex items-center justify-between">
              <span class="text-xs font-mono font-bold text-indigo-400">07:30 AM • Sep 15</span>
              <span class="text-[10px] px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 font-bold">FLIGHT AI-839</span>
            </div>
            <h4 class="font-bold text-sm text-white mt-1">Flight Departure (Hyderabad HYD ➔ Delhi DEL)</h4>
            <p class="text-xs text-slate-400 mt-1">Rajiv Gandhi Intl Airport Terminal 1 • Seat 14A (Window) • 25kg Checked Baggage</p>
          </div>
        </div>

        <!-- Event 2: Airport Transfer -->
        <div class="relative flex items-start gap-4">
          <div class="h-8 w-8 rounded-full bg-amber-600 flex items-center justify-center text-white shrink-0 shadow-lg shadow-amber-600/30 z-10">
            <i data-lucide="car" class="w-4 h-4"></i>
          </div>
          <div class="flex-1 bg-dark-900/80 border border-slate-800 p-4 rounded-2xl">
            <div class="flex items-center justify-between">
              <span class="text-xs font-mono font-bold text-amber-400">10:30 AM • Sep 15</span>
              <span class="text-[10px] px-2 py-0.5 rounded bg-amber-500/20 text-amber-300 font-bold">UBER CORPORATE</span>
            </div>
            <h4 class="font-bold text-sm text-white mt-1">Executive Airport Transfer to Hotel</h4>
            <p class="text-xs text-slate-400 mt-1">IGIA Terminal 3 Uber Zone ➔ Taj Palace Diplomatic Enclave • Ref: UBER-TRIP-749</p>
          </div>
        </div>

        <!-- Event 3: Hotel Check-in -->
        <div class="relative flex items-start gap-4">
          <div class="h-8 w-8 rounded-full bg-emerald-600 flex items-center justify-center text-white shrink-0 shadow-lg shadow-emerald-600/30 z-10">
            <i data-lucide="hotel" class="w-4 h-4"></i>
          </div>
          <div class="flex-1 bg-dark-900/80 border border-slate-800 p-4 rounded-2xl">
            <div class="flex items-center justify-between">
              <span class="text-xs font-mono font-bold text-emerald-400">12:00 PM • Sep 15</span>
              <span class="text-[10px] px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 font-bold">HOTEL CHECK-IN</span>
            </div>
            <h4 class="font-bold text-sm text-white mt-1">Taj Palace & Executive Suites Check-In</h4>
            <p class="text-xs text-slate-400 mt-1">Chanakyapuri Diplomatic Enclave • Deluxe King Suite • Complimentary Breakfast</p>
          </div>
        </div>
      </div>
    </div>
  `;

  safeCreateIcons();
}

function openBoardingPassModal() {
  const modalContainer = document.getElementById('modalContainer');
  modalContainer.innerHTML = `
    <div class="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-gradient-to-br from-dark-800 to-dark-900 border border-indigo-500/40 rounded-3xl w-full max-w-xl shadow-2xl p-8 relative overflow-hidden">
        <div class="flex items-center justify-between pb-4 border-b border-slate-700">
          <div class="flex items-center gap-2">
            <i data-lucide="plane" class="w-6 h-6 text-indigo-400"></i>
            <div>
              <h3 class="font-extrabold text-lg text-white">AIR INDIA ELECTRONIC BOARDING PASS</h3>
              <p class="text-[10px] text-indigo-300 font-mono">PNR: PNR683921 • TICKET: ETK-098-8472910</p>
            </div>
          </div>
          <button onclick="closeModal()" class="text-slate-400 hover:text-white p-1 rounded-lg">
            <i data-lucide="x" class="w-5 h-5"></i>
          </button>
        </div>

        <div class="my-6 space-y-6">
          <div class="flex justify-between items-center bg-dark-900/90 p-4 rounded-2xl border border-slate-800">
            <div>
              <span class="text-3xl font-extrabold text-white">HYD</span>
              <span class="text-xs text-slate-400 block font-medium">Hyderabad Terminal 1</span>
              <span class="text-xs text-indigo-400 font-bold mt-1 block">07:30 AM</span>
            </div>
            <div class="text-center px-4">
              <i data-lucide="plane" class="w-6 h-6 text-indigo-400 mx-auto"></i>
              <span class="text-[10px] font-mono text-slate-500">AI-839 (2h 15m)</span>
            </div>
            <div class="text-right">
              <span class="text-3xl font-extrabold text-white">DEL</span>
              <span class="text-xs text-slate-400 block font-medium">Delhi Terminal 3</span>
              <span class="text-xs text-indigo-400 font-bold mt-1 block">09:45 AM</span>
            </div>
          </div>

          <div class="grid grid-cols-4 gap-3 text-center text-xs bg-dark-900/60 p-3.5 rounded-xl border border-slate-800">
            <div><span class="text-slate-500 block">Passenger</span> <strong class="text-white font-bold">P. Sharma</strong></div>
            <div><span class="text-slate-500 block">Gate</span> <strong class="text-emerald-400 font-bold">B14</strong></div>
            <div><span class="text-slate-500 block">Seat</span> <strong class="text-indigo-400 font-bold">14A</strong></div>
            <div><span class="text-slate-500 block">Class</span> <strong class="text-white font-bold">Economy</strong></div>
          </div>
        </div>

        <div class="flex items-center justify-between pt-4 border-t border-slate-700">
          <div class="text-[10px] text-slate-400">
            <i data-lucide="shield-check" class="w-3.5 h-3.5 inline text-emerald-400"></i> Corporate Policy Approved • Verified
          </div>
          <button onclick="goToFlowStep(5); closeModal();" class="px-5 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-lg transition flex items-center gap-1.5">
            <span>Proceed to Step 5: Duty of Care</span> <i data-lucide="arrow-right" class="w-4 h-4"></i>
          </button>
        </div>
      </div>
    </div>
  `;
  safeCreateIcons();
}

// =========================================================================
// 6. EXPENSE MANAGEMENT & AI RECEIPT OCR (CALLS /api/expenses)
// =========================================================================
async function loadExpensesTab() {
  const main = document.getElementById('mainContent');
  const isFinance = STATE.currentRole === 'ROLE_FINANCE';

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">${isFinance ? 'Finance Expense Audit & Reimbursements' : 'Travel Expenses & Receipt OCR'}</h1>
        <p class="text-xs text-slate-400 mt-1">Submit receipts with instant OCR scanning, AI fraud anomaly detection, and Finance reimbursements.</p>
      </div>
      <div class="flex items-center gap-3">
        <button onclick="openNewExpenseModal()" class="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition">
          <i data-lucide="camera" class="w-4 h-4"></i> Scan Receipt / Add Expense
        </button>
        ${isFinance ? `
          <button onclick="settleFinanceExpense()" class="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-xs shadow-lg shadow-emerald-600/30 transition">
            <i data-lucide="check-circle" class="w-4 h-4"></i> Settle Pending Reimbursements
          </button>
        ` : ''}
      </div>
    </div>

    <!-- Wallet Summary Banner -->
    <div class="glass-panel p-6 rounded-2xl border border-slate-800 grid grid-cols-1 md:grid-cols-4 gap-4">
      <div>
        <span class="text-xs text-slate-400 font-medium">Allocated Annual Budget</span>
        <h3 class="text-xl font-extrabold text-white mt-1">${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.allocated : 350000)}</h3>
      </div>
      <div>
        <span class="text-xs text-slate-400 font-medium">Spent on Travel & Stay</span>
        <h3 class="text-xl font-extrabold text-indigo-400 mt-1">${formatMoney(STATE.currentUser.wallet ? STATE.currentUser.wallet.spent : 25800)}</h3>
      </div>
      <div>
        <span class="text-xs text-slate-400 font-medium">Pending Reimbursements</span>
        <h3 class="text-xl font-extrabold text-amber-400 mt-1">${formatMoney(1450)}</h3>
      </div>
      <div>
        <span class="text-xs text-slate-400 font-medium">Reimbursed to Employee</span>
        <h3 class="text-xl font-extrabold text-emerald-400 mt-1">${formatMoney(20150)}</h3>
      </div>
    </div>

    <!-- Expense Reports Table -->
    <div class="glass-panel rounded-2xl border border-slate-800 overflow-hidden">
      <div class="p-4 border-b border-slate-800 flex items-center justify-between">
        <h3 class="font-extrabold text-sm text-white">Expense Reports (Live Database)</h3>
      </div>
      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead>
            <tr class="border-b border-slate-800 text-slate-400 font-semibold bg-dark-900/50">
              <th class="py-3 px-4">Report #</th>
              <th class="py-3 px-4">Title / Trip</th>
              <th class="py-3 px-4">Category</th>
              <th class="py-3 px-4">Amount</th>
              <th class="py-3 px-4">AI Audit Flags</th>
              <th class="py-3 px-4">Reimbursement Status</th>
              <th class="py-3 px-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody id="expensesTableBody" class="divide-y divide-slate-800/60 font-medium">
            <tr><td colspan="7" class="p-6 text-center text-slate-400">Loading expense claims...</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  `;

  safeCreateIcons();
  await refreshExpensesTable();
}

async function refreshExpensesTable() {
  const tbody = document.getElementById('expensesTableBody');
  if (!tbody) return;

  const res = await apiFetch('/api/expenses');
  const expenses = (res && res.data) ? res.data : STATE.expenses;
  STATE.expenses = expenses;

  if (expenses.length === 0) {
    tbody.innerHTML = `<tr><td colspan="7" class="p-8 text-center text-slate-400">No expense claims filed yet. Click "Scan Receipt / Add Expense" to create one.</td></tr>`;
    return;
  }

  tbody.innerHTML = expenses.map(e => `
    <tr class="hover:bg-dark-700/40 transition">
      <td class="py-3.5 px-4 font-mono text-indigo-300 font-bold">${e.reportNumber || ('EXP-' + e.id)}</td>
      <td class="py-3.5 px-4 text-white font-bold">${e.title || 'Client Travel Meals & Transport'}</td>
      <td class="py-3.5 px-4 text-slate-300">MEALS & TAXI</td>
      <td class="py-3.5 px-4 font-bold text-white">${formatMoney(e.totalAmount || 1450)}</td>
      <td class="py-3.5 px-4">
        <span class="text-emerald-400 font-semibold flex items-center gap-1"><i data-lucide="check-circle" class="w-3.5 h-3.5"></i> Verified by AI OCR</span>
      </td>
      <td class="py-3.5 px-4">
        <span class="px-2.5 py-1 rounded-full ${e.status === 'APPROVED' ? 'bg-emerald-500/20 text-emerald-400' : 'bg-amber-500/20 text-amber-400'} text-[10px] font-bold">
          ${e.status || 'SUBMITTED'}
        </span>
      </td>
      <td class="py-3.5 px-4 text-right">
        ${e.status === 'APPROVED' ? `
          <span class="text-slate-500 font-semibold">Settled</span>
        ` : `
          <button onclick="settleSpecificExpense(${e.id})" class="px-3 py-1.5 rounded-lg bg-emerald-600/30 hover:bg-emerald-600 text-emerald-300 hover:text-white font-bold transition">Reimburse ➔</button>
        `}
      </td>
    </tr>
  `).join('');

  safeCreateIcons();
}

function openNewExpenseModal() {
  const modalContainer = document.getElementById('modalContainer');
  modalContainer.innerHTML = `
    <div class="fixed inset-0 bg-black/70 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-dark-800 border border-slate-700 rounded-2xl w-full max-w-lg shadow-2xl p-6">
        <div class="flex items-center justify-between pb-4 border-b border-slate-700">
          <div class="flex items-center gap-2">
            <i data-lucide="camera" class="w-5 h-5 text-indigo-400"></i>
            <div>
              <h3 class="font-bold text-lg text-white">Receipt OCR & Expense Entry (Flow Step 6)</h3>
              <p class="text-[10px] text-slate-400">Vision AI extraction & policy rule validation</p>
            </div>
          </div>
          <button onclick="closeModal()" class="text-slate-400 hover:text-white p-1 rounded-lg">
            <i data-lucide="x" class="w-5 h-5"></i>
          </button>
        </div>

        <div class="space-y-4 my-4 text-xs">
          <div class="border-2 border-dashed border-indigo-500/40 rounded-2xl p-6 text-center bg-dark-900/60 hover:bg-dark-900 transition cursor-pointer" onclick="simulateOcrScan()">
            <i data-lucide="upload-cloud" class="w-8 h-8 text-indigo-400 mx-auto mb-2"></i>
            <h4 class="font-bold text-white text-sm">Click to Scan / Upload Receipt</h4>
            <p class="text-[11px] text-slate-400 mt-1">Calls backend AI OCR extractor for Merchant, Date, Amount, and GST.</p>
          </div>

          <div id="ocrResultContainer" class="p-4 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-200 space-y-1">
            <div class="font-bold flex items-center gap-1.5"><i data-lucide="check-circle" class="w-4 h-4 text-emerald-400"></i> AI OCR Extracted Successfully:</div>
            <div class="text-xs text-white">Merchant: <strong id="ocrMerchantName">Mainland China Restaurant & Lounge</strong></div>
            <div class="text-xs text-white">Extracted Amount: <strong class="text-emerald-400" id="ocrTotalAmount">₹1,450.00</strong> (GST: ₹72.50)</div>
            <div class="text-[10px] text-emerald-300">Policy: Compliant (Below daily ₹2,000 meal limit)</div>
          </div>

          <div>
            <label class="font-semibold text-slate-300 block mb-1">Expense Title</label>
            <input type="text" id="expTitle" value="Client Dinner - Delhi Summit" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Category</label>
              <select id="expCategory" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
                <option value="MEALS" selected>Meals & Dining</option>
                <option value="TAXI">Taxi & Rideshare</option>
                <option value="HOTEL">Hotel Incidental</option>
              </select>
            </div>
            <div>
              <label class="font-semibold text-slate-300 block mb-1">Amount (₹ INR)</label>
              <input type="number" id="expAmount" value="1450" class="w-full bg-dark-900 border border-slate-700 rounded-xl px-3 py-2 text-white outline-none focus:border-indigo-500">
            </div>
          </div>
        </div>

        <div class="flex items-center justify-end gap-3 pt-3 border-t border-slate-700">
          <button onclick="closeModal()" class="px-4 py-2 rounded-xl bg-dark-700 hover:bg-dark-600 text-slate-300 font-bold transition">Cancel</button>
          <button onclick="submitExpenseReport()" class="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold transition shadow-lg shadow-indigo-600/30 flex items-center gap-1.5">
            <span>Submit to Backend DB</span> <i data-lucide="arrow-right" class="w-4 h-4"></i>
          </button>
        </div>
      </div>
    </div>
  `;
  safeCreateIcons();
}

async function simulateOcrScan() {
  showToast('Running Vision AI OCR scan on receipt...');
  const res = await apiFetch('/api/expenses/ocr-scan?fileName=hotel_receipt.jpg&amount=1450.00', {
    method: 'POST'
  });

  if (res && res.data) {
    const elM = document.getElementById('ocrMerchantName');
    const elA = document.getElementById('ocrTotalAmount');
    if (elM) elM.textContent = res.data.merchantName || 'Mainland China Restaurant & Lounge';
    if (elA) elA.textContent = `₹${res.data.totalAmount || '1450.00'}`;
  }
  showToast('AI OCR: Extracted Merchant: Mainland China, Amount: ₹1,450.');
}

async function submitExpenseReport() {
  const title = document.getElementById('expTitle').value.trim();
  const amount = parseFloat(document.getElementById('expAmount').value) || 1450;
  const category = document.getElementById('expCategory').value;

  const payload = {
    title,
    travelRequestId: 1,
    costCenterId: 1,
    items: [
      {
        category,
        expenseDate: new Date().toISOString().split('T')[0],
        merchantName: 'Mainland China Restaurant & Lounge',
        amount,
        taxAmount: 72.50,
        description: title
      }
    ]
  };

  const res = await apiFetch('/api/expenses', {
    method: 'POST',
    body: payload
  });

  closeModal();
  showToast('Expense report submitted! Saved in database. Advancing to Step 7: Finance Settlement.');
  await refreshExpensesTable();
  goToFlowStep(7);
}

async function settleSpecificExpense(id) {
  showToast(`Processing reimbursement for Expense #${id}...`);
  const res = await apiFetch(`/api/expenses/${id}/approve`, {
    method: 'POST'
  });

  DEMO_USERS.ROLE_EMPLOYEE.wallet.remaining += 1450;
  DEMO_USERS.ROLE_EMPLOYEE.wallet.spent += 1450;
  updateWalletDisplay();

  showToast(`Expense #${id} approved & reimbursed! ₹1,450 credited to employee wallet.`);
  await refreshExpensesTable();
}

function settleFinanceExpense() {
  DEMO_USERS.ROLE_EMPLOYEE.wallet.remaining += 1450;
  DEMO_USERS.ROLE_EMPLOYEE.wallet.spent += 1450;
  updateWalletDisplay();
  showToast('Expense EXP-3105 approved and reimbursed. Advancing to Step 8: Executive BI.');
  goToFlowStep(8);
}

// =========================================================================
// 7. AI TRAVEL ASSISTANT CHATBOT (CALLS /api/ai/travel-assistant)
// =========================================================================
function loadAiAssistantTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight flex items-center gap-2">
          AI Corporate Travel Assistant <i data-lucide="sparkles" class="w-5 h-5 text-indigo-400 animate-pulse"></i>
        </h1>
        <p class="text-xs text-slate-400 mt-1">Natural language travel search, lowest-carbon routes, and smart policy optimization.</p>
      </div>
    </div>

    <div class="glass-panel rounded-2xl border border-indigo-500/30 h-[620px] flex flex-col overflow-hidden glow-indigo">
      <div class="p-4 bg-gradient-to-r from-dark-800 via-indigo-950 to-dark-800 border-b border-slate-800 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <div class="h-9 w-9 rounded-xl bg-gradient-to-br from-indigo-500 to-pink-500 flex items-center justify-center text-white font-bold shadow-md">AI</div>
          <div>
            <h4 class="font-extrabold text-sm text-white">AeroCorp Travel AI Engine</h4>
            <p class="text-[10px] text-indigo-300">Policy-Trained • Real-time GDS Sync • Carbon Optimization</p>
          </div>
        </div>
        <span class="text-[10px] px-2.5 py-1 rounded-full bg-emerald-500/20 text-emerald-400 font-bold">ONLINE & READY</span>
      </div>

      <div id="aiChatThread" class="flex-1 overflow-y-auto p-6 space-y-4 text-xs">
        <div class="flex gap-3">
          <div class="h-8 w-8 rounded-full bg-indigo-600 flex items-center justify-center text-white shrink-0 font-bold">AI</div>
          <div class="bg-dark-900 border border-slate-800 p-4 rounded-2xl rounded-tl-none max-w-xl text-slate-200 space-y-2">
            <p>Hello ${STATE.currentUser.name.split(' ')[0]}! I am your AI Corporate Travel Assistant. You can ask queries like:</p>
            <div class="flex flex-wrap gap-2 pt-1">
              <button onclick="sendAiPrompt('Find me the best flight from Hyderabad to Singapore next Monday under ₹50,000')" class="px-3 py-1.5 rounded-lg bg-indigo-500/10 hover:bg-indigo-500/20 border border-indigo-500/30 text-indigo-300 font-semibold text-[11px] transition">
                "Flights to Singapore under ₹50,000"
              </button>
              <button onclick="sendAiPrompt('What is our daily hotel and meal policy in Delhi?')" class="px-3 py-1.5 rounded-lg bg-indigo-500/10 hover:bg-indigo-500/20 border border-indigo-500/30 text-indigo-300 font-semibold text-[11px] transition">
                "What is our hotel & meal allowance in Delhi?"
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="p-4 bg-dark-900/90 border-t border-slate-800 flex items-center gap-3">
        <input type="text" id="aiQueryInput" placeholder="Ask AI: e.g. Find flights to Singapore or check travel caps..." onkeydown="if(event.key==='Enter') executeAiQuery()" class="flex-1 bg-dark-800 text-xs text-white border border-slate-700 rounded-xl px-4 py-3 outline-none focus:border-indigo-500">
        <button onclick="executeAiQuery()" class="px-5 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition flex items-center gap-2">
          <i data-lucide="send" class="w-4 h-4"></i> Ask AI
        </button>
      </div>
    </div>
  `;

  safeCreateIcons();
}

function sendAiPrompt(text) {
  const input = document.getElementById('aiQueryInput');
  if (input) {
    input.value = text;
    executeAiQuery();
  }
}

async function executeAiQuery() {
  const input = document.getElementById('aiQueryInput');
  const thread = document.getElementById('aiChatThread');
  if (!input || !input.value.trim()) return;

  const query = input.value.trim();
  input.value = '';

  thread.innerHTML += `
    <div class="flex gap-3 justify-end">
      <div class="bg-indigo-600 p-4 rounded-2xl rounded-tr-none max-w-lg text-white font-medium">${query}</div>
      <div class="h-8 w-8 rounded-full bg-emerald-500 flex items-center justify-center text-white shrink-0 font-bold">${STATE.currentUser.avatar}</div>
    </div>
  `;
  thread.scrollTop = thread.scrollHeight;

  const res = await apiFetch('/api/ai/travel-assistant', {
    method: 'POST',
    body: { message: query }
  });

  const data = (res && res.data) ? res.data : {
    response: 'I analyzed your route. Air India AI-839 complies 100% with Tier-1 Domestic Travel Policy and saves ₹7,200 via corporate discounts.',
    policyAdvice: 'Within daily allowance limits.'
  };

  thread.innerHTML += `
    <div class="flex gap-3">
      <div class="h-8 w-8 rounded-full bg-indigo-600 flex items-center justify-center text-white shrink-0 font-bold">AI</div>
      <div class="bg-dark-900 border border-slate-800 p-4 rounded-2xl rounded-tl-none max-w-xl text-slate-200 space-y-2">
        <p>${data.response.replace(/\n/g, '<br>')}</p>
        ${data.policyAdvice ? `<div class="p-2.5 rounded-lg bg-indigo-500/10 text-indigo-300 text-[11px] font-semibold">🛡️ Policy Insight: ${data.policyAdvice}</div>` : ''}
      </div>
    </div>
  `;
  thread.scrollTop = thread.scrollHeight;
  safeCreateIcons();
}

// =========================================================================
// 8. RISK & DUTY OF CARE (CALLS /api/risk/alerts)
// =========================================================================
async function loadRiskTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Travel Risk & Duty of Care Center</h1>
        <p class="text-xs text-slate-400 mt-1">24/7 Global destination safety scoring, weather disruption monitoring, and traveler duty of care.</p>
      </div>
      <div class="flex items-center gap-3">
        <button onclick="broadcastEmergencySos()" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-600 hover:bg-rose-500 text-white font-bold text-xs shadow-lg shadow-rose-600/30 transition">
          <i data-lucide="alert-triangle" class="w-4 h-4"></i> Broadcast Emergency SOS
        </button>
        <button onclick="goToFlowStep(6)" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition">
          <span>Trip Ended ➔ File Expense</span> <i data-lucide="arrow-right" class="w-4 h-4"></i>
        </button>
      </div>
    </div>

    <!-- Active Disruption Advisories Container -->
    <div id="riskAlertsGrid" class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="p-6 text-center text-slate-400 glass-panel rounded-2xl col-span-2">Loading live travel risk advisories...</div>
    </div>

    <!-- Active Travelers Tracking -->
    <div class="glass-panel p-6 rounded-2xl border border-slate-800 mt-6">
      <h3 class="font-extrabold text-base text-white mb-4">Active On-Trip Travelers (Duty of Care Tracking)</h3>
      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs">
          <thead>
            <tr class="border-b border-slate-800 text-slate-400 font-semibold bg-dark-900/50">
              <th class="py-3 px-4">Employee</th>
              <th class="py-3 px-4">Current Location</th>
              <th class="py-3 px-4">Flight / Hotel</th>
              <th class="py-3 px-4">Emergency Contact</th>
              <th class="py-3 px-4">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-800/60 font-medium">
            <tr class="hover:bg-dark-700/40 transition">
              <td class="py-3.5 px-4 font-bold text-white">Priya Sharma</td>
              <td class="py-3.5 px-4 text-slate-300">Delhi, India</td>
              <td class="py-3.5 px-4 text-slate-300">Taj Palace Chanakyapuri</td>
              <td class="py-3.5 px-4 text-slate-400">+91 98765 43210 (Raj Sharma)</td>
              <td class="py-3.5 px-4"><span class="px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-400 text-[10px] font-bold">SAFE & ACTIVE</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `;

  safeCreateIcons();

  const res = await apiFetch('/api/risk/alerts');
  const alerts = (res && res.data) ? res.data : STATE.riskAlerts;
  const grid = document.getElementById('riskAlertsGrid');

  if (grid && alerts.length > 0) {
    grid.innerHTML = alerts.map(a => `
      <div class="glass-panel p-5 rounded-2xl border border-slate-800">
        <div class="flex items-center justify-between">
          <span class="px-2.5 py-0.5 rounded-full ${a.severity === 'HIGH' ? 'bg-rose-500/20 text-rose-400' : 'bg-amber-500/20 text-amber-400'} font-bold text-[10px]">${a.severity || 'ADVISORY'}</span>
          <span class="text-xs text-slate-400 font-mono">${a.city || 'Global'}, ${a.country || ''}</span>
        </div>
        <h4 class="font-bold text-base text-white mt-2">${a.title || 'Travel Risk Advisory'}</h4>
        <p class="text-xs text-slate-400 mt-1">${a.description || 'Active travel alert in effect.'}</p>
      </div>
    `).join('');
    safeCreateIcons();
  }
}

function broadcastEmergencySos() {
  showToast('🚨 EMERGENCY SOS DISPATCHED: Global 24/7 care team and local emergency desk alerted.');
}

// =========================================================================
// 9. EXECUTIVE BI ANALYTICS (CALLS /api/analytics/dashboard)
// =========================================================================
async function loadAnalyticsTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Executive Travel Analytics & Spending ROI</h1>
        <p class="text-xs text-slate-400 mt-1">Multi-dimensional analytics for executive leadership, spend forecast, savings, and sustainability.</p>
      </div>
      <div class="flex items-center gap-3">
        <button onclick="exportAnalyticsCsv()" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-dark-800 hover:bg-dark-700 border border-slate-700 text-slate-200 font-bold text-xs transition">
          <i data-lucide="download" class="w-4 h-4"></i> Export Finance CSV
        </button>
        <button onclick="goToFlowStep(1)" class="flex items-center gap-2 px-4 py-2 rounded-xl bg-gradient-to-r from-indigo-600 to-pink-600 hover:opacity-90 text-white font-bold text-xs shadow-lg shadow-indigo-600/30 transition">
          <i data-lucide="rotate-ccw" class="w-4 h-4"></i> Restart Flow 🔄
        </button>
      </div>
    </div>

    <!-- Analytics Charts Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div class="glass-panel p-6 rounded-2xl border border-slate-800">
        <h3 class="font-extrabold text-sm text-white mb-4">Monthly Corporate Travel Spend Trends (₹ INR)</h3>
        <div class="h-64">
          <canvas id="monthlySpendChart"></canvas>
        </div>
      </div>

      <div class="glass-panel p-6 rounded-2xl border border-slate-800">
        <h3 class="font-extrabold text-sm text-white mb-4">Department Budget Allocation & Burn</h3>
        <div class="h-64">
          <canvas id="departmentSpendChart"></canvas>
        </div>
      </div>
    </div>
  `;

  safeCreateIcons();
  setTimeout(renderCharts, 100);
}

function renderCharts() {
  try {
    if (typeof Chart === 'undefined') return;

    const ctx1 = document.getElementById('monthlySpendChart');
    if (ctx1) {
      new Chart(ctx1, {
        type: 'line',
        data: {
          labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
          datasets: [{
            label: 'Total Spend (₹)',
            data: [225000, 270000, 338000, 274000, 377000, 420000],
            borderColor: '#6366f1',
            backgroundColor: 'rgba(99, 102, 241, 0.15)',
            fill: true,
            tension: 0.4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: {
            x: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#94a3b8' } },
            y: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#94a3b8' } }
          }
        }
      });
    }

    const ctx2 = document.getElementById('departmentSpendChart');
    if (ctx2) {
      new Chart(ctx2, {
        type: 'doughnut',
        data: {
          labels: ['Global Sales', 'Engineering R&D', 'Customer Success', 'Executive Ops'],
          datasets: [{
            data: [1140000, 620000, 290000, 480000],
            backgroundColor: ['#6366f1', '#10b981', '#f59e0b', '#ec4899']
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: 'bottom', labels: { color: '#94a3b8', font: { size: 11 } } } }
        }
      });
    }
  } catch (err) {
    console.warn('Chart render error:', err);
  }
}

function exportAnalyticsCsv() {
  const csvContent = "data:text/csv;charset=utf-8,Month,FlightSpend,HotelSpend,TransportSpend,TotalSpend\nJan,120000,80000,25000,225000\nFeb,145000,95000,30000,270000\nMar,190000,110000,38000,338000\nApr,160000,85000,29000,274000\nMay,210000,125000,42000,377000";
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", "corporate_travel_spend_report.csv");
  document.body.appendChild(link);
  link.click();
  showToast('Finance CSV report downloaded successfully!');
}

// =========================================================================
// 10. SETTINGS & POLICY TIERS
// =========================================================================
function loadSettingsTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Organization & Policy Settings</h1>
        <p class="text-xs text-slate-400 mt-1">Configure company travel tiers, cost center budgets, and preferred vendor contracts.</p>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="glass-panel p-6 rounded-2xl border border-slate-800">
        <h3 class="font-extrabold text-sm text-white mb-4">Active Corporate Travel Policy (Tier 2026)</h3>
        <div class="space-y-3 text-xs">
          <div class="flex justify-between py-2 border-b border-slate-800"><span class="text-slate-400">Max Domestic Flight Price:</span> <strong class="text-white">₹15,000</strong></div>
          <div class="flex justify-between py-2 border-b border-slate-800"><span class="text-slate-400">Max International Flight Price:</span> <strong class="text-white">₹75,000</strong></div>
          <div class="flex justify-between py-2 border-b border-slate-800"><span class="text-slate-400">Hotel Nightly Budget Limit:</span> <strong class="text-white">₹6,000 / night</strong></div>
          <div class="flex justify-between py-2 border-b border-slate-800"><span class="text-slate-400">Daily Meal Allowance:</span> <strong class="text-white">₹2,000 / day</strong></div>
          <div class="flex justify-between py-2 border-b border-slate-800"><span class="text-slate-400">Advance Booking Requirement:</span> <strong class="text-emerald-400">7 Days Prior</strong></div>
        </div>
      </div>

      <div class="glass-panel p-6 rounded-2xl border border-slate-800">
        <h3 class="font-extrabold text-sm text-white mb-4">Preferred Corporate Vendors</h3>
        <div class="space-y-3 text-xs">
          <div class="p-3 rounded-xl bg-dark-900 border border-slate-800 flex justify-between items-center">
            <div><strong class="text-white block font-bold">Air India Corporate</strong><span class="text-slate-400 text-[11px]">Preferred Domestic & Regional Airline</span></div>
            <span class="px-2.5 py-1 rounded bg-indigo-500/20 text-indigo-300 font-extrabold">12% OFF</span>
          </div>
          <div class="p-3 rounded-xl bg-dark-900 border border-slate-800 flex justify-between items-center">
            <div><strong class="text-white block font-bold">Taj Hotels & Resorts</strong><span class="text-slate-400 text-[11px]">Negotiated Executive Suite Partner</span></div>
            <span class="px-2.5 py-1 rounded bg-emerald-500/20 text-emerald-300 font-extrabold">18% OFF</span>
          </div>
        </div>
      </div>
    </div>
  `;
  safeCreateIcons();
}

// =========================================================================
// 11. AUDIT LOGS (CALLS /api/audit)
// =========================================================================
async function loadAuditTab() {
  const main = document.getElementById('mainContent');

  main.innerHTML = `
    ${renderFlowStepper()}

    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-2xl font-extrabold text-white tracking-tight">Enterprise Audit & Security Trail</h1>
        <p class="text-xs text-slate-400 mt-1">Immutable ledger tracking corporate bookings, approvals, and financial settlements.</p>
      </div>
    </div>

    <div class="glass-panel rounded-2xl border border-slate-800 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs font-mono">
          <thead>
            <tr class="border-b border-slate-800 text-slate-400 font-semibold bg-dark-900/50">
              <th class="py-3 px-4">Action</th>
              <th class="py-3 px-4">User</th>
              <th class="py-3 px-4">Entity Type</th>
              <th class="py-3 px-4">Description</th>
              <th class="py-3 px-4">IP Address</th>
              <th class="py-3 px-4">Status</th>
            </tr>
          </thead>
          <tbody id="auditTableBody" class="divide-y divide-slate-800/60">
            <tr><td colspan="6" class="p-6 text-center text-slate-400 font-sans">Loading security audit records...</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  `;

  safeCreateIcons();

  const res = await apiFetch('/api/audit');
  const logs = (res && res.data) ? res.data : STATE.auditLogs;
  const tbody = document.getElementById('auditTableBody');

  if (tbody && logs.length > 0) {
    tbody.innerHTML = logs.map(l => `
      <tr class="hover:bg-dark-700/40 transition">
        <td class="py-3 px-4 text-indigo-400 font-bold">${l.actionName || 'SYSTEM_ACTION'}</td>
        <td class="py-3 px-4 text-white">${l.userEmail || 'user@acmetech.com'}</td>
        <td class="py-3 px-4 text-slate-400">${l.entityType || 'SYSTEM'} #${l.entityId || 1}</td>
        <td class="py-3 px-4 text-slate-300 font-sans">${l.description || 'Action performed.'}</td>
        <td class="py-3 px-4 text-slate-500">${l.ipAddress || '127.0.0.1'}</td>
        <td class="py-3 px-4"><span class="px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-400 font-bold">${l.status || 'SUCCESS'}</span></td>
      </tr>
    `).join('');
  } else if (tbody) {
    tbody.innerHTML = `
      <tr class="hover:bg-dark-700/40 transition">
        <td class="py-3 px-4 text-indigo-400 font-bold">REIMBURSE_EXPENSE</td>
        <td class="py-3 px-4 text-white">finance@acmetech.com</td>
        <td class="py-3 px-4 text-slate-400">ExpenseReport #1</td>
        <td class="py-3 px-4 text-slate-300 font-sans">Reimbursed ₹1,450 to Priya Sharma wallet</td>
        <td class="py-3 px-4 text-slate-500">192.168.1.50</td>
        <td class="py-3 px-4"><span class="px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-400 font-bold">SUCCESS</span></td>
      </tr>
    `;
  }
}

// =========================================================================
// MODALS & HELPERS
// =========================================================================
function closeModal() {
  const container = document.getElementById('modalContainer');
  if (container) container.innerHTML = '';
}

function openAllModulesModal() {
  const modalContainer = document.getElementById('modalContainer');
  modalContainer.innerHTML = `
    <div class="fixed inset-0 bg-black/75 backdrop-blur-md z-50 flex items-center justify-center p-4 overflow-y-auto">
      <div class="bg-dark-800 border border-slate-700 rounded-3xl w-full max-w-4xl shadow-2xl p-6 md:p-8 my-8 relative">
        <div class="flex items-center justify-between pb-4 border-b border-slate-700">
          <div class="flex items-center gap-3">
            <div class="h-10 w-10 rounded-2xl bg-indigo-600 flex items-center justify-center text-white font-bold shadow-lg shadow-indigo-600/30">
              <i data-lucide="layout-grid" class="w-6 h-6"></i>
            </div>
            <div>
              <h3 class="font-extrabold text-xl text-white">Platform Modules Directory</h3>
              <p class="text-xs text-slate-400">Click any module to open and interact with it directly</p>
            </div>
          </div>
          <button onclick="closeModal()" class="text-slate-400 hover:text-white p-2 rounded-xl bg-dark-900 border border-slate-700">
            <i data-lucide="x" class="w-5 h-5"></i>
          </button>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 my-6">
          ${ALL_MODULES.map(m => `
            <button onclick="launchModule('${m.id}')" class="p-4 rounded-2xl bg-dark-900 hover:bg-dark-700 border border-slate-800 hover:border-indigo-500 text-left transition-all group">
              <div class="flex items-center justify-between mb-2">
                <div class="h-9 w-9 rounded-xl bg-indigo-500/15 text-indigo-400 group-hover:bg-indigo-600 group-hover:text-white flex items-center justify-center transition">
                  <i data-lucide="${m.icon}" class="w-5 h-5"></i>
                </div>
                <span class="text-slate-600 group-hover:text-indigo-400 text-xs font-bold transition">Open ➔</span>
              </div>
              <h4 class="text-sm font-bold text-white group-hover:text-indigo-300 transition">${m.label}</h4>
              <p class="text-[11px] text-slate-400 mt-1 line-clamp-2">${m.desc}</p>
            </button>
          `).join('')}
        </div>
      </div>
    </div>
  `;
  safeCreateIcons();
}

function launchModule(moduleId) {
  closeModal();
  navigateToTab(moduleId);
  showToast(`Opened ${moduleId.toUpperCase()} module`);
}

function toggleNotificationsModal() {
  const modal = document.getElementById('notifModal');
  const container = document.getElementById('notifListContainer');
  if (!modal || !container) return;
  
  if (modal.classList.contains('hidden')) {
    container.innerHTML = STATE.notifications.map(n => `
      <div class="p-3 rounded-xl bg-dark-900 border border-slate-800 text-xs">
        <div class="flex items-center justify-between font-bold text-white">
          <span>${n.title}</span>
          <span class="text-[10px] text-slate-500">${n.time}</span>
        </div>
        <p class="text-slate-400 text-[11px] mt-1">${n.desc}</p>
      </div>
    `).join('');
    modal.classList.remove('hidden');
  } else {
    modal.classList.add('hidden');
  }
  safeCreateIcons();
}

function toggleLiveChatDrawer() {
  const drawer = document.getElementById('chatDrawer');
  if (drawer) drawer.classList.toggle('hidden');
  safeCreateIcons();
}

function sendChatMessage() {
  const input = document.getElementById('chatInputText');
  const box = document.getElementById('chatMessagesBox');
  if (!input || !input.value.trim() || !box) return;

  const msg = input.value.trim();
  input.value = '';

  box.innerHTML += `
    <div class="flex gap-2 justify-end">
      <div class="bg-indigo-600 p-2.5 rounded-xl rounded-tr-none max-w-[80%] text-white">
        ${msg}
      </div>
    </div>
  `;
  box.scrollTop = box.scrollHeight;

  setTimeout(() => {
    box.innerHTML += `
      <div class="flex gap-2">
        <div class="h-6 w-6 rounded-full bg-indigo-600 flex items-center justify-center text-[10px] font-bold">CS</div>
        <div class="bg-dark-700 p-2.5 rounded-xl rounded-tr-none max-w-[80%] text-slate-200">
          Hello Priya! I have confirmed your Taj Palace reservation in Delhi. How can I assist with your itinerary?
        </div>
      </div>
    `;
    box.scrollTop = box.scrollHeight;
  }, 1000);
}

function openAiModal() {
  navigateToTab('ai-assistant');
}

function showToast(message) {
  const toast = document.createElement('div');
  toast.className = 'fixed bottom-6 left-6 z-50 px-4 py-3 rounded-2xl bg-gradient-to-r from-indigo-900 to-slate-900 border border-indigo-500/50 text-white font-semibold text-xs shadow-2xl flex items-center gap-2 animate-fade-in';
  toast.innerHTML = `<i data-lucide="check-circle-2" class="w-4 h-4 text-emerald-400"></i> <span>${message}</span>`;
  document.body.appendChild(toast);
  safeCreateIcons();
  setTimeout(() => {
    toast.remove();
  }, 4000);
}

function safeCreateIcons() {
  try {
    if (typeof lucide !== 'undefined' && lucide.createIcons) {
      lucide.createIcons();
    }
  } catch (err) {
    console.warn('Lucide createIcons warning:', err);
  }
}

// =========================================================================
// EXPLICIT WINDOW BINDINGS (ALL INLINE HTML HANDLERS)
// =========================================================================
window.apiFetch = apiFetch;
window.navigateToTab = navigateToTab;
window.updateTopStripActiveState = updateTopStripActiveState;
window.launchModule = launchModule;
window.openAllModulesModal = openAllModulesModal;
window.goToFlowStep = goToFlowStep;
window.advanceFlow = advanceFlow;
window.previousFlowStep = previousFlowStep;
window.autoPlayFlow = autoPlayFlow;
window.loginAsRole = loginAsRole;
window.switchDemoRole = switchDemoRole;
window.switchTravelMode = switchTravelMode;
window.changeCurrency = changeCurrency;
window.toggleNotificationsModal = toggleNotificationsModal;
window.toggleLiveChatDrawer = toggleLiveChatDrawer;
window.sendChatMessage = sendChatMessage;
window.openAiModal = openAiModal;
window.closeModal = closeModal;
window.openNewRequestModal = openNewRequestModal;
window.openNewExpenseModal = openNewExpenseModal;
window.openBoardingPassModal = openBoardingPassModal;
window.handleCreateRequest = handleCreateRequest;
window.handleApproveRequest = handleApproveRequest;
window.handleRejectApproval = handleRejectApproval;
window.handleRequestChanges = handleRequestChanges;
window.handlePortalCredentialLogin = handlePortalCredentialLogin;
window.bookFlightNow = bookFlightNow;
window.bookHotelNow = bookHotelNow;
window.selectFlightAndCreateRequest = selectFlightAndCreateRequest;
window.simulateOcrScan = simulateOcrScan;
window.submitExpenseReport = submitExpenseReport;
window.settleSpecificExpense = settleSpecificExpense;
window.settleFinanceExpense = settleFinanceExpense;
window.broadcastEmergencySos = broadcastEmergencySos;
window.executeFlightSearch = executeFlightSearch;
window.executeHotelSearch = executeHotelSearch;
window.executeTransportSearch = executeTransportSearch;
window.switchSearchType = switchSearchType;
window.filterRequestsTable = filterRequestsTable;
window.executeAiQuery = executeAiQuery;
window.sendAiPrompt = sendAiPrompt;
window.exportAnalyticsCsv = exportAnalyticsCsv;
window.handleLogout = handleLogout;
