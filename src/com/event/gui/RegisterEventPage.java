package com.event.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.event.util.DBConnection;

public class RegisterEventPage extends JFrame {

    private JTextField nameField, dateField, capacityField;

    public RegisterEventPage() {
        setTitle("Create New Event");
        setSize(420, 330);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 255, 250));

        JLabel title = new JLabel("Create Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(60, 15, 300, 30);
        panel.add(title);

        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setBounds(40, 70, 100, 25);
        panel.add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 70, 220, 25);
        panel.add(nameField);

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setBounds(40, 110, 150, 25);
        panel.add(dateLabel);
        dateField = new JTextField();
        dateField.setBounds(200, 110, 170, 25);
        panel.add(dateField);

        JLabel capLabel = new JLabel("Capacity:");
        capLabel.setBounds(40, 150, 100, 25);
        panel.add(capLabel);
        capacityField = new JTextField();
        capacityField.setBounds(150, 150, 220, 25);
        panel.add(capacityField);

        JButton createBtn = new JButton("Create Event");
        createBtn.setBounds(110, 210, 120, 36);
        styleButton(createBtn, new Color(46, 139, 87));
        panel.add(createBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(250, 210, 120, 36);
        styleButton(cancelBtn, new Color(178, 34, 34));
        panel.add(cancelBtn);

        createBtn.addActionListener(e -> createEvent());
        cancelBtn.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }

    private void createEvent() {
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String capStr = capacityField.getText().trim();

        if (name.isEmpty() || date.isEmpty() || capStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capStr);
            if (capacity < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a positive integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO event (name, date, capacity) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setInt(3, capacity);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Event created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
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
