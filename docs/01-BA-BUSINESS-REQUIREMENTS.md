# TÀI LIỆU PHÂN TÍCH YÊU CẦU NGHIỆP VỤ (BA ENTERPRISE SPECIFICATION)
## DỰ ÁN: SHOP RESTFUL API - MÔ HÌNH THỰC THỂ DOANH NGHIỆP & FAIL-FAST CHUẨN MỰC

---

### 1. Bức Tranh Tổng Thể & Kiến Trúc Nghiệp Vụ Doanh Nghiệp
Hệ thống **Shop API** được thiết kế phục vụ nền tảng thương mại điện tử chuyên nghiệp theo quy trình **API-First Development** kết hợp **Spring Boot 3.x / JPA**. Toàn bộ hợp đồng được xác lập chặt chẽ để khi chuyển giao sang Backend, đội ngũ kỹ sư chỉ cần kế thừa Mock Server, UI Prototype và bộ Test Suite là có thể ánh xạ sang MySQL Entity 1-1.

---

#### 2. Mô Hình Thực Thể Hoàn Chỉnh (Enterprise Entity Model)
Hệ thống bao gồm 5 thực thể cốt lõi. **Tất cả các thực thể đều bắt buộc có trường thời gian (`createdAt`, `updatedAt`) chuẩn ISO-8601 ngay từ tầng BA** để phục vụ hiển thị UI, bộ lọc thời gian và kiểm toán dữ liệu (Audit Trail):

1. **Danh Mục Sản Phẩm (`Category`)**:
   - `id` (BIGINT, PK, Auto Increment)
   - `name` (VARCHAR(100), Unique, Not Null) - Tên hiển thị (Ví dụ: "Phụ kiện máy tính")
   - `code` (VARCHAR(50), Unique, Not Null) - Mã slug hệ thống (Ví dụ: "ACCESSORIES", "MONITOR")
   - `description` (VARCHAR(500)) - Mô tả nhóm danh mục
   - `status` (ENUM: ACTIVE, INACTIVE) - Trạng thái hoạt động
   - `createdAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm tạo danh mục
   - `updatedAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm cập nhật danh mục lần cuối
   - *Quan hệ*: $1:N$ với `Product`.

2. **Sản Phẩm (`Product`)**:
   - `id` (BIGINT, PK, Auto Increment)
   - `categoryId` (BIGINT, FK, Not Null) - Tham chiếu tới `Category`
   - `name` (VARCHAR(150), Not Null) - Tên thương mại sản phẩm
   - `sku` (VARCHAR(50), Unique, Not Null) - Mã SKU kiểm kê kho (`^[A-Z0-9_-]{3,30}$`)
   - `price` (DECIMAL(12,2), Not Null, $\ge 1,000$) - Đơn giá bán niêm yết
   - `stockQuantity` (INT, Not Null, $\ge 0$) - Số lượng tồn kho khả dụng
   - `imageUrl` (VARCHAR(500), URI, Not Null) - Ảnh đại diện sản phẩm sắc nét
   - `description` (TEXT) - Thông số kỹ thuật chi tiết
   - `status` (ENUM: ACTIVE, INACTIVE, OUT_OF_STOCK) - Trạng thái kinh doanh
   - `createdAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm thêm sản phẩm vào hệ thống
   - `updatedAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm cập nhật giá/kho/thông tin lần cuối

3. **Người Dùng & Khách Hàng (`Customer / User`)**:
   - `id` (BIGINT, PK, Auto Increment)
   - `email` (VARCHAR(100), Unique, Not Null) - Định danh tài khoản đăng nhập
   - `password` (VARCHAR(100), Not Null, Mã hóa BCrypt khi sang DB thật)
   - `fullName` (VARCHAR(100), Not Null, Min 2 ký tự)
   - `phone` (VARCHAR(15), Not Null, Regex 10-11 chữ số)
   - `address` (VARCHAR(255), Not Null, Min 5 ký tự)
   - `avatarUrl` (VARCHAR(500), URI) - Ảnh đại diện
   - `role` (ENUM: `CUSTOMER`, `STAFF`, `ADMIN`) - Phân quyền hệ thống 3 cấp
   - `status` (ENUM: ACTIVE, INACTIVE, LOCKED)
   - `createdAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm người dùng đăng ký tài khoản
   - `updatedAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm cập nhật hồ sơ lần cuối

4. **Đơn Hàng (`Order`)**:
   - `id` (BIGINT, PK) - Mã đơn hàng
   - `customerId` (BIGINT, FK, Not Null) - Khách hàng đặt mua
   - `totalAmount` (DECIMAL(14,2), Not Null) - Tổng tiền đơn hàng
   - `status` (ENUM: `PENDING`, `CONFIRMED`, `SHIPPING`, `DELIVERED`, `CANCELLED`)
   - `shippingAddress` (VARCHAR(255), Not Null) - Địa chỉ nhận hàng
   - `createdAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm khách chốt đơn hàng
   - `updatedAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm cập nhật trạng thái đơn (chuyển hàng, giao thành công, v.v.)

5. **Chi Tiết Đơn Hàng (`OrderItem`)**:
   - `id` (BIGINT, PK)
   - `orderId` (BIGINT, FK)
   - `productId` (BIGINT, FK)
   - `productName` (VARCHAR(150)) - Lưu snapshot tên sản phẩm thời điểm đặt
   - `unitPrice` (DECIMAL(12,2), Not Null) - **Bảo toàn đơn giá lịch sử**
   - `quantity` (INT, Not Null, $\ge 1$)
   - `subTotal` (DECIMAL(14,2), Not Null)
   - `createdAt` (DATETIME / ISO-8601 UTC, Not Null) - Thời điểm dòng đơn được tạo

---

### 3. Phân Định Trách Nhiệm Các Vai Trò (Roles & Responsibility Matrix)

#### A. Phân Quyền Người Dùng Hệ Thống Nghiệp Vụ (Business User Roles)
- **`CUSTOMER` (Khách hàng / Người mua)**:
  - Xem danh mục, danh sách sản phẩm, xem chi tiết sản phẩm và lọc theo thời gian/giá/danh mục.
  - Quản lý giỏ hàng, đặt hàng (`POST /orders`).
  - Xem lịch sử đơn hàng của bản thân kèm mốc thời gian đặt và cập nhật (`GET /customers/{id}/orders`).
  - Xem & sửa hồ sơ cá nhân (`GET /customers/{id}`, `PUT /customers/{id}`).
- **`STAFF` (Nhân viên vận hành & kho)**:
  - Quản lý danh mục & cập nhật nhanh tồn kho / giá bán sản phẩm (`PATCH /products/{id}`).
  - Xử lý tiến độ đơn hàng (`PATCH /orders/{id}/status`: PENDING $\rightarrow$ CONFIRMED $\rightarrow$ SHIPPING $\rightarrow$ DELIVERED).
- **`ADMIN` (Quản trị viên toàn quyền)**:
  - Toàn quyền CRUD Danh mục (`/categories`), Sản phẩm (`/products`), Khách hàng (`/customers`).
  - Hủy đơn hàng, khóa tài khoản vi phạm, tra cứu báo cáo theo khoảng thời gian (`createdAt`).

#### B. Phân Công Đội Ngũ Kỹ Thuật Trong Quy Trình API-First (Software Engineering Roles)
- **`BA` (Business Analyst - Chuyên viên Phân tích Nghiệp vụ)**:
  - Khảo sát và chuẩn hóa toàn bộ yêu cầu nghiệp vụ, định nghĩa mô hình thực thể (bắt buộc bao gồm cả các trường Audit `createdAt`, `updatedAt`).
  - Xây dựng ma trận Fail-Fast (FF-01 đến FF-06) và từ điển mã lỗi chuẩn HTTP.
  - Viết tài liệu đặc tả và tiêu chí nghiệm thu (Acceptance Criteria) cho cả FE, BE và QA.
- **`FE` (Frontend Engineer - Kỹ sư Giao diện)**:
  - Dựa vào tài liệu BA và OpenAPI Contract để dựng UI/UX hoàn chỉnh (Tailwind CSS, Storefront, Admin Portal).
  - Tích hợp trực tiếp với **Mock Server** độc lập, hiển thị đầy đủ trường dữ liệu (format hiển thị ngày giờ `createdAt`, phân trang, lọc sản phẩm theo thời gian).
  - Đảm bảo kiểm soát Client-Side Validation khớp 100% với quy tắc Fail-Fast trước khi gửi request.
- **`BE` (Backend Engineer - Kỹ sư Hệ thống / Nền tảng)**:
  - Kế thừa OpenAPI Contract và mô hình BA để sinh code / triển khai Spring Boot 3.x REST Controller.
  - Triển khai JPA Entity tương ứng 1-1 với các trường của BA (sử dụng `@CreatedDate`, `@LastModifiedDate` qua Spring Data JPA Auditing).
  - Cài đặt Service Layer xử lý logic giao dịch `@Transactional`, mã hóa mật khẩu BCrypt, xác thực JWT, và ném `ApiException` chuẩn theo Fail-Fast matrix.
- **`TEST / QA` (Quality Assurance Engineer - Kỹ sư Kiểm thử)**:
  - Thiết kế kịch bản kiểm thử API-First (Happy Path, Boundary Conditions, Fail-Fast Matrix).
  - Xây dựng Test Suite tự động (kiểm thử hợp đồng schema, kiểm thử định dạng ngày giờ ISO-8601, kiểm tra tính toàn vẹn và bất biến của `createdAt`).
  - Đảm bảo chất lượng trước khi nghiệm thu từng giai đoạn (Mock Stage $\rightarrow$ Backend Real DB Stage).

---

### 4. Quy Tắc Kiểm Soát Nghiệp Vụ & Fail-Fast Chi Tiết
1. **FF-01 (Kiểm soát Tồn kho Fail-Fast)**:
   - Khi đặt hàng, hệ thống duyệt từng item. Nếu `stockQuantity < quantity`, trả về `422 Unprocessable Entity`, code `OUT_OF_STOCK`, chỉ rõ trường `items[i].quantity`.
   - Ngăn chặn triệt để đặt âm kho hoặc tranh chấp dữ liệu.
2. **FF-02 (Ràng buộc Khóa ngoại Danh mục)**:
   - Khi tạo hoặc sửa sản phẩm (`POST/PATCH /products`), `categoryId` bắt buộc phải tồn tại trong CSDL. Nếu không tồn tại $\rightarrow$ trả về `404 CATEGORY_NOT_FOUND`.
   - Không cho phép xóa Danh mục nếu đang có Sản phẩm liên kết $\rightarrow$ trả về `409 CATEGORY_IN_USE`.
3. **FF-03 (Bảo toàn Giá Lịch sử)**:
   - `OrderItem.unitPrice` được chốt tại thời điểm bấm đặt hàng. Mọi thay đổi giá tương lai của `Product.price` không làm biến động tổng tiền các đơn cũ.
4. **FF-04 (Vòng đời Đơn hàng Bất biến sau kết thúc)**:
   - Đơn hàng đã ở trạng thái `DELIVERED` hoặc `CANCELLED` là trạng thái cuối (Terminal State). Bất kỳ yêu cầu chuyển trạng thái tiếp đều bị từ chối với mã `422 INVALID_STATE_TRANSITION`.
5. **FF-05 (Bảo vệ Định danh Duy nhất)**:
   - Email đăng ký trùng lặp $\rightarrow$ `409 EMAIL_ALREADY_EXISTS`.
   - Mã SKU sản phẩm trùng lặp $\rightarrow$ `409 SKU_ALREADY_EXISTS`.
   - Mã Code danh mục trùng lặp $\rightarrow$ `409 CATEGORY_CODE_EXISTS`.
6. **FF-06 (Quy tắc Audit Date & Bất Biến Dữ Liệu Thời Gian)**:
   - `createdAt` được ghi nhận tự động vào thời điểm bản ghi khởi tạo và là **trường bất biến (Immutable)**. Mọi thao tác `PUT` hoặc `PATCH` cố tình sửa `createdAt` sẽ bị bỏ qua hoặc từ chối.
   - `updatedAt` tự động cập nhật mốc thời gian thực khi có bất kỳ thay đổi nào trên bản ghi.
   - Định dạng trao đổi qua REST API bắt buộc là chuỗi **ISO-8601 UTC** (`YYYY-MM-DDTHH:mm:ss.sssZ`).

---

### 5. Ma Trận RESTful Endpoints Toàn Hệ Thống

| Nhóm Tài Nguyên | Method | URI Endpoint | Mã HTTP | Mô Tả Nghiệp Vụ & Dấu Vết Thời Gian |
| :--- | :--- | :--- | :---: | :--- |
| **Auth** | `POST` | `/api/v1/auth/login` | 200, 400, 401 | Đăng nhập lấy JWT Bearer Token |
| | `POST` | `/api/v1/auth/register` | 201, 400, 409 | Đăng ký tài khoản (khởi tạo `createdAt`) |
| | `GET` | `/api/v1/auth/me` | 200, 401 | Lấy thông tin tài khoản hiện tại |
| **Categories** | `GET` | `/api/v1/categories` | 200 | Lấy danh sách danh mục (hỗ trợ sort theo `createdAt`) |
| | `POST` | `/api/v1/categories` | 201, 400, 409 | Thêm danh mục mới (Admin/Staff) |
| | `GET` | `/api/v1/categories/{id}` | 200, 404 | Xem chi tiết danh mục |
| | `PUT` | `/api/v1/categories/{id}` | 200, 400, 404 | Chỉnh sửa danh mục (cập nhật `updatedAt`) |
| | `DELETE`| `/api/v1/categories/{id}`| 204, 404, 409 | Xóa danh mục (Fail-Fast nếu có hàng) |
| **Products** | `GET` | `/api/v1/products` | 200, 400 | Lọc theo categoryId, stock, sort (`createdAt`, `price`) |
| | `POST` | `/api/v1/products` | 201, 400, 404, 409 | Thêm sản phẩm mới (khởi tạo `createdAt`) |
| | `GET` | `/api/v1/products/{id}` | 200, 404 | Chi tiết thông số & tồn kho sản phẩm |
| | `PATCH` | `/api/v1/products/{id}` | 200, 400, 404 | Sửa giá/kho/mô tả (tự động cập nhật `updatedAt`) |
| | `DELETE`| `/api/v1/products/{id}` | 204, 404 | Xóa sản phẩm khỏi kho |
| **Customers** | `GET` | `/api/v1/customers` | 200, 400 | Danh sách khách hàng phân trang |
| | `GET` | `/api/v1/customers/{id}` | 200, 404 | Xem hồ sơ khách hàng |
| | `PUT` | `/api/v1/customers/{id}` | 200, 400, 404 | Cập nhật hồ sơ cá nhân (cập nhật `updatedAt`) |
| | `GET` | `/api/v1/customers/{id}/orders` | 200, 404 | Lịch sử đơn hàng của khách hàng theo thời gian |
| **Orders** | `GET` | `/api/v1/orders` | 200, 400 | Danh sách đơn hàng toàn hệ thống (lọc theo khoảng ngày) |
| | `POST` | `/api/v1/orders` | 201, 400, 404, 422 | Đặt hàng (khởi tạo `createdAt` thời điểm chốt đơn) |
| | `GET` | `/api/v1/orders/{id}` | 200, 404 | Chi tiết hóa đơn và đơn giá lịch sử |
| | `PATCH` | `/api/v1/orders/{id}/status` | 200, 400, 404, 422 | Cập nhật vòng đời đơn hàng (cập nhật `updatedAt`) |
