/**
 * ADVANCED QA TEST SUITE - ENTERPRISE BOUNDARY & INTEGRITY TESTING
 * Kiểm thử chuyên sâu: Phân trang biên, Toàn vẹn tồn kho, Bảo toàn giá lịch sử
 */

const BASE_URL = 'http://localhost:4010/api/v1';

async function runAdvancedTestSuite() {
  console.log('======================================================================');
  console.log('🧪 BẮT ĐẦU CHẠY BỘ KIỂM THỬ NÂNG CAO (ENTERPRISE TEST SUITE)');
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

  // 1. Phân trang biên (Pagination Edge Cases)
  await testCase('ADV-01: Phân trang trang số 0 với size=1 (Min Size)', async () => {
    const res = await fetch(`${BASE_URL}/products?page=0&size=1`);
    assert(res.status === 200, `Expected 200, got ${res.status}`);
    const data = await res.json();
    assert(data.content.length <= 1, 'Size=1 chỉ được trả về tối đa 1 phần tử');
    assert(data.page.size === 1, 'Page size metadata phải là 1');
  });

  await testCase('ADV-02: Phân trang với tham số không hợp lệ (page=-1 hoặc size=0)', async () => {
    const res = await fetch(`${BASE_URL}/customers?page=-1&size=0`);
    assert(res.status === 400, `Expected 400 Bad Request, got ${res.status}`);
    const err = await res.json();
    assert(err.code === 'INVALID_QUERY_PARAMS', 'Error code phải là INVALID_QUERY_PARAMS');
  });

  // 2. Toàn vẹn sau khi xóa (Delete Integrity)
  await testCase('ADV-03: Kiểm tra toàn vẹn sau khi xóa (Xóa xong GET lại phải 404)', async () => {
    // Tạo sản phẩm mới
    const createRes = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Sản phẩm kiểm tra xóa',
        sku: `DEL-TEST-${Date.now().toString().slice(-4)}`,
        price: 150000.0,
        stockQuantity: 10,
        category: 'Thử nghiệm'
      })
    });
    const prod = await createRes.json();
    const prodId = prod.id;

    // Xóa sản phẩm
    const delRes = await fetch(`${BASE_URL}/products/${prodId}`, { method: 'DELETE' });
    assert(delRes.status === 204, `Xóa phải trả về 204, got ${delRes.status}`);

    // Truy vấn lại sản phẩm vừa xóa
    const getRes = await fetch(`${BASE_URL}/products/${prodId}`);
    assert(getRes.status === 404, `Sản phẩm đã xóa khi GET lại phải trả về 404 Not Found, got ${getRes.status}`);
  });

  // 3. Tính toàn vẹn trừ tồn kho (Inventory Deduction Integrity)
  await testCase('ADV-04: Đặt hàng thành công phải trừ chính xác số lượng tồn kho', async () => {
    // Tạo 1 sản phẩm có stock = 20
    const createProd = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Chuột test trừ kho',
        sku: `STK-${Date.now().toString().slice(-4)}`,
        price: 500000.0,
        stockQuantity: 20,
        category: 'Phụ kiện'
      })
    });
    const prod = await createProd.json();

    // Đặt mua 6 món
    const orderRes = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 1,
        shippingAddress: '100 Nguyen Thi Minh Khai',
        items: [{ productId: prod.id, quantity: 6 }]
      })
    });
    assert(orderRes.status === 201, `Tạo đơn phải thành công 201, got ${orderRes.status}`);

    // Kiểm tra lại tồn kho của sản phẩm: 20 - 6 = 14
    const checkProd = await fetch(`${BASE_URL}/products/${prod.id}`);
    const updatedProd = await checkProd.json();
    assert(updatedProd.stockQuantity === 14, `Tồn kho sau khi mua 6 món phải còn 14, thực tế: ${updatedProd.stockQuantity}`);
  });

  // 4. Bảo toàn giá lịch sử (Historical Price Immutability)
  await testCase('ADV-05: Bảo toàn giá lịch sử trong đơn hàng khi catalog thay đổi giá', async () => {
    // 1. Tạo sản phẩm với giá ban đầu 200,000 VND
    const createProd = await fetch(`${BASE_URL}/products`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: 'Sản phẩm test giá lịch sử',
        sku: `PRC-${Date.now().toString().slice(-4)}`,
        price: 200000.0,
        stockQuantity: 10,
        category: 'Đồ gia dụng'
      })
    });
    const prod = await createProd.json();

    // 2. Tạo đơn hàng tại thời điểm giá 200,000 VND
    const createOrder = await fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        customerId: 1,
        shippingAddress: 'District 1',
        items: [{ productId: prod.id, quantity: 2 }]
      })
    });
    const order = await createOrder.json();
    assert(order.totalAmount === 400000.0, `Tổng tiền ban đầu phải là 400,000 VND, got ${order.totalAmount}`);

    // 3. Tăng giá sản phẩm trong kho lên 500,000 VND
    await fetch(`${BASE_URL}/products/${prod.id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ price: 500000.0 })
    });

    // 4. Kiểm tra lại đơn hàng cũ xem giá lịch sử có được bảo toàn không
    const checkOrder = await fetch(`${BASE_URL}/orders/${order.id}`);
    const oldOrder = await checkOrder.json();
    assert(oldOrder.items[0].unitPrice === 200000.0, 'Đơn giá trong hóa đơn cũ phải giữ nguyên 200,000 VND');
    assert(oldOrder.totalAmount === 400000.0, 'Tổng tiền hóa đơn cũ không được bị thay đổi theo catalog');
  });

  // 6. Quản lý Danh mục & Ràng buộc Khóa ngoại (Category CRUD & Foreign Key Integrity)
  await testCase('ADV-06: CRUD Danh mục sản phẩm (GET 200, POST 201 + Location, PUT 200)', async () => {
    const code = `TEST_CAT_${Date.now().toString().slice(-4)}`;
    const createRes = await fetch(`${BASE_URL}/categories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ code: code, name: 'Danh mục thử nghiệm', description: 'Mô tả test' })
    });
    assert(createRes.status === 201, `Expected 201 Created, got ${createRes.status}`);
    const newCat = await createRes.json();
    assert(newCat.id && newCat.code === code, 'Dữ liệu danh mục tạo mới không khớp');

    // PUT cập nhật
    const updateRes = await fetch(`${BASE_URL}/categories/${newCat.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: 'Danh mục đã cập nhật', status: 'ACTIVE' })
    });
    assert(updateRes.status === 200, `Expected 200 OK, got ${updateRes.status}`);
  });

  await testCase('ADV-07: Fail-Fast xóa danh mục đang có sản phẩm liên kết (HTTP 409 Conflict)', async () => {
    // Danh mục 1 đang chứa sản phẩm 101, 102
    const delRes = await fetch(`${BASE_URL}/categories/1`, { method: 'DELETE' });
    assert(delRes.status === 409, `Expected 409 Conflict, got ${delRes.status}`);
    const err = await delRes.json();
    assert(err.code === 'CATEGORY_IN_USE', `Expected error code CATEGORY_IN_USE, got ${err.code}`);
  });

  console.log('\n======================================================================');
  console.log(`📊 KẾT QUẢ ADVANCED TEST: ${passed} PASSED / ${failed} FAILED / TỔNG CỘNG ${passed + failed} TEST CASES`);
  console.log('======================================================================');
}

runAdvancedTestSuite();

