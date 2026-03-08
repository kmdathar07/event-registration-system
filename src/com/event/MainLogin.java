package com.event.gui;

import javax.swing.*;
import java.awt.*;

public class MainLogin extends JFrame {
    public MainLogin() {
        setTitle("Event Registration - Welcome");
        setSize(380, 230);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(null);
        p.setBackground(new Color(245, 245, 250));

        JLabel title = new JLabel("Event Registration System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(40, 10, 300, 30);
        p.add(title);

        JButton adminBtn = new JButton("Admin Login");
        JButton userBtn = new JButton("User Registration");
        JButton exitBtn = new JButton("Exit");

        adminBtn.setBounds(110, 60, 160, 36);
        userBtn.setBounds(110, 105, 160, 36);
        exitBtn.setBounds(250, 150, 80, 28);

        styleButton(adminBtn, new Color(46,139,87));
        styleButton(userBtn, new Color(0,102,204));
        styleButton(exitBtn, new Color(178,34,34));

        adminBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(AdminLogin::new);
        });

        userBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(UserLogin::new);
        });

        exitBtn.addActionListener(e -> System.exit(0));

        p.add(adminBtn);
        p.add(userBtn);
        p.add(exitBtn);
        add(p);
        setVisible(true);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainLogin::new);
    }
}
