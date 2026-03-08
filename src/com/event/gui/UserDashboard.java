package com.event.gui;

import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private int userId;
    private String userName;

    public UserDashboard(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("User Dashboard - " + userName);
        setSize(480, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 255, 250));

        JLabel title = new JLabel("Welcome, " + userName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(50, 20, 380, 30);
        panel.add(title);

        JButton registerBtn = new JButton("📝 Register for Event");
        JButton viewEventsBtn = new JButton("📅 View Events");
        JButton myEventBtn = new JButton("🔎 My Registration");
        JButton logoutBtn = new JButton("🚪 Logout");

        registerBtn.setBounds(140, 80, 200, 40);
        viewEventsBtn.setBounds(140, 140, 200, 40);
        myEventBtn.setBounds(140, 200, 200, 40);
        logoutBtn.setBounds(140, 260, 200, 40);

        styleButton(registerBtn, new Color(0, 102, 204));
        styleButton(viewEventsBtn, new Color(46, 139, 87));
        styleButton(myEventBtn, new Color(255, 140, 0));
        styleButton(logoutBtn, new Color(178, 34, 34));

        registerBtn.addActionListener(e -> new UserRegisterEventPage(userId, userName));
        viewEventsBtn.addActionListener(e -> new UserViewEventsPage());
        myEventBtn.addActionListener(e -> new MyRegistrationPage(userId));
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(MainLogin::new);
        });

        panel.add(registerBtn);
        panel.add(viewEventsBtn);
        panel.add(myEventBtn);
        panel.add(logoutBtn);

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
