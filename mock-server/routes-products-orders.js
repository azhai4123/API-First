/**
 * ROUTE HANDLERS: PRODUCTS & ORDERS
 */
const { sendJson, sendError } = require('./routes-auth-customers');

function handleProductRoutes(req, res, pathname, method, query, payload, db) {
  const { products } = db;

  // GET /api/v1/products
  if (method === 'GET' && pathname === '/api/v1/products') {
    const page = parseInt(query.get('page') || '0', 10);
    const size = parseInt(query.get('size') || '10', 10);
    const category = query.get('category');
    const stock = query.get('stock');
    const search = (query.get('search') || '').toLowerCase();
    const sort = query.get('sort');

    let filtered = products.filter(p => {
      let match = true;
      if (category && category !== 'ALL') {
        const catQuery = category.toLowerCase().trim();
        const pCat = p.category.toLowerCase().trim();
        if (!pCat.includes(catQuery) && !catQuery.includes(pCat)) match = false;
      }
      if (stock === 'in_stock' && p.stockQuantity <= 0) match = false;
      if (stock === 'out_of_stock' && p.stockQuantity > 0) match = false;
      if (search && !p.name.toLowerCase().includes(search) && !p.sku.toLowerCase().includes(search)) match = false;
      return match;
    });

    if (sort === 'price,asc') filtered.sort((a, b) => a.price - b.price);
    else if (sort === 'price,desc') filtered.sort((a, b) => b.price - a.price);
    else if (sort === 'name,asc') filtered.sort((a, b) => a.name.localeCompare(b.name));

    const start = page * size;
    const paginated = filtered.slice(start, start + size);
    const totalPages = Math.ceil(filtered.length / size) || 1;
    return sendJson(res, 200, {
      content: paginated,
      page: { number: page, size, totalElements: filtered.length, totalPages, last: page >= totalPages - 1 }
    });
  }

  // POST /api/v1/products
  if (method === 'POST' && pathname === '/api/v1/products') {
    const details = [];
    if (!payload?.name || payload.name.trim().length < 2) details.push({ field: "name", issue: "Tên ít nhất 2 ký tự" });
    if (!payload?.sku || !/^[A-Z0-9_-]{3,30}$/.test(payload.sku)) details.push({ field: "sku", issue: "SKU không đúng định dạng" });
    if (typeof payload?.price !== 'number' || payload.price < 1000) details.push({ field: "price", issue: "Giá tối thiểu 1.000 VND" });
    if (typeof payload?.stockQuantity !== 'number' || payload.stockQuantity < 0) details.push({ field: "stockQuantity", issue: "Tồn kho không âm" });
    if (!payload?.category) details.push({ field: "category", issue: "Danh mục bắt buộc" });

    const catId = payload.categoryId ? parseInt(payload.categoryId, 10) : null;
    let categoryName = payload.category;
    if (catId) {
      const catObj = db.categories?.find(c => c.id === catId);
      if (!catObj) return sendError(res, 404, "CATEGORY_NOT_FOUND", `Danh mục ID ${catId} không tồn tại`);
      categoryName = catObj.name;
    } else if (categoryName) {
      const catObj = db.categories?.find(c => c.name.toLowerCase() === categoryName.toLowerCase());
      if (catObj) payload.categoryId = catObj.id;
    }

    if (details.length > 0) return sendError(res, 400, "VALIDATION_FAILED", "Dữ liệu sản phẩm không hợp lệ", details);
    if (products.some(p => p.sku === payload.sku)) return sendError(res, 409, "SKU_ALREADY_EXISTS", "Mã SKU này đã tồn tại");

    const nowProd = new Date().toISOString();
    const newProduct = {
      id: products.length ? Math.max(...products.map(p => p.id)) + 1 : 101,
      categoryId: payload.categoryId || 1,
      category: categoryName || "Phụ kiện máy tính",
      name: payload.name.trim(),
      sku: payload.sku.trim(),
      price: payload.price,
      stockQuantity: payload.stockQuantity,
      description: payload.description ? payload.description.trim() : "",
      imageUrl: payload.imageUrl || "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500",
      status: payload.stockQuantity > 0 ? "ACTIVE" : "OUT_OF_STOCK",
      createdAt: nowProd,
      updatedAt: nowProd
    };
    products.push(newProduct);
    return sendJson(res, 201, newProduct, { 'Location': `/api/v1/products/${newProduct.id}` });
  }

  // GET /api/v1/products/{id}
  const prodMatch = pathname.match(/^\/api\/v1\/products\/(\d+)$/);
  if (method === 'GET' && prodMatch) {
    const id = parseInt(prodMatch[1], 10);
    const prod = products.find(p => p.id === id);
    if (!prod) return sendError(res, 404, "PRODUCT_NOT_FOUND", `Không tìm thấy sản phẩm ID ${id}`);
    return sendJson(res, 200, prod);
  }

  // PATCH /api/v1/products/{id}
  if (method === 'PATCH' && prodMatch) {
    const id = parseInt(prodMatch[1], 10);
    const prod = products.find(p => p.id === id);
    if (!prod) return sendError(res, 404, "PRODUCT_NOT_FOUND", `Không tìm thấy sản phẩm ID ${id}`);

    if (payload?.categoryId) {
      const catId = parseInt(payload.categoryId, 10);
      const catObj = db.categories?.find(c => c.id === catId);
      if (!catObj) return sendError(res, 404, "CATEGORY_NOT_FOUND", `Danh mục ID ${catId} không tồn tại`);
      prod.categoryId = catId;
      prod.category = catObj.name;
    } else if (payload?.category) {
      prod.category = payload.category.trim();
      const catObj = db.categories?.find(c => c.name.toLowerCase() === payload.category.toLowerCase());
      if (catObj) prod.categoryId = catObj.id;
    }

    if (payload?.name) prod.name = payload.name.trim();
    if (typeof payload?.price === 'number') prod.price = payload.price;
    if (typeof payload?.stockQuantity === 'number') {
      prod.stockQuantity = payload.stockQuantity;
      prod.status = prod.stockQuantity > 0 ? "ACTIVE" : "OUT_OF_STOCK";
    }
    if (payload?.description !== undefined) prod.description = payload.description.trim();
    if (payload?.imageUrl) prod.imageUrl = payload.imageUrl;
    prod.updatedAt = new Date().toISOString(); // Cập nhật thời gian sửa đổi, giữ nguyên createdAt (FF-06)
    return sendJson(res, 200, prod);
  }

  // DELETE /api/v1/products/{id}
  if (method === 'DELETE' && prodMatch) {
    const id = parseInt(prodMatch[1], 10);
    const idx = products.findIndex(p => p.id === id);
    if (idx === -1) return sendError(res, 404, "PRODUCT_NOT_FOUND", `Không tìm thấy sản phẩm ID ${id}`);
    products.splice(idx, 1);
    return sendJson(res, 204, null);
  }

  return false;
}

function handleOrderRoutes(req, res, pathname, method, query, payload, db) {
  const { customers, products, orders } = db;

  // GET /api/v1/orders
  if (method === 'GET' && pathname === '/api/v1/orders') {
    const page = parseInt(query.get('page') || '0', 10);
    const size = parseInt(query.get('size') || '10', 10);
    const status = query.get('status');
    const custId = query.get('customerId');

    let filtered = orders.filter(o => {
      let match = true;
      if (status && o.status !== status) match = false;
      if (custId && o.customerId !== parseInt(custId, 10)) match = false;
      return match;
    });

    const start = page * size;
    const paginated = filtered.slice(start, start + size);
    const totalPages = Math.ceil(filtered.length / size) || 1;
    return sendJson(res, 200, {
      content: paginated,
      page: { number: page, size, totalElements: filtered.length, totalPages, last: page >= totalPages - 1 }
    });
  }

  // POST /api/v1/orders
  if (method === 'POST' && pathname === '/api/v1/orders') {
    const details = [];
    if (!payload?.customerId) details.push({ field: "customerId", issue: "ID khách hàng bắt buộc" });
    if (!payload?.shippingAddress || payload.shippingAddress.trim().length < 5) details.push({ field: "shippingAddress", issue: "Địa chỉ giao hàng ít nhất 5 ký tự" });
    if (!payload?.items || !Array.isArray(payload.items) || payload.items.length === 0) details.push({ field: "items", issue: "Danh sách sản phẩm ít nhất 1 món" });

    if (details.length > 0) return sendError(res, 400, "VALIDATION_FAILED", "Dữ liệu đơn hàng không hợp lệ", details);
    const customer = customers.find(c => c.id === payload.customerId);
    if (!customer) return sendError(res, 404, "CUSTOMER_NOT_FOUND", `Khách hàng ID ${payload.customerId} không tồn tại`);

    let totalAmount = 0;
    const orderItems = [];
    const nowOrder = new Date().toISOString();

    for (let i = 0; i < payload.items.length; i++) {
      const itemReq = payload.items[i];
      if (typeof itemReq?.quantity !== 'number' || itemReq.quantity <= 0 || !Number.isInteger(itemReq.quantity)) {
        return sendError(res, 400, "VALIDATION_FAILED", "Số lượng mặt hàng không hợp lệ", [
          { field: `items[${i}].quantity`, issue: "Số lượng phải là số nguyên dương lớn hơn hoặc bằng 1" }
        ]);
      }
      const product = products.find(p => p.id === itemReq.productId);
      if (!product) return sendError(res, 404, "PRODUCT_NOT_FOUND", `Sản phẩm ID ${itemReq.productId} không tồn tại`);
      if (product.stockQuantity < itemReq.quantity) {
        return sendError(res, 422, "OUT_OF_STOCK", `Sản phẩm '${product.name}' chỉ còn ${product.stockQuantity} món trong kho (không đủ để đặt ${itemReq.quantity})`, [
          { field: `items[${i}].quantity`, issue: `Số lượng yêu cầu (${itemReq.quantity}) vượt quá tồn kho hiện tại (${product.stockQuantity})` }
        ]);
      }
      const subTotal = product.price * itemReq.quantity;
      totalAmount += subTotal;
      orderItems.push({
        id: (orders.length * 10) + i + 9001,
        productId: product.id,
        productName: product.name,
        unitPrice: product.price, // Bảo toàn đơn giá lịch sử (FF-03)
        quantity: itemReq.quantity,
        subTotal: subTotal,
        createdAt: nowOrder
      });
    }

    for (const itemReq of payload.items) {
      const product = products.find(p => p.id === itemReq.productId);
      product.stockQuantity -= itemReq.quantity;
      if (product.stockQuantity === 0) product.status = "OUT_OF_STOCK";
    }

    const newOrder = {
      id: orders.length ? Math.max(...orders.map(o => o.id)) + 1 : 5001,
      customerId: payload.customerId,
      totalAmount: totalAmount,
      status: "PENDING",
      shippingAddress: payload.shippingAddress.trim(),
      items: orderItems,
      createdAt: nowOrder,
      updatedAt: nowOrder
    };
    orders.push(newOrder);
    return sendJson(res, 201, newOrder, { 'Location': `/api/v1/orders/${newOrder.id}` });
  }

  // GET /api/v1/orders/{id}
  const orderMatch = pathname.match(/^\/api\/v1\/orders\/(\d+)$/);
  if (method === 'GET' && orderMatch) {
    const id = parseInt(orderMatch[1], 10);
    const order = orders.find(o => o.id === id);
    if (!order) return sendError(res, 404, "ORDER_NOT_FOUND", `Không tìm thấy đơn hàng ID ${id}`);
    return sendJson(res, 200, order);
  }

  // PATCH /api/v1/orders/{id}/status
  const orderStatusMatch = pathname.match(/^\/api\/v1\/orders\/(\d+)\/status$/);
  if (method === 'PATCH' && orderStatusMatch) {
    const id = parseInt(orderStatusMatch[1], 10);
    const order = orders.find(o => o.id === id);
    if (!order) return sendError(res, 404, "ORDER_NOT_FOUND", `Không tìm thấy đơn hàng ID ${id}`);

    const validStatuses = ['PENDING', 'PAID', 'SHIPPED', 'SHIPPING', 'CANCELLED', 'CONFIRMED', 'DELIVERED'];
    if (!payload?.status || !validStatuses.includes(payload.status)) {
      return sendError(res, 400, "INVALID_STATUS", `Trạng thái không hợp lệ. Phải là: ${validStatuses.join(', ')}`);
    }

    // Fail-Fast FF-04: Đơn hàng ở trạng thái kết thúc (DELIVERED/SHIPPED/CANCELLED) là bất biến
    if (order.status === 'DELIVERED' || order.status === 'SHIPPED' || order.status === 'CANCELLED') {
      return sendError(res, 422, "INVALID_STATE_TRANSITION", `Đơn hàng ở trạng thái ${order.status} không thể chuyển trạng thái tiếp`);
    }

    // Kiểm tra tính tuần tự của vòng đời đơn hàng:
    // Chuẩn đề bài: PENDING -> PAID -> SHIPPED hoặc CANCELLED
    // Hỗ trợ đồng thời: PENDING -> CONFIRMED -> SHIPPING -> DELIVERED
    const ALLOWED_TRANSITIONS = {
      'PENDING': ['PAID', 'CONFIRMED', 'CANCELLED'],
      'PAID': ['SHIPPED', 'CANCELLED'],
      'CONFIRMED': ['SHIPPING', 'DELIVERED', 'SHIPPED', 'CANCELLED'],
      'SHIPPING': ['DELIVERED', 'SHIPPED', 'CANCELLED']
    };
    if (!ALLOWED_TRANSITIONS[order.status]?.includes(payload.status)) {
      return sendError(res, 422, "INVALID_STATE_TRANSITION", `Không thể chuyển trạng thái từ ${order.status} sang ${payload.status}`);
    }

    // Nghiệp vụ Hoàn lại số lượng tồn kho khi Hủy đơn (Bắt buộc theo Đề bài)
    if (payload.status === 'CANCELLED' && order.status !== 'CANCELLED') {
      if (order.items && Array.isArray(order.items)) {
        order.items.forEach(item => {
          const prod = products.find(p => p.id === item.productId);
          if (prod) {
            prod.stockQuantity += item.quantity;
            if (prod.stockQuantity > 0 && prod.status === 'OUT_OF_STOCK') {
              prod.status = 'ACTIVE';
            }
          }
        });
      }
    }

    order.status = payload.status;
    order.updatedAt = new Date().toISOString(); // Cập nhật thời gian sửa đổi (FF-06)
    return sendJson(res, 200, order);
  }

  return false;
}

module.exports = { handleProductRoutes, handleOrderRoutes };
