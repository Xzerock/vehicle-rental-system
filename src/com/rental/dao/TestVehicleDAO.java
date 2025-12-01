package com.rental.dao;

import com.rental.model.Car;

public class TestVehicleDAO {
    public static void main(String[] args) {
        try {
            VehicleDAO dao = new VehicleDAO();

            Car car = new Car(0, "Toyota", "Vios",
                    "ABC123", 150.0, true, 5);

            dao.addVehicle(car);
            System.out.println("✅ Vehicle inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
