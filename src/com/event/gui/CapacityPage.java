package com.event.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import com.event.util.DBConnection;

public class CapacityPage extends JFrame {

    public CapacityPage() {
        setTitle("Event Capacity Overview");
        setSize(650, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Event ID", "Event Name", "Total Capacity", "Registered Count", "Seats Available"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT e.id, e.name, e.capacity, COUNT(r.id) AS registered, " +
                     "(e.capacity - COUNT(r.id)) AS available " +
                     "FROM event e LEFT JOIN registrations r ON e.id = r.event_id " +
                     "GROUP BY e.id, e.name, e.capacity")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getInt("registered"),
                        rs.getInt("available")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage());
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }
}
