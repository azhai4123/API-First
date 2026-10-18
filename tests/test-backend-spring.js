/**
 * Test Suite kiểm thử tích hợp Backend Spring Boot 3.x + JPA + MySQL (Giai đoạn 2)
 */
const BASE_URL = 'http://localhost:8080/api/v1';

async function runTests() {
    console.log('=== BẮT ĐẦU KIỂM THỬ BACKEND SPRING BOOT + MYSQL LARAGON ===\n');
    let passCount = 0;
    let failCount = 0;

    async function test(name, fn) {
        try {
            await fn();
            console.log(`[PASS] ${name}`);
            passCount++;
        } catch (e) {
            console.error(`[FAIL] ${name}: ${e.message}`);
            failCount++;
        }
    }

    // 1. Test Login Admin
    let adminToken = '';
    await test('1. Đăng nhập Admin (admin@shop.com)', async () => {
        const res = await fetch(`${BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: 'admin@shop.com', password: 'admin123' })
        });
        if (res.status !== 200) throw new Error(`Status ${res.status}`);
        const data = await res.json();
        if (!data.token || data.user.role !== 'ADMIN') throw new Error('Token hoặc role không đúng');
        adminToken = data.token;
    });

    // 2. Test Login Customer
    let customerToken = '';
    let customerId = null;
    await test('2. Đăng nhập Customer (nguyenvana@example.com)', async () => {
        const res = await fetch(`${BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: 'nguyenvana@example.com', password: 'pass123' })
        });
        if (res.status !== 200) throw new Error(`Status ${res.status}`);
        const data = await res.json();
        if (!data.token) throw new Error('Không có token');
        customerToken = data.token;
        customerId = data.user.id;
    });

    // 3. Test Lấy danh mục sản phẩm
    await test('3. Lấy danh sách Categories từ MySQL', async () => {
        const res = await fetch(`${BASE_URL}/categories`);
        if (res.status !== 200) throw new Error(`Status ${res.status}`);
        const list = await res.json();
        if (!Array.isArray(list) || list.length < 4) throw new Error(`Số lượng danh mục không khớp: ${list.length}`);
    });

    // 4. Test Lấy danh sách sản phẩm phân trang
    let initialStock = 0;
    await test('4. Lấy danh sách Products phân trang (page 1, size 5)', async () => {
        const res = await fetch(`${BASE_URL}/products?page=1&size=5`);
        if (res.status !== 200) throw new Error(`Status ${res.status}`);
        const data = await res.json();
        if (!data.content || data.content.length === 0) throw new Error('Danh sách rỗng');
        const p1 = data.content.find(p => p.id === 1);
        if (!p1) throw new Error('Không tìm thấy sản phẩm ID 1');
        initialStock = p1.stockQuantity;
        console.log(`       -> Tồn kho hiện tại của sản phẩm ID 1 (${p1.name}): ${initialStock}`);
    });

    // 5. Test Fail-Fast FF-01: Mua quá số lượng tồn kho (422 OUT_OF_STOCK)
    await test('5. Fail-Fast FF-01: Đặt hàng vượt tồn kho (mong đợi HTTP 422 OUT_OF_STOCK)', async () => {
        const res = await fetch(`${BASE_URL}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${customerToken}`
            },
            body: JSON.stringify({
                customerId: customerId,
                shippingAddress: '123 Test Street, Hà Nội',
                items: [{ productId: 1, quantity: 99999 }]
            })
        });
        if (res.status !== 422) throw new Error(`Kỳ vọng 422 nhưng nhận ${res.status}`);
        const err = await res.json();
        if (err.code !== 'OUT_OF_STOCK') throw new Error(`ErrorCode không khớp: ${err.code}`);
    });

    // 6. Test Đặt hàng thành công & Trừ tồn kho ACID (@Transactional)
    let createdOrderId = null;
    await test('6. Đặt hàng thành công & Giảm tồn kho sản phẩm ID 1 (mua 2 chiếc)', async () => {
        const buyQty = 2;
        const res = await fetch(`${BASE_URL}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${customerToken}`
            },
            body: JSON.stringify({
                customerId: customerId,
                shippingAddress: '456 Phố Huế, Hai Bà Trưng, Hà Nội',
                items: [{ productId: 1, quantity: buyQty }]
            })
        });
        if (res.status !== 201) throw new Error(`Kỳ vọng 201 nhưng nhận ${res.status}`);
        const order = await res.json();
        createdOrderId = order.id;
        if (!order.totalAmount || order.items.length !== 1) throw new Error('Dữ liệu đơn hàng không hợp lệ');

        // Kiểm tra tồn kho sau khi mua
        const pRes = await fetch(`${BASE_URL}/products/1`);
        const pData = await pRes.json();
        if (pData.stockQuantity !== initialStock - buyQty) {
            throw new Error(`Tồn kho không giảm đúng: kỳ vọng ${initialStock - buyQty}, thực tế ${pData.stockQuantity}`);
        }
        console.log(`       -> Đơn hàng #${createdOrderId} tạo thành công. Tồn kho mới ID 1: ${pData.stockQuantity}`);
    });

    // 7. Test Hủy đơn hàng và TỰ ĐỘNG HOÀN KHO (RESTOCK)
    await test('7. Hủy đơn hàng (CANCELLED) & Tự động hoàn lại tồn kho (+2 chiếc)', async () => {
        const res = await fetch(`${BASE_URL}/orders/${createdOrderId}/status`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${adminToken}`
            },
            body: JSON.stringify({ status: 'CANCELLED' })
        });
        if (res.status !== 200) throw new Error(`Kỳ vọng 200 nhưng nhận ${res.status}`);
        const updatedOrder = await res.json();
        if (updatedOrder.status !== 'CANCELLED') throw new Error(`Trạng thái không phải CANCELLED: ${updatedOrder.status}`);

        // Kiểm tra tồn kho đã được hoàn lại chưa
        const pRes = await fetch(`${BASE_URL}/products/1`);
        const pData = await pRes.json();
        if (pData.stockQuantity !== initialStock) {
            throw new Error(`Tồn kho chưa hoàn lại đủ: kỳ vọng ${initialStock}, thực tế ${pData.stockQuantity}`);
        }
        console.log(`       -> Đã hoàn kho thành công. Tồn kho khôi phục lại: ${pData.stockQuantity}`);
    });

    // 8. Test Fail-Fast: Hủy đơn hàng đã ở trạng thái CANCELLED (Kỳ vọng 422 INVALID_STATE_TRANSITION)
    await test('8. Fail-Fast: Thao tác hủy tiếp trên đơn đã CANCELLED (mong đợi 422 INVALID_STATE_TRANSITION)', async () => {
        const res = await fetch(`${BASE_URL}/orders/${createdOrderId}/status`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${adminToken}`
            },
            body: JSON.stringify({ status: 'CANCELLED' })
        });
        if (res.status !== 422) throw new Error(`Kỳ vọng 422 nhưng nhận ${res.status}`);
        const err = await res.json();
        if (err.code !== 'INVALID_STATE_TRANSITION') throw new Error(`ErrorCode không khớp: ${err.code}`);
    });

    // 9. Test Lấy lịch sử đơn hàng của khách hàng (Nesting max 2 cấp)
    await test('9. Lấy lịch sử đơn hàng của Customer (/customers/{id}/orders)', async () => {
        const res = await fetch(`${BASE_URL}/customers/${customerId}/orders`);
        if (res.status !== 200) throw new Error(`Status ${res.status}`);
        const page = await res.json();
        if (!page.content || page.content.length === 0) throw new Error('Không có đơn hàng nào');
        console.log(`       -> Khách hàng có ${page.content.length} đơn hàng trong lịch sử`);
    });

    console.log(`\n=== TỔNG KẾT KIỂM THỬ: ${passCount} PASS / ${failCount} FAIL ===`);
}

runTests();
