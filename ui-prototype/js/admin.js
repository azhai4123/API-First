/**
 * ADMIN PORTAL & CRUD MODULE
 */
async function loadAdminProducts(resetPage = false) {
  if (resetPage) state.prodPage = 0;
  const search = document.getElementById('admin-prod-search')?.value || '';
  const category = document.getElementById('admin-prod-cat')?.value || 'ALL';
  const stock = document.getElementById('admin-prod-stock')?.value || 'all';

  const query = new URLSearchParams({ page: state.prodPage, size: state.prodSize });
  if (search) query.append('search', search);
  if (category && category !== 'ALL') query.append('category', category);
  if (stock && stock !== 'all') query.append('stock', stock);

  const res = await apiCall('GET', `/products?${query.toString()}`);
  const tbody = document.querySelector('#admin-prod-table tbody');
  if (!tbody) return;

  const items = res.data?.content || [];
  if (items.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" class="text-center py-6 text-slate-400">Không có sản phẩm nào.</td></tr>`;
    return;
  }

  tbody.innerHTML = items.map(p => `
    <tr class="hover:bg-slate-50 transition border-b border-slate-100">
      <td class="p-3 font-mono font-medium text-slate-500">#${p.id}</td>
      <td class="p-3">
        <div class="flex items-center gap-3">
          <img src="${p.imageUrl}" class="w-10 h-10 rounded object-cover border border-slate-200" onerror="this.src='https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500'" />
          <div>
            <div class="font-semibold text-slate-800">${p.name}</div>
            <div class="text-xs font-mono text-slate-400">${p.sku}</div>
          </div>
        </div>
      </td>
      <td class="p-3"><span class="px-2 py-0.5 rounded text-xs bg-slate-100 text-slate-700">${p.category}</span></td>
      <td class="p-3 font-semibold text-blue-600">${p.price.toLocaleString()} đ</td>
      <td class="p-3">
        <span class="px-2 py-0.5 rounded text-xs font-bold ${p.stockQuantity > 0 ? 'bg-emerald-100 text-emerald-800' : 'bg-rose-100 text-rose-800'}">
          ${p.stockQuantity > 0 ? `Còn ${p.stockQuantity}` : 'HẾT HÀNG'}
        </span>
      </td>
      <td class="p-3 text-right">
        <div class="flex justify-end gap-1.5">
          <button onclick="viewProductDetail(${p.id})" class="px-2 py-1 bg-slate-100 text-slate-600 hover:bg-slate-200 rounded text-xs font-medium" title="Xem chi tiết">🔍</button>
          <button onclick="openEditProduct(${p.id})" class="px-2 py-1 bg-amber-50 text-amber-600 hover:bg-amber-100 rounded text-xs font-medium">✏️ Sửa</button>
          <button onclick="deleteProduct(${p.id}, '${p.name.replace(/'/g, "\\'")}')" class="px-2 py-1 bg-rose-50 text-rose-600 hover:bg-rose-100 rounded text-xs font-medium">🗑️</button>
        </div>
      </td>
    </tr>
  `).join('');

  if (res.data?.page) {
    state.prodTotalPages = res.data.page.totalPages;
    const pageCurrent = document.getElementById('admin-prod-page-current');
    if (pageCurrent) pageCurrent.textContent = `Trang ${res.data.page.number + 1} / ${res.data.page.totalPages}`;
  }
}

async function openEditProduct(id) {
  const res = await apiCall('GET', `/products/${id}`);
  if (res.status === 200 && res.data) {
    document.getElementById('edit-prod-id').value = res.data.id;
    document.getElementById('edit-prod-name').value = res.data.name;
    document.getElementById('edit-prod-cat').value = res.data.categoryId || 1;
    document.getElementById('edit-prod-price').value = res.data.price;
    document.getElementById('edit-prod-stock').value = res.data.stockQuantity;
    document.getElementById('edit-prod-desc').value = res.data.description || '';
    document.getElementById('edit-prod-img').value = res.data.imageUrl || '';
    openModal('modal-product-edit');
  }
}

async function handleUpdateProduct(e) {
  e.preventDefault();
  const id = document.getElementById('edit-prod-id').value;
  const catSelect = document.getElementById('edit-prod-cat');
  const catId = parseInt(catSelect.value, 10);
  const catName = catSelect.options[catSelect.selectedIndex]?.text || '';

  const payload = {
    name: document.getElementById('edit-prod-name').value,
    categoryId: catId,
    category: catName,
    price: parseFloat(document.getElementById('edit-prod-price').value),
    stockQuantity: parseInt(document.getElementById('edit-prod-stock').value, 10),
    description: document.getElementById('edit-prod-desc').value,
    imageUrl: document.getElementById('edit-prod-img').value
  };
  const res = await apiCall('PATCH', `/products/${id}`, payload);
  if (res.status === 200) {
    closeModal('modal-product-edit');
    showToast(`Đã cập nhật sản phẩm #${id}`);
    loadAdminProducts();
    loadBuyerCatalog();
  }
}

async function handleCreateProduct(e) {
  e.preventDefault();
  const catSelect = document.getElementById('new-prod-cat');
  const catId = parseInt(catSelect.value, 10);
  const catName = catSelect.options[catSelect.selectedIndex]?.text || '';

  const payload = {
    name: document.getElementById('new-prod-name').value,
    sku: document.getElementById('new-prod-sku').value,
    categoryId: catId,
    category: catName,
    price: parseFloat(document.getElementById('new-prod-price').value),
    stockQuantity: parseInt(document.getElementById('new-prod-stock').value, 10),
    description: document.getElementById('new-prod-desc').value,
    imageUrl: document.getElementById('new-prod-img').value
  };
  const res = await apiCall('POST', '/products', payload);
  if (res.status === 201) {
    closeModal('modal-product-add');
    showToast(`Đã thêm mới sản phẩm "${payload.name}"!`);
    loadAdminProducts();
    loadBuyerCatalog();
  } else {
    showToast(res.data?.message || 'Lỗi thêm sản phẩm', true);
  }
}

async function deleteProduct(id, name) {
  if (!confirm(`Bạn có chắc chắn muốn xóa sản phẩm #${id} [${name}]?`)) return;
  const res = await apiCall('DELETE', `/products/${id}`);
  if (res.status === 204) {
    showToast(`Đã xóa sản phẩm #${id}`);
    loadAdminProducts();
    loadBuyerCatalog();
  }
}

async function loadAdminOrders() {
  const res = await apiCall('GET', '/orders');
  const tbody = document.querySelector('#admin-orders-table tbody');
  if (!tbody) return;

  const orders = res.data?.content || [];
  tbody.innerHTML = orders.map(o => `
    <tr class="hover:bg-slate-50 transition border-b border-slate-100">
      <td class="p-3 font-mono font-bold text-slate-700">#${o.id}</td>
      <td class="p-3 font-medium">Khách #${o.customerId}</td>
      <td class="p-3 font-semibold text-blue-600">${o.totalAmount.toLocaleString()} đ</td>
      <td class="p-3">
        <select onchange="updateOrderStatus(${o.id}, this.value)" class="text-xs font-semibold rounded border border-slate-200 px-2 py-1 ${o.status === 'DELIVERED' ? 'bg-emerald-50 text-emerald-700' : (o.status === 'CANCELLED' ? 'bg-rose-50 text-rose-700' : 'bg-amber-50 text-amber-700')}">
          ${['PENDING', 'CONFIRMED', 'SHIPPING', 'DELIVERED', 'CANCELLED'].map(s => `<option value="${s}" ${s === o.status ? 'selected' : ''}>${s}</option>`).join('')}
        </select>
      </td>
      <td class="p-3 text-xs text-slate-500">${new Date(o.createdAt).toLocaleString()}</td>
    </tr>
  `).join('');
}

async function updateOrderStatus(orderId, newStatus) {
  const res = await apiCall('PATCH', `/orders/${orderId}/status`, { status: newStatus });
  if (res.status === 200) {
    showToast(`Đã cập nhật trạng thái đơn #${orderId} -> ${newStatus}`);
    loadAdminOrders();
  } else {
    showToast(res.data?.message || 'Không thể đổi trạng thái', true);
    loadAdminOrders();
  }
}

async function loadAdminCustomers() {
  const res = await apiCall('GET', '/customers');
  const tbody = document.querySelector('#admin-cust-table tbody');
  if (!tbody) return;

  const custs = res.data?.content || [];
  tbody.innerHTML = custs.map(c => `
    <tr class="hover:bg-slate-50 transition border-b border-slate-100">
      <td class="p-3 font-mono">#${c.id}</td>
      <td class="p-3">
        <div class="flex items-center gap-2">
          <img src="${c.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150'}" class="w-7 h-7 rounded-full object-cover" />
          <div>
            <div class="font-medium text-slate-800">${c.fullName}</div>
            <div class="text-xs text-slate-400 font-mono">${c.email}</div>
          </div>
        </div>
      </td>
      <td class="p-3"><span class="px-2 py-0.5 rounded text-xs font-mono font-semibold ${c.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' : 'bg-slate-100 text-slate-800'}">${c.role || 'CUSTOMER'}</span></td>
      <td class="p-3 text-xs text-slate-600">${c.phone}</td>
      <td class="p-3"><span class="px-2 py-0.5 rounded-full text-xs font-bold bg-emerald-100 text-emerald-800">${c.status}</span></td>
    </tr>
  `).join('');
}

