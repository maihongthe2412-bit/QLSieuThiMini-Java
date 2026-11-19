package view;

import dao.DBConnect;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FrmDangNhap extends JFrame {
    private JTextField txtMaNV;
    private JPasswordField txtMatKhau;

    public FrmDangNhap() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG SIÊU THỊ MINI");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP PHẦN MỀM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridLayout(2, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        pnlCenter.add(new JLabel("Mã nhân viên (VD: NV01):"));
        txtMaNV = new JTextField("NV01");
        pnlCenter.add(txtMaNV);

        pnlCenter.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JPasswordField("123456");
        pnlCenter.add(txtMatKhau);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        JButton btnDangNhap = new JButton("Đăng nhập");
        JButton btnThoat = new JButton("Thoát");

        pnlBottom.add(btnDangNhap);
        pnlBottom.add(btnThoat);
        add(pnlBottom, BorderLayout.SOUTH);

        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        btnThoat.addActionListener(e -> System.exit(0));
    }

    private void xuLyDangNhap() {
        String maNV = txtMaNV.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (maNV.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã nhân viên và Mật khẩu!");
            return;
        }

        String sql = "SELECT HoTen, ChucVu FROM NhanVien WHERE MaNV = ? AND MatKhau = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setString(2, matKhau);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString("HoTen");
                String chucVu = rs.getString("ChucVu");
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + hoTen + " (" + chucVu + ")");
                this.dispose();
                hienThiTrangChu(hoTen, chucVu);
            } else {
                JOptionPane.showMessageDialog(this, "Sai Mã nhân viên hoặc Mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + ex.getMessage());
        }
    }

    private void hienThiTrangChu(String hoTen, String chucVu) {
        JFrame frmMain = new JFrame("HỆ THỐNG QUẢN LÝ CỬA HÀNG SIÊU THỊ MINI");
        frmMain.setSize(600, 400);
        frmMain.setLocationRelativeTo(null);
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMain.setLayout(new BorderLayout(15, 15));

        JLabel lblHeader = new JLabel("PHẦN MỀM QUẢN LÝ SIÊU THỊ MINI", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        lblHeader.setForeground(new Color(0, 128, 64));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        frmMain.add(lblHeader, BorderLayout.NORTH);

        JPanel pnlMenu = new JPanel(new GridLayout(2, 2, 20, 20));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton btnQLSanPham = new JButton("1. QUẢN LÝ SẢN PHẨM");
        btnQLSanPham.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnQLLoaiSP = new JButton("2. QUẢN LÝ LOẠI SẢN PHẨM");
        btnQLLoaiSP.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnQLNhanVien = new JButton("3. QUẢN LÝ NHÂN VIÊN");
        btnQLNhanVien.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnDangXuat = new JButton("4. ĐĂNG XUẤT");
        btnDangXuat.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangXuat.setForeground(Color.RED);

        pnlMenu.add(btnQLSanPham);
        pnlMenu.add(btnQLLoaiSP);
        pnlMenu.add(btnQLNhanVien);
        pnlMenu.add(btnDangXuat);

        frmMain.add(pnlMenu, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("  Đang đăng nhập: " + hoTen + " | Chức vụ: " + chucVu);
        lblFooter.setBorder(BorderFactory.createEtchedBorder());
        frmMain.add(lblFooter, BorderLayout.SOUTH);

        btnQLSanPham.addActionListener(e -> new FrmSanPham().setVisible(true));
        btnQLLoaiSP.addActionListener(e -> new FrmLoaiSanPham().setVisible(true));
        btnQLNhanVien.addActionListener(e -> new FrmNhanVien().setVisible(true));
        btnDangXuat.addActionListener(e -> {
            frmMain.dispose();
            new FrmDangNhap().setVisible(true);
        });

        frmMain.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
    }
}
