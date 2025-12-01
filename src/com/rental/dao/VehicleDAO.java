package com.rental.dao;

import com.rental.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // INSERT
    public void addVehicle(Vehicle vehicle) throws SQLException {
        String sql = """
            INSERT INTO vehicle(type, brand, model, plate_number, price_per_day, available)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicle.getType());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getModel());
            ps.setString(4, vehicle.getPlateNumber());
            ps.setDouble(5, vehicle.getPricePerDay());
            ps.setBoolean(6, vehicle.isAvailable());

            ps.executeUpdate();
        }
    }

    // SELECT ALL
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
        }
        return list;
    }

    // SELECT AVAILABLE
    public List<Vehicle> getAvailableVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE available = true";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToVehicle(rs));
            }
        }
        return list;
    }

    // UPDATE availability
    public void updateAvailability(int vehicleId, boolean available) throws SQLException {
        String sql = "UPDATE vehicle SET available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, vehicleId);
            ps.executeUpdate();
        }
    }

    // ✅ POLYMORPHISM HANDLER
    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        String type = rs.getString("type");

        Vehicle v = switch (type) {
            case "CAR" -> new Car();
            case "BIKE" -> new Bike();
            case "VAN" -> new Van();
            default -> throw new IllegalArgumentException("Unknown vehicle type");
        };

        v.setId(rs.getInt("id"));
        v.setBrand(rs.getString("brand"));
        v.setModel(rs.getString("model"));
        v.setPlateNumber(rs.getString("plate_number"));
        v.setPricePerDay(rs.getDouble("price_per_day"));
        v.setAvailable(rs.getBoolean("available"));

        return v;
    }
}
