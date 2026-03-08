package com.event.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.event.util.DBConnection;

public class MyRegistrationPage extends JFrame {

    private int userId;

    public MyRegistrationPage(int userId) {
        this.userId = userId;
        setTitle("My Registration");
        setSize(520, 220);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(null);
        p.setBackground(new Color(250,250,250));

        JLabel title = new JLabel("My Registered Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(20, 10, 480, 30);
        p.add(title);

        JLabel info = new JLabel("", SwingConstants.CENTER);
        info.setBounds(20, 60, 480, 30);
        p.add(info);

        JButton cancelBtn = new JButton("Cancel Registration");
        cancelBtn.setBounds(160, 110, 200, 36);
        styleButton(cancelBtn, new Color(178,34,34));
        p.add(cancelBtn);

        cancelBtn.addActionListener(e -> cancelRegistration(info));

        add(p);
        loadInfo(info);
        setVisible(true);
    }

    private void loadInfo(JLabel info) {
        String sql = "SELECT r.id AS regid, e.name, e.date FROM registrations r LEFT JOIN event e ON r.event_id = e.id WHERE r.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String date = rs.getString("date");
                info.setText((name == null ? "You have not registered for any event." : name + " on " + date));
            } else {
                info.setText("No registration found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelRegistration(JLabel info) {
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel your event registration?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE registrations SET event_id = NULL WHERE id = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Registration cancelled.");
            loadInfo(info);
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
