package com.event.gui;

import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Event Registration Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 248, 255));

        JLabel title = new JLabel("Event Registration Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(50, 30, 400, 30);
        panel.add(title);

        JButton addEventBtn = new JButton("➕  Create Event");
        JButton viewEventBtn = new JButton("📋  View Events");
        JButton registerUserBtn = new JButton("👤  Register User");
        JButton exitBtn = new JButton("🚪  Exit");

        addEventBtn.setBounds(150, 100, 200, 40);
        viewEventBtn.setBounds(150, 160, 200, 40);
        registerUserBtn.setBounds(150, 220, 200, 40);
        exitBtn.setBounds(150, 280, 200, 40);

        styleButton(addEventBtn, new Color(46, 139, 87));
        styleButton(viewEventBtn, new Color(0, 102, 204));
        styleButton(registerUserBtn, new Color(255, 140, 0));
        styleButton(exitBtn, new Color(178, 34, 34));

        panel.add(addEventBtn);
        panel.add(viewEventBtn);
        panel.add(registerUserBtn);
        panel.add(exitBtn);

        addEventBtn.addActionListener(e -> new RegisterEventPage());
        viewEventBtn.addActionListener(e -> new ViewEventsPage());
        registerUserBtn.addActionListener(e -> new RegisterUserPage());
        exitBtn.addActionListener(e -> System.exit(0));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
