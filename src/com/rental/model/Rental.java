package com.rental.model;

import java.time.LocalDate;

public class Rental {
    
    private int rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate rentalDate;
    private LocalDate expectedReturnDate; // ✅ NEW: When vehicle should be returned
    private LocalDate returnDate;         // When vehicle was actually returned (NULL if active)
    private double totalPrice;

    // Constructors
    public Rental() {}

    public Rental(Customer customer, Vehicle vehicle, LocalDate rentalDate, 
                  LocalDate expectedReturnDate, double totalPrice) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDate = rentalDate;
        this.expectedReturnDate = expectedReturnDate;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", customer=" + customer.getName() +
                ", vehicle=" + vehicle.getBrand() + " " + vehicle.getModel() +
                ", rentalDate=" + rentalDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", returnDate=" + returnDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}