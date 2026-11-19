package view;

import dao.DBConnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrmNhanVien extends JFrame {
    private JTextField txtMaNV, txtHoTen, txtSDT, txtMatKhau, txtTimKiem;
    private JComboBox<String> cboChucVu;
    private JTable tblNhanVien;
    private DefaultTableModel model;

    public FrmNhanVien() {
        setTitle("QUẢN LÝ NHÂN VIÊN - SIÊU THỊ MINI");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân Viên"));

        pnlInput.add(new JLabel("Mã nhân viên (VD: NV01):"));
        txtMaNV = new JTextField();
        pnlInput.add(txtMaNV);

        pnlInput.add(new JLabel("Họ và tên:"));
        txtHoTen = new JTextField();
        pnlInput.add(txtHoTen);

        pnlInput.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        pnlInput.add(txtSDT);

        pnlInput.add(new JLabel("Chức vụ:"));
        String[] chucVuList = {"Quản lý", "Thu ngân", "Nhân viên kho", "Nhân viên bán hàng"};
        cboChucVu = new JComboBox<>(chucVuList);
        pnlInput.add(cboChucVu);

        pnlInput.add(new JLabel("Mật khẩu đăng nhập:"));
        txtMatKhau = new JTextField();
        pnlInput.add(txtMatKhau);

        pnlInput.add(new JLabel(""));
        pnlInput.add(new JLabel(""));

        add(pnlInput, BorderLayout.NORTH);

        String[] columns = {"Mã NV", "Họ và Tên", "Số Điện Thoại", "Chức Vụ", "Mật Khẩu"};
        model = new DefaultTableModel(columns, 0);
        tblNhanVien = new JTable(model);
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        txtTimKiem = new JTextField(15);
        JButton btnTimKiem = new JButton("Tìm theo Họ tên");

        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);
        pnlBottom.add(txtTimKiem);
        pnlBottom.add(btnTimKiem);
        add(pnlBottom, BorderLayout.SOUTH);

        loadData("");

        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(model.getValueAt(row, 0).toString());
                    txtHoTen.setText(model.getValueAt(row, 1).toString());
                    txtSDT.setText(model.getValueAt(row, 2).toString());
                    cboChucVu.setSelectedItem(model.getValueAt(row, 3).toString());
                    txtMatKhau.setText(model.getValueAt(row, 4).toString());
                    txtMaNV.setEditable(false);
                }
            }
        });

        btnThem.addActionListener(e -> {
            if (txtMaNV.getText().trim().isEmpty() || txtHoTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã NV và Họ tên!");
                return;
            }
            String sql = "INSERT INTO NhanVien (MaNV, HoTen, SoDienThoai, ChucVu, MatKhau) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaNV.getText().trim());
                ps.setString(2, txtHoTen.getText().trim());
                ps.setString(3, txtSDT.getText().trim());
                ps.setString(4, cboChucVu.getSelectedItem().toString());
                ps.setString(5, txtMatKhau.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm nhân viên: " + ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaNV.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa trên bảng!");
                return;
            }
            String sql = "UPDATE NhanVien SET HoTen=?, SoDienThoai=?, ChucVu=?, MatKhau=? WHERE MaNV=?";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtHoTen.getText().trim());
                ps.setString(2, txtSDT.getText().trim());
                ps.setString(3, cboChucVu.getSelectedItem().toString());
                ps.setString(4, txtMatKhau.getText().trim());
                ps.setString(5, txtMaNV.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaNV.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM NhanVien WHERE MaNV=?";
                try (Connection conn = DBConnect.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, txtMaNV.getText().trim());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                    loadData("");
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa dữ liệu: " + ex.getMessage());
                }
            }
        });

        btnTimKiem.addActionListener(e -> loadData(txtTimKiem.getText().trim()));
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData("");
        });
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaNV"),
                    rs.getString("HoTen"),
                    rs.getString("SoDienThoai"),
                    rs.getString("ChucVu"),
                    rs.getString("MatKhau")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        cboChucVu.setSelectedIndex(0);
        txtMatKhau.setText("");
        txtTimKiem.setText("");
        txtMaNV.setEditable(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmNhanVien().setVisible(true));
    }
}
