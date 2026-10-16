/**
 * ROUTE HANDLERS: AUTHENTICATION & CUSTOMERS
 */
function sendJson(res, statusCode, data, headers = {}) {
  res.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE, OPTIONS',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization',
    ...headers
  });
  res.end(JSON.stringify(data, null, 2));
}

function sendError(res, statusCode, code, message, details = []) {
  return sendJson(res, statusCode, {
    timestamp: new Date().toISOString(),
    status: statusCode,
    code: code,
    error: code,
    message: message,
    details: details
  });
}

function handleAuthRoutes(req, res, pathname, method, payload, db) {
  const { customers } = db;

  // POST /api/v1/auth/login
  if (method === 'POST' && pathname === '/api/v1/auth/login') {
    if (!payload?.email || !payload?.password) {
      return sendError(res, 400, "VALIDATION_FAILED", "Email và mật khẩu không được để trống", [
        { field: "email", issue: "Email bắt buộc" },
        { field: "password", issue: "Mật khẩu bắt buộc" }
      ]);
    }
    const user = customers.find(c => c.email.toLowerCase() === payload.email.toLowerCase() && c.password === payload.password);
    if (!user) {
      return sendError(res, 401, "UNAUTHORIZED", "Email hoặc mật khẩu không chính xác");
    }
    const token = `mock-jwt-token-eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.uid${user.id}.${user.role}`;
    return sendJson(res, 200, {
      token,
      tokenType: "Bearer",
      expiresIn: 86400,
      user: {
        id: user.id,
        fullName: user.fullName,
        email: user.email,
        role: user.role || 'CUSTOMER',
        avatarUrl: user.avatarUrl
      }
    });
  }

  // POST /api/v1/auth/register
  if (method === 'POST' && pathname === '/api/v1/auth/register') {
    const details = [];
    if (!payload?.fullName || payload.fullName.trim().length < 2) details.push({ field: "fullName", issue: "Tên ít nhất 2 ký tự" });
    if (!payload?.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(payload.email)) details.push({ field: "email", issue: "Email không đúng định dạng" });
    if (!payload?.password || payload.password.length < 6) details.push({ field: "password", issue: "Mật khẩu ít nhất 6 ký tự" });
    if (!payload?.phone || !/^[0-9]{10,11}$/.test(payload.phone)) details.push({ field: "phone", issue: "Số điện thoại từ 10-11 số" });
    if (!payload?.address || payload.address.trim().length < 5) details.push({ field: "address", issue: "Địa chỉ ít nhất 5 ký tự" });

    if (details.length > 0) return sendError(res, 400, "VALIDATION_FAILED", "Dữ liệu không hợp lệ", details);
    if (customers.some(c => c.email.toLowerCase() === payload.email.toLowerCase())) {
      return sendError(res, 409, "EMAIL_ALREADY_EXISTS", "Email này đã được sử dụng trong hệ thống");
    }

    const nowIso = new Date().toISOString();
    const newId = customers.length ? Math.max(...customers.map(c => c.id)) + 1 : 1;
    const newUser = {
      id: newId,
      fullName: payload.fullName.trim(),
      email: payload.email.trim(),
      password: payload.password,
      role: "CUSTOMER",
      phone: payload.phone.trim(),
      address: payload.address.trim(),
      avatarUrl: payload.avatarUrl || "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
      status: "ACTIVE",
      createdAt: nowIso,
      updatedAt: nowIso
    };
    customers.push(newUser);

    const token = `mock-jwt-token-eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.uid${newUser.id}.CUSTOMER`;
    return sendJson(res, 201, {
      token,
      tokenType: "Bearer",
      expiresIn: 86400,
      user: {
        id: newUser.id,
        fullName: newUser.fullName,
        email: newUser.email,
        role: newUser.role,
        avatarUrl: newUser.avatarUrl
      }
    });
  }

  // GET /api/v1/auth/me
  if (method === 'GET' && pathname === '/api/v1/auth/me') {
    const authHeader = req.headers['authorization'];
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return sendError(res, 401, "UNAUTHORIZED", "Thiếu Bearer Token");
    }
    const token = authHeader.replace('Bearer ', '');
    const match = token.match(/uid(\d+)/);
    const userId = match ? parseInt(match[1], 10) : 1;
    const user = customers.find(c => c.id === userId);
    if (!user) return sendError(res, 404, "USER_NOT_FOUND", "Không tìm thấy người dùng");
    return sendJson(res, 200, {
      id: user.id,
      fullName: user.fullName,
      email: user.email,
      role: user.role,
      avatarUrl: user.avatarUrl
    });
  }

  return false;
}

function handleCustomerRoutes(req, res, pathname, method, query, payload, db) {
  const { customers, orders } = db;

  // GET /api/v1/customers
  if (method === 'GET' && pathname === '/api/v1/customers') {
    const pageStr = query.get('page');
    const sizeStr = query.get('size');
    const page = pageStr !== null ? parseInt(pageStr, 10) : 0;
    const size = sizeStr !== null ? parseInt(sizeStr, 10) : 10;

    if (isNaN(page) || page < 0 || isNaN(size) || size < 1) {
      return sendError(res, 400, "INVALID_QUERY_PARAMS", "Tham số phân trang không hợp lệ: page phải >= 0 và size phải >= 1");
    }

    const status = query.get('status');
    const search = (query.get('search') || '').toLowerCase();

    let filtered = customers.filter(c => {
      let match = true;
      if (status && c.status !== status) match = false;
      if (search && !c.fullName.toLowerCase().includes(search) && !c.email.toLowerCase().includes(search)) match = false;
      return match;
    });

    const start = page * size;
    const paginated = filtered.slice(start, start + size).map(({ password, ...rest }) => rest);
    const totalPages = Math.ceil(filtered.length / size) || 1;
    return sendJson(res, 200, {
      content: paginated,
      page: { number: page, size, totalElements: filtered.length, totalPages, last: page >= totalPages - 1 }
    });
  }

  // POST /api/v1/customers
  if (method === 'POST' && pathname === '/api/v1/customers') {
    const details = [];
    if (!payload?.fullName || payload.fullName.trim().length < 2) details.push({ field: "fullName", issue: "Họ và tên ít nhất 2 ký tự" });
    if (!payload?.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(payload.email)) details.push({ field: "email", issue: "Email không hợp lệ" });
    if (!payload?.phone || !/^[0-9]{10,11}$/.test(payload.phone)) details.push({ field: "phone", issue: "Số điện thoại từ 10-11 số" });
    if (!payload?.address || payload.address.trim().length < 5) details.push({ field: "address", issue: "Địa chỉ ít nhất 5 ký tự" });

    if (details.length > 0) return sendError(res, 400, "VALIDATION_FAILED", "Dữ liệu không hợp lệ", details);
    if (customers.some(c => c.email.toLowerCase() === payload.email.toLowerCase())) {
      return sendError(res, 409, "EMAIL_ALREADY_EXISTS", "Email đã tồn tại");
    }

    const nowCust = new Date().toISOString();
    const newCustomer = {
      id: customers.length ? Math.max(...customers.map(c => c.id)) + 1 : 1,
      fullName: payload.fullName.trim(),
      email: payload.email.trim(),
      password: "pass123",
      role: payload.role || "CUSTOMER",
      avatarUrl: payload.avatarUrl || "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
      phone: payload.phone.trim(),
      address: payload.address.trim(),
      status: "ACTIVE",
      createdAt: nowCust,
      updatedAt: nowCust
    };
    customers.push(newCustomer);
    const { password, ...resData } = newCustomer;
    return sendJson(res, 201, resData, { 'Location': `/api/v1/customers/${newCustomer.id}` });
  }

  // GET /api/v1/customers/{id}
  const custMatch = pathname.match(/^\/api\/v1\/customers\/(\d+)$/);
  if (method === 'GET' && custMatch) {
    const id = parseInt(custMatch[1], 10);
    const cust = customers.find(c => c.id === id);
    if (!cust) return sendError(res, 404, "CUSTOMER_NOT_FOUND", `Không tìm thấy khách hàng ID ${id}`);
    const { password, ...resData } = cust;
    return sendJson(res, 200, resData);
  }

  // PUT /api/v1/customers/{id}
  if (method === 'PUT' && custMatch) {
    const id = parseInt(custMatch[1], 10);
    const cust = customers.find(c => c.id === id);
    if (!cust) return sendError(res, 404, "CUSTOMER_NOT_FOUND", `Không tìm thấy khách hàng ID ${id}`);
    if (payload?.fullName) cust.fullName = payload.fullName.trim();
    if (payload?.phone) cust.phone = payload.phone.trim();
    if (payload?.address) cust.address = payload.address.trim();
    if (payload?.avatarUrl) cust.avatarUrl = payload.avatarUrl;
    cust.updatedAt = new Date().toISOString(); // Cập nhật Audit Date
    const { password, ...resData } = cust;
    return sendJson(res, 200, resData);
  }

  // DELETE /api/v1/customers/{id}
  if (method === 'DELETE' && custMatch) {
    const id = parseInt(custMatch[1], 10);
    const idx = customers.findIndex(c => c.id === id);
    if (idx === -1) return sendError(res, 404, "CUSTOMER_NOT_FOUND", `Không tìm thấy khách hàng ID ${id}`);
    customers.splice(idx, 1);
    return sendJson(res, 204, null);
  }

  // GET /api/v1/customers/{id}/orders
  const custOrdersMatch = pathname.match(/^\/api\/v1\/customers\/(\d+)\/orders$/);
  if (method === 'GET' && custOrdersMatch) {
    const id = parseInt(custOrdersMatch[1], 10);
    const cust = customers.find(c => c.id === id);
    if (!cust) return sendError(res, 404, "CUSTOMER_NOT_FOUND", `Không tìm thấy khách hàng ID ${id}`);
    const userOrders = orders.filter(o => o.customerId === id);
    return sendJson(res, 200, {
      content: userOrders,
      page: { number: 0, size: 10, totalElements: userOrders.length, totalPages: 1, last: true }
    });
  }

  return false;
}

module.exports = { sendJson, sendError, handleAuthRoutes, handleCustomerRoutes };
