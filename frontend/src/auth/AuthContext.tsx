import { createContext, useContext, useState, useCallback } from 'react';
import type { ReactNode } from 'react';
import type { AuthUser, LoginRequest, PortalType } from '../types/auth';
import * as authApi from '../api/authApi';
import { setTokens, clearTokens } from '../api/axios';

interface AuthContextType {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (request: LoginRequest) => Promise<AuthUser>;
  logout: () => Promise<void>;
  setUser: (user: AuthUser | null) => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

function loadStoredUser(): AuthUser | null {
  const raw = sessionStorage.getItem('authUser');
  return raw ? JSON.parse(raw) : null;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUserState] = useState<AuthUser | null>(loadStoredUser);

  const setUser = useCallback((u: AuthUser | null) => {
    setUserState(u);
    if (u) sessionStorage.setItem('authUser', JSON.stringify(u));
    else sessionStorage.removeItem('authUser');
  }, []);

  const login = useCallback(async (request: LoginRequest) => {
    const response = await authApi.login(request);
    setTokens(response.accessToken, response.refreshToken);
    const authUser: AuthUser = {
      userId: response.userId,
      username: response.username,
      email: response.email,
      fullName: response.fullName,
      organizationId: response.organizationId,
      organizationName: response.organizationName,
      roles: Array.from(response.roles),
      portal: request.portal,
      emailVerified: response.emailVerified,
    };
    setUser(authUser);
    return authUser;
  }, [setUser]);

  const logout = useCallback(async () => {
    const refresh = sessionStorage.getItem('refreshToken');
    try {
      await authApi.logout(refresh);
    } catch {
      // ignore
    }
    clearTokens();
    setUser(null);
  }, [setUser]);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout, setUser }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

export function userHasRole(user: AuthUser | null, role: string): boolean {
  return user?.roles.includes(role) ?? false;
}

export function userMatchesPortal(user: AuthUser | null, portal: PortalType): boolean {
  if (!user) return false;
  return user.portal === portal;
}
