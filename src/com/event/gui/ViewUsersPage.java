package com.event.gui;

import com.event.util.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewUsersPage extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public ViewUsersPage() {
        setTitle("Registered Users");
        setSize(800, 420);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] cols = {"ID", "Name", "Email", "Phone", "Password", "Event ID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton deleteBtn = new JButton("🗑️ Delete Selected User");

        styleButton(refreshBtn, new Color(46,139,87));
        styleButton(deleteBtn, new Color(178,34,34));

        JPanel bottom = new JPanel();
        bottom.add(refreshBtn);
        bottom.add(deleteBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadUsers());
        deleteBtn.addActionListener(e -> deleteUser());

        loadUsers();
        setVisible(true);
    }

    private void loadUsers() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, email, phone, password, event_id FROM registrations")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"), // ✅ Password visible to admin
                        rs.getObject("event_id")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userId = (int) model.getValueAt(sel, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this user?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM registrations WHERE id = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ User deleted.");
            loadUsers();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }
}
