import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import DashboardLayout from '../components/DashboardLayout';
import { PORTALS } from '../auth/portals';
import * as policiesApi from '../api/policiesApi';

const TRAVEL_ADMIN_NAV = [
  { label: 'Dashboard', path: '/travel-admin/dashboard' },
  { label: 'Policies', path: '/travel-admin/policies' },
  { label: 'Bookings', path: '/travel-admin/dashboard' },
  { label: 'Vendors', path: '/travel-admin/dashboard' },
];

export default function TravelAdminPoliciesPage() {
  const [evalBudget, setEvalBudget] = useState('48000');
  const [evalResult, setEvalResult] = useState<policiesApi.PolicyEvaluation | null>(null);

  const { data: policies = [], isLoading } = useQuery({
    queryKey: ['policies'],
    queryFn: policiesApi.listPolicies,
  });

  const { data: violations = [] } = useQuery({
    queryKey: ['policy-violations'],
    queryFn: policiesApi.listViolations,
  });

  const handleEvaluate = async () => {
    const policy = policies[0];
    if (!policy) return;
    const result = await policiesApi.evaluatePolicy({
      organizationId: policy.organizationId,
      flightAmount: Number(evalBudget),
      estimatedBudget: Number(evalBudget),
      departureDate: new Date(Date.now() + 14 * 86400000).toISOString().slice(0, 10),
      international: false,
    });
    setEvalResult(result);
  };

  const activePolicy = policies.find((p) => p.active) ?? policies[0];

  return (
    <DashboardLayout config={PORTALS.TRAVEL_ADMIN} navItems={TRAVEL_ADMIN_NAV}>
      <div className="max-w-6xl space-y-8">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Travel Policies</h1>
          <p className="text-slate-500 text-sm mt-1">Configure and evaluate company travel policy rules</p>
        </div>

        {isLoading ? (
          <p className="text-slate-500">Loading policies...</p>
        ) : activePolicy ? (
          <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
            <h2 className="font-semibold text-lg mb-4">{activePolicy.name}</h2>
            <div className="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
              <div><span className="text-slate-500">Max Domestic Flight</span><p className="font-medium">₹{activePolicy.maxDomesticFlightPrice?.toLocaleString()}</p></div>
              <div><span className="text-slate-500">Max International Flight</span><p className="font-medium">₹{activePolicy.maxInternationalFlightPrice?.toLocaleString()}</p></div>
              <div><span className="text-slate-500">Max Hotel/Night</span><p className="font-medium">₹{activePolicy.maxHotelPricePerNight?.toLocaleString()}</p></div>
              <div><span className="text-slate-500">Finance Threshold</span><p className="font-medium">₹{activePolicy.financeApprovalThreshold?.toLocaleString()}</p></div>
              <div><span className="text-slate-500">Admin Threshold</span><p className="font-medium">₹{activePolicy.adminApprovalThreshold?.toLocaleString()}</p></div>
              <div><span className="text-slate-500">Advance Booking</span><p className="font-medium">{activePolicy.advanceBookingDays} days</p></div>
            </div>
          </div>
        ) : (
          <p className="text-slate-500">No policy configured.</p>
        )}

        <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
          <h2 className="font-semibold text-lg mb-4">Policy Evaluation Simulator</h2>
          <div className="flex gap-3 items-end">
            <div>
              <label className="block text-sm text-slate-500 mb-1">Flight Amount (₹)</label>
              <input value={evalBudget} onChange={(e) => setEvalBudget(e.target.value)}
                className="px-3 py-2 border rounded-lg w-40" />
            </div>
            <button onClick={handleEvaluate}
              className="px-4 py-2 bg-violet-600 text-white rounded-lg text-sm hover:bg-violet-700">
              Evaluate
            </button>
          </div>
          {evalResult && (
            <div className="mt-4 p-4 rounded-lg bg-slate-50 text-sm space-y-2">
              <p><strong>Status:</strong> <span className={evalResult.status === 'POLICY_VIOLATION' ? 'text-red-600' : 'text-green-600'}>{evalResult.status}</span></p>
              <p>{evalResult.summary}</p>
              {evalResult.violations.map((v, i) => (
                <div key={i} className="border-l-2 border-red-400 pl-3 text-red-700">
                  {v.explanation}
                  {v.differenceAmount != null && <span> (₹{v.differenceAmount} over limit)</span>}
                </div>
              ))}
              {evalResult.requiresFinanceApproval && <p className="text-amber-700">⚠ Finance approval required</p>}
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
          <h2 className="font-semibold text-lg mb-4">Recent Policy Violations ({violations.length})</h2>
          {violations.length === 0 ? (
            <p className="text-slate-500 text-sm">No violations recorded yet.</p>
          ) : (
            <div className="space-y-2 text-sm">
              {(violations as Array<{ explanation?: string; violationType?: string; differenceAmount?: number }>).slice(0, 5).map((v, i) => (
                <div key={i} className="p-3 bg-red-50 rounded-lg text-red-800">
                  <strong>{v.violationType}</strong>: {v.explanation}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
}
