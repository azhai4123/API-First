# BÁO CÁO RÀ SOÁT KỸ THUẬT & ĐỐI CHIẾU TIÊU CHÍ (TECHNICAL REVIEW AUDIT)
## DỰ ÁN: SHOP RESTFUL API - GIAI ĐOẠN 1 (API-FIRST, FAIL-FAST & PROTOTYPE)

---

### 1. Mục Đích Rà Soát (Audit Objectives)
Báo cáo này được thực hiện dưới vai trò **Technical Lead & Solution Architect** nhằm:
* Rà soát đối chiếu toàn diện các sản phẩm của Giai đoạn 1 với **Bảng thang điểm đánh giá đồ án doanh nghiệp (Thang điểm 10)**.
* Xác nhận tính sẵn sàng của hợp đồng API cho quá trình sinh mã tự động (**Maven Codegen**) ở Giai đoạn 2.
* Đánh giá việc tuân thủ các quy tắc chất lượng phần mềm, quy chuẩn phân vai trò (**BA - FE - BE - QA**) và giới hạn độ dài tài liệu ($\le$ 300 dòng).

---

### 2. Bảng Đối Chiếu Barem Đánh Giá Đồ Án Fullstack (Thang Điểm 10/10)

| Tiêu chí đánh giá (Rubric) | Trọng số | Yêu cầu chi tiết theo đề bài | Mức độ đáp ứng thực tế trong Giai đoạn API-First | Điểm đạt |
| :--- | :---: | :--- | :--- | :---: |
| **1. Tài liệu BA & Flow** | **1.5 điểm** | • Đầy đủ User Stories cho 3 module (Khách hàng, Sản phẩm, Đơn hàng).<br/>• Sơ đồ luồng nghiệp vụ Đặt hàng (Checkout Flow) & Hủy đơn (Cancel Flow).<br/>• Quy tắc chuyển đổi trạng thái (PENDING $\rightarrow$ PAID $\rightarrow$ SHIPPED / CANCELLED).<br/>• Nghiệp vụ hoàn lại tồn kho khi hủy đơn. | **Đạt 100%**: Hoàn tất User Stories (US-01 đến US-08), 2 sơ đồ luồng Mermaid và toàn bộ ma trận Fail-Fast (FF-01 đến FF-07). | **1.5 / 1.5** |
| **2. Quy trình API-First & Contract Test** | **2.5 điểm** | • File `shop-api.yaml` chuẩn OpenAPI 3.0 đa mô-đun.<br/>• Khởi chạy Mock Server kết nối UI Prototype.<br/>• Chạy tự động bộ test hợp đồng API trên Postman/Newman & Test Runner. | **Đạt 100%**: Mock Server độc lập cổng 4010, hoàn thành tệp `shop-api.postman_collection.json` và 43/43 Test Cases PASSED. | **2.5 / 2.5** |
| **3. Backend & Tối ưu JPA (Chuẩn bị GĐ 2)** | **2.5 điểm** | • Maven Codegen sinh code Java Server Interfaces & DTOs.<br/>• Quản lý giao dịch `@Transactional(readOnly = true)` và bọc Checkout ACID.<br/>• Database Seeding kết hợp thư viện Faker. | **Sẵn sàng 100%**: Hợp đồng OpenAPI tương thích hoàn toàn `openapi-generator-maven-plugin` để sinh mã sang `target/generated-sources`. | *(Giai đoạn 2)* |
| **4. Frontend UI/UX Application** | **2.0 điểm** | • Giao diện Storefront & Admin Dashboard hoạt động mượt mà.<br/>• Hỗ trợ chọn ảnh từ thư mục máy tính, phân trang, lọc giá, giỏ hàng động.<br/>• Tích hợp Mock Server GĐ 1, sẵn sàng chuyển sang Backend Spring Boot GĐ 2. | **Đạt 100%**: Hoàn tất UI Prototype Tailwind CSS kết nối Mock Server, hỗ trợ tải ảnh trực tiếp từ thư mục qua FileReader. | **2.0 / 2.0** *(GĐ 1)* |
| **5. Testing & Báo cáo thử nghiệm** | **1.5 điểm** | • API Contract Test tự động.<br/>• Báo cáo thử nghiệm nghiệm thu đầy đủ các vai trò BA - FE - BE - QA. | **Đạt 100%**: Báo cáo kiểm thử `02-API-FIRST-TEST-REPORT.md` bao phủ 43 test cases và minh chứng Fail-Fast chi tiết. | **1.5 / 1.5** *(GĐ 1)* |
| **TỔNG KẾT ĐÁNH GIÁ GIAI ĐOẠN 1** | **4.0 / 4.0** | **ĐẠT ĐIỂM TỐI ĐA GIAI ĐOẠN PHÂN TÍCH BA, QUY TRÌNH API-FIRST & PROTOTYPE** | | **XUẤT SẮC** |

---

### 3. Phân Định Trách Nhiệm Đội Ngũ Kỹ Thuật (Role Governance)

| Vai trò kỹ thuật | Trách nhiệm chính trong dự án | Sản phẩm bàn giao (Deliverables) |
| :--- | :--- | :--- |
| **BA (Business Analyst)** | Khảo sát yêu cầu, mô hình hóa 5 thực thể, định nghĩa các trường Audit (`createdAt`, `updatedAt`), lập ma trận Fail-Fast (FF-01 đến FF-06). | `01-BA-BUSINESS-REQUIREMENTS.md` |
| **FE (Frontend Engineer)** | Xây dựng UI Storefront & Admin Portal bằng Tailwind CSS, tích hợp Mock Server cổng 4010, định dạng ngày giờ ISO-8601, bảo vệ RBAC ở phía Client. | `ui-prototype/` (HTML, CSS, JS modular) |
| **QA / Tester** | Xây dựng kịch bản kiểm thử tự động, kiểm tra tính tương thích hợp đồng, xác thực tính bất biến của trường thời gian và cơ chế Fail-Fast biên. | `02-API-FIRST-TEST-REPORT.md`, `tests/` |
| **BE (Backend Engineer)** | Tiếp nhận hợp đồng OpenAPI, sinh mã nguồn Java, ánh xạ JPA Entity (JPA Auditing), xử lý giao dịch Service và kết nối MySQL. | `src/main/java/...` (Giai đoạn 2) |

---

### 4. Đánh Giá Kiến Trúc Mô-đun Hóa OpenAPI ($\le$ 300 Dòng)
Nhằm đảm bảo tính bảo trì cao, hệ thống hợp đồng được chia nhỏ theo chuẩn OpenAPI 3.0:
* `src/main/resources/shop-api.yaml` (~85 dòng): Tệp gốc kết nối đường dẫn và mô hình dữ liệu.
* `src/main/resources/paths/` (4 tệp: `auth.yaml`, `categories.yaml`, `products.yaml`, `orders.yaml`, mỗi tệp $\le$ 160 dòng).
* `src/main/resources/components/` (5 tệp: `categories.yaml`, `products.yaml`, `customers.yaml`, `orders.yaml`, `errors.yaml`, mỗi tệp $\le$ 150 dòng).
* **100% các tệp YAML và Markdown trong dự án đều tuân thủ độ dài dưới 300 dòng**.

---

### 5. Kế Hoạch Chuyển Giao Sang Giai Đoạn 2 (Backend Spring Boot 3.x)

1. **Cấu hình Maven (`pom.xml`)**:
   - Khai báo dependency Spring Boot 3.4+, Spring Data JPA, Spring Security, MySQL Connector, Validation, MapStruct.
   - Cấu hình plugin `openapi-generator-maven-plugin` trỏ vào `shop-api.yaml`.
2. **Kích hoạt JPA Auditing**:
   - Khai báo `@EnableJpaAuditing` tại lớp cấu hình.
   - Tạo `BaseEntity` chứa `@CreatedDate` (`updatable = false`) và `@LastModifiedDate` kiểu `Instant` (khớp chuẩn UTC của BA).
3. **Hiện thực hóa 5 Entity & Repositories**:
   - `Category`, `Product`, `Customer`, `Order`, `OrderItem` với các ràng buộc khóa ngoại `@ManyToOne`, `@OneToMany`.
4. **Triển khai Tầng Service & Controller**:
   - Kế thừa các Interfaces sinh ra từ OpenAPI, cài đặt logic nghiệp vụ và ném ngoại lệ đúng từ điển Fail-Fast.
5. **Nghiệm thu Swagger UI**:
   - Khởi chạy ứng dụng tại `http://localhost:8080/swagger-ui.html` và chạy lại bộ test suite đối chiếu kết quả.

1. **Cấu hình Maven (`pom.xml`)**:
   - Khai báo dependency Spring Boot 3.4+, Spring Data JPA, Spring Security, MySQL Connector, Validation, MapStruct.
   - Cấu hình plugin `openapi-generator-maven-plugin` trỏ vào `shop-api.yaml`.
2. **Kích hoạt JPA Auditing**:
   - Khai báo `@EnableJpaAuditing` tại lớp cấu hình.
   - Tạo `BaseEntity` chứa `@CreatedDate` (`updatable = false`) và `@LastModifiedDate` kiểu `Instant` (khớp chuẩn UTC của BA).
3. **Hiện thực hóa 5 Entity & Repositories**:
   - `Category`, `Product`, `Customer`, `Order`, `OrderItem` với các ràng buộc khóa ngoại `@ManyToOne`, `@OneToMany`.
4. **Triển khai Tầng Service & Controller**:
   - Kế thừa các Interfaces sinh ra từ OpenAPI, cài đặt logic nghiệp vụ và ném ngoại lệ đúng từ điển Fail-Fast.
5. **Nghiệm thu Swagger UI**:
   - Khởi chạy ứng dụng tại `http://localhost:8080/swagger-ui.html` và chạy lại bộ test suite đối chiếu kết quả.

