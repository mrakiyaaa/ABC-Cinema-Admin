package com.example.abc_cinema_admin;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                System.out.println("Database connection is successful!");
            } else {
                System.out.println("Database connection failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
