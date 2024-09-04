package main.java.ma.wora.impl;

import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.repositories.TicketRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketRepositoryImpl implements TicketRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "tickets";

    public TicketRepositoryImpl() throws SQLException {
    }

    @Override
    public Ticket findById(UUID id) {
        final String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, id);
            final ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                // Extract data from the result set
                UUID ticketId = UUID.fromString(resultSet.getString("id"));
                String transportType = resultSet.getString("transport_type");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double sellingPrice = resultSet.getDouble("selling_price");
                java.sql.Date saleDate = resultSet.getDate("sale_date");
                String status = resultSet.getString("status");

                Ticket ticket = new Ticket();
                ticket.setId(ticketId);
                ticket.setTransportType(TransportType.valueOf(transportType));
                ticket.setPurchasePrice(BigDecimal.valueOf(purchasePrice));
                ticket.setSellingPrice(BigDecimal.valueOf(sellingPrice));

                // Set the sale date as a java.sql.Date
                ticket.setSaleDate(saleDate);

                ticket.setStatus(TicketStatus.valueOf(status));

                displayTicketDetails(ticket);
                return ticket;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Ticket update(Ticket ticket) {
        final String query = "UPDATE " + tableName + " SET transport_type = ?, purchase_price = ?, selling_price = ?, sale_date = ?, status = ? WHERE id = ?";
        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, ticket.getTransportType().name());
            pstmt.setBigDecimal(2, ticket.getPurchasePrice());
            pstmt.setBigDecimal(3, ticket.getSellingPrice());

            // Convert LocalDate to java.sql.Date
            pstmt.setDate(4, new java.sql.Date(ticket.getSaleDate().getTime()));

            pstmt.setString(5, ticket.getStatus().name());
            pstmt.setObject(6, ticket.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return ticket;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean delete(Ticket ticket) {
        final String query = "DELETE FROM " + tableName + " WHERE id = ?";
        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, ticket.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        final String query = "SELECT * FROM " + tableName;
        List<Ticket> tickets = new ArrayList<>();
        try (final Statement stmt = connection.createStatement();
             final ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                // Extract data from the result set
                UUID ticketId = UUID.fromString(resultSet.getString("id"));
                String transportType = resultSet.getString("transport_type");
                double purchasePrice = resultSet.getDouble("purchase_price");
                double sellingPrice = resultSet.getDouble("selling_price");
                java.sql.Date saleDate = resultSet.getDate("sale_date");
                String status = resultSet.getString("status");

                Ticket ticket = new Ticket();
                ticket.setId(ticketId);
                ticket.setTransportType(TransportType.valueOf(transportType));
                ticket.setPurchasePrice(BigDecimal.valueOf(purchasePrice));
                ticket.setSellingPrice(BigDecimal.valueOf(sellingPrice));

                ticket.setSaleDate(saleDate);

                ticket.setStatus(TicketStatus.valueOf(status));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tickets;
    }
//test
    @Override
    public Ticket add(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(UUID.randomUUID());
        }
        final String query = "INSERT INTO " + tableName + " (id, transport_type, purchase_price, selling_price, sale_date, status, contract_id) VALUES (?, ?, ?, ?,?, ?, ?)";
        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, ticket.getId());
            pstmt.setString(2, ticket.getTransportType().name());
            pstmt.setBigDecimal(3, ticket.getPurchasePrice());
            pstmt.setBigDecimal(4, ticket.getSellingPrice());

            pstmt.setDate(5, new java.sql.Date(ticket.getSaleDate().getTime()));

            pstmt.setString(6, ticket.getStatus().name());
            pstmt.setObject(7,ticket.getContract());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return ticket;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void displayTicketDetails(Ticket ticket) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = ticket.getSaleDate().toLocalDate().format(formatter);

        System.out.println("Ticket Details:");
        System.out.println("ID: " + ticket.getId());
        System.out.println("Transport Type: " + ticket.getTransportType());
        System.out.println("Purchase Price: " + ticket.getPurchasePrice());
        System.out.println("Selling Price: " + ticket.getSellingPrice());
        System.out.println("Sale Date: " + formattedDate);
        System.out.println("Status: " + ticket.getStatus());
    }
}
