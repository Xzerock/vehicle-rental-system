package com.rental.model;

public abstract class Vehicle {

    protected int id;
    protected String brand;
    protected String model;
    protected String plateNumber;
    protected double pricePerDay;
    protected boolean available;
    protected String imagePath;

    public Vehicle() {}

    public Vehicle(int id, String brand, String model,
                   String plateNumber, double pricePerDay, boolean available, String imagePath) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.pricePerDay = pricePerDay;
        this.available = available;
        this.imagePath = imagePath;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public abstract String getExtra();

    public abstract String getType();
}
