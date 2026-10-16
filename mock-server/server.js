/**
 * MOCK SERVER - ENTERPRISE SHOP API
 * Tuân thủ 100% OpenAPI 3.0.3, Auth JWT, Dual Views & Pagination
 */
const http = require('http');
const fs = require('fs');
const path = require('path');
const db = require('./data');
const { sendJson, sendError, handleAuthRoutes, handleCustomerRoutes } = require('./routes-auth-customers');
const { handleProductRoutes, handleOrderRoutes } = require('./routes-products-orders');
const { handleCategoryRoutes } = require('./routes-categories');

const PORT = 4010;

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
  const urlObj = new URL(req.url, `http://${req.headers.host || 'localhost'}`);
  const pathname = urlObj.pathname;
  const method = req.method;

  // Handle CORS Preflight
  if (method === 'OPTIONS') {
    res.writeHead(204, {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization'
    });
    return res.end();
  }

  // Parse JSON Body
  let body = '';
  req.on('data', chunk => { body += chunk; });
  req.on('end', () => {
    let payload = null;
    if (body) {
      try { payload = JSON.parse(body); } catch (e) {
        return sendError(res, 400, "INVALID_JSON", "Payload gửi lên không đúng định dạng JSON chuẩn");
      }
    }

    // Static Prototype UI Files
    if (!pathname.startsWith('/api/v1/')) {
      let relativePath = pathname === '/' ? 'index.html' : pathname.slice(1);
      const filePath = path.join(__dirname, '..', 'ui-prototype', relativePath);
      const ext = path.extname(filePath).toLowerCase();

      fs.readFile(filePath, (err, content) => {
        if (err) {
          if (err.code === 'ENOENT') {
            res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
            return res.end('File không tồn tại trên UI Prototype');
          }
          res.writeHead(500);
          return res.end('Lỗi máy chủ nội bộ: ' + err.code);
        }
        res.writeHead(200, { 'Content-Type': MIME_TYPES[ext] || 'application/octet-stream' });
        return res.end(content);
      });
      return;
    }

    // API Routing
    const query = urlObj.searchParams;

    // 1. Auth routes
    if (pathname.startsWith('/api/v1/auth/')) {
      if (handleAuthRoutes(req, res, pathname, method, payload, db) !== false) return;
    }

    // 2. Category routes
    if (pathname.startsWith('/api/v1/categories')) {
      if (handleCategoryRoutes(req, res, pathname, method, query, payload, db) !== false) return;
    }

    // 3. Customer routes
    if (pathname.startsWith('/api/v1/customers')) {
      if (handleCustomerRoutes(req, res, pathname, method, query, payload, db) !== false) return;
    }

    // 3. Product routes
    if (pathname.startsWith('/api/v1/products')) {
      if (handleProductRoutes(req, res, pathname, method, query, payload, db) !== false) return;
    }

    // 4. Order routes
    if (pathname.startsWith('/api/v1/orders')) {
      if (handleOrderRoutes(req, res, pathname, method, query, payload, db) !== false) return;
    }

    // 404 Route Not Found
    return sendError(res, 404, "ENDPOINT_NOT_FOUND", `Không tìm thấy endpoint tại [${method}] ${pathname}`);
  });
});

server.listen(PORT, () => {
  console.log(`[MOCK SERVER] Enterprise Shop API Server đang chạy tại: http://localhost:${PORT}/api/v1`);
  console.log(`[UI PROTOTYPE] Giao diện Tailwind CSS chạy tại: http://localhost:${PORT}/`);
});
