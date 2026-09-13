import { useAuth } from '../auth/AuthContext';
import type { PortalConfig } from '../auth/portals';

export default function DashboardLayout({
  config,
  navItems,
  children,
}: {
  config: PortalConfig;
  navItems: string[];
  children: React.ReactNode;
}) {
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
          {navItems.map((item) => (
            <div
              key={item}
              className={`px-3 py-2 rounded-lg text-sm ${item === 'Dashboard' ? 'bg-white/10 text-white font-medium' : 'text-slate-400 hover:text-white hover:bg-white/5'}`}
            >
              {item}
            </div>
          ))}
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
