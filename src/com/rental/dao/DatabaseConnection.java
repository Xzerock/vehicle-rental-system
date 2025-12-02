package com.rental.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/rentaldb"
      + "?useSSL=false"
      + "&serverTimezone=UTC"
      + "&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "11701170";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}