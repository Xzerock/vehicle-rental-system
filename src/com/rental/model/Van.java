package com.rental.model;

public class Van extends Vehicle {

    private double cargoCapacity;

    public Van() {}

    public Van(int id, String brand, String model,
               String plateNumber, double pricePerDay,
               boolean available, double cargoCapacity, String imagePath) {
        super(id, brand, model, plateNumber, pricePerDay, available, imagePath);
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
    @Override
    public String getExtra() {
        return cargoCapacity + " kg";
    }
}
