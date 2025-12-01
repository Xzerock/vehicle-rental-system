package com.rental.model;

public class Car extends Vehicle {

    private int seats;

    public Car() {}

    public Car(int id, String brand, String model,
               String plateNumber, double pricePerDay,
               boolean available, int seats) {
        super(id, brand, model, plateNumber, pricePerDay, available);
        this.seats = seats;
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    @Override
    public String getType() {
        return "CAR";
    }
}
