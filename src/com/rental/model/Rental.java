package com.rental.model;

import java.time.LocalDate;

public class Rental {

    private int rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double totalPrice;

    public Rental() {}

    public Rental(Customer customer, Vehicle vehicle,
                  LocalDate rentalDate, LocalDate returnDate,
                  double totalPrice) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
    }

    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}