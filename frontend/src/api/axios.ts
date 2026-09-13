import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

let accessToken: string | null = sessionStorage.getItem('accessToken');
let refreshToken: string | null = sessionStorage.getItem('refreshToken');
let isRefreshing = false;

export function setTokens(access: string | null, refresh: string | null) {
  accessToken = access;
  refreshToken = refresh;
  if (access) sessionStorage.setItem('accessToken', access);
  else sessionStorage.removeItem('accessToken');
  if (refresh) sessionStorage.setItem('refreshToken', refresh);
  else sessionStorage.removeItem('refreshToken');
}

export function getAccessToken() {
  return accessToken;
}

export function clearTokens() {
  setTokens(null, null);
  sessionStorage.removeItem('authUser');
}

api.interceptors.request.use((config) => {
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && refreshToken && !original._retry) {
      original._retry = true;
      if (!isRefreshing) {
        isRefreshing = true;
        try {
          const res = await axios.post('/api/auth/refresh', { refreshToken });
          const data = res.data.data;
          setTokens(data.accessToken, data.refreshToken);
          isRefreshing = false;
          original.headers.Authorization = `Bearer ${data.accessToken}`;
          return api(original);
        } catch {
          clearTokens();
          window.location.href = '/employee/login';
          isRefreshing = false;
        }
      }
    }
    return Promise.reject(error);
  }
);

export default api;
