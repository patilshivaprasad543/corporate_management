import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import { LoginRoute, PortalRoute } from './auth/guards';
import { PORTALS, EMPLOYEE_NAV, MANAGER_NAV } from './auth/portals';
import PortalLoginPage from './components/PortalLoginPage';
import RoleDashboard from './pages/RoleDashboard';
import RegisterPage from './pages/RegisterPage';
import VerifyOtpPage from './pages/VerifyOtpPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/employee/login" replace />} />

          {/* Employee */}
          <Route path="/employee/login" element={<LoginRoute config={PORTALS.EMPLOYEE}><PortalLoginPage config={PORTALS.EMPLOYEE} /></LoginRoute>} />
          <Route path="/employee/dashboard" element={<PortalRoute portal="EMPLOYEE"><RoleDashboard config={PORTALS.EMPLOYEE} navItems={EMPLOYEE_NAV} /></PortalRoute>} />

          {/* Manager */}
          <Route path="/manager/login" element={<LoginRoute config={PORTALS.MANAGER}><PortalLoginPage config={PORTALS.MANAGER} /></LoginRoute>} />
          <Route path="/manager/dashboard" element={<PortalRoute portal="MANAGER"><RoleDashboard config={PORTALS.MANAGER} navItems={MANAGER_NAV} /></PortalRoute>} />

          {/* Travel Admin */}
          <Route path="/travel-admin/login" element={<LoginRoute config={PORTALS.TRAVEL_ADMIN}><PortalLoginPage config={PORTALS.TRAVEL_ADMIN} /></LoginRoute>} />
          <Route path="/travel-admin/dashboard" element={<PortalRoute portal="TRAVEL_ADMIN"><RoleDashboard config={PORTALS.TRAVEL_ADMIN} navItems={['Dashboard', 'Bookings', 'Policies', 'Vendors', 'Reports']} /></PortalRoute>} />

          {/* Finance */}
          <Route path="/finance/login" element={<LoginRoute config={PORTALS.FINANCE}><PortalLoginPage config={PORTALS.FINANCE} /></LoginRoute>} />
          <Route path="/finance/dashboard" element={<PortalRoute portal="FINANCE"><RoleDashboard config={PORTALS.FINANCE} navItems={['Dashboard', 'Expenses', 'Reimbursements', 'Payments', 'Reports', 'Audit']} /></PortalRoute>} />

          {/* Travel Agent */}
          <Route path="/travel-agent/login" element={<LoginRoute config={PORTALS.TRAVEL_AGENT}><PortalLoginPage config={PORTALS.TRAVEL_AGENT} /></LoginRoute>} />
          <Route path="/travel-agent/dashboard" element={<PortalRoute portal="TRAVEL_AGENT"><RoleDashboard config={PORTALS.TRAVEL_AGENT} navItems={['Dashboard', 'Bookings', 'Itineraries', 'Support', 'Vendors']} /></PortalRoute>} />

          {/* Vendor */}
          <Route path="/vendor/login" element={<LoginRoute config={PORTALS.VENDOR}><PortalLoginPage config={PORTALS.VENDOR} /></LoginRoute>} />
          <Route path="/vendor/dashboard" element={<PortalRoute portal="VENDOR"><RoleDashboard config={PORTALS.VENDOR} navItems={['Dashboard', 'Bookings', 'Invoices', 'Profile']} /></PortalRoute>} />

          {/* Super Admin */}
          <Route path="/super-admin/login" element={<LoginRoute config={PORTALS.SUPER_ADMIN}><PortalLoginPage config={PORTALS.SUPER_ADMIN} /></LoginRoute>} />
          <Route path="/super-admin/dashboard" element={<PortalRoute portal="SUPER_ADMIN"><RoleDashboard config={PORTALS.SUPER_ADMIN} navItems={['Dashboard', 'Organizations', 'Users', 'System Config', 'Audit Logs']} /></PortalRoute>} />

          {/* Registration */}
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/verify-otp" element={<VerifyOtpPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
