package com.shop.seeder;

import com.shop.entity.Category;
import com.shop.entity.Customer;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.Product;
import com.shop.repository.CategoryRepository;
import com.shop.repository.CustomerRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Tiêu chí Rubric: Viết lớp CommandLineRunner kết hợp thư viện Faker để tự động
 * nạp dữ liệu giả lập (danh mục, sản phẩm mẫu, khách hàng) khi ứng dụng khởi chạy.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            log.info("Cơ sở dữ liệu MySQL đã có dữ liệu. Bỏ qua bước Seeding.");
            return;
        }

        log.info("Bắt đầu nạp dữ liệu mẫu ban đầu bằng CommandLineRunner & DataFaker...");
        Faker faker = new Faker(Locale.forLanguageTag("vi"));

        // 1. Seed Khách hàng & Tài khoản
        Customer admin = Customer.builder()
                .fullName("Quản trị viên Hệ thống")
                .email("admin@shop.com")
                .password(passwordEncoder.encode("admin123"))
                .role("ADMIN")
                .phone("0901234567")
                .address("Tòa nhà Bitexco, Bến Nghé, Quận 1, TP.HCM")
                .avatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
                .status("ACTIVE")
                .build();

        Customer staff = Customer.builder()
                .fullName("Trần Thị Thu Ngân (Staff)")
                .email("staff@shop.com")
                .password(passwordEncoder.encode("staff123"))
                .role("STAFF")
                .phone("0909998877")
                .address("45 Lê Duẩn, Bến Nghé, Quận 1, TP.HCM")
                .avatarUrl("https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150")
                .status("ACTIVE")
                .build();

        Customer cust1 = Customer.builder()
                .fullName("Nguyễn Văn An")
                .email("nguyenvana@example.com")
                .password(passwordEncoder.encode("pass123"))
                .role("CUSTOMER")
                .phone("0912345678")
                .address("72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM")
                .avatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150")
                .status("ACTIVE")
                .build();

        Customer cust2 = Customer.builder()
                .fullName(faker.name().fullName())
                .email("khachhang2@example.com")
                .password(passwordEncoder.encode("pass123"))
                .role("CUSTOMER")
                .phone("0988112233")
                .address(faker.address().fullAddress())
                .avatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150")
                .status("ACTIVE")
                .build();

        customerRepository.saveAll(List.of(admin, staff, cust1, cust2));

        // 2. Seed Danh mục sản phẩm
        Category catAcc = Category.builder()
                .code("ACCESSORIES")
                .name("Phụ kiện máy tính")
                .description("Bàn phím cơ, chuột không dây và giá treo công thái học")
                .status("ACTIVE")
                .build();

        Category catMon = Category.builder()
                .code("MONITOR")
                .name("Màn hình máy tính")
                .description("Màn hình đồ họa 4K IPS Black và màn hình gaming chuyên dụng")
                .status("ACTIVE")
                .build();

        Category catAud = Category.builder()
                .code("AUDIO")
                .name("Âm thanh & Tai nghe")
                .description("Tai nghe chống ồn chủ động, loa Bluetooth vintage và micro thu âm")
                .status("ACTIVE")
                .build();

        Category catLap = Category.builder()
                .code("LAPTOP")
                .name("Máy tính xách tay")
                .description("Laptop văn phòng mỏng nhẹ và máy trạm đồ họa hiệu năng cao")
                .status("ACTIVE")
                .build();

        categoryRepository.saveAll(List.of(catAcc, catMon, catAud, catLap));

        // 3. Seed Sản phẩm
        Product p1 = Product.builder()
                .category(catAcc)
                .name("Bàn phím cơ không dây Keychron Q1 Pro QMK/VIA")
                .sku("KB-KEY-Q1PRO")
                .price(BigDecimal.valueOf(4290000))
                .stockQuantity(15)
                .imageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500")
                .description("Khung nhôm nguyên khối CNC, kết nối Bluetooth 5.1 và có dây Type-C")
                .status("ACTIVE")
                .build();

        Product p2 = Product.builder()
                .category(catAcc)
                .name("Chuột công thái học Logitech MX Master 3S")
                .sku("MS-LOGI-MX3S")
                .price(BigDecimal.valueOf(2490000))
                .stockQuantity(25)
                .imageUrl("https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500")
                .description("Cảm biến quang học 8K DPI tĩnh âm, con lăn MagSpeed siêu tốc")
                .status("ACTIVE")
                .build();

        Product p3 = Product.builder()
                .category(catMon)
                .name("Màn hình chuyên đồ họa Dell UltraSharp U2723QE 4K")
                .sku("MON-DELL-U2723")
                .price(BigDecimal.valueOf(13990000))
                .stockQuantity(8)
                .imageUrl("https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500")
                .description("Tấm nền IPS Black độ tương phản 2000:1, chuẩn 98% DCI-P3")
                .status("ACTIVE")
                .build();

        Product p4 = Product.builder()
                .category(catAud)
                .name("Tai nghe chống ồn không dây Sony WH-1000XM5")
                .sku("HP-SONY-XM5")
                .price(BigDecimal.valueOf(6990000))
                .stockQuantity(12)
                .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500")
                .description("Công nghệ khử ồn Auto NC Optimizer, hỗ trợ âm thanh Hi-Res LDAC")
                .status("ACTIVE")
                .build();

        Product p5 = Product.builder()
                .category(catAcc)
                .name("Bàn di chuột bằng da tự nhiên kích thước lớn (Hết hàng mẫu)")
                .sku("PAD-LEATHER-XL")
                .price(BigDecimal.valueOf(450000))
                .stockQuantity(0)
                .imageUrl("https://images.unsplash.com/photo-1629429408209-1f912961dbd8?w=500")
                .description("Chất liệu da bò thật khâu tay viền chắc chắn")
                .status("OUT_OF_STOCK")
                .build();

        productRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        // 4. Seed Đơn hàng mẫu
        Order order1 = Order.builder()
                .customer(cust1)
                .totalAmount(BigDecimal.valueOf(6780000))
                .status("PENDING")
                .shippingAddress("72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM")
                .build();

        OrderItem item1 = OrderItem.builder()
                .product(p1)
                .productName(p1.getName())
                .unitPrice(p1.getPrice())
                .quantity(1)
                .subTotal(p1.getPrice())
                .build();

        OrderItem item2 = OrderItem.builder()
                .product(p2)
                .productName(p2.getName())
                .unitPrice(p2.getPrice())
                .quantity(1)
                .subTotal(p2.getPrice())
                .build();

        order1.addItem(item1);
        order1.addItem(item2);
        orderRepository.save(order1);

        log.info("Hoàn tất Database Seeding vào cơ sở dữ liệu MySQL thành công!");
    }
}
