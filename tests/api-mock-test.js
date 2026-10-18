/**
 * AUTOMATED QA TEST SUITE - SHOP RESTFUL API FIRST TESTING
 * Kiểm thử toàn diện hợp đồng OpenAPI 3.0.3 trên Mock Server
 */

const BASE_URL = 'http://localhost:4010/api/v1';

async function runTestSuite() {
  console.log('======================================================================');
  console.log('🚀 BẮT ĐẦU CHẠY BỘ KIỂM THỬ TỰ ĐỘNG - SHOP RESTFUL API (API-FIRST QA)');
  console.log(`📡 Target Endpoint: ${BASE_URL}`);
  console.log('======================================================================\n');

  let passed = 0;
  let failed = 0;

  async function testCase(name, fn) {
    try {
      await fn();
      console.log(`✅ [PASS] ${name}`);
      passed++;
    } catch (err) {
      console.error(`❌ [FAIL] ${name}`);
      console.error(`   👉 Lý do: ${err.message}\n`);
      failed++;
    }
  }

  function assert(condition, message) {
    if (!condition) throw new Error(message);
  }

  // --- MODULE 1: CUSTOMERS ---
  await testCase('TC01: GET /customers - Phân trang & Cấu trúc danh sách', async () => {
    const res = await fetch(`${BASE_URL}/customers?page=0&size=5`);
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const data = await res.json();
    assert(Array.isArray(data.content), 'Field "content" phải là array');
    assert(data.page && typeof data.page.totalElements === 'number', 'Thiếu object phân trang "page"');
  });

  await testCase('TC02: POST /customers - Đăng ký hợp lệ (HTTP 201 + Location Header)', async () => {
    const payload = {
      fullName: 'Le Thi Hoa',
      email: `hoa.le.${Date.now()}@example.com`,
      phone: '0933445566',
      address: '88 Nguyen Hue, Ben Nghe, Quan 1'
    };
    const res = await fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    assert(res.status === 201, `Expected 201 Created, got ${res.status}`);
    const location = res.headers.get('location');
    assert(location && location.includes('/customers/'), `Location header thiếu hoặc không hợp lệ: ${location}`);
    const data = await res.json();
    assert(data.id && data.fullName === payload.fullName, 'Dữ liệu trả về không khớp');
  });

  await testCase('TC03: POST /customers - Lỗi Validation đầu vào (HTTP 400 + JSON thống nhất)', async () => {
    const invalidPayload = {
      fullName: '', // Rỗng
      email: 'not-an-email', // Sai format
      phone: '123', // Ngắn
      address: 'ABC' // Quá ngắn
    };
    const res = await fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(invalidPayload)
    });
    assert(res.status === 400, `Expected 400 Bad Request, got ${res.status}`);
    const error = await res.json();
    assert(error.code === 'VALIDATION_FAILED', `Error code phải là VALIDATION_FAILED, got ${error.code}`);
    assert(error.message && Array.isArray(error.details) && error.details.length >= 3, 'Thiếu cấu trúc chi tiết details');
  });

  await testCase('TC04: POST /customers - Trùng lặp Email (HTTP 409 Conflict)', async () => {
    const duplicatePayload = {
      fullName: 'Duplicate User',
      email: 'nguyenvana@example.com', // Đã tồn tại
      phone: '0909999999',
      address: '100 Hai Ba Trung, Quan 1'
    };
    const res = await fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(duplicatePayload)
    });
    assert(res.status === 409, `Expected 409 Conflict, got ${res.status}`);
    const error = await res.json();
    assert(error.code === 'EMAIL_ALREADY_EXISTS', `Error code không đúng: ${error.code}`);
  });

  await testCase('TC05: GET /customers/{id} - Lấy chi tiết & Case 404 Not Found', async () => {
    const resSuccess = await fetch(`${BASE_URL}/customers/1`);
    assert(resSuccess.status === 200, `Expected 200 OK, got ${resSuccess.status}`);

    const resNotFound = await fetch(`${BASE_URL}/customers/99999`);
    assert(resNotFound.status === 404, `Expected 404 Not Found, got ${resNotFound.status}`);
  });

  await testCase('TC06: GET /customers/{id}/orders - Nesting endpoint max 2 cấp', async () => {
    const res = await fetch(`${BASE_URL}/customers/1/orders`);
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const data = await res.json();
    assert(Array.isArray(data.content), 'Orders phải là array');
  });

  // --- MODULE 2: PRODUCTS ---
  await testCase('TC07: GET /products - Danh mục sản phẩm & Query Filter', async () => {
    const res = await fetch(`${BASE_URL}/products?category=máy tính`);
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const data = await res.json();
    assert(Array.isArray(data.content) && data.content.length > 0, 'Phải có sản phẩm');
  });

  await testCase('TC08: POST /products - Thêm sản phẩm mới (HTTP 201 + Location)', async () => {
    const payload = {
      name: 'Tai nghe Sony WH-1000XM5',
      sku: `HD-SONY-XM5-${Date.now().toString().slice(-4)}`,
      price: 6990000.0,
      stockQuantity: 15,
      category: 'Âm thanh',
      description: 'Tai nghe chống ồn chủ động cao cấp'
    };
    const res = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    assert(res.status === 201, `Expected 201 Created, got ${res.status}`);
    assert(res.headers.get('location'), 'Thiếu Location header');
  });

  await testCase('TC09: PATCH /products/{id} - Cập nhật một phần (Giá & Tồn kho)', async () => {
    const patchPayload = {
      price: 1890000.0,
      stockQuantity: 60
    };
    const res = await fetch(`${BASE_URL}/products/101`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(patchPayload)
    });
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const updated = await res.json();
    assert(updated.price === 1890000.0 && updated.stockQuantity === 60, 'Giá hoặc tồn kho chưa được cập nhật');
  });

  // --- MODULE 3: ORDERS & FAIL-FAST ---
  await testCase('TC10: POST /orders - Đặt hàng thành công (HTTP 201 + Trừ kho + Location)', async () => {
    const payload = {
      customerId: 1,
      shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
      items: [
        { productId: 101, quantity: 1 }
      ]
    };
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    assert(res.status === 201, `Expected 201 Created, got ${res.status}`);
    const order = await res.json();
    assert(order.id && order.status === 'PENDING', 'Đơn hàng không hợp lệ');
    assert(order.totalAmount > 0, 'Tổng tiền phải lớn hơn 0');
  });

  await testCase('TC11: POST /orders - Fail-Fast kiểm tra hết hàng (HTTP 422 Unprocessable Entity)', async () => {
    const payload = {
      customerId: 1,
      shippingAddress: '72 Le Thanh Ton, Quan 1',
      items: [
        { productId: 103, quantity: 2 } // Sản phẩm 103 có stockQuantity = 0
      ]
    };
    const res = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    assert(res.status === 422, `Expected 422 Unprocessable Entity, got ${res.status}`);
    const error = await res.json();
    assert(error.code === 'OUT_OF_STOCK', `Code phải là OUT_OF_STOCK, got ${error.code}`);
    assert(error.details && error.details[0].field === 'items[0].quantity', 'Chi tiết lỗi không đúng trường');
  });

  let testOrderId = 5001;

  await testCase('TC12: PATCH /orders/{id}/status - Cập nhật sang CONFIRMED (HTTP 200)', async () => {
    // Tạo đơn mới độc lập để test vòng đời trạng thái
    const createRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 1,
        shippingAddress: '72 Le Thanh Ton, Quan 1, TP.HCM',
        items: [{ productId: 101, quantity: 1 }]
      })
    });
    const newOrder = await createRes.json();
    testOrderId = newOrder.id;

    const res = await fetch(`${BASE_URL}/orders/${testOrderId}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'CONFIRMED' })
    });
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const order = await res.json();
    assert(order.status === 'CONFIRMED', 'Trạng thái chưa được cập nhật sang CONFIRMED');
  });

  await testCase('TC13: PATCH /orders/{id}/status - Cập nhật sang DELIVERED (HTTP 200)', async () => {
    const res = await fetch(`${BASE_URL}/orders/${testOrderId}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'DELIVERED' })
    });
    assert(res.status === 200, `Expected 200 OK, got ${res.status}`);
    const order = await res.json();
    assert(order.status === 'DELIVERED', 'Trạng thái chưa được cập nhật sang DELIVERED');
  });

  await testCase('TC14: PATCH /orders/{id}/status - Vi phạm chuyển trạng thái từ DELIVERED (HTTP 422)', async () => {
    // Đơn hàng đã ở DELIVERED, cố tình đổi lại sang SHIPPING
    const res = await fetch(`${BASE_URL}/orders/${testOrderId}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'SHIPPING' })
    });
    assert(res.status === 422, `Expected 422 Unprocessable Entity, got ${res.status}`);
    const error = await res.json();
    assert(error.code === 'INVALID_STATE_TRANSITION', `Code phải là INVALID_STATE_TRANSITION, got ${error.code}`);
  });

  await testCase('TC15: POST & PATCH /orders/{id}/status - Hủy đơn hàng sang CANCELLED & Hoàn kho (HTTP 200)', async () => {
    // 1. Lấy tồn kho trước khi đặt
    const pBefore = await (await fetch(`${BASE_URL}/products/101`)).json();
    const stockBefore = pBefore.stockQuantity;

    // 2. Tạo đơn mới để test hủy (mua 1 cái)
    const createRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 2,
        shippingAddress: '15 Nguyen Dinh Chieu, Quan 3',
        items: [{ productId: 101, quantity: 1 }]
      })
    });
    const newOrder = await createRes.json();
    
    // 3. Hủy đơn
    const cancelRes = await fetch(`${BASE_URL}/orders/${newOrder.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'CANCELLED' })
    });
    assert(cancelRes.status === 200, `Expected 200 OK, got ${cancelRes.status}`);
    const cancelledOrder = await cancelRes.json();
    assert(cancelledOrder.status === 'CANCELLED', 'Trạng thái phải là CANCELLED');

    // 4. Kiểm tra hoàn lại số lượng tồn kho
    const pAfter = await (await fetch(`${BASE_URL}/products/101`)).json();
    assert(pAfter.stockQuantity === stockBefore, `Tồn kho phải được hoàn lại ${stockBefore}, hiện tại là ${pAfter.stockQuantity}`);
  });

  await testCase('TC15B: Chuỗi chuyển đổi trạng thái chuẩn đề bài (PENDING -> PAID -> SHIPPED)', async () => {
    const createRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 1,
        shippingAddress: 'Tòa nhà Bitexco, Q1',
        items: [{ productId: 102, quantity: 1 }]
      })
    });
    const order = await createRes.json();

    // PENDING -> PAID
    const paidRes = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'PAID' })
    });
    assert(paidRes.status === 200, 'Chuyển sang PAID thất bại');

    // PAID -> SHIPPED
    const shippedRes = await fetch(`${BASE_URL}/orders/${order.id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status: 'SHIPPED' })
    });
    assert(shippedRes.status === 200, 'Chuyển sang SHIPPED thất bại');
  });

  await testCase('TC16: DELETE /products/{id} - Xóa tài nguyên (HTTP 204 No Content)', async () => {
    // Tạo 1 sản phẩm tạm thời để test xóa độc lập
    const createProd = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Sản phẩm tạm để xóa',
        sku: `TMP-DEL-${Date.now().toString().slice(-4)}`,
        price: 99000.0,
        stockQuantity: 5,
        category: 'Thử nghiệm'
      })
    });
    const prod = await createProd.json();

    const res = await fetch(`${BASE_URL}/products/${prod.id}`, {
      method: 'DELETE'
    });
    assert(res.status === 204, `Expected 204 No Content, got ${res.status}`);
    const text = await res.text();
    assert(text === '', '204 No Content không được chứa response body');
  });

  console.log('\n======================================================================');
  console.log(`📊 KẾT QUẢ KIỂM THỬ: ${passed} PASSED / ${failed} FAILED / TỔNG CỘNG ${passed + failed} TEST CASES`);
  console.log('======================================================================');

  if (failed > 0) {
    process.exit(1);
  }
}

runTestSuite();

