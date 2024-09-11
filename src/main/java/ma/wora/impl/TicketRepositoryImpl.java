package main.java.ma.wora.impl;

import main.java.ma.wora.models.entities.Journey;
import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.repositories.TicketRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            pstmt.setObject(7, ticket.getContract());

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

    public List<Ticket> searchTicketByDestination(String departurePoint, String arrivalPoint, LocalDate departureTime) {
        Map<UUID, Journey> journeyMap = new HashMap<>();
        List<Ticket> allTickets = new ArrayList<>();

        // Query Direct Journeys Using View
        String directQuery = "SELECT t.id, t.transport_type, t.purchase_price, t.selling_price, t.sale_date, t.status, t.discount, t.contract_id, "
                + "d.journey_id, d.departure_time, d.arrival_time, d.departure_station, d.arrival_station "
                + "FROM Tickets t "
                + "JOIN direct_journeys d ON t.journey_id = d.journey_id "
                + "WHERE d.departure_station = ? "
                + "AND d.arrival_station = ? "
                + "AND d.departure_time >= ?";

        try (PreparedStatement pst = connection.prepareStatement(directQuery)) {
            pst.setString(1, departurePoint);
            pst.setString(2, arrivalPoint);
            pst.setTimestamp(3, Timestamp.valueOf(departureTime.atStartOfDay()));

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                UUID journeyId = UUID.fromString(resultSet.getString("journey_id"));

                Journey journey = journeyMap.get(journeyId);
                if (journey == null) {
                    journey = new Journey(
                            journeyId,
                            resultSet.getTimestamp("departure_time"),
                            resultSet.getTimestamp("arrival_time"),
                            resultSet.getString("departure_station"),
                            resultSet.getString("arrival_station"),
                            new ArrayList<>()
                    );
                    journeyMap.put(journeyId, journey);
                }

                Ticket ticket = new Ticket(
                        UUID.fromString(resultSet.getString("id")),
                        TransportType.valueOf(resultSet.getString("transport_type")),
                        resultSet.getBigDecimal("purchase_price"),
                        resultSet.getBigDecimal("selling_price"),
                        resultSet.getDate("sale_date"),
                        TicketStatus.valueOf(resultSet.getString("status")),
                        resultSet.getInt("discount"),
                        UUID.fromString(resultSet.getString("contract_id")),
                        journey
                );

                journey.getTickets().add(ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving direct tickets by destination: " + e.getMessage(), e);
        }

        // Query Indirect Journeys Using View
        String indirectQuery = "SELECT t1.id AS first_ticket_id, t1.transport_type AS first_transport_type, t1.purchase_price AS first_purchase_price, "
                + "       t1.selling_price AS first_selling_price, t1.sale_date AS first_sale_date, t1.status AS first_status, "
                + "       t1.discount AS first_discount, t1.contract_id AS first_contract_id, "
                + "       t2.id AS second_ticket_id, t2.transport_type AS second_transport_type, t2.purchase_price AS second_purchase_price, "
                + "       t2.selling_price AS second_selling_price, t2.sale_date AS second_sale_date, t2.status AS second_status, "
                + "       t2.discount AS second_discount, t2.contract_id AS second_contract_id, "
                + "       i.first_journey_id, i.second_journey_id, i.first_departure_time, i.first_arrival_time, i.first_departure_station, "
                + "       i.first_arrival_station, i.second_departure_time, i.second_arrival_time, i.second_departure_station, "
                + "       i.second_arrival_station "
                + "FROM indirect_journeys i "
                + "LEFT JOIN Tickets t1 ON i.first_journey_id = t1.journey_id "
                + "LEFT JOIN Tickets t2 ON i.second_journey_id = t2.journey_id "
                + "WHERE i.first_departure_station = ? "
                + "AND i.second_arrival_station = ?";

        try (PreparedStatement pst = connection.prepareStatement(indirectQuery)) {
            pst.setString(1, departurePoint);
            pst.setString(2, arrivalPoint);

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                UUID firstJourneyId = UUID.fromString(resultSet.getString("first_journey_id"));
                UUID secondJourneyId = UUID.fromString(resultSet.getString("second_journey_id"));

                Journey firstJourney = journeyMap.computeIfAbsent(firstJourneyId, id -> {
                    try {
                        return new Journey(
                                id,
                                resultSet.getTimestamp("first_departure_time"),
                                resultSet.getTimestamp("first_arrival_time"),
                                resultSet.getString("first_departure_station"),
                                resultSet.getString("first_arrival_station"),
                                new ArrayList<>()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Journey secondJourney = journeyMap.computeIfAbsent(secondJourneyId, id -> {
                    try {
                        return new Journey(
                                id,
                                resultSet.getTimestamp("second_departure_time"),
                                resultSet.getTimestamp("second_arrival_time"),
                                resultSet.getString("second_departure_station"),
                                resultSet.getString("second_arrival_station"),
                                new ArrayList<>()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (resultSet.getString("first_ticket_id") != null) {
                    Ticket firstTicket = new Ticket(
                            UUID.fromString(resultSet.getString("first_ticket_id")),
                            TransportType.valueOf(resultSet.getString("first_transport_type")),
                            resultSet.getBigDecimal("first_purchase_price"),
                            resultSet.getBigDecimal("first_selling_price"),
                            resultSet.getDate("first_sale_date"),
                            TicketStatus.valueOf(resultSet.getString("first_status")),
                            resultSet.getInt("first_discount"),
                            UUID.fromString(resultSet.getString("first_contract_id")),
                            firstJourney
                    );
                    firstJourney.getTickets().add(firstTicket);
                }

                if (resultSet.getString("second_ticket_id") != null) {
                    Ticket secondTicket = new Ticket(
                            UUID.fromString(resultSet.getString("second_ticket_id")),
                            TransportType.valueOf(resultSet.getString("second_transport_type")),
                            resultSet.getBigDecimal("second_purchase_price"),
                            resultSet.getBigDecimal("second_selling_price"),
                            resultSet.getDate("second_sale_date"),
                            TicketStatus.valueOf(resultSet.getString("second_status")),
                            resultSet.getInt("second_discount"),
                            UUID.fromString(resultSet.getString("second_contract_id")),
                            secondJourney
                    );
                    secondJourney.getTickets().add(secondTicket);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving indirect tickets by destination: " + e.getMessage(), e);
        }

        // Collect all tickets from journeys
        for (Journey journey : journeyMap.values()) {
            allTickets.addAll(journey.getTickets());
        }

        return allTickets;
    }
}