package com.event.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.event.util.DBConnection;

public class UserViewEventsPage extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public UserViewEventsPage() {
        setTitle("Available Events");
        setSize(700, 420);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] cols = {"ID", "Event Name", "Date", "Capacity", "Registered"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setEnabled(false);

        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("📅 Available Events", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        top.add(title, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(178, 34, 34));
        closeBtn.addActionListener(e -> dispose());
        bottom.add(closeBtn);
        add(bottom, BorderLayout.SOUTH);

        loadEvents();
        setVisible(true);
    }

    private void loadEvents() {
        model.setRowCount(0);
        String sql = "SELECT e.id, e.name, e.date, e.capacity, COUNT(r.id) AS registered " +
                     "FROM event e LEFT JOIN registrations r ON e.id = r.event_id " +
                     "GROUP BY e.id, e.name, e.date, e.capacity ORDER BY e.date";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getInt("capacity"),
                        rs.getInt("registered")
                });
            }
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
