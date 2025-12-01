package com.rental.model;

public class Van extends Vehicle {

    private double cargoCapacity;

    public Van() {}

    public Van(int id, String brand, String model,
               String plateNumber, double pricePerDay,
               boolean available, double cargoCapacity) {
        super(id, brand, model, plateNumber, pricePerDay, available);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public String getType() {
        return "VAN";
    }
}
