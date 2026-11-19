-- =================================================================
-- CƠ SỞ DỮ LIỆU: QUẢN LÝ CỬA HÀNG SIÊU THỊ MINI (QLSieuThiMini)
-- Hệ quản trị: Microsoft SQL Server
-- =================================================================

ALTER LOGIN sa ENABLE;
GO
ALTER LOGIN sa WITH PASSWORD = '123456', CHECK_POLICY = OFF;
GO

IF DB_ID('QLSieuThiMini') IS NOT NULL
BEGIN
    ALTER DATABASE QLSieuThiMini SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLSieuThiMini;
END
GO

CREATE DATABASE QLSieuThiMini;
GO
USE QLSieuThiMini;
GO

CREATE TABLE LoaiSanPham (
    MaLoai VARCHAR(10) PRIMARY KEY,
    TenLoai NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(255)
);

CREATE TABLE SanPham (
    MaSP VARCHAR(10) PRIMARY KEY,
    TenSP NVARCHAR(150) NOT NULL,
    DonViTinh NVARCHAR(30) NOT NULL,
    DonGia FLOAT NOT NULL CHECK (DonGia >= 0),
    SoLuongTon INT NOT NULL CHECK (SoLuongTon >= 0),
    MaLoai VARCHAR(10) NOT NULL,
    CONSTRAINT FK_SanPham_LoaiSanPham FOREIGN KEY (MaLoai)
        REFERENCES LoaiSanPham(MaLoai) ON DELETE CASCADE
);

CREATE TABLE NhanVien (
    MaNV VARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    ChucVu NVARCHAR(50) NOT NULL,
    MatKhau VARCHAR(50) NOT NULL
);

CREATE TABLE HoaDon (
    MaHD VARCHAR(10) PRIMARY KEY,
    NgayLap DATE NOT NULL DEFAULT GETDATE(),
    MaNV VARCHAR(10) NOT NULL,
    TongTien FLOAT DEFAULT 0,
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (MaNV)
        REFERENCES NhanVien(MaNV) ON DELETE CASCADE
);

CREATE TABLE ChiTietHoaDon (
    MaHD VARCHAR(10) NOT NULL,
    MaSP VARCHAR(10) NOT NULL,
    SoLuongBan INT NOT NULL CHECK (SoLuongBan > 0),
    DonGiaBan FLOAT NOT NULL CHECK (DonGiaBan >= 0),
    ThanhTien AS (SoLuongBan * DonGiaBan),
    PRIMARY KEY (MaHD, MaSP),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (MaHD)
        REFERENCES HoaDon(MaHD) ON DELETE CASCADE,
    CONSTRAINT FK_CTHD_SanPham FOREIGN KEY (MaSP)
        REFERENCES SanPham(MaSP) ON DELETE CASCADE
);
GO

INSERT INTO LoaiSanPham VALUES
('L01', N'Đồ uống giải khát', N'Nước ngọt, bia, sữa, nước khoáng'),
('L02', N'Bánh kẹo & Đồ ăn nhanh', N'Snack, bánh quy, mì ăn liền'),
('L03', N'Hóa mỹ phẩm & Gia dụng', N'Dầu gội, sữa tắm, nước rửa chén');

INSERT INTO SanPham VALUES
('SP01', N'Thùng Bia Tiger 24 lon', N'Thùng', 380000, 50, 'L01'),
('SP02', N'Lốc Sữa tươi Vinamilk 180ml', N'Lốc', 32000, 120, 'L01'),
('SP03', N'Bánh Chocopie hộp 12 cái', N'Hộp', 55000, 80, 'L02'),
('SP04', N'Nước rửa chén Sunlight 750g', N'Chai', 28000, 65, 'L03');

INSERT INTO NhanVien VALUES
('NV01', N'Nguyễn Văn An', '0901234567', N'Quản lý', '123456'),
('NV02', N'Trần Thị Bình', '0912345678', N'Thu ngân', '123456'),
('NV03', N'Lê Hoàng Nam', '0987654321', N'Nhân viên kho', '123456');

INSERT INTO HoaDon VALUES
('HD01', '2026-10-10', 'NV02', 444000),
('HD02', '2026-10-12', 'NV02', 83000);

INSERT INTO ChiTietHoaDon (MaHD, MaSP, SoLuongBan, DonGiaBan) VALUES
('HD01', 'SP01', 1, 380000),
('HD01', 'SP02', 2, 32000),
('HD02', 'SP03', 1, 55000),
('HD02', 'SP04', 1, 28000);
GO
