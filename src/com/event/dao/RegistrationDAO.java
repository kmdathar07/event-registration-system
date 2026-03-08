package com.event.dao;

import java.sql.*;
import java.util.*;
import com.event.model.Registration;
import com.event.util.DBConnection;

public class RegistrationDAO {

    // 🔹 CREATE — Add new registration
    public boolean addRegistration(Registration reg) {
        String sql = "INSERT INTO registrations (name, email, event_name, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reg.getName());
            stmt.setString(2, reg.getEmail());
            stmt.setString(3, reg.getEventName());
            stmt.setString(4, reg.getPhone());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 READ — Get all registrations
    public List<Registration> getAllRegistrations() {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT * FROM registrations";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Registration reg = new Registration(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("event_name"),
                        rs.getString("phone")
                );
                list.add(reg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 UPDATE — Modify existing registration
    public boolean updateRegistration(Registration reg) {
        String sql = "UPDATE registrations SET name=?, email=?, event_name=?, phone=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reg.getName());
            stmt.setString(2, reg.getEmail());
            stmt.setString(3, reg.getEventName());
            stmt.setString(4, reg.getPhone());
            stmt.setInt(5, reg.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 DELETE — Remove registration by ID
    public boolean deleteRegistration(int id) {
        String sql = "DELETE FROM registrations WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔍 SEARCH — Find registrations by name
    public List<Registration> searchByName(String name) {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT * FROM registrations WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Registration reg = new Registration(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("event_name"),
                        rs.getString("phone")
                );
                list.add(reg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 📊 COUNT — Total registrations for each event
    public void countByEvent() {
        String sql = "SELECT event_name, COUNT(*) AS total FROM registrations GROUP BY event_name";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- 📊 Event Registration Count ---");
            while (rs.next()) {
                System.out.println(rs.getString("event_name") + " → " + rs.getInt("total") + " registrations");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
