/**
 * CATEGORY MANAGEMENT MODULE (ADMIN / STAFF)
 */
async function loadCategories() {
  const res = await apiCall('GET', '/categories');
  const cats = res.data || [];
  state.categories = cats;

  // Populate category dropdowns
  populateCategoryDropdowns(cats);

  // Render categories table in Admin pane
  const tbody = document.querySelector('#admin-cats-table tbody');
  if (!tbody) return;

  if (cats.length === 0) {
    tbody.innerHTML = `<tr><td colspan="5" class="text-center py-6 text-slate-400">Chưa có danh mục nào.</td></tr>`;
    return;
  }

  tbody.innerHTML = cats.map(c => `
    <tr class="hover:bg-slate-50 transition border-b border-slate-100">
      <td class="p-3 font-mono font-bold text-slate-700">#${c.id}</td>
      <td class="p-3">
        <div class="font-semibold text-slate-800">${c.name}</div>
        <div class="text-xs text-slate-400">${c.description || ''}</div>
      </td>
      <td class="p-3"><code class="px-2 py-0.5 rounded text-xs bg-slate-100 text-slate-700 font-mono">${c.code}</code></td>
      <td class="p-3"><span class="px-2 py-0.5 rounded-full text-xs font-bold ${c.status === 'ACTIVE' ? 'bg-emerald-100 text-emerald-800' : 'bg-slate-100 text-slate-600'}">${c.status}</span></td>
      <td class="p-3 text-right">
        <div class="flex justify-end gap-1.5">
          <button onclick="openEditCategory(${c.id})" class="px-2 py-1 bg-amber-50 text-amber-600 hover:bg-amber-100 rounded text-xs font-medium">✏️ Sửa</button>
          <button onclick="deleteCategory(${c.id}, '${c.name.replace(/'/g, "\\'")}')" class="px-2 py-1 bg-rose-50 text-rose-600 hover:bg-rose-100 rounded text-xs font-medium">🗑️ Xóa</button>
        </div>
      </td>
    </tr>
  `).join('');
}

function populateCategoryDropdowns(cats) {
  // 1. Buyer filter
  const buyerCat = document.getElementById('buyer-cat');
  if (buyerCat) {
    const cur = buyerCat.value;
    buyerCat.innerHTML = `<option value="ALL">📁 Tất cả danh mục</option>` + cats.map(c => `<option value="${c.name}">${c.name}</option>`).join('');
    if (cur) buyerCat.value = cur;
  }

  // 2. Admin product filter
  const adminProdCat = document.getElementById('admin-prod-cat');
  if (adminProdCat) {
    const cur = adminProdCat.value;
    adminProdCat.innerHTML = `<option value="ALL">Tất cả danh mục</option>` + cats.map(c => `<option value="${c.name}">${c.name}</option>`).join('');
    if (cur) adminProdCat.value = cur;
  }

  // 3. New Product modal select
  const newProdCat = document.getElementById('new-prod-cat');
  if (newProdCat) {
    newProdCat.innerHTML = cats.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
  }

  // 4. Edit Product modal select
  const editProdCat = document.getElementById('edit-prod-cat');
  if (editProdCat) {
    editProdCat.innerHTML = cats.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
  }
}

async function handleCreateCategory(e) {
  e.preventDefault();
  const payload = {
    code: document.getElementById('new-cat-code').value.trim().toUpperCase(),
    name: document.getElementById('new-cat-name').value.trim(),
    description: document.getElementById('new-cat-desc').value.trim()
  };

  const res = await apiCall('POST', '/categories', payload);
  if (res.status === 201) {
    closeModal('modal-category-add');
    showToast(`Đã thêm danh mục mới: "${payload.name}"!`);
    loadCategories();
  } else {
    showToast(res.data?.message || 'Lỗi thêm danh mục', true);
  }
}

async function openEditCategory(id) {
  const res = await apiCall('GET', `/categories/${id}`);
  if (res.status === 200 && res.data) {
    const c = res.data;
    document.getElementById('edit-cat-id').value = c.id;
    document.getElementById('edit-cat-code').value = c.code;
    document.getElementById('edit-cat-name').value = c.name;
    document.getElementById('edit-cat-desc').value = c.description || '';
    document.getElementById('edit-cat-status').value = c.status || 'ACTIVE';
    openModal('modal-category-edit');
  }
}

async function handleUpdateCategory(e) {
  e.preventDefault();
  const id = document.getElementById('edit-cat-id').value;
  const payload = {
    name: document.getElementById('edit-cat-name').value.trim(),
    description: document.getElementById('edit-cat-desc').value.trim(),
    status: document.getElementById('edit-cat-status').value
  };

  const res = await apiCall('PUT', `/categories/${id}`, payload);
  if (res.status === 200) {
    closeModal('modal-category-edit');
    showToast(`Cập nhật danh mục #${id} thành công!`);
    loadCategories();
    loadAdminProducts();
    loadBuyerCatalog();
  } else {
    showToast(res.data?.message || 'Lỗi cập nhật danh mục', true);
  }
}

async function deleteCategory(id, name) {
  if (!confirm(`Bạn có chắc muốn xóa danh mục #${id} [${name}]?`)) return;
  const res = await apiCall('DELETE', `/categories/${id}`);
  if (res.status === 204) {
    showToast(`Đã xóa danh mục #${id}`);
    loadCategories();
  } else {
    // Fail-Fast thông báo rõ ràng lỗi ràng buộc khóa ngoại
    showToast(res.data?.message || 'Không thể xóa danh mục này', true);
  }
}

