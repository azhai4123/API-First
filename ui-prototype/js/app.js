/**
 * MAIN APP CONTROLLER & VIEW SWITCHER
 */
function switchView(viewName) {
  if (viewName === 'ADMIN') {
    if (!state.currentUser) {
      showToast('Vui lòng đăng nhập với tài khoản Quản trị viên (ADMIN)', true);
      openModal('modal-login');
      return;
    }
    if (state.currentUser.role !== 'ADMIN') {
      showToast('Truy cập bị từ chối: Tài khoản khách hàng không có quyền truy cập Quản trị!', true);
      return;
    }
  }

  state.currentView = viewName;
  localStorage.setItem('shop_view', viewName);

  const buyerSection = document.getElementById('view-buyer');
  const adminSection = document.getElementById('view-admin');
  const viewToggleBtn = document.getElementById('view-toggle-btn');

  if (viewName === 'ADMIN') {
    if (buyerSection) buyerSection.classList.add('hidden');
    if (adminSection) adminSection.classList.remove('hidden');
    if (viewToggleBtn) viewToggleBtn.textContent = '🛍️ Sang Trang Mua Hàng';
    loadAdminProducts();
    loadAdminOrders();
    loadAdminCustomers();
  } else {
    if (adminSection) adminSection.classList.add('hidden');
    if (buyerSection) buyerSection.classList.remove('hidden');
    if (viewToggleBtn) viewToggleBtn.textContent = '⚙️ Sang Trang Quản Trị';
    loadBuyerCatalog();
    loadBuyerOrders();
  }
}

function toggleView() {
  const nextView = state.currentView === 'BUYER' ? 'ADMIN' : 'BUYER';
  switchView(nextView);
}

function switchAdminTab(tabId) {
  document.querySelectorAll('.admin-tab-btn').forEach(b => {
    b.classList.remove('border-blue-600', 'text-blue-600');
    b.classList.add('border-transparent', 'text-slate-500');
  });
  document.querySelectorAll('.admin-tab-pane').forEach(p => p.classList.add('hidden'));

  const activeBtn = document.getElementById(`btn-tab-${tabId}`);
  const activePane = document.getElementById(`pane-${tabId}`);
  if (activeBtn) {
    activeBtn.classList.remove('border-transparent', 'text-slate-500');
    activeBtn.classList.add('border-blue-600', 'text-blue-600');
  }
  if (activePane) activePane.classList.remove('hidden');

  if (tabId === 'categories') loadCategories();
  if (tabId === 'products') loadAdminProducts();
  if (tabId === 'orders') loadAdminOrders();
  if (tabId === 'customers') loadAdminCustomers();
}

// Pagination controls for Admin
function prevAdminProductPage() {
  if (state.prodPage > 0) {
    state.prodPage--;
    loadAdminProducts();
  }
}

function nextAdminProductPage() {
  if (state.prodPage < state.prodTotalPages - 1) {
    state.prodPage++;
    loadAdminProducts();
  }
}

// App Initialization
window.addEventListener('DOMContentLoaded', () => {
  updateAuthUI();
  updateCartBadge();
  loadCategories();
  switchView(state.currentView);
  updateHeaderStats();
});
