package com.rental.dao;

import com.rental.model.Customer;

public class TestCustomerDAO {
    public static void main(String[] args) {
        try {
            CustomerDAO dao = new CustomerDAO();

            Customer c = new Customer(0, "REGULAR",
                    "John Tan", "0123456789", "john@mail.com");

            dao.addCustomer(c);
            System.out.println("✅ Customer inserted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}