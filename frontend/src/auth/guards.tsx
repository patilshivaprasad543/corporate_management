import { Navigate } from 'react-router-dom';
import { useAuth, userMatchesPortal } from './AuthContext';
import { PORTALS } from './portals';
import type { PortalConfig } from './portals';
import type { PortalType } from '../types/auth';

export function PortalRoute({ portal, children }: { portal: PortalType; children: React.ReactNode }) {
  const { user, isAuthenticated } = useAuth();
  const config = PORTALS[portal];

  if (!isAuthenticated || !user) {
    return <Navigate to={config.loginPath} replace />;
  }

  if (!userMatchesPortal(user, portal)) {
    const userPortal = PORTALS[user.portal];
    return <Navigate to={userPortal.dashboardPath} replace />;
  }

  return <>{children}</>;
}

export function LoginRoute({ config, children }: { config: PortalConfig; children: React.ReactNode }) {
  const { user, isAuthenticated } = useAuth();

  if (isAuthenticated && user && userMatchesPortal(user, config.portal)) {
    return <Navigate to={config.dashboardPath} replace />;
  }

  return <>{children}</>;
}
