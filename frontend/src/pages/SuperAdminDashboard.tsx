import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import { useAuth } from '../auth/AuthContext';
import { PORTALS } from '../auth/portals';
import * as companiesApi from '../api/companiesApi';

const SUPER_ADMIN_NAV = [
  { label: 'Dashboard', path: '/super-admin/dashboard' },
  { label: 'Organizations', path: '/super-admin/companies' },
  { label: 'Users', path: '/super-admin/dashboard' },
  { label: 'Audit Logs', path: '/super-admin/dashboard' },
];

export default function SuperAdminDashboard() {
  const { user } = useAuth();
  const { data: stats, isLoading } = useQuery({
    queryKey: ['system-stats'],
    queryFn: companiesApi.getSystemStats,
  });

  const cards = [
    { label: 'Total Companies', value: stats?.totalCompanies },
    { label: 'Active Companies', value: stats?.activeCompanies },
    { label: 'Total Users', value: stats?.totalUsers },
    { label: 'Departments', value: stats?.totalDepartments },
    { label: 'Travel Requests', value: stats?.totalTravelRequests },
    { label: 'Bookings', value: stats?.totalBookings },
  ];

  return (
    <DashboardLayout config={PORTALS.SUPER_ADMIN} navItems={SUPER_ADMIN_NAV}>
      <div className="max-w-6xl">
        <h1 className="text-2xl font-bold text-slate-900 mb-2">Welcome, {user?.fullName}</h1>
        <p className="text-slate-500 mb-8">Super Administration Portal — system overview</p>

        {isLoading ? (
          <p className="text-slate-500">Loading statistics...</p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
            {cards.map((card) => (
              <div key={card.label} className="bg-white rounded-xl border border-slate-200 p-5 shadow-sm">
                <p className="text-sm text-slate-500">{card.label}</p>
                <p className="text-3xl font-bold text-slate-900 mt-1">{card.value ?? '—'}</p>
              </div>
            ))}
          </div>
        )}

        <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-slate-900">Company Management</h2>
            <Link
              to="/super-admin/companies"
              className="px-4 py-2 bg-rose-600 text-white text-sm font-medium rounded-lg hover:bg-rose-700"
            >
              Manage Companies
            </Link>
          </div>
          <p className="text-slate-500 text-sm">
            Create, activate, and deactivate companies. All statistics above are loaded from live database values.
          </p>
        </div>
      </div>
    </DashboardLayout>
  );
}
