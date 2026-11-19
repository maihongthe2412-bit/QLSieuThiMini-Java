package view;

import dao.DBConnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrmSanPham extends JFrame {
    private JTextField txtMaSP, txtTenSP, txtDonVi, txtDonGia, txtSoLuong, txtMaLoai, txtTimKiem;
    private JTable tblSanPham;
    private DefaultTableModel model;

    public FrmSanPham() {
        setTitle("QUẢN LÝ SẢN PHẨM - SIÊU THỊ MINI");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        pnlInput.add(new JLabel("Mã sản phẩm:"));
        txtMaSP = new JTextField();
        pnlInput.add(txtMaSP);

        pnlInput.add(new JLabel("Tên sản phẩm:"));
        txtTenSP = new JTextField();
        pnlInput.add(txtTenSP);

        pnlInput.add(new JLabel("Đơn vị tính:"));
        txtDonVi = new JTextField();
        pnlInput.add(txtDonVi);

        pnlInput.add(new JLabel("Đơn giá:"));
        txtDonGia = new JTextField();
        pnlInput.add(txtDonGia);

        pnlInput.add(new JLabel("Số lượng tồn:"));
        txtSoLuong = new JTextField();
        pnlInput.add(txtSoLuong);

        pnlInput.add(new JLabel("Mã loại (L01, L02...):"));
        txtMaLoai = new JTextField();
        pnlInput.add(txtMaLoai);

        add(pnlInput, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Đơn Vị Tính", "Đơn Giá", "Số Lượng Tồn", "Mã Loại"};
        model = new DefaultTableModel(columns, 0);
        tblSanPham = new JTable(model);
        add(new JScrollPane(tblSanPham), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        txtTimKiem = new JTextField(15);
        JButton btnTimKiem = new JButton("Tìm kiếm theo tên");

        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);
        pnlBottom.add(txtTimKiem);
        pnlBottom.add(btnTimKiem);
        add(pnlBottom, BorderLayout.SOUTH);

        loadData("");

        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblSanPham.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(model.getValueAt(row, 0).toString());
                    txtTenSP.setText(model.getValueAt(row, 1).toString());
                    txtDonVi.setText(model.getValueAt(row, 2).toString());
                    txtDonGia.setText(model.getValueAt(row, 3).toString());
                    txtSoLuong.setText(model.getValueAt(row, 4).toString());
                    txtMaLoai.setText(model.getValueAt(row, 5).toString());
                    txtMaSP.setEditable(false);
                }
            }
        });

        btnThem.addActionListener(e -> {
            String sql = "INSERT INTO SanPham VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaSP.getText().trim());
                ps.setString(2, txtTenSP.getText().trim());
                ps.setString(3, txtDonVi.getText().trim());
                ps.setFloat(4, Float.parseFloat(txtDonGia.getText().trim()));
                ps.setInt(5, Integer.parseInt(txtSoLuong.getText().trim()));
                ps.setString(6, txtMaLoai.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm dữ liệu: " + ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            String sql = "UPDATE SanPham SET TenSP=?, DonViTinh=?, DonGia=?, SoLuongTon=?, MaLoai=? WHERE MaSP=?";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenSP.getText().trim());
                ps.setString(2, txtDonVi.getText().trim());
                ps.setFloat(3, Float.parseFloat(txtDonGia.getText().trim()));
                ps.setInt(4, Integer.parseInt(txtSoLuong.getText().trim()));
                ps.setString(5, txtMaLoai.getText().trim());
                ps.setString(6, txtMaSP.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM SanPham WHERE MaSP=?";
                try (Connection conn = DBConnect.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, txtMaSP.getText().trim());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
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
        String sql = "SELECT * FROM SanPham WHERE TenSP LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaSP"),
                    rs.getString("TenSP"),
                    rs.getString("DonViTinh"),
                    rs.getFloat("DonGia"),
                    rs.getInt("SoLuongTon"),
                    rs.getString("MaLoai")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonVi.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtMaLoai.setText("");
        txtTimKiem.setText("");
        txtMaSP.setEditable(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmSanPham().setVisible(true));
    }
}
