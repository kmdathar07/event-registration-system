package com.event.gui;

import java.awt.*;
import javax.swing.*;

public class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login - Event Registration");
        setSize(380, 230);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245,245,245));

        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(80, 10, 200, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 60, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 60, 180, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 100, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 100, 180, 25);
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(140, 140, 180, 32);
        styleButton(loginBtn, new Color(60,179,113));
        panel.add(loginBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(40, 140, 80, 32);
        styleButton(backBtn, new Color(120,120,120));
        panel.add(backBtn);

        loginBtn.addActionListener(e -> checkLogin());
        backBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(MainLogin::new);
        });

        add(panel);
        setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void checkLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();

        if ("admin".equals(user) && "admin1234".equals(pass)) {
            JOptionPane.showMessageDialog(this, "✅ Admin login successful");
            dispose();
            SwingUtilities.invokeLater(AdminDashboard::new);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Invalid admin credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
