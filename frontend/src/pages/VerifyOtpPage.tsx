import { useState } from 'react';
import { useSearchParams, Link, useNavigate } from 'react-router-dom';
import * as authApi from '../api/authApi';

export default function VerifyOtpPage() {
  const [searchParams] = useSearchParams();
  const email = searchParams.get('email') || '';
  const navigate = useNavigate();
  const [otp, setOtp] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await authApi.verifyOtp(email, otp);
      navigate('/employee/login');
    } catch (err: unknown) {
      setError((err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Invalid OTP');
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    try {
      await authApi.resendOtp(email);
      setError('');
      alert('OTP resent. Check server logs in development.');
    } catch (err: unknown) {
      setError((err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Could not resend OTP');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 px-4">
      <div className="w-full max-w-md bg-white rounded-2xl p-8 shadow-2xl">
        <h1 className="text-xl font-bold text-center mb-2">Verify Your Email</h1>
        <p className="text-slate-500 text-center text-sm mb-6">Enter the 6-digit code sent to {email}</p>
        <form onSubmit={handleVerify} className="space-y-4">
          <input
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            maxLength={6}
            className="w-full text-center text-2xl tracking-widest px-4 py-3 border rounded-lg"
            placeholder="000000"
          />
          {error && <p className="text-red-600 text-sm">{error}</p>}
          <button type="submit" disabled={loading || otp.length !== 6} className="w-full py-2.5 bg-indigo-600 text-white rounded-lg font-semibold disabled:opacity-50">
            {loading ? 'Verifying...' : 'Verify'}
          </button>
        </form>
        <button onClick={handleResend} className="w-full mt-3 text-sm text-indigo-600 hover:underline">Resend OTP</button>
        <p className="text-center text-sm mt-4"><Link to="/employee/login" className="text-slate-500">Back to login</Link></p>
      </div>
    </div>
  );
}
