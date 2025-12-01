package com.rental.dao;

import com.rental.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // INSERT
    public void addCustomer(Customer customer) throws SQLException {
        String sql = """
            INSERT INTO customer(type, name, phone, email)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getType());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());

            ps.executeUpdate();
        }
    }

    // SELECT ALL
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setType(rs.getString("type"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                list.add(c);
            }
        }
        return list;
    }

    // SELECT BY ID
    public Customer getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setType(rs.getString("type"));
                    c.setName(rs.getString("name"));
                    c.setPhone(rs.getString("phone"));
                    c.setEmail(rs.getString("email"));
                    return c;
                }
            }
        }
        return null;
    }
}
