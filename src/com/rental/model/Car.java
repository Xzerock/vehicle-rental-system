package com.rental.model;

public class Car extends Vehicle {

    private int seats;

    public Car() {}

    public Car(int id, String brand, String model, String plateNumber,
               double pricePerDay, boolean available, int seats, String imagePath) {
        super(id, brand, model, plateNumber, pricePerDay, available, imagePath);
        this.seats = seats;
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    @Override
    public String getType() {
        return "CAR";
    }
    
    @Override
    public String getExtra() {
        return seats + " seats";
    }
}
