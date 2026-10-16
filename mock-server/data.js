/**
 * MOCK DATABASE SEED DATA
 * Đầy đủ 5 thực thể: Category, Product, Customer, Order, OrderItem
 * Chuẩn Audit ISO-8601 UTC: createdAt, updatedAt
 * Phân quyền 3 roles: CUSTOMER, STAFF, ADMIN
 */
const categories = [
  {
    id: 1,
    code: "ACCESSORIES",
    name: "Phụ kiện máy tính",
    description: "Bàn phím cơ, chuột không dây và giá treo công thái học",
    status: "ACTIVE",
    createdAt: "2026-10-10T08:00:00.000Z",
    updatedAt: "2026-10-10T08:00:00.000Z"
  },
  {
    id: 2,
    code: "MONITOR",
    name: "Màn hình máy tính",
    description: "Màn hình đồ họa 4K IPS Black và màn hình gaming chuyên dụng",
    status: "ACTIVE",
    createdAt: "2026-10-10T08:30:00.000Z",
    updatedAt: "2026-10-10T08:30:00.000Z"
  },
  {
    id: 3,
    code: "AUDIO",
    name: "Âm thanh & Tai nghe",
    description: "Tai nghe chống ồn chủ động, loa Bluetooth vintage và micro thu âm",
    status: "ACTIVE",
    createdAt: "2026-10-10T09:00:00.000Z",
    updatedAt: "2026-10-10T09:00:00.000Z"
  }
];

const customers = [
  {
    id: 1,
    fullName: "Quản trị viên Hệ thống",
    email: "admin@shop.com",
    password: "admin123",
    role: "ADMIN",
    phone: "0901234567",
    address: "Tòa nhà Bitexco, Bến Nghé, Quận 1, TP.HCM",
    avatarUrl: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
    status: "ACTIVE",
    createdAt: "2026-10-10T08:00:00.000Z",
    updatedAt: "2026-10-10T08:00:00.000Z"
  },
  {
    id: 2,
    fullName: "Nguyễn Văn An",
    email: "nguyenvana@example.com",
    password: "pass123",
    role: "CUSTOMER",
    phone: "0912345678",
    address: "72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM",
    avatarUrl: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
    status: "ACTIVE",
    createdAt: "2026-10-14T08:30:00.000Z",
    updatedAt: "2026-10-14T08:30:00.000Z"
  },
  {
    id: 3,
    fullName: "Trần Thị Bích",
    email: "tranthib@example.com",
    password: "pass123",
    role: "CUSTOMER",
    phone: "0933112233",
    address: "15 Nguyễn Đình Chiểu, Phường Đa Kao, Quận 1, TP.HCM",
    avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
    status: "ACTIVE",
    createdAt: "2026-10-14T08:45:00.000Z",
    updatedAt: "2026-10-14T08:45:00.000Z"
  },
  {
    id: 4,
    fullName: "Lê Hoàng Nam",
    email: "nam.le@example.com",
    password: "pass123",
    role: "CUSTOMER",
    phone: "0944556677",
    address: "88 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội",
    avatarUrl: "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
    status: "ACTIVE",
    createdAt: "2026-10-14T09:00:00.000Z",
    updatedAt: "2026-10-14T09:00:00.000Z"
  },
  {
    id: 5,
    fullName: "Hoàng Văn Kho (Staff)",
    email: "staff@shop.com",
    password: "staff123",
    role: "STAFF",
    phone: "0988112233",
    address: "Kho vận Trung tâm, Tân Bình, TP.HCM",
    avatarUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
    status: "ACTIVE",
    createdAt: "2026-10-12T08:00:00.000Z",
    updatedAt: "2026-10-12T08:00:00.000Z"
  }
];

const products = [
  {
    id: 101,
    categoryId: 1,
    category: "Phụ kiện máy tính",
    name: "Bàn phím cơ không dây Keychron K2 V2",
    sku: "KB-KEYCHRON-K2",
    price: 1850000.0,
    stockQuantity: 45,
    imageUrl: "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500",
    description: "Bàn phím cơ Bluetooth 75% Layout, switch Gateron Brown, hỗ trợ Mac và Windows",
    status: "ACTIVE",
    createdAt: "2026-10-10T09:00:00.000Z",
    updatedAt: "2026-10-10T09:00:00.000Z"
  },
  {
    id: 102,
    categoryId: 1,
    category: "Phụ kiện máy tính",
    name: "Chuột không dây công thái học Logitech MX Master 3S",
    sku: "MS-LOGI-MX3S",
    price: 2450000.0,
    stockQuantity: 32,
    imageUrl: "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500",
    description: "Cảm biến 8K DPI trên mọi bề mặt, con lăn điện từ MagSpeed êm ái",
    status: "ACTIVE",
    createdAt: "2026-10-10T09:15:00.000Z",
    updatedAt: "2026-10-10T09:15:00.000Z"
  },
  {
    id: 103,
    categoryId: 2,
    category: "Màn hình máy tính",
    name: "Màn hình chuyên đồ họa Dell UltraSharp U2723QE 4K",
    sku: "MN-DELL-U2723QE",
    price: 13990000.0,
    stockQuantity: 0, // Hết hàng phục vụ kiểm thử Fail-Fast FF-01
    imageUrl: "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500",
    description: "Màn hình 27 inch 4K IPS Black, độ tương phản 2000:1, USB-C 90W Hub (Hết hàng)",
    status: "OUT_OF_STOCK",
    createdAt: "2026-10-10T09:30:00.000Z",
    updatedAt: "2026-10-10T09:30:00.000Z"
  },
  {
    id: 104,
    categoryId: 3,
    category: "Âm thanh & Tai nghe",
    name: "Tai nghe chống ồn không dây Sony WH-1000XM5",
    sku: "AU-SONY-1000XM5",
    price: 6990000.0,
    stockQuantity: 18,
    imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500",
    description: "Chống ồn chủ động đỉnh cao kép 8 micro, pin 30 giờ, âm thanh Hi-Res Audio",
    status: "ACTIVE",
    createdAt: "2026-10-10T09:45:00.000Z",
    updatedAt: "2026-10-10T09:45:00.000Z"
  },
  {
    id: 105,
    categoryId: 2,
    category: "Màn hình máy tính",
    name: "Màn hình Gaming LG 27GP850-B UltraGear Nano IPS",
    sku: "MN-LG-27GP850",
    price: 8590000.0,
    stockQuantity: 12,
    imageUrl: "https://images.unsplash.com/photo-1547119957-637f8679db1e?w=500",
    description: "Nano IPS 2K QHD 165Hz (O/C 180Hz), 1ms G-Sync Compatible, HDR400",
    status: "ACTIVE",
    createdAt: "2026-10-10T10:00:00.000Z",
    updatedAt: "2026-10-10T10:00:00.000Z"
  },
  {
    id: 106,
    categoryId: 3,
    category: "Âm thanh & Tai nghe",
    name: "Loa Bluetooth cao cấp Marshall Acton III",
    sku: "AU-MARSHALL-ACT3",
    price: 6490000.0,
    stockQuantity: 8,
    imageUrl: "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=500",
    description: "Thiết kế vintage huyền thoại, công suất 60W, âm trường stereo mở rộng",
    status: "ACTIVE",
    createdAt: "2026-10-10T10:15:00.000Z",
    updatedAt: "2026-10-10T10:15:00.000Z"
  },
  {
    id: 107,
    categoryId: 3,
    category: "Âm thanh & Tai nghe",
    name: "Tai nghe Apple AirPods Pro 2 USB-C",
    sku: "AU-APPLE-APP2",
    price: 5490000.0,
    stockQuantity: 35,
    imageUrl: "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=500",
    description: "Chip Apple H2, chống ồn chủ động gấp 2 lần, Adaptive Audio, cổng sạc Type-C",
    status: "ACTIVE",
    createdAt: "2026-10-10T10:30:00.000Z",
    updatedAt: "2026-10-10T10:30:00.000Z"
  },
  {
    id: 108,
    categoryId: 2,
    category: "Màn hình máy tính",
    name: "Màn hình di động ASUS ZenScreen MB16AC Full HD",
    sku: "MN-ASUS-MB16AC",
    price: 5290000.0,
    stockQuantity: 0,
    imageUrl: "https://images.unsplash.com/photo-1585792180666-f7347c490ee2?w=500",
    description: "15.6 inch Full HD IPS mỏng nhẹ 780g, kết nối cáp USB Type-C tiện lợi (Hết hàng)",
    status: "OUT_OF_STOCK",
    createdAt: "2026-10-10T10:45:00.000Z",
    updatedAt: "2026-10-10T10:45:00.000Z"
  }
];

const orders = [
  {
    id: 5001,
    customerId: 2,
    totalAmount: 4300000.0,
    status: "CONFIRMED",
    shippingAddress: "72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM",
    items: [
      { id: 9001, productId: 101, productName: "Bàn phím cơ không dây Keychron K2 V2", unitPrice: 1850000.0, quantity: 1, subTotal: 1850000.0, createdAt: "2026-10-14T10:15:00.000Z" },
      { id: 9002, productId: 102, productName: "Chuột không dây Logitech MX Master 3S", unitPrice: 2450000.0, quantity: 1, subTotal: 2450000.0, createdAt: "2026-10-14T10:15:00.000Z" }
    ],
    createdAt: "2026-10-14T10:15:00.000Z",
    updatedAt: "2026-10-14T10:20:00.000Z"
  },
  {
    id: 5002,
    customerId: 3,
    totalAmount: 13990000.0,
    status: "SHIPPING",
    shippingAddress: "15 Nguyễn Đình Chiểu, Phường Đa Kao, Quận 1, TP.HCM",
    items: [
      { id: 9003, productId: 103, productName: "Màn hình chuyên đồ họa Dell UltraSharp U2723QE 4K", unitPrice: 13990000.0, quantity: 1, subTotal: 13990000.0, createdAt: "2026-10-14T11:20:00.000Z" }
    ],
    createdAt: "2026-10-14T11:20:00.000Z",
    updatedAt: "2026-10-14T12:00:00.000Z"
  },
  {
    id: 5003,
    customerId: 4,
    totalAmount: 6990000.0,
    status: "DELIVERED", // Trạng thái Terminal phục vụ FF-04
    shippingAddress: "88 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội",
    items: [
      { id: 9004, productId: 104, productName: "Tai nghe chống ồn không dây Sony WH-1000XM5", unitPrice: 6990000.0, quantity: 1, subTotal: 6990000.0, createdAt: "2026-10-13T09:00:00.000Z" }
    ],
    createdAt: "2026-10-13T09:00:00.000Z",
    updatedAt: "2026-10-13T14:30:00.000Z"
  }
];

module.exports = { categories, customers, products, orders };
