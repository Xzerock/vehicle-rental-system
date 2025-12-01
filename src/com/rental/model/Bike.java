package com.rental.model;

public class Bike extends Vehicle {

    private int engineCC;

    public Bike() {}

    public Bike(int id, String brand, String model,
                String plateNumber, double pricePerDay,
                boolean available, int engineCC) {
        super(id, brand, model, plateNumber, pricePerDay, available);
        this.engineCC = engineCC;
    }

    public int getEngineCC() { return engineCC; }
    public void setEngineCC(int engineCC) { this.engineCC = engineCC; }

    @Override
    public String getType() {
        return "BIKE";
    }
}
