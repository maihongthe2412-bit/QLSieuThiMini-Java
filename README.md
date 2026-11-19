# Phần Mềm Quản Lý Cửa Hàng Siêu Thị Mini (Java Swing + SQL Server)

Đồ án môn học Lập trình Java: Ứng dụng Desktop quản lý cửa hàng siêu thị mini sử dụng **Java Swing** kết nối cơ sở dữ liệu **Microsoft SQL Server** qua **JDBC**.

## Cấu trúc thư mục
```text
QLSieuThiMini/
├── database/
│   └── QLSieuThiMini.sql           # Kịch bản tạo CSDL, 5 bảng và dữ liệu mẫu
├── lib/
│   └── HUONG_DAN_THEM_JDBC.txt     # Hướng dẫn tải & gắn file mssql-jdbc.jar
├── src/
│   ├── dao/
│   │   └── DBConnect.java          # Kết nối SQL Server qua JDBC
│   └── view/
│       ├── FrmDangNhap.java        # Form Đăng nhập & Màn hình Trang chủ (Chạy file này)
│       ├── FrmSanPham.java         # Form Quản lý Sản phẩm (Thêm, Sửa, Xóa, Tìm kiếm)
│       ├── FrmLoaiSanPham.java     # Form Quản lý Loại sản phẩm
│       └── FrmNhanVien.java        # Form Quản lý Nhân viên
├── HUONG_DAN_CHAY_VA_UP_GITHUB.txt # Hướng dẫn chi tiết chạy project & đẩy lên GitHub
└── .gitignore
```

## Tài khoản đăng nhập mẫu
- **Mã nhân viên:** `NV01`
- **Mật khẩu:** `123456`
