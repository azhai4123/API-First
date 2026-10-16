/**
 * AUTHENTICATION MODULE (LOGIN, REGISTER, LOGOUT, PROFILE)
 */
function updateAuthUI() {
  const user = state.currentUser;
  const userDisplay = document.getElementById('user-display');
  const viewToggleBtn = document.getElementById('view-toggle-btn');

  if (user) {
    const isAdmin = user.role === 'ADMIN';
    userDisplay.innerHTML = `
      <div class="flex items-center gap-2">
        <button onclick="openUserProfileModal()" class="flex items-center gap-2 p-1 rounded-lg hover:bg-slate-100 transition text-left" title="Bấm xem hồ sơ cá nhân">
          <img src="${user.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150'}" class="w-8 h-8 rounded-full border border-slate-200 object-cover" />
          <div class="hidden sm:block text-xs">
            <div class="font-semibold text-slate-800 flex items-center gap-1">${user.fullName} <span class="text-[10px] text-blue-500">🔍</span></div>
            <div class="text-[10px] font-mono ${isAdmin ? 'text-purple-600 font-bold' : 'text-slate-500'}">${user.role}</div>
          </div>
        </button>
        <button onclick="handleLogout()" class="text-xs text-rose-600 hover:text-rose-800 ml-1 font-medium">Thoát</button>
      </div>`;

    if (viewToggleBtn) {
      if (isAdmin) {
        viewToggleBtn.classList.remove('hidden');
        viewToggleBtn.textContent = state.currentView === 'BUYER' ? '⚙️ Sang Trang Quản Trị' : '🛍️ Sang Trang Mua Hàng';
        viewToggleBtn.className = "text-xs font-semibold px-3 py-1.5 rounded-lg border border-purple-300 text-purple-700 bg-purple-50 hover:bg-purple-100 transition shadow-sm";
      } else {
        // Tài khoản khách hàng: ẩn hoặc vô hiệu hóa nút chuyển sang Admin
        viewToggleBtn.classList.add('hidden');
      }
    }
  } else {
    userDisplay.innerHTML = `
      <div class="flex items-center gap-2">
        <button onclick="openModal('modal-login')" class="text-xs font-medium text-blue-600 hover:underline">Đăng nhập</button>
        <span class="text-slate-300">|</span>
        <button onclick="openModal('modal-register')" class="text-xs font-medium bg-blue-600 text-white px-2.5 py-1 rounded hover:bg-blue-700">Đăng ký</button>
      </div>`;
    if (viewToggleBtn) viewToggleBtn.classList.add('hidden');
  }
}

async function handleLogin(e) {
  e.preventDefault();
  const email = document.getElementById('login-email').value;
  const password = document.getElementById('login-password').value;

  const res = await apiCall('POST', '/auth/login', { email, password });
  if (res.status === 200) {
    state.currentUser = res.data.user;
    state.token = res.data.token;
    localStorage.setItem('shop_user', JSON.stringify(res.data.user));
    localStorage.setItem('shop_token', res.data.token);
    closeModal('modal-login');
    showToast(`Đăng nhập thành công! Xin chào ${res.data.user.fullName}`);
    if (res.data.user.role === 'ADMIN') switchView('ADMIN');
    else switchView('BUYER');
    updateAuthUI();
  } else {
    showToast(res.data?.message || 'Đăng nhập thất bại', true);
  }
}

async function handleRegister(e) {
  e.preventDefault();
  const payload = {
    fullName: document.getElementById('reg-name').value,
    email: document.getElementById('reg-email').value,
    password: document.getElementById('reg-pass').value,
    phone: document.getElementById('reg-phone').value,
    address: document.getElementById('reg-address').value,
    avatarUrl: document.getElementById('reg-avatar').value || "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150"
  };

  const res = await apiCall('POST', '/auth/register', payload);
  if (res.status === 201) {
    state.currentUser = res.data.user;
    state.token = res.data.token;
    localStorage.setItem('shop_user', JSON.stringify(res.data.user));
    localStorage.setItem('shop_token', res.data.token);
    closeModal('modal-register');
    showToast(`Đăng ký tài khoản thành công!`);
    switchView('BUYER');
    updateAuthUI();
  } else {
    const issue = res.data?.details?.map(d => d.issue).join(', ') || res.data?.message;
    showToast(issue || 'Đăng ký thất bại', true);
  }
}

function handleLogout() {
  state.currentUser = null;
  state.token = null;
  localStorage.removeItem('shop_user');
  localStorage.removeItem('shop_token');
  showToast('Đã đăng xuất khỏi hệ thống');
  switchView('BUYER');
  updateAuthUI();
}

function quickLogin(role) {
  if (role === 'ADMIN') {
    document.getElementById('login-email').value = 'admin@shop.com';
    document.getElementById('login-password').value = 'admin123';
  } else {
    document.getElementById('login-email').value = 'nguyenvana@example.com';
    document.getElementById('login-password').value = 'pass123';
  }
}

async function openUserProfileModal() {
  if (!state.currentUser) return;
  const res = await apiCall('GET', `/customers/${state.currentUser.id}`);
  const user = res.status === 200 ? res.data : state.currentUser;

  document.getElementById('profile-id').textContent = `#${user.id}`;
  document.getElementById('profile-role').textContent = user.role || 'CUSTOMER';
  document.getElementById('profile-avatar-img').src = user.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150';
  document.getElementById('profile-name').value = user.fullName || '';
  document.getElementById('profile-email').value = user.email || '';
  document.getElementById('profile-phone').value = user.phone || '';
  document.getElementById('profile-address').value = user.address || '';
  document.getElementById('profile-avatar').value = user.avatarUrl || '';

  openModal('modal-user-profile');
}

async function handleUpdateProfile(e) {
  e.preventDefault();
  if (!state.currentUser) return;

  const payload = {
    fullName: document.getElementById('profile-name').value.trim(),
    phone: document.getElementById('profile-phone').value.trim(),
    address: document.getElementById('profile-address').value.trim(),
    avatarUrl: document.getElementById('profile-avatar').value.trim()
  };

  const res = await apiCall('PUT', `/customers/${state.currentUser.id}`, payload);
  if (res.status === 200) {
    state.currentUser = { ...state.currentUser, ...res.data };
    localStorage.setItem('shop_user', JSON.stringify(state.currentUser));
    updateAuthUI();
    closeModal('modal-user-profile');
    showToast('Cập nhật hồ sơ cá nhân thành công!');
    if (state.currentView === 'ADMIN') loadAdminCustomers();
  } else {
    showToast(res.data?.message || 'Lỗi cập nhật hồ sơ', true);
  }
}

