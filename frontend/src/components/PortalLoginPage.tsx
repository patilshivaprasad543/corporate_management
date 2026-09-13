import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import type { PortalConfig } from '../auth/portals';
import * as authApi from '../api/authApi';
import type { CompanyOption } from '../types/auth';

interface LoginFormData {
  email: string;
  password: string;
  organizationId?: string;
}

const accentMap: Record<string, string> = {
  indigo: 'bg-indigo-600 hover:bg-indigo-700 focus:ring-indigo-500',
  blue: 'bg-blue-600 hover:bg-blue-700 focus:ring-blue-500',
  violet: 'bg-violet-600 hover:bg-violet-700 focus:ring-violet-500',
  emerald: 'bg-emerald-600 hover:bg-emerald-700 focus:ring-emerald-500',
  cyan: 'bg-cyan-600 hover:bg-cyan-700 focus:ring-cyan-500',
  amber: 'bg-amber-600 hover:bg-amber-700 focus:ring-amber-500',
  rose: 'bg-rose-600 hover:bg-rose-700 focus:ring-rose-500',
};

export default function PortalLoginPage({ config }: { config: PortalConfig }) {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [apiError, setApiError] = useState('');
  const [loading, setLoading] = useState(false);
  const [companies, setCompanies] = useState<CompanyOption[]>([]);
  const requiresCompany = config.portal !== 'SUPER_ADMIN';
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>();

  useEffect(() => {
    if (!requiresCompany) return;
    authApi.listCompanies()
      .then(setCompanies)
      .catch(() => setCompanies([]));
  }, [requiresCompany]);

  const onSubmit = async (data: LoginFormData) => {
    setApiError('');
    setLoading(true);
    try {
      await login({
        email: data.email,
        password: data.password,
        portal: config.portal,
        organizationId: requiresCompany ? Number(data.organizationId) : undefined,
      });
      navigate(config.dashboardPath);
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message
        || 'Login failed. Please check your credentials.';
      setApiError(msg);
    } finally {
      setLoading(false);
    }
  };

  const btnClass = accentMap[config.accentColor] || accentMap.indigo;

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 px-4">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-2xl p-8">
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-14 h-14 rounded-xl bg-slate-900 text-white text-2xl font-bold mb-4">CT</div>
            <h1 className="text-2xl font-bold text-slate-900">{config.title}</h1>
            <p className="text-slate-500 mt-1 font-medium">{config.subtitle}</p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            {requiresCompany && (
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Company</label>
                <select
                  {...register('organizationId', { required: 'Company is required' })}
                  className="w-full px-4 py-2.5 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none bg-white"
                  defaultValue=""
                >
                  <option value="" disabled>Select your company</option>
                  {companies.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
                {errors.organizationId && <p className="text-red-500 text-sm mt-1">{errors.organizationId.message}</p>}
              </div>
            )}

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Email</label>
              <input
                type="email"
                {...register('email', { required: 'Email is required' })}
                className="w-full px-4 py-2.5 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
                placeholder="you@company.com"
                autoComplete="email"
              />
              {errors.email && <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Password</label>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  {...register('password', { required: 'Password is required' })}
                  className="w-full px-4 py-2.5 border border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none pr-10"
                  placeholder="••••••••"
                  autoComplete="current-password"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 text-sm"
                >
                  {showPassword ? 'Hide' : 'Show'}
                </button>
              </div>
              {errors.password && <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>}
            </div>

            {apiError && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                {apiError}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className={`w-full py-2.5 text-white font-semibold rounded-lg transition ${btnClass} disabled:opacity-50`}
            >
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          <div className="mt-6 flex items-center justify-between text-sm">
            <a href="#" className="text-slate-500 hover:text-slate-700">Forgot Password?</a>
            {config.showRegister && (
              <Link to="/register" className="text-indigo-600 hover:text-indigo-800 font-medium">Register</Link>
            )}
          </div>
        </div>
        <p className="text-center text-slate-500 text-xs mt-6">Secured by CorporateTravel360 Enterprise Auth</p>
      </div>
    </div>
  );
}
