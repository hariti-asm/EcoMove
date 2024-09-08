package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();

    public ClientRepositoryImpl() throws SQLException {
    }

    private final String tableName = "clients";

    @Override
    public boolean createClient(Client client) {
        String query = "INSERT INTO " + tableName + " (id, first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setObject(1, client.getId());
            pst.setString(2, client.getFirstName());
            pst.setString(3, client.getLastName());
            pst.setString(4, client.getEmail());
            pst.setString(5, client.getPhone());  // Set phone as a String

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Client isn't created");
                return false;
            }

            System.out.println("Client created successfully");
            return true;

        } catch (SQLException e) {
            System.err.println("Error creating client: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client authenticate(String last_name, String email) {
        String query = "SELECT * FROM " + tableName + " WHERE last_name = ? AND email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, last_name);
            pst.setString(2, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Client is authenticated");
                    // Create and return the Client object
                    return new Client(
                            rs.getObject("id", UUID.class),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        String query = "SELECT * FROM " + tableName + " WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Client is found");
                    UUID id = UUID.fromString(rs.getString("id"));
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phone = rs.getString("phone_number");
                    return Optional.of(new Client(
                            id,
                            firstName,
                            lastName,
                            email,
                            phone
                    ));
                } else {
                    System.out.println("Client not found");
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional <Client> getClientByFirstName(String firstName) {
        String query = "SELECT * FROM " + tableName + " WHERE first_name = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, firstName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Client is found");
                    UUID id = UUID.fromString(rs.getString("id"));
                    String email = rs.getString("email");
                    String lastName = rs.getString("last_name");
                    String phone = rs.getString("phone_number");
                    return Optional.of( new Client(
                            id,
                            firstName,
                            lastName,
                            email,
                            phone
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Client updateClient(Client client) {
        String query = "UPDATE " + tableName + " SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, client.getFirstName());
            pst.setString(2, client.getLastName());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getPhone());
            pst.setObject(5, client.getId());
            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Client isn't updated");
                return null;
            }
            System.out.println("Client updated successfully");
            return client;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
