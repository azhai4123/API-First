/**
 * API SERVICE & SHARED STATE MANAGEMENT - KẾT NỐI SPRING BOOT 3.x + MYSQL LARAGON
 */
const API_BASE = 'http://localhost:8080/api/v1';

const state = {
  currentUser: JSON.parse(localStorage.getItem('shop_user')) || {
    id: 1,
    fullName: "Quản trị viên",
    email: "admin@shop.com",
    role: "ADMIN",
    avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150"
  },
  token: localStorage.getItem('shop_token') || 'mock-jwt-token-eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.uid1.ADMIN',
  currentView: localStorage.getItem('shop_view') || 'BUYER', // 'BUYER' or 'ADMIN'
  cart: JSON.parse(localStorage.getItem('shop_cart')) || [],
  prodPage: 0,
  prodSize: 6,
  prodTotalPages: 1,
  custPage: 0,
  custSize: 5
};

async function apiCall(method, endpoint, data = null) {
  const url = `${API_BASE}${endpoint}`;
  const headers = { 'Content-Type': 'application/json' };
  if (state.token) headers['Authorization'] = `Bearer ${state.token}`;

  const summaryEl = document.getElementById('inspector-summary');
  if (summaryEl) summaryEl.innerHTML = `<span class="bg-blue-600 text-white px-2 py-0.5 rounded text-xs">${method}</span> <span class="font-mono text-xs">${endpoint}</span> ...`;
  const reqLog = document.getElementById('req-log');
  if (reqLog) reqLog.textContent = data ? JSON.stringify(data, null, 2) : '// Không có request body';

  const startTime = performance.now();
  try {
    const res = await fetch(url, { method, headers, body: data ? JSON.stringify(data) : null });
    const duration = Math.round(performance.now() - startTime);
    const loc = res.headers.get('Location');
    const json = res.status !== 204 ? await res.json() : null;

    if (summaryEl) {
      summaryEl.innerHTML = `<span class="bg-blue-600 text-white px-1.5 py-0.5 rounded text-xs">${method}</span> <span class="font-mono text-xs">${endpoint}</span> <span class="status-pill s-${res.status}">HTTP ${res.status}</span> <span class="text-xs text-slate-400">(${duration}ms)</span> ${loc ? `<span class="text-xs text-blue-400">Loc: ${loc}</span>` : ''}`;
    }
    const resLog = document.getElementById('res-log');
    if (resLog) resLog.textContent = json ? JSON.stringify(json, null, 2) : `// HTTP ${res.status} No Content (Thành công)`;
    
    updateHeaderStats();
    return { status: res.status, data: json, headers: res.headers };
  } catch (err) {
    const resLog = document.getElementById('res-log');
    if (resLog) resLog.textContent = `// LỖI KẾT NỐI MOCK SERVER: ${err.message}`;
    return { status: 500, error: err };
  }
}

async function updateHeaderStats() {
  try {
    const [cRes, pRes, oRes] = await Promise.all([
      fetch(`${API_BASE}/customers`),
      fetch(`${API_BASE}/products?size=100`),
      fetch(`${API_BASE}/orders`)
    ]);
    const [cData, pData, oData] = await Promise.all([cRes.json(), pRes.json(), oRes.json()]);
    if (cData.page && document.getElementById('stat-cust')) document.getElementById('stat-cust').textContent = cData.page.totalElements;
    if (pData.content && document.getElementById('stat-prod')) {
      document.getElementById('stat-prod').textContent = pData.page ? pData.page.totalElements : pData.content.length;
      document.getElementById('stat-out').textContent = pData.content.filter(p => p.stockQuantity === 0).length;
    }
    if (oData.page && document.getElementById('stat-orders')) document.getElementById('stat-orders').textContent = oData.page.totalElements;
  } catch (e) {}
}

function showToast(msg, isError = false) {
  const toast = document.createElement('div');
  toast.className = `fixed bottom-24 right-6 z-50 px-4 py-3 rounded-lg shadow-xl text-white text-sm font-medium flex items-center gap-2 transition-all duration-300 ${isError ? 'bg-red-600' : 'bg-emerald-600'}`;
  toast.innerHTML = `<span>${isError ? '⚠️' : '✅'}</span><span>${msg}</span>`;
  document.body.appendChild(toast);
  setTimeout(() => { toast.style.opacity = '0'; setTimeout(() => toast.remove(), 300); }, 3000);
}

const openModal = id => { const el = document.getElementById(id); if (el) el.classList.remove('hidden'); };
const closeModal = id => { const el = document.getElementById(id); if (el) el.classList.add('hidden'); };

function closeModalOnBackdrop(e, id) {
  if (e.target.id === id) closeModal(id);
}

document.addEventListener('keydown', e => {
  if (e.key === 'Escape') {
    document.querySelectorAll('.modal-overlay:not(.hidden), [id^="modal-"]:not(.hidden), #drawer-cart:not(.hidden)').forEach(el => el.classList.add('hidden'));
  }
});

