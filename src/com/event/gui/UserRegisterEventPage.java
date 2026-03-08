package com.event.gui;

import com.event.util.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class UserRegisterEventPage extends JFrame {

    private int userId;
    private String userName;
    private JComboBox<String> eventCombo;

    public UserRegisterEventPage(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Register For Event - " + userName);
        setSize(420, 320);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 255, 250));

        JLabel title = new JLabel("Register for Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(40, 15, 340, 30);
        panel.add(title);

        JLabel sel = new JLabel("Select Event:");
        sel.setBounds(40, 80, 100, 25);
        panel.add(sel);

        eventCombo = new JComboBox<>();
        eventCombo.setBounds(150, 80, 220, 25);
        panel.add(eventCombo);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(100, 160, 100, 36);
        styleButton(registerBtn, new Color(0, 102, 204));
        panel.add(registerBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(230, 160, 100, 36);
        styleButton(backBtn, new Color(178, 34, 34));
        panel.add(backBtn);

        registerBtn.addActionListener(e -> registerForEvent());
        backBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new UserDashboard(userId, userName));
        });

        loadEvents();
        add(panel);
        setVisible(true);
    }

    private void loadEvents() {
        eventCombo.removeAllItems();
        String sql = "SELECT id, name FROM event ORDER BY date";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                eventCombo.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
            if (eventCombo.getItemCount() == 0) eventCombo.addItem("0 - (no events)");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerForEvent() {
        if (eventCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select an event first.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sel = (String) eventCombo.getSelectedItem();
        int eventId = Integer.parseInt(sel.split(" - ")[0]);
        if (eventId == 0) {
            JOptionPane.showMessageDialog(this, "No events available.", "No events", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String capSql = "SELECT e.capacity, COUNT(r.id) AS registered " +
                        "FROM event e LEFT JOIN registrations r ON e.id = r.event_id WHERE e.id = ? GROUP BY e.capacity";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement capStmt = con.prepareStatement(capSql)) {

            capStmt.setInt(1, eventId);
            try (ResultSet rs = capStmt.executeQuery()) {
                if (rs.next()) {
                    int capacity = rs.getInt("capacity");
                    int registered = rs.getInt("registered");
                    if (registered >= capacity) {
                        JOptionPane.showMessageDialog(this, "⚠️ Sorry, this event is full. Please choose another.", "No vacancy", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Update user's registration row to set event_id
            try (PreparedStatement ps = con.prepareStatement("UPDATE registrations SET event_id = ? WHERE id = ?")) {
                ps.setInt(1, eventId);
                ps.setInt(2, userId);
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    // fallback: insert new registration (shouldn't normally happen because user was created)
                    try (PreparedStatement insert = con.prepareStatement(
                            "INSERT INTO registrations (name, email, phone, password, event_id) SELECT name, email, phone, password, ? FROM registrations WHERE id = ?")) {
                        insert.setInt(1, eventId);
                        insert.setInt(2, userId);
                        insert.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "✅ Registered successfully!");
            dispose();
            SwingUtilities.invokeLater(() -> new UserDashboard(userId, userName));

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
