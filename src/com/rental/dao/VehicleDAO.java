package com.rental.dao;

import com.rental.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // INSERT
    public void addVehicle(Vehicle vehicle) throws SQLException {
        String sql = """
            INSERT INTO vehicle(type, brand, model, plate_number, price_per_day, available, image_path, seats, cc, capacity)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehicle.getType());
            ps.setString(2, vehicle.getBrand());
            ps.setString(3, vehicle.getModel());
            ps.setString(4, vehicle.getPlateNumber());
            ps.setDouble(5, vehicle.getPricePerDay());
            ps.setBoolean(6, vehicle.isAvailable());
            ps.setString(7, vehicle.getImagePath());

            // extra fields based on type
            if (vehicle instanceof Car c) {
                ps.setInt(8, c.getSeats());
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.DOUBLE);
            } 
            else if (vehicle instanceof Bike b) {
                ps.setNull(8, Types.INTEGER);
                ps.setInt(9, b.getEngineCC());
                ps.setNull(10, Types.DOUBLE);
            } 
            else if (vehicle instanceof Van v) {
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.INTEGER);
                ps.setDouble(10, v.getCargoCapacity());
            }

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
        String sql = "SELECT * FROM vehicle WHERE available = TRUE";

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

private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
    String type = rs.getString("type");
    Vehicle v;

    switch (type) {
        case "CAR" -> {
            int seats = rs.getInt("seats");
            v = new Car(
                rs.getInt("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("plate_number"),
                rs.getDouble("price_per_day"),
                rs.getBoolean("available"),
                seats,
                rs.getString("image_path")
            );
        }
        case "BIKE" -> {
            int cc = rs.getInt("cc");
            v = new Bike(
                rs.getInt("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("plate_number"),
                rs.getDouble("price_per_day"),
                rs.getBoolean("available"),
                cc,
                rs.getString("image_path")
            );
        }
        case "VAN" -> {
            double capacity = rs.getDouble("capacity");
            v = new Van(
                rs.getInt("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("plate_number"),
                rs.getDouble("price_per_day"),
                rs.getBoolean("available"),
                capacity,
                rs.getString("image_path")
            );
        }
        default -> throw new IllegalArgumentException("Unknown vehicle type");
    }

    return v;
}
}
