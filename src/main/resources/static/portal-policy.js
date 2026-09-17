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

  const selector = document.getElementById('roleSelector');
  if (selector) {
    Array.from(selector.options).forEach(function (option) {
      if (!visibleRoles.has(option.value)) option.remove();
    });
    const labels = {
      ROLE_COMPANY_ADMIN: 'Admin', ROLE_EMPLOYEE: 'Employee', ROLE_APPROVER: 'Manager',
      ROLE_HR: 'HR', ROLE_FINANCE: 'Finance', ROLE_SUPPORT: 'Support'
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

  /* ----------------------------------------------------------------------
     Premium UI layer: splash, safe 3D tilt, ripple feedback, scroll glow,
     animated counters and accessible motion controls. No business logic is
     changed by this layer.
     ---------------------------------------------------------------------- */
  function bootPremiumUI() {
    if (document.documentElement.dataset.premiumUi === 'true') return;
    document.documentElement.dataset.premiumUi = 'true';

    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    if (!document.querySelector('.ct360-splash')) {
      const splash = document.createElement('div');
      splash.className = 'ct360-splash';
      splash.setAttribute('aria-label', 'Loading Corporate Travel 360');
      splash.innerHTML = '<div class="ct360-splash-orb ct360-splash-orb-a"></div>' +
        '<div class="ct360-splash-orb ct360-splash-orb-b"></div>' +
        '<div class="ct360-splash-core"><div class="ct360-logo-mark">✦</div>' +
        '<div class="ct360-splash-title">CorporateTravel<span>360</span></div>' +
        '<div class="ct360-splash-subtitle">Enterprise Travel & Expense Platform</div>' +
        '<div class="ct360-splash-loader"><i></i></div></div>';
      document.body.prepend(splash);
      window.setTimeout(function () { splash.classList.add('is-hidden'); }, reduceMotion ? 250 : 1200);
      window.setTimeout(function () { splash.remove(); }, reduceMotion ? 500 : 1800);
    }

    const candidates = document.querySelectorAll('.glass-panel, .card-3d, .flow-step-node, .module-card, .stat-card');
    candidates.forEach(function (el) {
      if (!el.classList.contains('card-3d')) el.classList.add('card-3d');
      if (!el.querySelector(':scope > .card-3d-content')) {
        const children = Array.from(el.childNodes);
        if (children.length) {
          const wrap = document.createElement('div');
          wrap.className = 'card-3d-content';
          children.forEach(function (child) { wrap.appendChild(child); });
          el.appendChild(wrap);
        }
      }
    });

    if (!reduceMotion) {
      document.addEventListener('pointermove', function (event) {
        const card = event.target.closest('.card-3d[data-tilt], .glass-panel[data-tilt]');
        if (!card || window.innerWidth < 768) return;
        const rect = card.getBoundingClientRect();
        if (rect.width < 80 || rect.height < 60) return;
        const x = (event.clientX - rect.left) / rect.width - 0.5;
        const y = (event.clientY - rect.top) / rect.height - 0.5;
        card.style.setProperty('--tilt-x', (y * -3.2).toFixed(2) + 'deg');
        card.style.setProperty('--tilt-y', (x * 3.2).toFixed(2) + 'deg');
      }, { passive: true });
      document.addEventListener('pointerout', function (event) {
        const card = event.target.closest('.card-3d[data-tilt], .glass-panel[data-tilt]');
        if (card && !card.contains(event.relatedTarget)) {
          card.style.removeProperty('--tilt-x');
          card.style.removeProperty('--tilt-y');
        }
      }, { passive: true });
    }

    document.addEventListener('pointerdown', function (event) {
      const target = event.target.closest('button, [role="button"], .btn-gradient-primary');
      if (!target || reduceMotion) return;
      const rect = target.getBoundingClientRect();
      const ripple = document.createElement('span');
      ripple.className = 'ct360-ripple';
      ripple.style.left = (event.clientX - rect.left) + 'px';
      ripple.style.top = (event.clientY - rect.top) + 'px';
      target.appendChild(ripple);
      window.setTimeout(function () { ripple.remove(); }, 650);
    }, { passive: true });

    let progress = document.querySelector('.ct360-scroll-progress');
    if (!progress) {
      progress = document.createElement('div');
      progress.className = 'ct360-scroll-progress';
      document.body.appendChild(progress);
    }
    const updateProgress = function () {
      const max = document.documentElement.scrollHeight - window.innerHeight;
      progress.style.transform = 'scaleX(' + (max > 0 ? window.scrollY / max : 0) + ')';
    };
    window.addEventListener('scroll', updateProgress, { passive: true });
    updateProgress();

    document.querySelectorAll('[data-animate-number]').forEach(function (el) {
      if (el.dataset.animated === 'true') return;
      const target = Number(String(el.textContent).replace(/[^0-9.-]/g, ''));
      if (!Number.isFinite(target)) return;
      el.dataset.animated = 'true';
      if (reduceMotion) return;
      const start = performance.now();
      const duration = 850;
      const tick = function (now) {
        const p = Math.min(1, (now - start) / duration);
        const eased = 1 - Math.pow(1 - p, 3);
        el.textContent = Math.round(target * eased).toLocaleString('en-IN');
        if (p < 1) requestAnimationFrame(tick);
      };
      el.textContent = '0';
      requestAnimationFrame(tick);
    });
  }

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', bootPremiumUI, { once: true });
  else bootPremiumUI();
})();
