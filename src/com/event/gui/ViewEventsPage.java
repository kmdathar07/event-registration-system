package com.event.gui;

import com.event.util.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewEventsPage extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public ViewEventsPage() {
        setTitle("Manage Events");
        setSize(750, 420);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Event Name", "Date", "Capacity"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JButton refreshBtn = new JButton("🔄 Refresh");
        JButton updateBtn = new JButton("✏️ Update Event");
        JButton deleteBtn = new JButton("🗑️ Delete Event");

        styleButton(refreshBtn, new Color(46, 139, 87));
        styleButton(updateBtn, new Color(0, 102, 204));
        styleButton(deleteBtn, new Color(178, 34, 34));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadEvents());
        updateBtn.addActionListener(e -> updateEvent());
        deleteBtn.addActionListener(e -> deleteEvent());

        loadEvents();
        setVisible(true);
    }

    private void loadEvents() {
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name, date, capacity FROM event ORDER BY date")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getInt("capacity")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEvent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int eventId = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete event \"" + name + "\" and all its registrations?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps1 = con.prepareStatement("DELETE FROM registrations WHERE event_id = ?")) {
                ps1.setInt(1, eventId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = con.prepareStatement("DELETE FROM event WHERE id = ?")) {
                ps2.setInt(1, eventId);
                ps2.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "✅ Event and linked registrations deleted.");
            loadEvents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEvent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to update.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int eventId = (int) model.getValueAt(row, 0);
        String oldName = (String) model.getValueAt(row, 1);
        String oldDate = (String) model.getValueAt(row, 2);
        int oldCap = (int) model.getValueAt(row, 3);

        String newName = JOptionPane.showInputDialog(this, "New name:", oldName);
        if (newName == null || newName.trim().isEmpty()) return;
        String newDate = JOptionPane.showInputDialog(this, "New date (YYYY-MM-DD):", oldDate);
        if (newDate == null || newDate.trim().isEmpty()) return;
        String newCapStr = JOptionPane.showInputDialog(this, "New capacity:", oldCap);
        if (newCapStr == null || newCapStr.trim().isEmpty()) return;

        int newCap;
        try {
            newCap = Integer.parseInt(newCapStr);
            if (newCap < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a non-negative integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE event SET name=?, date=?, capacity=? WHERE id=?")) {
            ps.setString(1, newName);
            ps.setString(2, newDate);
            ps.setInt(3, newCap);
            ps.setInt(4, eventId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Event updated.");
            loadEvents();
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
