/**
 * TOÀN DIỆN KIỂM THỬ MA TRẬN FAIL-FAST (FF-01 ĐẾN FF-06 & EDGE CASES)
 * Target: http://localhost:4010/api/v1
 */

const BASE_URL = 'http://localhost:4010/api/v1';

async function runFailFastTestSuite() {
  console.log('================================================================================');
  console.log('🛡️  BỘ KIỂM THỬ CHUYÊN SÂU MA TRẬN FAIL-FAST (ENTERPRISE SHOP RESTFUL API)');
  console.log(`🌐 Máy chủ kiểm thử: ${BASE_URL}`);
  console.log('================================================================================\n');

  let totalTests = 0;
  let passedTests = 0;
  let failedTests = 0;

  async function testCase(id, title, fn) {
    totalTests++;
    try {
      await fn();
      console.log(`✅ [PASS] ${id}: ${title}`);
      passedTests++;
    } catch (err) {
      console.error(`❌ [FAIL] ${id}: ${title}`);
      console.error(`   👉 Chi tiết lỗi: ${err.message}\n`);
      failedTests++;
    }
  }

  function assert(condition, message) {
    if (!condition) throw new Error(message);
  }

  // =========================================================================
  // FF-01: KIỂM SOÁT TỒN KHO FAIL-FAST (OUT_OF_STOCK & INVENTORY INTEGRITY)
  // =========================================================================
  await testCase('FF-01.1', 'Đặt mua sản phẩm tồn kho = 0 phải bị chặn ngay với HTTP 422 OUT_OF_STOCK', async () => {
    // Sản phẩm ID 103 có stockQuantity = 0
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
        items: [{ productId: 103, quantity: 1 }]
      })
    });
    assert(res.status === 422, `Expected 422 Unprocessable Entity, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'OUT_OF_STOCK', `Code phải là OUT_OF_STOCK, got ${data.code}`);
    assert(Array.isArray(data.details) && data.details.some(d => d.field.includes('quantity')), 'Phải có details chỉ rõ trường quantity');
  });

  await testCase('FF-01.2', 'Đặt mua số lượng vượt quá tồn kho khả dụng phải trả về HTTP 422 OUT_OF_STOCK', async () => {
    // Sản phẩm ID 106 có stockQuantity = 8
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
        items: [{ productId: 106, quantity: 999 }]
      })
    });
    assert(res.status === 422, `Expected 422, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'OUT_OF_STOCK', `Expected OUT_OF_STOCK, got ${data.code}`);
  });

  await testCase('FF-01.3', 'Đặt hàng với số lượng âm hoặc bằng 0 bị chặn với HTTP 400 VALIDATION_FAILED', async () => {
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
        items: [{ productId: 101, quantity: -5 }]
      })
    });
    assert(res.status === 400, `Expected 400, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'VALIDATION_FAILED', `Expected VALIDATION_FAILED, got ${data.code}`);
  });

  await testCase('FF-01.4', 'Đặt hàng hợp lệ phải trừ chính xác số lượng tồn kho của sản phẩm', async () => {
    const prodRes = await fetch(`${BASE_URL}/products/104`);
    const prodBefore = await prodRes.json();
    const buyQty = 2;

    const orderRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
        items: [{ productId: 104, quantity: buyQty }]
      })
    });
    assert(orderRes.status === 201, `Tạo đơn hàng phải trả về 201, got ${orderRes.status}`);

    const prodAfterRes = await fetch(`${BASE_URL}/products/104`);
    const prodAfter = await prodAfterRes.json();
    assert(prodAfter.stockQuantity === prodBefore.stockQuantity - buyQty, 
      `Tồn kho phải giảm từ ${prodBefore.stockQuantity} xuống ${prodBefore.stockQuantity - buyQty}, thực tế: ${prodAfter.stockQuantity}`);
  });

  // =========================================================================
  // FF-02: RÀNG BUỘC KHÓA NGOẠI DANH MỤC (FOREIGN KEY INTEGRITY)
  // =========================================================================
  await testCase('FF-02.1', 'Tạo sản phẩm với categoryId không tồn tại trả về HTTP 404 CATEGORY_NOT_FOUND', async () => {
    const res = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Chuột không tồn tại danh mục',
        sku: `TEST-CAT-${Date.now().toString().slice(-4)}`,
        price: 500000.0,
        stockQuantity: 10,
        categoryId: 999999 // Không tồn tại
      })
    });
    assert(res.status === 404, `Expected 404 Not Found, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'CATEGORY_NOT_FOUND', `Expected CATEGORY_NOT_FOUND, got ${data.code}`);
  });

  await testCase('FF-02.2', 'Xóa danh mục đang có sản phẩm liên kết bị chặn với HTTP 409 CATEGORY_IN_USE', async () => {
    // Danh mục ID 1 đang liên kết sản phẩm 101, 102
    const res = await fetch(`${BASE_URL}/categories/1`, { method: 'DELETE' });
    assert(res.status === 409, `Expected 409 Conflict, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'CATEGORY_IN_USE', `Expected CATEGORY_IN_USE, got ${data.code}`);
  });

  await testCase('FF-02.3', 'Xóa danh mục trống (không có sản phẩm) thành công với HTTP 204 No Content', async () => {
    // Tạo danh mục trống mới
    const catRes = await fetch(`${BASE_URL}/categories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        code: `EMPTY-${Date.now().toString().slice(-4)}`,
        name: `Danh mục rỗng ${Date.now().toString().slice(-4)}`,
        description: 'Dùng để test xóa'
      })
    });
    assert(catRes.status === 201, 'Tạo danh mục rỗng thất bại');
    const newCat = await catRes.json();

    // Xóa danh mục này
    const delRes = await fetch(`${BASE_URL}/categories/${newCat.id}`, { method: 'DELETE' });
    assert(delRes.status === 204, `Expected 204 No Content, got ${delRes.status}`);
  });

  // =========================================================================
  // FF-03: BẢO TOÀN ĐƠN GIÁ LỊCH SỬ (HISTORICAL PRICE IMMUTABILITY)
  // =========================================================================
  await testCase('FF-03.1', 'Thay đổi giá sản phẩm tương lai không làm đổi đơn giá lịch sử trong đơn cũ', async () => {
    // 1. Tạo sản phẩm ban đầu giá 1,000,000 VND
    const createProd = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Sản phẩm test giá lịch sử',
        sku: `HIST-${Date.now().toString().slice(-4)}`,
        price: 1000000.0,
        stockQuantity: 50,
        category: 'Phụ kiện máy tính',
        categoryId: 1
      })
    });
    const prod = await createProd.json();

    // 2. Đặt hàng 2 sản phẩm này -> Tổng 2,000,000 VND
    const orderRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton',
        items: [{ productId: prod.id, quantity: 2 }]
      })
    });
    const order = await orderRes.json();
    assert(order.totalAmount === 2000000.0, `Tổng tiền ban đầu phải là 2,000,000, got ${order.totalAmount}`);

    // 3. Sửa giá sản phẩm lên 3,500,000 VND
    await fetch(`${BASE_URL}/products/${prod.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ price: 3500000.0 })
    });

    // 4. Lấy lại đơn hàng cũ và kiểm tra
    const getOrderRes = await fetch(`${BASE_URL}/orders/${order.id}`);
    const oldOrder = await getOrderRes.json();
    assert(oldOrder.items[0].unitPrice === 1000000.0, 
      `Đơn giá lịch sử phải được bảo toàn là 1,000,000, got ${oldOrder.items[0].unitPrice}`);
    assert(oldOrder.totalAmount === 2000000.0, 
      `Tổng tiền đơn hàng cũ phải giữ nguyên 2,000,000, got ${oldOrder.totalAmount}`);
  });

  // =========================================================================
  // FF-04: VÒNG ĐỜI ĐƠN HÀNG BẤT BIẾN SAU KẾT THÚC (TERMINAL STATE IMMUTABILITY)
  // =========================================================================
  await testCase('FF-04.1', 'Đơn hàng đã DELIVERED cấm chuyển trạng thái tiếp (HTTP 422 INVALID_STATE_TRANSITION)', async () => {
    // Đơn hàng 5003 đã có status DELIVERED
    const res = await fetch(`${BASE_URL}/orders/5003/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'CANCELLED' })
    });
    assert(res.status === 422, `Expected 422, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'INVALID_STATE_TRANSITION', `Expected INVALID_STATE_TRANSITION, got ${data.code}`);
  });

  await testCase('FF-04.2', 'Chuyển trạng thái nhảy cóc (PENDING -> DELIVERED) bị từ chối với HTTP 422', async () => {
    // Tạo đơn mới đang ở PENDING
    const orderRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton',
        items: [{ productId: 101, quantity: 1 }]
      })
    });
    const order = await orderRes.json();

    // Nhảy cóc sang DELIVERED
    const patchRes = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'DELIVERED' })
    });
    assert(patchRes.status === 422, `Expected 422 for invalid leap, got ${patchRes.status}`);
  });

  await testCase('FF-04.3', 'Chuyển trạng thái tuần tự hợp lệ (PENDING -> CONFIRMED -> SHIPPING -> DELIVERED)', async () => {
    // Tạo đơn mới
    const orderRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton',
        items: [{ productId: 101, quantity: 1 }]
      })
    });
    const order = await orderRes.json();

    // 1. CONFIRMED
    const s1 = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'CONFIRMED' })
    });
    assert(s1.status === 200 && (await s1.json()).status === 'CONFIRMED', 'Failed PENDING -> CONFIRMED');

    // 2. SHIPPING
    const s2 = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'SHIPPING' })
    });
    assert(s2.status === 200 && (await s2.json()).status === 'SHIPPING', 'Failed CONFIRMED -> SHIPPING');

    // 3. DELIVERED
    const s3 = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'DELIVERED' })
    });
    assert(s3.status === 200 && (await s3.json()).status === 'DELIVERED', 'Failed SHIPPING -> DELIVERED');
  });

  // =========================================================================
  // FF-05: BẢO VỆ ĐỊNH DANH DUY NHẤT (UNIQUE IDENTIFIER CONFLICTS)
  // =========================================================================
  await testCase('FF-05.1', 'Đăng ký trùng Email trả về HTTP 409 EMAIL_ALREADY_EXISTS', async () => {
    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        fullName: 'Trùng Email',
        email: 'admin@shop.com', // Đã có
        password: 'password123',
        phone: '0912345678',
        address: '100 Hai Ba Trung'
      })
    });
    assert(res.status === 409, `Expected 409 Conflict, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'EMAIL_ALREADY_EXISTS', `Expected EMAIL_ALREADY_EXISTS, got ${data.code}`);
  });

  await testCase('FF-05.2', 'Thêm sản phẩm trùng SKU trả về HTTP 409 SKU_ALREADY_EXISTS', async () => {
    const res = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Trùng SKU',
        sku: 'KB-KEYCHRON-K2', // Đã có
        price: 1500000.0,
        stockQuantity: 10,
        category: 'Phụ kiện máy tính'
      })
    });
    assert(res.status === 409, `Expected 409 Conflict, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'SKU_ALREADY_EXISTS', `Expected SKU_ALREADY_EXISTS, got ${data.code}`);
  });

  await testCase('FF-05.3', 'Thêm danh mục trùng Mã code trả về HTTP 409 CATEGORY_CODE_EXISTS', async () => {
    const res = await fetch(`${BASE_URL}/categories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        code: 'ACCESSORIES', // Đã có
        name: 'Phụ kiện máy tính mới',
        description: 'Mô tả test'
      })
    });
    assert(res.status === 409, `Expected 409, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'CATEGORY_CODE_EXISTS', `Expected CATEGORY_CODE_EXISTS, got ${data.code}`);
  });

  // =========================================================================
  // FF-06: QUY TẮC AUDIT DATE & BẤT BIẾN DỮ LIỆU THỜI GIAN (AUDIT TRAILS)
  // =========================================================================
  await testCase('FF-06.1', 'Tất cả thực thể trả về đều có createdAt và updatedAt chuẩn ISO-8601 UTC', async () => {
    const cat = (await (await fetch(`${BASE_URL}/categories`)).json())[0];
    const prod = (await (await fetch(`${BASE_URL}/products`)).json()).content[0];
    const cust = (await (await fetch(`${BASE_URL}/customers`)).json()).content[0];
    const ord = (await (await fetch(`${BASE_URL}/orders`)).json()).content[0];

    const isoRegex = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d{3})?Z$/;
    assert(isoRegex.test(cat.createdAt) && isoRegex.test(cat.updatedAt), 'Category thiếu hoặc sai format createdAt/updatedAt');
    assert(isoRegex.test(prod.createdAt) && isoRegex.test(prod.updatedAt), 'Product thiếu hoặc sai format createdAt/updatedAt');
    assert(isoRegex.test(cust.createdAt) && isoRegex.test(cust.updatedAt), 'Customer thiếu hoặc sai format createdAt/updatedAt');
    assert(isoRegex.test(ord.createdAt) && isoRegex.test(ord.updatedAt), 'Order thiếu hoặc sai format createdAt/updatedAt');
  });

  await testCase('FF-06.2', 'PATCH/PUT cập nhật updatedAt nhưng bảo toàn tuyệt đối tính bất biến của createdAt', async () => {
    const prodBefore = await (await fetch(`${BASE_URL}/products/101`)).json();
    const originalCreatedAt = prodBefore.createdAt;

    // Chờ 20ms để mốc thời gian khác biệt
    await new Promise(r => setTimeout(r, 20));

    // Cố tình gửi payload có createdAt mới nhằm cố sửa
    const patchRes = await fetch(`${BASE_URL}/products/101`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        price: 1900000.0,
        createdAt: '1970-01-01T00:00:00.000Z' // Cố tình ghi đè
      })
    });
    const prodAfter = await patchRes.json();

    assert(prodAfter.createdAt === originalCreatedAt, 
      `createdAt PHẢI BẤT BIẾN! Mong đợi ${originalCreatedAt}, thực tế ${prodAfter.createdAt}`);
    assert(prodAfter.updatedAt !== prodBefore.updatedAt, 'updatedAt phải được cập nhật thời gian mới');
  });

  // =========================================================================
  // EDGE CASES & BOUNDARY VALIDATION
  // =========================================================================
  await testCase('EDGE-01', 'Tham số phân trang âm (page=-1 hoặc size=0) trả về 400 INVALID_QUERY_PARAMS', async () => {
    const res = await fetch(`${BASE_URL}/customers?page=-1&size=0`);
    assert(res.status === 400, `Expected 400, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'INVALID_QUERY_PARAMS', `Expected INVALID_QUERY_PARAMS, got ${data.code}`);
  });

  await testCase('EDGE-02', 'Đăng nhập với mật khẩu sai trả về HTTP 401 UNAUTHORIZED', async () => {
    const res = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: 'admin@shop.com',
        password: 'wrong_password_999'
      })
    });
    assert(res.status === 401, `Expected 401, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'UNAUTHORIZED', `Expected UNAUTHORIZED, got ${data.code}`);
  });

  await testCase('EDGE-03', 'Đặt hàng với danh sách items rỗng [] trả về HTTP 400 VALIDATION_FAILED', async () => {
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '72 Le Thanh Ton',
        items: []
      })
    });
    assert(res.status === 400, `Expected 400, got ${res.status}`);
    const data = await res.json();
    assert(data.code === 'VALIDATION_FAILED', `Expected VALIDATION_FAILED, got ${data.code}`);
  });

  console.log('\n================================================================================');
  console.log(`📊 KẾT QUẢ KIỂM THỬ: ${passedTests}/${totalTests} Test Cases ĐẠT (${Math.round((passedTests/totalTests)*100)}%)`);
  if (failedTests === 0) {
    console.log('🎉 TOÀN BỘ MA TRẬN FAIL-FAST (FF-01 ĐẾN FF-06) ĐÃ ĐẠT CHUẨN DOANH NGHIỆP XUẤT SẮC!');
  } else {
    console.log(`⚠️ Có ${failedTests} test case chưa đạt. Cần kiểm tra lại.`);
  }
  console.log('================================================================================\n');
}

runFailFastTestSuite();

