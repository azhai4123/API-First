/**
 * ROUTE HANDLERS: CATEGORIES (CRUD & FAIL-FAST)
 */
const { sendJson, sendError } = require('./routes-auth-customers');

function handleCategoryRoutes(req, res, pathname, method, query, payload, db) {
  const { categories, products } = db;

  // GET /api/v1/categories
  if (method === 'GET' && pathname === '/api/v1/categories') {
    return sendJson(res, 200, categories);
  }

  // POST /api/v1/categories
  if (method === 'POST' && pathname === '/api/v1/categories') {
    const details = [];
    if (!payload?.name || payload.name.trim().length < 2) {
      details.push({ field: "name", issue: "Tên danh mục bắt buộc và có ít nhất 2 ký tự" });
    }
    if (!payload?.code || !/^[A-Z0-9_-]{3,30}$/.test(payload.code)) {
      details.push({ field: "code", issue: "Mã danh mục bắt buộc (3-30 ký tự viết hoa, số hoặc gạch nối)" });
    }
    if (details.length > 0) {
      return sendError(res, 400, "VALIDATION_FAILED", "Dữ liệu danh mục không hợp lệ", details);
    }

    if (categories.some(c => c.code.toUpperCase() === payload.code.toUpperCase())) {
      return sendError(res, 409, "CATEGORY_CODE_EXISTS", `Mã danh mục '${payload.code}' đã tồn tại trong hệ thống`);
    }
    if (categories.some(c => c.name.toLowerCase() === payload.name.trim().toLowerCase())) {
      return sendError(res, 409, "CATEGORY_NAME_EXISTS", `Tên danh mục '${payload.name}' đã tồn tại trong hệ thống`);
    }

    const nowCat = new Date().toISOString();
    const newCat = {
      id: categories.length ? Math.max(...categories.map(c => c.id)) + 1 : 1,
      code: payload.code.toUpperCase().trim(),
      name: payload.name.trim(),
      description: payload.description ? payload.description.trim() : "",
      status: "ACTIVE",
      createdAt: nowCat,
      updatedAt: nowCat
    };
    categories.push(newCat);
    return sendJson(res, 201, newCat, { 'Location': `/api/v1/categories/${newCat.id}` });
  }

  // GET /api/v1/categories/{id}
  const catMatch = pathname.match(/^\/api\/v1\/categories\/(\d+)$/);
  if (method === 'GET' && catMatch) {
    const id = parseInt(catMatch[1], 10);
    const cat = categories.find(c => c.id === id);
    if (!cat) return sendError(res, 404, "CATEGORY_NOT_FOUND", `Không tìm thấy danh mục với ID ${id}`);
    return sendJson(res, 200, cat);
  }

  // PUT /api/v1/categories/{id}
  if (method === 'PUT' && catMatch) {
    const id = parseInt(catMatch[1], 10);
    const cat = categories.find(c => c.id === id);
    if (!cat) return sendError(res, 404, "CATEGORY_NOT_FOUND", `Không tìm thấy danh mục với ID ${id}`);
    if (!payload?.name || payload.name.trim().length < 2) {
      return sendError(res, 400, "VALIDATION_FAILED", "Tên danh mục ít nhất 2 ký tự");
    }
    cat.name = payload.name.trim();
    if (payload.description !== undefined) cat.description = payload.description.trim();
    if (payload.status) cat.status = payload.status;
    cat.updatedAt = new Date().toISOString(); // Cập nhật Audit Date

    // Cập nhật tên danh mục đồng bộ sang các sản phẩm
    products.forEach(p => {
      if (p.categoryId === id) p.category = cat.name;
    });

    return sendJson(res, 200, cat);
  }

  // DELETE /api/v1/categories/{id} (Fail-Fast nếu có sản phẩm ràng buộc)
  if (method === 'DELETE' && catMatch) {
    const id = parseInt(catMatch[1], 10);
    const catIdx = categories.findIndex(c => c.id === id);
    if (catIdx === -1) return sendError(res, 404, "CATEGORY_NOT_FOUND", `Không tìm thấy danh mục với ID ${id}`);

    // Fail-Fast: Kiểm tra xem có sản phẩm nào thuộc danh mục này không
    const linkedProducts = products.filter(p => p.categoryId === id);
    if (linkedProducts.length > 0) {
      return sendError(res, 409, "CATEGORY_IN_USE", `Không thể xóa danh mục '${categories[catIdx].name}' vì đang có ${linkedProducts.length} sản phẩm liên kết.`);
    }

    categories.splice(catIdx, 1);
    return sendJson(res, 204, null);
  }

  return false;
}

module.exports = { handleCategoryRoutes };

