import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import DashboardLayout from '../components/DashboardLayout';
import { PORTALS } from '../auth/portals';
import * as companiesApi from '../api/companiesApi';
import type { CreateCompanyRequest } from '../api/companiesApi';

const SUPER_ADMIN_NAV = [
  { label: 'Dashboard', path: '/super-admin/dashboard' },
  { label: 'Organizations', path: '/super-admin/companies' },
  { label: 'Users', path: '/super-admin/dashboard' },
  { label: 'Audit Logs', path: '/super-admin/dashboard' },
];

export default function CompaniesPage() {
  const queryClient = useQueryClient();
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const { register, handleSubmit, reset } = useForm<CreateCompanyRequest>();

  const { data: companies = [], isLoading } = useQuery({
    queryKey: ['companies'],
    queryFn: companiesApi.listCompanies,
  });

  const createMutation = useMutation({
    mutationFn: companiesApi.createCompany,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['companies'] });
      queryClient.invalidateQueries({ queryKey: ['system-stats'] });
      reset();
      setShowForm(false);
      setError('');
    },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Failed to create company');
    },
  });

  const toggleMutation = useMutation({
    mutationFn: ({ id, active }: { id: number; active: boolean }) =>
      active ? companiesApi.deactivateCompany(id) : companiesApi.activateCompany(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['companies'] }),
  });

  const onSubmit = (data: CreateCompanyRequest) => {
    setError('');
    createMutation.mutate(data);
  };

  return (
    <DashboardLayout config={PORTALS.SUPER_ADMIN} navItems={SUPER_ADMIN_NAV}>
      <div className="max-w-6xl">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-2xl font-bold text-slate-900">Companies</h1>
            <p className="text-slate-500 text-sm mt-1">Manage all organizations in the system</p>
          </div>
          <button
            onClick={() => setShowForm(!showForm)}
            className="px-4 py-2 bg-rose-600 text-white text-sm font-medium rounded-lg hover:bg-rose-700"
          >
            {showForm ? 'Cancel' : 'Create Company'}
          </button>
        </div>

        {showForm && (
          <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-xl border border-slate-200 p-6 mb-6 space-y-4">
            <h2 className="font-semibold text-slate-900">New Company</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <input {...register('name', { required: true })} placeholder="Company Name" className="px-3 py-2 border rounded-lg" />
              <input {...register('code', { required: true })} placeholder="Code (e.g. ACME-GLOBAL)" className="px-3 py-2 border rounded-lg" />
              <input {...register('registrationNumber')} placeholder="Registration Number" className="px-3 py-2 border rounded-lg" />
              <input {...register('taxNumber')} placeholder="Tax Number" className="px-3 py-2 border rounded-lg" />
              <input {...register('email')} placeholder="Email" type="email" className="px-3 py-2 border rounded-lg" />
              <input {...register('phone')} placeholder="Phone" className="px-3 py-2 border rounded-lg" />
              <input {...register('domainName')} placeholder="Email domain (e.g. acmetech.com)" className="px-3 py-2 border rounded-lg" />
              <input {...register('country', { required: true })} placeholder="Country" defaultValue="India" className="px-3 py-2 border rounded-lg" />
              <input {...register('currencyCode', { required: true })} placeholder="Currency" defaultValue="INR" className="px-3 py-2 border rounded-lg" />
              <input {...register('timezone')} placeholder="Timezone" defaultValue="Asia/Kolkata" className="px-3 py-2 border rounded-lg" />
            </div>
            {error && <p className="text-red-600 text-sm">{error}</p>}
            <button type="submit" disabled={createMutation.isPending} className="px-4 py-2 bg-rose-600 text-white rounded-lg disabled:opacity-50">
              {createMutation.isPending ? 'Creating...' : 'Create Company'}
            </button>
          </form>
        )}

        {isLoading ? (
          <p className="text-slate-500">Loading companies...</p>
        ) : (
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
            <table className="w-full text-sm">
              <thead className="bg-slate-50 border-b border-slate-200">
                <tr>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Name</th>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Code</th>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Country</th>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Employees</th>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Status</th>
                  <th className="text-left px-4 py-3 font-medium text-slate-600">Actions</th>
                </tr>
              </thead>
              <tbody>
                {companies.map((c) => (
                  <tr key={c.id} className="border-b border-slate-100 last:border-0">
                    <td className="px-4 py-3 font-medium">{c.name}</td>
                    <td className="px-4 py-3 text-slate-500">{c.code}</td>
                    <td className="px-4 py-3 text-slate-500">{c.country}</td>
                    <td className="px-4 py-3">{c.employeeCount ?? 0}</td>
                    <td className="px-4 py-3">
                      <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${c.active ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                        {c.active ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <button
                        onClick={() => toggleMutation.mutate({ id: c.id, active: !!c.active })}
                        className="text-rose-600 hover:text-rose-800 text-xs font-medium"
                      >
                        {c.active ? 'Deactivate' : 'Activate'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}
