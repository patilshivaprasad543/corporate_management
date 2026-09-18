/* CorporateTravel360: six user-facing portals; Travel Agent is part of Support. */
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
    /* Remove legacy vendor/travel-manager/super-admin personas from the UI. */
    delete DEMO_USERS.ROLE_VENDOR;
    delete DEMO_USERS.ROLE_TRAVEL_MANAGER;
    delete DEMO_USERS.ROLE_SUPER_ADMIN;

    /* The former Travel Agent persona is now represented by Support. */
    if (DEMO_USERS.ROLE_SUPPORT) {
      DEMO_USERS.ROLE_SUPPORT.portalLabel = 'Support';
      DEMO_USERS.ROLE_SUPPORT.designation = 'Travel Agent & 24/7 Global Traveler Support';
      DEMO_USERS.ROLE_SUPPORT.department = 'Travel Operations & Customer Success';
      DEMO_USERS.ROLE_SUPPORT.avatar = 'TA';
    }

    DEMO_USERS.ROLE_COMPANY_ADMIN.portalLabel = 'Admin';
    DEMO_USERS.ROLE_EMPLOYEE.portalLabel = 'Employee';
    DEMO_USERS.ROLE_APPROVER.portalLabel = 'Manager';
    DEMO_USERS.ROLE_HR.portalLabel = 'HR';
    DEMO_USERS.ROLE_FINANCE.portalLabel = 'Finance';
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

    /* Support owns both traveler-care and travel-agent operational work. */
    ROLE_NAVS.ROLE_SUPPORT = [
      { id: 'login-portal', label: '🔐 Login Portal', icon: 'shield-check' },
      { id: 'dashboard', label: 'Support Command Center', icon: 'layout-dashboard' },
      { id: 'requests', label: 'Travel Service Queue', icon: 'briefcase-business', badge: '1' },
      { id: 'itinerary', label: 'Traveler Itineraries', icon: 'calendar-days' },
      { id: 'search', label: 'Travel Agent Desk', icon: 'plane' },
      { id: 'risk', label: 'Emergency & Duty of Care', icon: 'shield-alert' },
      { id: 'expenses', label: 'Travel Expense Assistance', icon: 'receipt' },
      { id: 'analytics', label: 'Travel Operations', icon: 'bar-chart-3' }
    ];
  }

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
      ROLE_SUPPORT: 'Support • Travel Agent'
    };
    Array.from(selector.options).forEach(function (option) {
      if (labels[option.value]) option.textContent = labels[option.value];
    });
  }

  window.CORPORATE_PORTALS = Object.freeze([
    { key: 'ROLE_COMPANY_ADMIN', label: 'Admin', description: 'Company administration, policies, approvals, budgets and audit.' },
    { key: 'ROLE_EMPLOYEE', label: 'Employee', description: 'Travel requests, bookings, itineraries, expenses and personal travel assistance.' },
    { key: 'ROLE_APPROVER', label: 'Manager', description: 'Team travel approvals, team requests, expenses and budgets.' },
    { key: 'ROLE_HR', label: 'HR', description: 'Employee travel records, people operations, budgets and duty of care.' },
    { key: 'ROLE_FINANCE', label: 'Finance', description: 'Expense verification, reimbursements, budgets and financial reporting.' },
    { key: 'ROLE_SUPPORT', label: 'Support', description: 'Travel-agent operations, booking coordination, traveler assistance, documents, incidents and alerts.' }
  ]);

  /* ----------------------------------------------------------------------
     Premium UI layer: splash, safe 3D tilt, ripple feedback, scroll glow,
     animated counters and accessible motion controls.
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

  /* ----------------------------------------------------------------------
     Visual parity layer: premium six-portal launcher inspired by the
     approved UI mockup. It stays data-free and routes through the existing
     SPA role/navigation functions.
     ---------------------------------------------------------------------- */
  function bootPortalExperience() {
    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    function decorateBrand() {
      const brand = document.querySelector('header span.font-extrabold');
      if (brand) brand.textContent = 'CorporateTravel360';
      const company = document.querySelector('header p.text-\\[11px\\]');
      if (company) company.textContent = 'Enterprise Travel & Expense Platform';
    }

    function portalIcon(role) {
      return {
        ROLE_COMPANY_ADMIN: 'settings-2',
        ROLE_EMPLOYEE: 'user-round',
        ROLE_APPROVER: 'users-round',
        ROLE_HR: 'heart-handshake',
        ROLE_FINANCE: 'wallet-cards',
        ROLE_SUPPORT: 'headphones'
      }[role] || 'plane';
    }

    function portalAccent(role) {
      return {
        ROLE_COMPANY_ADMIN: 'admin',
        ROLE_EMPLOYEE: 'employee',
        ROLE_APPROVER: 'manager',
        ROLE_HR: 'hr',
        ROLE_FINANCE: 'finance',
        ROLE_SUPPORT: 'support'
      }[role] || 'employee';
    }

    function renderPortalLauncher() {
      const main = document.getElementById('mainContent');
      if (!main || main.dataset.portalLauncherReady === 'true') return;

      const active = document.querySelector('#strip-login-portal.bg-gradient-to-r') ||
        (typeof STATE !== 'undefined' && STATE.activeTab === 'login-portal');
      if (!active && !(typeof STATE !== 'undefined' && STATE.activeTab === 'login-portal')) return;

      const launcher = document.createElement('section');
      launcher.className = 'ct360-portal-launcher glass-panel hero-3d';
      launcher.dataset.portalLauncher = 'true';
      launcher.innerHTML =
        '<div class="ct360-launcher-header">' +
          '<div>' +
            '<span class="ct360-eyebrow">CORPORATETRAVEL360</span>' +
            '<h2>Select your portal</h2>' +
            '<p>Choose the workspace that matches your responsibility.</p>' +
          '</div>' +
          '<div class="ct360-launcher-badge"><span></span> Secure enterprise access</div>' +
        '</div>' +
        '<div class="ct360-portal-grid">' +
          window.CORPORATE_PORTALS.map(function (portal) {
            return '<button type="button" class="ct360-portal-card ' + portalAccent(portal.key) + '" data-portal-role="' + portal.key + '">' +
              '<span class="ct360-portal-icon"><i data-lucide="' + portalIcon(portal.key) + '"></i></span>' +
              '<span class="ct360-portal-copy"><strong>' + portal.label + '</strong><small>' + portal.description + '</small></span>' +
              '<span class="ct360-portal-arrow">↗</span>' +
            '</button>';
          }).join('') +
        '</div>' +
        '<div class="ct360-launcher-footer"><span>6 secure portals</span><span>•</span><span>Travel Agent operations are part of Support</span><span>•</span><span>Role-based access</span></div>';

      launcher.addEventListener('click', function (event) {
        const card = event.target.closest('[data-portal-role]');
        if (!card) return;
        const role = card.getAttribute('data-portal-role');
        if (typeof switchDemoRole === 'function') switchDemoRole(role);
        if (typeof navigateToTab === 'function') window.setTimeout(function () { navigateToTab('dashboard'); }, 80);
      });

      main.prepend(launcher);
      main.dataset.portalLauncherReady = 'true';
      launcher.querySelectorAll('[data-tilt]').forEach(function (el) { el.dataset.tilt = 'true'; });
      if (window.lucide) window.lucide.createIcons();
    }

    function observePortalView() {
      const main = document.getElementById('mainContent');
      if (!main) return;
      const refresh = function () {
        const launcher = main.querySelector('[data-portal-launcher]');
        const isLogin = typeof STATE !== 'undefined' && STATE.activeTab === 'login-portal';
        if (isLogin && !launcher) {
          main.dataset.portalLauncherReady = 'false';
          renderPortalLauncher();
        } else if (!isLogin && launcher) {
          launcher.remove();
          main.dataset.portalLauncherReady = 'false';
        }
      };
      new MutationObserver(refresh).observe(main, { childList: true, subtree: true });
      window.setInterval(refresh, 700);
      refresh();
    }

    function addPortalMotion() {
      if (reduceMotion) return;
      document.addEventListener('pointermove', function (event) {
        const card = event.target.closest('.ct360-portal-card');
        if (!card || window.innerWidth < 768) return;
        const rect = card.getBoundingClientRect();
        const x = (event.clientX - rect.left) / rect.width - 0.5;
        const y = (event.clientY - rect.top) / rect.height - 0.5;
        card.style.setProperty('--portal-rx', (y * -5).toFixed(2) + 'deg');
        card.style.setProperty('--portal-ry', (x * 5).toFixed(2) + 'deg');
      }, { passive: true });
      document.addEventListener('pointerout', function (event) {
        const card = event.target.closest('.ct360-portal-card');
        if (card && !card.contains(event.relatedTarget)) {
          card.style.removeProperty('--portal-rx');
          card.style.removeProperty('--portal-ry');
        }
      }, { passive: true });
    }

    decorateBrand();
    observePortalView();
    addPortalMotion();

    if (!document.getElementById('ct360-portal-style')) {
      const style = document.createElement('style');
      style.id = 'ct360-portal-style';
      style.textContent =
        '.ct360-portal-launcher{padding:clamp(20px,3vw,34px);overflow:hidden;background:linear-gradient(145deg,rgba(12,20,44,.94),rgba(18,14,44,.88));border-color:rgba(129,140,248,.28);box-shadow:0 30px 80px -35px rgba(0,0,0,.85),0 0 55px -20px rgba(99,102,241,.45)}' +
        '.ct360-launcher-header{display:flex;align-items:flex-start;justify-content:space-between;gap:20px;margin-bottom:24px}' +
        '.ct360-eyebrow{font-size:10px;font-weight:800;letter-spacing:.18em;color:#818cf8}' +
        '.ct360-launcher-header h2{margin:6px 0 4px;font-size:clamp(24px,3vw,34px);font-weight:800;letter-spacing:-.04em;color:#fff}' +
        '.ct360-launcher-header p{margin:0;color:#94a3b8;font-size:13px}' +
        '.ct360-launcher-badge{display:flex;align-items:center;gap:8px;padding:9px 12px;border:1px solid rgba(52,211,153,.22);border-radius:999px;background:rgba(16,185,129,.08);color:#86efac;font-size:10px;font-weight:700;white-space:nowrap}' +
        '.ct360-launcher-badge span{width:7px;height:7px;border-radius:50%;background:#34d399;box-shadow:0 0 12px #34d399}' +
        '.ct360-portal-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:14px;perspective:1200px}' +
        '.ct360-portal-card{position:relative;display:flex;align-items:center;gap:14px;min-height:128px;padding:18px;border:1px solid rgba(148,163,184,.14);border-radius:20px;text-align:left;color:#fff;overflow:hidden;cursor:pointer;transform:perspective(900px) rotateX(var(--portal-rx,0deg)) rotateY(var(--portal-ry,0deg)) translateY(0);transform-style:preserve-3d;transition:transform .35s cubic-bezier(.16,1,.3,1),border-color .25s ease,box-shadow .35s ease,background .25s ease}' +
        '.ct360-portal-card:before{content:"";position:absolute;inset:-45%;background:radial-gradient(circle at 20% 20%,rgba(255,255,255,.14),transparent 30%);transform:translateZ(-1px);pointer-events:none}' +
        '.ct360-portal-card:hover{transform:perspective(900px) rotateX(var(--portal-rx,-1deg)) rotateY(var(--portal-ry,1deg)) translateY(-7px) translateZ(8px);border-color:rgba(129,140,248,.55);box-shadow:0 24px 45px -20px rgba(0,0,0,.75),0 0 35px -12px rgba(99,102,241,.42)}' +
        '.ct360-portal-card.admin{background:linear-gradient(145deg,rgba(30,64,175,.34),rgba(15,23,42,.88))}.ct360-portal-card.employee{background:linear-gradient(145deg,rgba(6,78,59,.34),rgba(15,23,42,.88))}.ct360-portal-card.manager{background:linear-gradient(145deg,rgba(120,53,15,.30),rgba(15,23,42,.88))}.ct360-portal-card.hr{background:linear-gradient(145deg,rgba(79,70,229,.30),rgba(15,23,42,.88))}.ct360-portal-card.finance{background:linear-gradient(145deg,rgba(8,145,178,.28),rgba(15,23,42,.88))}.ct360-portal-card.support{background:linear-gradient(145deg,rgba(190,24,93,.28),rgba(15,23,42,.88))}' +
        '.ct360-portal-icon{position:relative;z-index:1;width:52px;height:52px;flex:0 0 52px;display:grid;place-items:center;border-radius:16px;background:linear-gradient(145deg,rgba(255,255,255,.18),rgba(255,255,255,.04));border:1px solid rgba(255,255,255,.16);box-shadow:inset 0 1px 0 rgba(255,255,255,.28),0 12px 25px -14px rgba(0,0,0,.9);transform:translateZ(22px);color:#c4b5fd}' +
        '.ct360-portal-icon svg{width:24px;height:24px}.ct360-portal-copy{position:relative;z-index:1;display:flex;flex-direction:column;gap:5px;min-width:0;transform:translateZ(18px)}' +
        '.ct360-portal-copy strong{font-size:16px;font-weight:800;letter-spacing:-.02em}.ct360-portal-copy small{font-size:10px;line-height:1.45;color:#94a3b8}.ct360-portal-arrow{position:absolute;right:14px;top:12px;font-size:18px;color:#818cf8;transform:translateZ(24px);transition:transform .25s ease}.ct360-portal-card:hover .ct360-portal-arrow{transform:translateZ(28px) translate(2px,-2px)}' +
        '.ct360-launcher-footer{display:flex;flex-wrap:wrap;gap:8px;margin-top:20px;color:#64748b;font-size:10px;font-weight:700}.ct360-launcher-footer span:nth-child(odd){color:#94a3b8}' +
        '@media(max-width:900px){.ct360-portal-grid{grid-template-columns:repeat(2,minmax(0,1fr))}}' +
        '@media(max-width:620px){.ct360-launcher-header{flex-direction:column}.ct360-launcher-badge{white-space:normal}.ct360-portal-grid{grid-template-columns:1fr}.ct360-portal-card{min-height:104px}.ct360-portal-copy small{font-size:10px}}' +
        '@media(prefers-reduced-motion:reduce){.ct360-portal-card{transform:none!important;transition:none}.ct360-portal-arrow{transition:none}}';
      document.head.appendChild(style);
    }
  }

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', bootPortalExperience, { once: true });
  else bootPortalExperience();

})();