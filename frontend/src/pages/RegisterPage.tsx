import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import * as authApi from '../api/authApi';
import type { CompanyOption, DepartmentOption } from '../types/auth';

interface RegisterForm {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  employeeId: string;
  phone?: string;
  organizationId: string;
  departmentId?: string;
}

export default function RegisterPage() {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [companies, setCompanies] = useState<CompanyOption[]>([]);
  const [departments, setDepartments] = useState<DepartmentOption[]>([]);
  const { register, handleSubmit, watch } = useForm<RegisterForm>();

  const selectedCompanyId = watch('organizationId');

  useEffect(() => {
    authApi.listCompanies().then(setCompanies).catch(() => setCompanies([]));
  }, []);

  useEffect(() => {
    if (!selectedCompanyId) {
      setDepartments([]);
      return;
    }
    authApi.listDepartments(Number(selectedCompanyId))
      .then(setDepartments)
      .catch(() => setDepartments([]));
  }, [selectedCompanyId]);

  const onSubmit = async (data: RegisterForm) => {
    setError('');
    if (data.password !== data.confirmPassword) {
      setError('Password and confirm password must match');
      return;
    }
    setLoading(true);
    try {
      await authApi.register({
        username: data.username,
        email: data.email,
        password: data.password,
        confirmPassword: data.confirmPassword,
        firstName: data.firstName,
        lastName: data.lastName,
        employeeId: data.employeeId,
        phone: data.phone,
        organizationId: Number(data.organizationId),
        departmentId: data.departmentId ? Number(data.departmentId) : undefined,
      });
      navigate(`/verify-otp?email=${encodeURIComponent(data.email)}`);
    } catch (err: unknown) {
      setError((err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 to-slate-800 px-4 py-8">
      <div className="w-full max-w-lg bg-white rounded-2xl shadow-2xl p-8">
        <h1 className="text-2xl font-bold text-center mb-2">Employee Registration</h1>
        <p className="text-slate-500 text-center mb-8 text-sm">Create your corporate travel account</p>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">Company</label>
            <select
              {...register('organizationId', { required: true })}
              className="w-full px-3 py-2 border rounded-lg bg-white"
              defaultValue=""
            >
              <option value="" disabled>Select company</option>
              {companies.map((c) => (
                <option key={c.id} value={c.id}>{c.name}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Department</label>
            <select {...register('departmentId')} className="w-full px-3 py-2 border rounded-lg bg-white" defaultValue="">
              <option value="">Select department (optional)</option>
              {departments.map((d) => (
                <option key={d.id} value={d.id}>{d.name}</option>
              ))}
            </select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">First Name</label>
              <input {...register('firstName', { required: true })} className="w-full px-3 py-2 border rounded-lg" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Last Name</label>
              <input {...register('lastName', { required: true })} className="w-full px-3 py-2 border rounded-lg" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Employee ID</label>
            <input {...register('employeeId', { required: true })} className="w-full px-3 py-2 border rounded-lg" placeholder="EMP-12345" />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">Username</label>
            <input {...register('username', { required: true })} className="w-full px-3 py-2 border rounded-lg" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Email</label>
            <input type="email" {...register('email', { required: true })} className="w-full px-3 py-2 border rounded-lg" />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Password</label>
              <input type="password" {...register('password', { required: true, minLength: 8 })} className="w-full px-3 py-2 border rounded-lg" />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Confirm Password</label>
              <input type="password" {...register('confirmPassword', { required: true, minLength: 8 })} className="w-full px-3 py-2 border rounded-lg" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Phone (optional)</label>
            <input {...register('phone')} className="w-full px-3 py-2 border rounded-lg" />
          </div>

          {error && <div className="text-red-600 text-sm bg-red-50 p-3 rounded-lg">{error}</div>}

          <button type="submit" disabled={loading} className="w-full py-2.5 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-700 disabled:opacity-50">
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <p className="text-center text-sm mt-6 text-slate-500">
          Already have an account? <Link to="/employee/login" className="text-indigo-600 font-medium">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
