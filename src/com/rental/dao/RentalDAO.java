package com.rental.dao;

import com.rental.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    // CREATE RENTAL
    public void createRental(Rental rental) throws SQLException {

        String sql = """
            INSERT INTO rental(customer_id, vehicle_id, rental_date, return_date, total_price)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rental.getCustomer().getId());
            ps.setInt(2, rental.getVehicle().getId());
            ps.setDate(3, Date.valueOf(rental.getRentalDate()));
            ps.setDate(4, rental.getReturnDate() == null
                    ? null
                    : Date.valueOf(rental.getReturnDate()));
            ps.setDouble(5, rental.getTotalPrice());

            ps.executeUpdate();
        }

        // mark vehicle unavailable
        new VehicleDAO().updateAvailability(
                rental.getVehicle().getId(), false);
    }

    // GET ACTIVE RENTALS (not returned)
    public List<Rental> getActiveRentals() throws SQLException {

        List<Rental> list = new ArrayList<>();

        String sql = """
            SELECT r.*, c.name, v.type, v.brand, v.model
            FROM rental r
            JOIN customer c ON r.customer_id = c.id
            JOIN vehicle v ON r.vehicle_id = v.id
            WHERE r.return_date IS NULL
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rental rental = new Rental();

                rental.setRentalId(rs.getInt("rental_id"));
                rental.setRentalDate(rs.getDate("rental_date").toLocalDate());

                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) {
                    rental.setReturnDate(returnDate.toLocalDate());
                }

                rental.setTotalPrice(rs.getDouble("total_price"));

                // customer
                Customer customer = new Customer();
                customer.setId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));

                // vehicle (simplified)
                Vehicle vehicle = new Car();
                vehicle.setId(rs.getInt("vehicle_id"));
                vehicle.setBrand(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));

                rental.setCustomer(customer);
                rental.setVehicle(vehicle);

                list.add(rental);
            }
        }
        return list;
    }
    
    public List<Rental> getActiveRentalsByCustomer(int customerId) throws SQLException {

        List<Rental> list = new ArrayList<>();

        String sql = """
            SELECT 
                r.*, 
                c.name AS customer_name,
                v.id AS v_id,
                v.type,
                v.brand,
                v.model,
                v.plate_number,
                v.price_per_day,
                v.available,
                v.image_path
            FROM rental r
            JOIN customer c ON r.customer_id = c.id
            JOIN vehicle v ON r.vehicle_id = v.id
            WHERE r.return_date IS NULL
              AND r.customer_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Rental rental = new Rental();
                    
                    rental.setRentalId(rs.getInt("rental_id"));
                    rental.setRentalDate(rs.getDate("rental_date").toLocalDate());

                    // 🔥 THIS WAS MISSING — add this
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) {
                        rental.setReturnDate(returnDate.toLocalDate());
                    }

                    rental.setTotalPrice(rs.getDouble("total_price"));
                    // Customer
                    Customer customer = new Customer();
                    customer.setId(customerId);
                    customer.setName(rs.getString("customer_name"));
                    rental.setCustomer(customer);

                    // Vehicle (POLYMORPHIC)
                    String type = rs.getString("type");
                    Vehicle v = switch (type) {
                        case "CAR" -> new Car();
                        case "BIKE" -> new Bike();
                        case "VAN" -> new Van();
                        default -> throw new IllegalArgumentException("Unknown type: " + type);
                    };

                    v.setId(rs.getInt("v_id"));
                    v.setBrand(rs.getString("brand"));
                    v.setModel(rs.getString("model"));
                    v.setPlateNumber(rs.getString("plate_number"));
                    v.setPricePerDay(rs.getDouble("price_per_day"));
                    v.setAvailable(rs.getBoolean("available"));
                    v.setImagePath(rs.getString("image_path")); // ✅ FIXED — now image loads

                    rental.setVehicle(v);

                    list.add(rental);
                }
            }
        }
        return list;
    }

    // RETURN VEHICLE
    public void returnVehicle(int rentalId, LocalDate returnDate)
            throws SQLException {

        String getVehicleSql =
                "SELECT vehicle_id FROM rental WHERE rental_id = ?";

        int vehicleId;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getVehicleSql)) {

            ps.setInt(1, rentalId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return;
            vehicleId = rs.getInt("vehicle_id");
        }

        String updateRentalSql =
                "UPDATE rental SET return_date = ? WHERE rental_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateRentalSql)) {

            ps.setDate(1, Date.valueOf(returnDate));
            ps.setInt(2, rentalId);
            ps.executeUpdate();
        }

        // mark vehicle available
        new VehicleDAO().updateAvailability(vehicleId, true);
    }
}
