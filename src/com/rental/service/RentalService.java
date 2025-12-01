package com.rental.service;

import com.rental.dao.*;
import com.rental.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.SQLException;

public class RentalService {

    private final RentalDAO rentalDAO = new RentalDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    // RENT VEHICLE (business logic)
    public void rentVehicle(Customer customer,
                            Vehicle vehicle,
                            LocalDate startDate,
                            LocalDate endDate)
            throws SQLException {

        if (!vehicle.isAvailable()) {
            throw new IllegalStateException("Vehicle is not available");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            throw new IllegalArgumentException("Invalid rental period");
        }

        double totalPrice = days * vehicle.getPricePerDay();

        // discount for premium customer
        if ("PREMIUM".equalsIgnoreCase(customer.getType())) {
            totalPrice *= 0.9; // 10% discount
        }

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setVehicle(vehicle);
        rental.setRentalDate(startDate);
        rental.setReturnDate(null);
        rental.setTotalPrice(totalPrice);

        rentalDAO.createRental(rental);
    }

    // RETURN VEHICLE
    public void returnVehicle(int rentalId) throws SQLException {
        rentalDAO.returnVehicle(rentalId, LocalDate.now());
    }
}
