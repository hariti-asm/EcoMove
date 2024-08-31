package main.java.ma.wora.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcPostgresqlConnection {

    private static Connection connection = null;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/ecomove";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "asmaa123";

    // Private constructor to prevent instantiation
    private JdbcPostgresqlConnection() {}

    // Method to establish and return a connection
    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                System.out.println("Connected to database");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }

    // Method to get a Statement instance
    public static Statement getInstance() {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                return conn.createStatement();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
