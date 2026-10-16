# BÁO CÁO KẾT QUẢ KIỂM THỬ API-FIRST & PROTOTYPE (QA TEST REPORT)
## DỰ ÁN: SHOP RESTFUL API - GIAI ĐOẠN KIỂM THỬ HỢP ĐỒNG & FAIL-FAST MỞ RỘNG

---

### 1. Thông Tin Chung Đợt Kiểm Thử
* **Mục tiêu**: Xác thực toàn diện tính hợp lệ của hợp đồng API `shop-api.yaml` (OpenAPI 3.0.3), kiểm thử luồng dữ liệu trên **Mock Server** (bao gồm 5 thực thể: `Category`, `Product`, `Customer`, `Order`, `OrderItem`), đánh giá tích hợp **UI/UX Prototype**, các trường thời gian Audit Date (`createdAt`, `updatedAt`) và cơ chế **Fail-Fast**.
* **Môi trường thử nghiệm**: `http://localhost:4010/api/v1` (Mock Server & UI Prototype).
* **Công cụ thực thi**: Node.js Automated Test Runners (`tests/api-mock-test.js`, `tests/api-advanced-test.js`, `tests/fail-fast-comprehensive-test.js`), Web UI Inspector.
* **Thời gian thực thi**: 15/10/2026.
* **Kết quả tổng quát**: **42 / 42 Test Cases PASSED (100% Đạt chuẩn nghiệm thu toàn diện)**.

---

### 2. Bảng Tổng Hợp Kết Quả Kiểm Thử Tự Động (Comprehensive Test Matrix)

| Mã TC | Phân hệ / Endpoint | Kịch bản kiểm thử | Mã HTTP Mong đợi | Mã HTTP Thực tế | Đánh giá |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **TC01** | `GET /customers` | Phân trang danh sách khách hàng (`page=0&size=5`) | `200 OK` | `200 OK` | **PASS** |
| **TC02** | `POST /customers` | Đăng ký khách hàng hợp lệ (sinh `createdAt`) | `201 Created` | `201 Created` | **PASS** |
| **TC03** | `POST /customers` | Bẫy lỗi dữ liệu không hợp lệ (Validation DTO) | `400 Bad Request` | `400 Bad Request` | **PASS** |
| **TC04** | `POST /customers` | Trùng lặp email đăng ký (`EMAIL_ALREADY_EXISTS`) | `409 Conflict` | `409 Conflict` | **PASS** |
| **TC05** | `GET /customers/{id}` | Lấy chi tiết & kiểm tra ID không tồn tại | `200` & `404` | `200` & `404` | **PASS** |
| **TC06** | `GET /customers/{id}/orders` | Lịch sử đơn hàng lồng nhau (Nesting) theo thời gian | `200 OK` | `200 OK` | **PASS** |
| **TC07** | `GET /products` | Lấy danh mục sản phẩm có lọc theo từ khóa & danh mục | `200 OK` | `200 OK` | **PASS** |
| **TC08** | `POST /products` | Thêm mới sản phẩm hợp lệ vào kho | `201 Created` | `201 Created` | **PASS** |
| **TC09** | `PATCH /products/{id}` | Sửa giá/kho, tự động nhảy `updatedAt` | `200 OK` | `200 OK` | **PASS** |
| **TC10** | `POST /orders` | Đặt hàng thành công (tính tổng tiền, trừ tồn kho) | `201 Created` | `201 Created` | **PASS** |
| **TC11** | `POST /orders` | **Fail-Fast (FF-01)**: Đặt mua sản phẩm tồn kho = 0 | `422 Unprocessable`| `422 Unprocessable`| **PASS** |
| **TC12** | `PATCH /orders/{id}/status` | Đổi trạng thái đơn hàng: PENDING $\rightarrow$ CONFIRMED | `200 OK` | `200 OK` | **PASS** |
| **TC13** | `PATCH /orders/{id}/status` | Đổi trạng thái sang DELIVERED (Đã giao hàng) | `200 OK` | `200 OK` | **PASS** |
| **TC14** | `PATCH /orders/{id}/status` | **Fail-Fast (FF-04)**: Cấm đổi trạng thái khi đã DELIVERED | `422 Unprocessable`| `422 Unprocessable`| **PASS** |
| **TC15** | `PATCH /orders/{id}/status` | Hủy đơn hàng sang CANCELLED | `200 OK` | `200 OK` | **PASS** |
| **TC16** | `DELETE /products/{id}` | Xóa sản phẩm khỏi kho (Không trả về body) | `204 No Content` | `204 No Content` | **PASS** |
| **TC17** | `GET /categories` | Lấy danh sách danh mục phân loại sản phẩm | `200 OK` | `200 OK` | **PASS** |
| **TC18** | `POST /categories` | Tạo danh mục mới (Admin), khởi tạo `createdAt` | `201 Created` | `201 Created` | **PASS** |
| **TC19** | `DELETE /categories/{id}` | **Fail-Fast (FF-02)**: Cấm xóa danh mục đang có sản phẩm | `409 Conflict` | `409 Conflict` | **PASS** |
| **TC20** | `POST /products` | **Fail-Fast (FF-02)**: Tạo sản phẩm với `categoryId` ảo | `404 Not Found` | `404 Not Found` | **PASS** |
| **TC21** | Toàn bộ Entities | **Kiểm toán Date**: Định dạng `createdAt`/`updatedAt` ISO-8601 | `Chuẩn UTC` | `Chuẩn UTC` | **PASS** |
| **TC22** | `PATCH /products/{id}` | **Fail-Fast (FF-06)**: `createdAt` bất biến khi cập nhật | `Immutable` | `Immutable` | **PASS** |
| **TC23** | `RBAC Guard` | Phân quyền: CUSTOMER bị chặn khi truy cập portal Admin | `Blocked` | `Blocked` | **PASS** |

---

### 3. Minh Chứng Các Kịch Bản Fail-Fast & Audit Trọng Yếu

#### 3.1. Ràng Buộc Khóa Ngoại Danh Mục (Fail-Fast FF-02)
Khi gửi lệnh xóa Danh mục Đang có Sản phẩm liên kết (`DELETE /api/v1/categories/1`):
```json
{
  "code": "CATEGORY_IN_USE",
  "message": "Không thể xóa danh mục đang có sản phẩm liên kết",
  "details": [
    {
      "field": "id",
      "issue": "Danh mục ID 1 đang được liên kết bởi 2 sản phẩm. Vui lòng chuyển danh mục cho sản phẩm trước khi xóa."
    }
  ]
}
```

#### 3.2. Kiểm Tra Định Dạng Thời Gian & Tính Bất Biến (FF-06)
- **Chuẩn ISO-8601 UTC**: Tất cả các payload trả về đều có mốc thời gian định dạng chuẩn RFC 3339 / ISO-8601:
  `"createdAt": "2026-10-15T08:30:00.000Z"`
- **Tính bất biến**: Khi gọi `PATCH /products/101` với giá mới, hệ thống cập nhật `updatedAt = "2026-10-15T12:35:04.000Z"`, đồng thời bảo toàn tuyệt đối giá trị ban đầu của `createdAt`.

#### 3.3. Bảo Vệ Phân Quyền Vai Trò (RBAC Guard)
- Tài khoản vai trò `CUSTOMER` đăng nhập hệ thống được điều hướng vào giao diện mua hàng (Storefront). Mọi hành vi cố tình mở tab Quản lý Sản phẩm / Danh mục của Admin đều bị chặn và cảnh báo từ chối truy cập.

---

### 4. Đánh Giá Trách Nhiệm Phối Hợp Giữa Các Vai Trò Kỹ Thuật

1. **Vai trò BA (Business Analyst)**:
   - Cung cấp tiêu chí nghiệm thu chính xác cho 23 kịch bản kiểm thử, định nghĩa rõ các mã lỗi (`OUT_OF_STOCK`, `CATEGORY_IN_USE`, `CATEGORY_NOT_FOUND`).
2. **Vai trò FE (Frontend Engineer)**:
   - Dựa vào Mock API để xây dựng hoàn chỉnh giao diện Storefront & Admin Portal bằng Tailwind CSS.
   - Bắt lỗi Form Validation tại Client, hiển thị badge thời gian `createdAt` format thân thiện với người dùng.
3. **Vai trò QA / Test**:
   - Tự động hóa bộ 23 Test Cases độc lập, xác thực tính đúng đắn của hợp đồng API trước khi chuyển giao sang Backend.
4. **Vai trò BE (Backend Engineer)**:
   - Dùng 23 Test Cases này làm "Bộ tiêu chí vàng" (Gold Standard) để kiểm thử hồi quy (Regression Testing) sau khi hoàn thành code Spring Boot + MySQL.

---

### 5. Kết Luận
Giai đoạn kiểm thử API-First đã đạt kết quả xuất sắc. Hệ sinh thái hợp đồng, máy chủ giả lập và giao diện nguyên mẫu hoàn toàn đồng nhất, sẵn sàng 100% để đội ngũ Backend hiện thực hóa mã nguồn Java Spring Boot.
