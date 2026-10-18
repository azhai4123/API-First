/**
 * SHARED NAVIGATION & CORE UTILITIES FOR MULTI-PAGE UI
 */

function renderSharedHeader(activePage = 'shop') {
  const headerContainer = document.getElementById('shared-header');
  if (!headerContainer) return;

  const user = state.currentUser || { fullName: 'Khách', role: 'CUSTOMER', email: 'guest@shop.com' };
  const isAdmin = user.role === 'ADMIN';
  const cartCount = state.cart ? state.cart.reduce((sum, item) => sum + item.quantity, 0) : 0;

  headerContainer.innerHTML = `
    <header class="bg-white/95 backdrop-blur-md border-b border-slate-200 sticky top-0 z-30 shadow-sm transition-all">
      <div class="max-w-7xl mx-auto px-4 h-16 flex items-center justify-between gap-4">
        
        <!-- Brand Logo & Slogan -->
        <a href="/index.html" class="flex items-center gap-3 group">
          <div class="w-10 h-10 rounded-xl bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white shadow-md shadow-blue-500/20 group-hover:scale-105 transition">
            <span class="text-xl">🛍️</span>
          </div>
          <div>
            <div class="font-extrabold text-slate-900 leading-tight text-base tracking-tight flex items-center gap-1.5">
              Shop API Platform
              <span class="text-[10px] px-1.5 py-0.5 rounded bg-blue-100 text-blue-700 font-semibold uppercase">v2.0</span>
            </div>
            <div class="text-[11px] text-slate-500 font-mono">Spring Boot 3 + MySQL RESTful</div>
          </div>
        </a>

        <!-- Main Navigation Links -->
        <nav class="hidden md:flex items-center gap-1 text-sm font-semibold">
          <a href="/index.html" class="px-3.5 py-2 rounded-lg transition ${activePage === 'shop' ? 'bg-blue-50 text-blue-600 font-bold' : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'}">
            🏬 Cửa hàng
          </a>
          <a href="/orders.html" class="px-3.5 py-2 rounded-lg transition ${activePage === 'orders' ? 'bg-blue-50 text-blue-600 font-bold' : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'}">
            📦 Đơn hàng của tôi
          </a>
          <a href="/admin.html" class="px-3.5 py-2 rounded-lg transition flex items-center gap-1.5 ${activePage === 'admin' ? 'bg-indigo-50 text-indigo-700 font-bold' : 'text-slate-600 hover:text-indigo-700 hover:bg-indigo-50/50'}">
            <span class="text-xs">⚙️</span> Quản trị Portal
            ${isAdmin ? '<span class="w-2 h-2 rounded-full bg-emerald-500"></span>' : ''}
          </a>
        </nav>

        <!-- Right Tools: Cart, User Profile & API Inspector Toggle -->
        <div class="flex items-center gap-3">
          
          <!-- Cart Button (for Buyer pages) -->
          <button onclick="toggleCartDrawer()" class="relative p-2.5 text-slate-600 hover:text-blue-600 hover:bg-slate-100 rounded-xl transition border border-slate-200">
            <span class="text-lg">🛒</span>
            <span id="cart-badge" class="${cartCount > 0 ? '' : 'hidden'} absolute -top-1.5 -right-1.5 px-1.5 py-0.5 bg-rose-500 text-white text-[10px] font-bold rounded-full shadow-sm animate-pulse">
              ${cartCount}
            </span>
          </button>

          <!-- API Inspector Toggle Button -->
          <button onclick="toggleInspector()" title="Bật/Tắt Live API Inspector" class="hidden sm:flex items-center gap-1 px-2.5 py-1.5 text-xs font-mono font-medium rounded-lg border border-slate-200 hover:bg-slate-100 text-slate-600 transition">
            <span>⚡ API Log</span>
          </button>

          <!-- User Display & Switcher -->
          <div class="relative group">
            <a href="/login.html" class="flex items-center gap-2 pl-2 pr-3 py-1.5 rounded-xl border border-slate-200 hover:border-slate-300 bg-white hover:bg-slate-50 transition shadow-sm">
              <img src="${user.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100'}" alt="${user.fullName}" class="w-7 h-7 rounded-lg object-cover ring-1 ring-slate-200">
              <div class="text-left hidden lg:block">
                <div class="text-xs font-bold text-slate-800 leading-tight">${user.fullName || 'Người dùng'}</div>
                <div class="text-[10px] font-semibold text-blue-600 uppercase tracking-wider">${user.role || 'CUSTOMER'}</div>
              </div>
              <span class="text-slate-400 text-xs">▼</span>
            </a>
          </div>

        </div>
      </div>
    </header>
  `;
}

function renderSharedFooter() {
  const footerContainer = document.getElementById('shared-footer');
  if (!footerContainer) return;

  footerContainer.innerHTML = `
    <footer class="bg-white border-t border-slate-200 mt-auto py-6 text-xs text-slate-500">
      <div class="max-w-7xl mx-auto px-4 flex flex-col md:flex-row items-center justify-between gap-4">
        <div>
          <strong class="text-slate-700">Enterprise E-Commerce API-First Platform</strong> — Đồ án RESTful Web Services
        </div>
        <div class="flex items-center gap-6">
          <a href="http://localhost:8080/api/v1/swagger-ui/index.html" target="_blank" class="hover:text-blue-600 flex items-center gap-1 font-semibold text-blue-600">
            <span>📑</span> Swagger UI v3 (Cổng 8080)
          </a>
          <a href="http://localhost:8080/api/v1/v3/api-docs" target="_blank" class="hover:text-slate-700">
            Raw OpenAPI JSON
          </a>
          <button onclick="toggleInspector()" class="hover:text-blue-600 font-mono">
            ⚡ Live REST Inspector
          </button>
        </div>
      </div>
    </footer>
  `;
}

function renderCartDrawerHtml() {
  if (document.getElementById('cart-drawer-container')) return;

  const div = document.createElement('div');
  div.id = 'cart-drawer-container';
  div.innerHTML = `
    <!-- Cart Overlay & Drawer -->
    <div id="cart-drawer-backdrop" onclick="toggleCartDrawer()" class="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 hidden transition-opacity"></div>
    <div id="cart-drawer" class="fixed top-0 right-0 h-full w-full max-w-md bg-white shadow-2xl z-50 transform translate-x-full transition-transform duration-300 flex flex-col">
      <div class="p-4 border-b border-slate-200 flex items-center justify-between bg-slate-50">
        <div class="flex items-center gap-2">
          <span class="text-xl">🛒</span>
          <h3 class="font-bold text-slate-800 text-base">Giỏ hàng của bạn</h3>
        </div>
        <button onclick="toggleCartDrawer()" class="p-1.5 rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-200 text-lg leading-none">✕</button>
      </div>

      <div id="cart-drawer-items" class="flex-1 overflow-y-auto p-4 space-y-3">
        <!-- Dynamic Items -->
      </div>

      <div class="p-4 border-t border-slate-200 bg-slate-50 space-y-3">
        <div class="space-y-1">
          <label class="text-xs font-semibold text-slate-700">Địa chỉ giao hàng (Bắt buộc cho đơn):</label>
          <input type="text" id="checkout-address" value="Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội" placeholder="Nhập địa chỉ nhận hàng..." class="w-full px-3 py-2 text-xs rounded-lg border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <div class="flex justify-between items-center text-sm">
          <span class="text-slate-600 font-medium">Tổng tiền thanh toán:</span>
          <strong id="cart-drawer-total" class="text-lg font-bold text-blue-600">0 đ</strong>
        </div>
        <div class="grid grid-cols-2 gap-2">
          <button onclick="clearCart()" class="w-full py-2.5 rounded-xl border border-slate-300 text-slate-600 hover:bg-slate-100 text-xs font-semibold transition">Xóa giỏ</button>
          <button onclick="checkoutCart()" class="w-full py-2.5 rounded-xl bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold transition shadow-md shadow-blue-500/20">🚀 Đặt hàng (Checkout)</button>
        </div>
      </div>
    </div>
  `;
  document.body.appendChild(div);
}

function toggleCartDrawer() {
  const drawer = document.getElementById('cart-drawer');
  const backdrop = document.getElementById('cart-drawer-backdrop');
  if (!drawer || !backdrop) return;

  const isClosed = drawer.classList.contains('translate-x-full');
  if (isClosed) {
    updateCartDrawer();
    drawer.classList.remove('translate-x-full');
    backdrop.classList.remove('hidden');
  } else {
    drawer.classList.add('translate-x-full');
    backdrop.classList.add('hidden');
  }
}

function updateCartDrawer() {
  const itemsContainer = document.getElementById('cart-drawer-items');
  const totalEl = document.getElementById('cart-drawer-total');
  const badge = document.getElementById('cart-badge');
  if (!itemsContainer) return;

  const cart = state.cart || [];
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const totalCount = cart.reduce((sum, item) => sum + item.quantity, 0);

  if (badge) {
    if (totalCount > 0) {
      badge.textContent = totalCount;
      badge.classList.remove('hidden');
    } else {
      badge.classList.add('hidden');
    }
  }

  if (totalEl) totalEl.textContent = `${total.toLocaleString()} đ`;

  if (cart.length === 0) {
    itemsContainer.innerHTML = `
      <div class="text-center py-16 text-slate-400 space-y-3">
        <span class="text-4xl block">🛍️</span>
        <div class="text-sm font-medium">Giỏ hàng của bạn đang trống</div>
        <p class="text-xs text-slate-500">Hãy thêm sản phẩm từ cửa hàng để tiến hành mua sắm!</p>
      </div>
    `;
    return;
  }

  itemsContainer.innerHTML = cart.map((item, idx) => `
    <div class="flex items-center justify-between p-3 bg-white rounded-xl border border-slate-200 shadow-sm gap-3">
      <div class="flex-1 min-w-0">
        <h4 class="font-semibold text-slate-800 text-xs truncate" title="${item.name}">${item.name}</h4>
        <div class="text-xs text-blue-600 font-bold mt-0.5">${item.price.toLocaleString()} đ</div>
      </div>
      <div class="flex items-center gap-1.5">
        <button onclick="changeCartQty(${idx}, -1)" class="w-6 h-6 rounded-md bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold flex items-center justify-center text-xs">-</button>
        <span class="w-6 text-center text-xs font-semibold">${item.quantity}</span>
        <button onclick="changeCartQty(${idx}, 1)" class="w-6 h-6 rounded-md bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold flex items-center justify-center text-xs">+</button>
        <button onclick="removeCartItem(${idx})" class="p-1 text-slate-400 hover:text-rose-500 text-xs ml-1" title="Xóa">🗑️</button>
      </div>
    </div>
  `).join('');
}

function changeCartQty(index, delta) {
  if (!state.cart[index]) return;
  state.cart[index].quantity += delta;
  if (state.cart[index].quantity <= 0) {
    state.cart.splice(index, 1);
  }
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartDrawer();
}

function removeCartItem(index) {
  state.cart.splice(index, 1);
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartDrawer();
}

function clearCart() {
  state.cart = [];
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartDrawer();
  showToast('Đã dọn sạch giỏ hàng.');
}

async function checkoutCart() {
  if (!state.cart || state.cart.length === 0) {
    showToast('Giỏ hàng đang trống! Vui lòng chọn sản phẩm.', true);
    return;
  }

  const address = document.getElementById('checkout-address')?.value || 'Hà Nội, Việt Nam';
  const customerId = state.currentUser?.id || 1;

  const payload = {
    customerId: customerId,
    shippingAddress: address.trim(),
    items: state.cart.map(item => ({
      productId: item.productId,
      quantity: item.quantity
    }))
  };

  const res = await apiCall('POST', '/orders', payload);
  if (res.status === 201) {
    showToast(`🎉 Đặt hàng thành công! Đơn hàng #${res.data.id} đã được tạo.`);
    state.cart = [];
    localStorage.setItem('shop_cart', JSON.stringify(state.cart));
    updateCartDrawer();
    toggleCartDrawer();
    // Chuyển hướng sang trang theo dõi đơn hàng sau 1 giây
    setTimeout(() => {
      window.location.href = '/orders.html';
    }, 1000);
  } else {
    const errorMsg = res.data?.message || 'Không thể tạo đơn hàng';
    showToast(`❌ Thất bại: ${errorMsg}`, true);
  }
}

function renderSharedInspectorHtml() {
  if (document.getElementById('api-inspector-container')) return;

  const div = document.createElement('div');
  div.id = 'api-inspector-container';
  div.innerHTML = `
    <!-- Live REST Inspector Floating Drawer -->
    <div id="inspector-drawer" class="fixed bottom-0 left-0 right-0 max-h-72 bg-slate-900 border-t border-slate-700 text-slate-100 z-40 transform translate-y-full transition-transform duration-300 shadow-2xl flex flex-col font-mono text-xs">
      <div class="px-4 py-2 bg-slate-800 border-b border-slate-700 flex items-center justify-between">
        <div class="flex items-center gap-3">
          <span class="text-amber-400 font-bold">⚡ LIVE REST API INSPECTOR</span>
          <span id="inspector-summary" class="text-slate-400 text-[11px]">// Sẵn sàng bắt các lời gọi HTTP tới Spring Boot</span>
        </div>
        <div class="flex items-center gap-2">
          <button onclick="copyInspectorData()" class="px-2 py-0.5 rounded bg-slate-700 hover:bg-slate-600 text-[11px]">📋 Sao chép</button>
          <button onclick="toggleInspector()" class="p-1 text-slate-400 hover:text-white">✕</button>
        </div>
      </div>
      <div class="flex-1 grid grid-cols-1 md:grid-cols-2 divide-y md:divide-y-0 md:divide-x divide-slate-800 overflow-hidden">
        <div class="p-3 overflow-y-auto">
          <div class="text-[10px] uppercase font-bold text-slate-400 mb-1">Request Payload:</div>
          <pre id="req-log" class="text-slate-300 text-[11px]">// Chưa có request</pre>
        </div>
        <div class="p-3 overflow-y-auto bg-slate-950">
          <div class="text-[10px] uppercase font-bold text-slate-400 mb-1 flex justify-between">
            <span>Response JSON:</span>
            <span id="res-status-badge"></span>
          </div>
          <pre id="res-log" class="text-emerald-400 text-[11px]">// Chưa có response</pre>
        </div>
      </div>
    </div>
  `;
  document.body.appendChild(div);
}

function toggleInspector() {
  const drawer = document.getElementById('inspector-drawer');
  if (!drawer) return;
  drawer.classList.toggle('translate-y-full');
}

function copyInspectorData() {
  const req = document.getElementById('req-log')?.textContent || '';
  const res = document.getElementById('res-log')?.textContent || '';
  navigator.clipboard.writeText(`REQUEST:\n${req}\n\nRESPONSE:\n${res}`);
  showToast('Đã sao chép log Request/Response vào bộ nhớ tạm!');
}

document.addEventListener('DOMContentLoaded', () => {
  renderCartDrawerHtml();
  renderSharedInspectorHtml();
});
