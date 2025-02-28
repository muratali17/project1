package com.onlineshop.project1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/JavaFXProject";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "123456789";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(
                        DB_URL,
                        DB_USER,
                        DB_PASS);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to create the database connection.", e);
            }
        }
        return connection;
    }


    // simdilik gerek yok ama kalsin.
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
