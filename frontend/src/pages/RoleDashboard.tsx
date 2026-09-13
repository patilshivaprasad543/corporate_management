import DashboardLayout from '../components/DashboardLayout';
import { useAuth } from '../auth/AuthContext';
import type { PortalConfig } from '../auth/portals';

export default function RoleDashboard({ config, navItems }: { config: PortalConfig; navItems: string[] }) {
  const { user } = useAuth();

  return (
    <DashboardLayout config={config} navItems={navItems}>
      <div className="max-w-4xl">
        <h1 className="text-2xl font-bold text-slate-900 mb-2">Welcome, {user?.fullName}</h1>
        <p className="text-slate-500 mb-8">{config.subtitle} Dashboard</p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
            <h3 className="text-sm font-medium text-slate-500 uppercase tracking-wide">Account</h3>
            <dl className="mt-4 space-y-2 text-sm">
              <div className="flex justify-between"><dt className="text-slate-500">Email</dt><dd className="font-medium">{user?.email}</dd></div>
              <div className="flex justify-between"><dt className="text-slate-500">Username</dt><dd className="font-medium">{user?.username}</dd></div>
              <div className="flex justify-between"><dt className="text-slate-500">Organization</dt><dd className="font-medium">{user?.organizationName || '—'}</dd></div>
              <div className="flex justify-between"><dt className="text-slate-500">Role</dt><dd className="font-medium">{user?.roles.join(', ')}</dd></div>
            </dl>
          </div>
          <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-6">
            <h3 className="text-sm font-medium text-slate-500 uppercase tracking-wide">Portal Access</h3>
            <p className="mt-4 text-2xl font-bold text-slate-900">{config.portal}</p>
            <p className="text-slate-500 text-sm mt-2">Authenticated via separate portal login with backend role validation.</p>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
