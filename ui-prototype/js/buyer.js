/**
 * BUYER STOREFRONT & CART MODULE
 */
async function loadBuyerCatalog() {
  const search = document.getElementById('buyer-search')?.value || '';
  const category = document.getElementById('buyer-cat')?.value || 'ALL';
  const query = new URLSearchParams({ page: 0, size: 20 });
  if (search) query.append('search', search);
  if (category && category !== 'ALL') query.append('category', category);

  const res = await apiCall('GET', `/products?${query.toString()}`);
  const grid = document.getElementById('buyer-products-grid');
  if (!grid) return;

  const items = res.data?.content || [];
  if (items.length === 0) {
    grid.innerHTML = `<div class="col-span-full text-center py-12 text-slate-400">Không tìm thấy sản phẩm nào phù hợp.</div>`;
    return;
  }

  grid.innerHTML = items.map(p => `
    <div class="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-sm hover:shadow-md transition flex flex-col group">
      <div onclick="viewProductDetail(${p.id})" class="h-44 bg-slate-100 overflow-hidden relative cursor-pointer" title="Bấm để xem chi tiết sản phẩm">
        <img src="${p.imageUrl}" alt="${p.name}" class="w-full h-full object-cover transition duration-300 group-hover:scale-105" onerror="this.src='https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500'" />
        <span class="absolute top-2 right-2 px-2 py-0.5 rounded text-xs font-semibold ${p.stockQuantity > 0 ? 'bg-emerald-100 text-emerald-800' : 'bg-rose-100 text-rose-800'}">
          ${p.stockQuantity > 0 ? `Còn ${p.stockQuantity}` : 'Hết hàng'}
        </span>
      </div>
      <div class="p-4 flex-1 flex flex-col justify-between">
        <div onclick="viewProductDetail(${p.id})" class="cursor-pointer" title="Bấm để xem chi tiết sản phẩm">
          <span class="text-xs font-medium text-blue-600 bg-blue-50 px-2 py-0.5 rounded">${p.category}</span>
          <h3 class="font-semibold text-slate-800 mt-2 text-sm line-clamp-2 hover:text-blue-600 transition" title="${p.name}">${p.name}</h3>
          <p class="text-xs text-slate-500 mt-1 line-clamp-2">${p.description || ''}</p>
        </div>
        <div class="mt-4 pt-3 border-t border-slate-100 flex items-center justify-between">
          <div class="font-bold text-blue-600 text-base">${p.price.toLocaleString()} đ</div>
          <div class="flex items-center gap-1.5">
            <button onclick="viewProductDetail(${p.id})" class="p-1.5 rounded-lg border border-slate-200 hover:bg-slate-100 text-slate-600 text-xs" title="Xem chi tiết">🔍</button>
            <button onclick="addToCart(${p.id}, '${p.name.replace(/'/g, "\\'")}', ${p.price}, ${p.stockQuantity})" 
              ${p.stockQuantity === 0 ? 'disabled' : ''}
              class="px-3 py-1.5 rounded-lg text-xs font-medium ${p.stockQuantity > 0 ? 'bg-blue-600 hover:bg-blue-700 text-white' : 'bg-slate-200 text-slate-400 cursor-not-allowed'} transition">
              ${p.stockQuantity > 0 ? '🛒 Thêm' : 'Hết hàng'}
            </button>
          </div>
        </div>
      </div>
    </div>
  `).join('');
}

function addToCart(productId, name, price, stock) {
  const existing = state.cart.find(item => item.productId === productId);
  if (existing) {
    if (existing.quantity >= stock) {
      showToast(`Kho hàng chỉ còn ${stock} sản phẩm!`, true);
      return;
    }
    existing.quantity++;
  } else {
    state.cart.push({ productId, name, price, quantity: 1, stock });
  }
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartBadge();
  showToast(`Đã thêm "${name}" vào giỏ hàng`);
}

function updateCartBadge() {
  const count = state.cart.reduce((sum, i) => sum + i.quantity, 0);
  const badge = document.getElementById('cart-badge');
  if (badge) {
    badge.textContent = count;
    badge.classList.toggle('hidden', count === 0);
  }
}

function renderCartDrawer() {
  const container = document.getElementById('cart-items-container');
  const totalEl = document.getElementById('cart-total-price');
  if (!container) return;

  if (state.cart.length === 0) {
    container.innerHTML = `<div class="text-center py-10 text-slate-400 text-sm">Giỏ hàng của bạn đang trống</div>`;
    if (totalEl) totalEl.textContent = '0 đ';
    return;
  }

  let total = 0;
  container.innerHTML = state.cart.map(item => {
    const sub = item.price * item.quantity;
    total += sub;
    return `
      <div class="flex items-center justify-between py-2 border-b border-slate-100 text-sm">
        <div class="flex-1 pr-2">
          <div class="font-medium text-slate-800 line-clamp-1">${item.name}</div>
          <div class="text-xs text-blue-600">${item.price.toLocaleString()} đ x ${item.quantity}</div>
        </div>
        <div class="flex items-center gap-2">
          <button onclick="changeCartQty(${item.productId}, -1)" class="w-6 h-6 rounded bg-slate-100 text-slate-600 flex items-center justify-center font-bold text-xs">-</button>
          <span class="text-xs font-semibold">${item.quantity}</span>
          <button onclick="changeCartQty(${item.productId}, 1)" class="w-6 h-6 rounded bg-slate-100 text-slate-600 flex items-center justify-center font-bold text-xs">+</button>
          <button onclick="removeCartItem(${item.productId})" class="text-rose-500 hover:text-rose-700 ml-1 text-xs">✕</button>
        </div>
      </div>
    `;
  }).join('');
  if (totalEl) totalEl.textContent = `${total.toLocaleString()} đ`;
}

function changeCartQty(productId, delta) {
  const item = state.cart.find(i => i.productId === productId);
  if (!item) return;
  item.quantity += delta;
  if (item.quantity <= 0) state.cart = state.cart.filter(i => i.productId !== productId);
  else if (item.quantity > item.stock) {
    item.quantity = item.stock;
    showToast(`Kho hàng chỉ còn ${item.stock} sản phẩm!`, true);
  }
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartBadge();
  renderCartDrawer();
}

function removeCartItem(productId) {
  state.cart = state.cart.filter(i => i.productId !== productId);
  localStorage.setItem('shop_cart', JSON.stringify(state.cart));
  updateCartBadge();
  renderCartDrawer();
}

async function handleCheckout() {
  if (state.cart.length === 0) return showToast('Giỏ hàng trống!', true);
  if (!state.currentUser) {
    showToast('Vui lòng đăng nhập để tiến hành đặt hàng', true);
    openModal('modal-login');
    return;
  }

  const payload = {
    customerId: state.currentUser.id,
    shippingAddress: state.currentUser.address || "Địa chỉ mặc định khách hàng",
    items: state.cart.map(i => ({ productId: i.productId, quantity: i.quantity }))
  };

  const res = await apiCall('POST', '/orders', payload);
  if (res.status === 201) {
    state.cart = [];
    localStorage.removeItem('shop_cart');
    updateCartBadge();
    closeModal('drawer-cart');
    showToast(`🎉 Đặt hàng thành công! Mã đơn: #${res.data.id}`);
    loadBuyerCatalog();
    loadBuyerOrders();
  } else {
    showToast(res.data?.message || 'Đặt hàng thất bại', true);
  }
}

async function loadBuyerOrders() {
  if (!state.currentUser) return;
  const res = await apiCall('GET', `/customers/${state.currentUser.id}/orders`);
  const container = document.getElementById('buyer-orders-list');
  if (!container) return;

  const orders = res.data?.content || [];
  if (orders.length === 0) {
    container.innerHTML = `<div class="text-center py-6 text-slate-400 text-sm">Bạn chưa có đơn hàng nào</div>`;
    return;
  }

  container.innerHTML = orders.map(o => `
    <div class="bg-white p-3 rounded-lg border border-slate-200 text-xs flex justify-between items-center">
      <div>
        <span class="font-bold text-slate-800">Đơn #${o.id}</span>
        <span class="text-slate-400 ml-2">${new Date(o.createdAt).toLocaleDateString()}</span>
        <div class="text-slate-600 mt-1">${o.items?.length || 0} sản phẩm · ${o.shippingAddress}</div>
      </div>
      <div class="text-right">
        <div class="font-bold text-blue-600">${o.totalAmount.toLocaleString()} đ</div>
        <span class="inline-block mt-1 px-2 py-0.5 rounded text-[10px] font-bold ${o.status === 'DELIVERED' ? 'bg-emerald-100 text-emerald-700' : (o.status === 'CANCELLED' ? 'bg-rose-100 text-rose-700' : 'bg-amber-100 text-amber-700')}">${o.status}</span>
      </div>
    </div>
  `).join('');
}

async function viewProductDetail(id) {
  const res = await apiCall('GET', `/products/${id}`);
  if (res.status === 200 && res.data) {
    const p = res.data;
    document.getElementById('detail-prod-img').src = p.imageUrl || 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500';
    document.getElementById('detail-prod-id').textContent = `#${p.id}`;
    document.getElementById('detail-prod-name').textContent = p.name;
    document.getElementById('detail-prod-sku').textContent = p.sku;
    document.getElementById('detail-prod-cat').textContent = p.category;
    document.getElementById('detail-prod-price').textContent = `${p.price.toLocaleString()} đ`;
    document.getElementById('detail-prod-desc').textContent = p.description || 'Chưa có thông tin mô tả chi tiết';
    
    const stockEl = document.getElementById('detail-prod-stock');
    if (p.stockQuantity > 0) {
      stockEl.className = "px-2.5 py-1 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-800";
      stockEl.textContent = `Còn ${p.stockQuantity} sản phẩm trong kho`;
    } else {
      stockEl.className = "px-2.5 py-1 rounded-full text-xs font-semibold bg-rose-100 text-rose-800";
      stockEl.textContent = `HẾT HÀNG (0 sản phẩm)`;
    }

    const buyBtn = document.getElementById('detail-prod-btn-buy');
    if (buyBtn) {
      buyBtn.disabled = p.stockQuantity === 0;
      buyBtn.onclick = () => {
        addToCart(p.id, p.name, p.price, p.stockQuantity);
        closeModal('modal-product-detail');
      };
    }
    openModal('modal-product-detail');
  }
}

