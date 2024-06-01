package ceu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/FDS";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // Method to get a connection to the database
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection established");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to establish connection");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to close connection");
            }
        }
    }
}