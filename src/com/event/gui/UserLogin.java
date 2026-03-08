package com.event.gui;

import com.event.util.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class UserLogin extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;

    public UserLogin() {
        setTitle("User Registration");
        setSize(420, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("User Registration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(80, 20, 250, 30);
        panel.add(title);

        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setBounds(60, 80, 100, 25);
        panel.add(nameLbl);
        nameField = new JTextField();
        nameField.setBounds(160, 80, 180, 25);
        panel.add(nameField);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(60, 120, 100, 25);
        panel.add(emailLbl);
        emailField = new JTextField();
        emailField.setBounds(160, 120, 180, 25);
        panel.add(emailField);

        JLabel phoneLbl = new JLabel("Phone:");
        phoneLbl.setBounds(60, 160, 100, 25);
        panel.add(phoneLbl);
        phoneField = new JTextField();
        phoneField.setBounds(160, 160, 180, 25);
        panel.add(phoneField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(60, 200, 100, 25);
        panel.add(passLbl);
        passwordField = new JPasswordField();
        passwordField.setBounds(160, 200, 180, 25);
        panel.add(passwordField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(90, 260, 100, 35);
        styleButton(registerBtn, new Color(46, 139, 87));
        panel.add(registerBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(220, 260, 100, 35);
        styleButton(backBtn, new Color(178, 34, 34));
        panel.add(backBtn);

        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(MainLogin::new);
        });

        add(panel);
        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        // ✅ Validation checks
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        // ✅ Name validation (only alphabets and spaces)
        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid name! Please use only alphabets.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }

        // ✅ Phone number validation (only digits, exactly 10 preferred)
        if (!phone.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid phone number! Only digits are allowed.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return;
        }

        if (phone.length() < 6 || phone.length() > 15) {
            JOptionPane.showMessageDialog(this,
                    "Phone number length must be between 6 and 15 digits.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO registrations (name, email, phone, password) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                JOptionPane.showMessageDialog(this, "✅ Registration successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new UserDashboard(userId, name));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + ex.getMessage());
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
