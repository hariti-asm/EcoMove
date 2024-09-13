package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.models.entities.Reservation;
import main.java.ma.wora.repositories.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "clients";

    public ClientRepositoryImpl() throws SQLException {
    }

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
    public Optional<Client> authenticate(String lastName, String email) {
        String query = "SELECT * FROM " + tableName + " WHERE last_name = ? AND email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, lastName);
            pst.setString(2, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    UUID id = rs.getObject("id", UUID.class);
                    String firstName = rs.getString("first_name");
                    String phone = rs.getString("phone");

                    List<Reservation> reservations = new ArrayList<>();

                    return Optional.of(new Client(
                            id,
                            firstName,
                            lastName,
                            email,
                            phone,
                            reservations
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating client", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        String query = "SELECT * FROM " + tableName + " WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    UUID id = rs.getObject("id", UUID.class);
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phone = rs.getString("phone");

                    List<Reservation> reservations = new ArrayList<>();
                    return Optional.of(new Client(
                            id,
                            firstName,
                            lastName,
                            email,
                            phone,
                            reservations
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying client by email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> getClientByFirstName(String firstName) {
        String query = "SELECT * FROM " + tableName + " WHERE first_name = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, firstName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    UUID clientId = rs.getObject("id", UUID.class);
                    String firstNameDb = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");

                    List<Reservation> reservations = new ArrayList<>();

                    Client client = new Client(clientId, firstNameDb, lastName, email, phone, reservations);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving client by first name", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> getClientById(UUID id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID clientId = resultSet.getObject("id", UUID.class);
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");

                    List<Reservation> reservations = new ArrayList<>();
                    return Optional.of(new Client(clientId, firstName, lastName, email, phone, reservations));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving client by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> updateClient(Client client) {
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
                return Optional.empty();
            }
            System.out.println("Client updated successfully");
            return Optional.of(client);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating client", e);
        }
    }
}
