package main.java.ma.wora.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcPostgresqlConnection {

    public static void main(String[] args) {
        Connection conn = null;
        Statement statement = null;

        try {
            String dbURL3 = "jdbc:postgresql://localhost:5432/ecomove";
            String username = "postgres";
            String password = "asmaa123";

            // Establish connection by get con
            conn = DriverManager.getConnection(dbURL3, username, password);
            statement = conn.createStatement();

            if (conn != null) {
                System.out.println("Connected to database");
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null && !statement.isClosed()) {
                    statement.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
