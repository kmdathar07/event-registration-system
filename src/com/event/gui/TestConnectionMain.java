package com.event.gui;

import com.event.util.DBConnection;
import java.sql.Connection;
import javax.swing.*;

public class TestConnectionMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection c = DBConnection.getConnection()) {
                JOptionPane.showMessageDialog(null, "✅ DB Connection successful!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "❌ DB Connection failed:\n" + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
