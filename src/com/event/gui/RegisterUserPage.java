package com.event.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.event.util.DBConnection;

public class RegisterUserPage extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> eventDropdown;

    public RegisterUserPage() {
        setTitle("Register User for Event");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(250, 250, 250));

        JLabel title = new JLabel("User Registration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(60, 20, 280, 40);
        panel.add(title);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 90, 100, 25);
        panel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 90, 180, 25);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 130, 100, 25);
        panel.add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(150, 130, 180, 25);
        panel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 170, 100, 25);
        panel.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(150, 170, 180, 25);
        panel.add(phoneField);

        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setBounds(50, 210, 100, 25);
        panel.add(eventLabel);
        eventDropdown = new JComboBox<>();
        eventDropdown.setBounds(150, 210, 180, 25);
        panel.add(eventDropdown);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");
        registerBtn.setBounds(80, 270, 100, 35);
        backBtn.setBounds(200, 270, 100, 35);
        styleButton(registerBtn, new Color(60, 179, 113));
        styleButton(backBtn, new Color(220, 20, 60));
        panel.add(registerBtn);
        panel.add(backBtn);

        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
        loadEvents();
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void loadEvents() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM event")) {
            while (rs.next()) {
                eventDropdown.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading events: " + e.getMessage());
        }
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String selected = (String) eventDropdown.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || selected == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        int eventId = Integer.parseInt(selected.split(" - ")[0]);

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO registrations (name, email, phone, event_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, eventId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ User registered successfully!");
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage());
        }
    }
}
