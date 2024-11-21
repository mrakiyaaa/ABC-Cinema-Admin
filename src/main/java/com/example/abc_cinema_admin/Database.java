package com.example.abc_cinema_admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/ABC_Cinema?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Sudubaba@123";

    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            e.printStackTrace();
        }
        // Return the connection
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
