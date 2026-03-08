package com.event.gui;

import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard - Event Registration");
        setSize(520, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 248, 255));

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(60, 20, 400, 30);
        panel.add(title);

        JButton createEventBtn = new JButton("➕ Create Event");
        JButton viewEventsBtn = new JButton("📋 Manage Events");
        JButton viewUsersBtn = new JButton("👥 Registered Users");
        JButton logoutBtn = new JButton("🚪 Logout");

        createEventBtn.setBounds(150, 90, 220, 45);
        viewEventsBtn.setBounds(150, 150, 220, 45);
        viewUsersBtn.setBounds(150, 210, 220, 45);
        logoutBtn.setBounds(150, 270, 220, 45);

        styleButton(createEventBtn, new Color(46, 139, 87));
        styleButton(viewEventsBtn, new Color(0, 102, 204));
        styleButton(viewUsersBtn, new Color(255, 140, 0));
        styleButton(logoutBtn, new Color(178, 34, 34));

        panel.add(createEventBtn);
        panel.add(viewEventsBtn);
        panel.add(viewUsersBtn);
        panel.add(logoutBtn);

        createEventBtn.addActionListener(e -> new RegisterEventPage());
        viewEventsBtn.addActionListener(e -> new ViewEventsPage());
        viewUsersBtn.addActionListener(e -> new ViewUsersPage());
        logoutBtn.addActionListener(e -> {
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
}
