import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import type { PortalConfig } from '../auth/portals';

export interface NavItem {
  label: string;
  path: string;
}

export default function DashboardLayout({
  config,
  navItems,
  children,
}: {
  config: PortalConfig;
  navItems: (string | NavItem)[];
  children: React.ReactNode;
}) {
  const location = useLocation();
  const { logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    window.location.href = config.loginPath;
  };

  return (
    <div className="min-h-screen bg-slate-50 flex">
      <aside className="w-64 bg-slate-900 text-white flex flex-col">
        <div className="p-6 border-b border-slate-700">
          <h2 className="font-bold text-lg">{config.title}</h2>
          <p className="text-slate-400 text-sm">{config.subtitle}</p>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          {navItems.map((item) => {
            const label = typeof item === 'string' ? item : item.label;
            const path = typeof item === 'string' ? config.dashboardPath : item.path;
            const active = location.pathname === path;
            return (
              <Link
                key={label}
                to={path}
                className={`block px-3 py-2 rounded-lg text-sm ${active ? 'bg-white/10 text-white font-medium' : 'text-slate-400 hover:text-white hover:bg-white/5'}`}
              >
                {label}
              </Link>
            );
          })}
        </nav>
        <div className="p-4 border-t border-slate-700">
          <button
            onClick={handleLogout}
            className="w-full px-3 py-2 text-sm text-red-400 hover:text-red-300 hover:bg-white/5 rounded-lg text-left"
          >
            Logout
          </button>
        </div>
      </aside>
      <main className="flex-1 p-8">
        {children}
      </main>
    </div>
  );
}
