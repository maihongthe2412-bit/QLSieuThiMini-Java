package view;

import dao.DBConnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FrmLoaiSanPham extends JFrame {
    private JTextField txtMaLoai, txtTenLoai, txtMoTa, txtTimKiem;
    private JTable tblLoaiSP;
    private DefaultTableModel model;

    public FrmLoaiSanPham() {
        setTitle("QUẢN LÝ LOẠI SẢN PHẨM - SIÊU THỊ MINI");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin Loại Sản Phẩm"));

        pnlInput.add(new JLabel("Mã loại sản phẩm (VD: L01, L02):"));
        txtMaLoai = new JTextField();
        pnlInput.add(txtMaLoai);

        pnlInput.add(new JLabel("Tên loại sản phẩm:"));
        txtTenLoai = new JTextField();
        pnlInput.add(txtTenLoai);

        pnlInput.add(new JLabel("Mô tả chi tiết:"));
        txtMoTa = new JTextField();
        pnlInput.add(txtMoTa);

        add(pnlInput, BorderLayout.NORTH);

        String[] columns = {"Mã Loại", "Tên Loại Sản Phẩm", "Mô Tả"};
        model = new DefaultTableModel(columns, 0);
        tblLoaiSP = new JTable(model);
        add(new JScrollPane(tblLoaiSP), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        txtTimKiem = new JTextField(15);
        JButton btnTimKiem = new JButton("Tìm kiếm tên loại");

        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);
        pnlBottom.add(txtTimKiem);
        pnlBottom.add(btnTimKiem);
        add(pnlBottom, BorderLayout.SOUTH);

        loadData("");

        tblLoaiSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblLoaiSP.getSelectedRow();
                if (row >= 0) {
                    txtMaLoai.setText(model.getValueAt(row, 0).toString());
                    txtTenLoai.setText(model.getValueAt(row, 1).toString());
                    Object moTaObj = model.getValueAt(row, 2);
                    txtMoTa.setText(moTaObj != null ? moTaObj.toString() : "");
                    txtMaLoai.setEditable(false);
                }
            }
        });

        btnThem.addActionListener(e -> {
            if (txtMaLoai.getText().trim().isEmpty() || txtTenLoai.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã loại và Tên loại!");
                return;
            }
            String sql = "INSERT INTO LoaiSanPham (MaLoai, TenLoai, MoTa) VALUES (?, ?, ?)";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtMaLoai.getText().trim());
                ps.setString(2, txtTenLoai.getText().trim());
                ps.setString(3, txtMoTa.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm loại sản phẩm thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm dữ liệu (Có thể trùng Mã loại): " + ex.getMessage());
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaLoai.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần sửa trên bảng!");
                return;
            }
            String sql = "UPDATE LoaiSanPham SET TenLoai=?, MoTa=? WHERE MaLoai=?";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtTenLoai.getText().trim());
                ps.setString(2, txtMoTa.getText().trim());
                ps.setString(3, txtMaLoai.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật loại sản phẩm thành công!");
                loadData("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + ex.getMessage());
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaLoai.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa loại sản phẩm này sẽ xóa các sản phẩm thuộc loại đó. Bạn có chắc chắn?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM LoaiSanPham WHERE MaLoai=?";
                try (Connection conn = DBConnect.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, txtMaLoai.getText().trim());
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
        String sql = "SELECT * FROM LoaiSanPham WHERE TenLoai LIKE ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaLoai"),
                    rs.getString("TenLoai"),
                    rs.getString("MoTa")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        txtMaLoai.setEditable(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLoaiSanPham().setVisible(true));
    }
}
