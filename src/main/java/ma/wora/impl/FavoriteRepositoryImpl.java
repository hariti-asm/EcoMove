package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.*;
import main.java.ma.wora.models.enums.*;
import main.java.ma.wora.repositories.ClientRepository;
import main.java.ma.wora.repositories.FavoriteRepository;
import main.java.ma.wora.repositories.ReservationRepository;
import main.java.ma.wora.services.ClientService;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "favorites";
    private final ClientService clientService;

    public FavoriteRepositoryImpl(ClientService clientService) throws SQLException {
        this.clientService = clientService;
    }

    @Override
    public Void mentionAsFavorite(Favorite favorite) {
        String query = "INSERT INTO " + tableName + " (id, client_id, journey_id) VALUES (?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            for (Journey journey : favorite.getJourneys()) {
                pst.setObject(1, favorite.getId());
                pst.setObject(2, favorite.getClient().get().getId());
                pst.setObject(3, journey.getId());

                int affectedRows = pst.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Journey " + journey.getId() + " is marked as favorite successfully.");
                } else {
                    System.out.println("Failed to mark journey " + journey.getId() + " as favorite.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to mark journeys as favorite.");
        }
        return null;
    }

    @Override
    public Optional<Favorite> findById(Favorite favorite) {
        String sqlFavorite = "SELECT * FROM favorites WHERE id = ?";
        String sqlJourneys = "SELECT * FROM journeys WHERE favorite_id = ?";
        String sqlTickets = "SELECT * FROM tickets WHERE journey_id = ?";
        String sqlContracts = "SELECT * FROM contracts WHERE id = ?";
        String sqlPartners = "SELECT * FROM partners WHERE id = ?"; // Query to fetch partners

        try (
                PreparedStatement statementFavorite = connection.prepareStatement(sqlFavorite);
                PreparedStatement statementJourneys = connection.prepareStatement(sqlJourneys);
                PreparedStatement statementTickets = connection.prepareStatement(sqlTickets);
                PreparedStatement statementContracts = connection.prepareStatement(sqlContracts);
                PreparedStatement statementPartners = connection.prepareStatement(sqlPartners)
        ) {
            statementFavorite.setObject(1, favorite.getId());
            ResultSet resultSetFavorite = statementFavorite.executeQuery();

            if (resultSetFavorite.next()) {
                UUID favoriteId = UUID.fromString(resultSetFavorite.getString("id"));
                UUID clientId = UUID.fromString(resultSetFavorite.getString("client_id"));

                Optional<Client> client = clientService.getClientById(clientId);

                statementJourneys.setObject(1, favoriteId);
                ResultSet resultSetJourneys = statementJourneys.executeQuery();
                List<Journey> journeys = new ArrayList<>();

                while (resultSetJourneys.next()) {
                    UUID journeyId = UUID.fromString(resultSetJourneys.getString("id"));
                    Timestamp departureTime = resultSetJourneys.getTimestamp("departure_time");
                    Timestamp arrivalTime = resultSetJourneys.getTimestamp("arrival_time");
                    String departureStation = resultSetJourneys.getString("departure_station");
                    String arrivalStation = resultSetJourneys.getString("arrival_station");

                    statementTickets.setObject(1, journeyId);
                    ResultSet resultSetTickets = statementTickets.executeQuery();
                    List<Ticket> tickets = new ArrayList<>();

                    while (resultSetTickets.next()) {
                        UUID ticketId = UUID.fromString(resultSetTickets.getString("id"));
                        TransportType transportType = TransportType.valueOf(resultSetTickets.getString("transport_type"));
                        BigDecimal purchasePrice = resultSetTickets.getBigDecimal("purchase_price");
                        BigDecimal sellingPrice = resultSetTickets.getBigDecimal("selling_price");
                        Date saleDate = resultSetTickets.getDate("sale_date");
                        TicketStatus status = TicketStatus.valueOf(resultSetTickets.getString("status"));
                        BigDecimal discount = resultSetTickets.getBigDecimal("discount");
                        UUID contractId = UUID.fromString(resultSetTickets.getString("contract_id"));

                        statementContracts.setObject(1, contractId);
                        ResultSet resultSetContract = statementContracts.executeQuery();
                        Contract contract = null;
                        if (resultSetContract.next()) {
                            UUID contractIdDb = UUID.fromString(resultSetContract.getString("id"));
                            Date startDate = resultSetContract.getDate("start_date");
                            Date endDate = resultSetContract.getDate("end_date");
                            BigDecimal specialRate = resultSetContract.getBigDecimal("special_rate");
                            String agreementConditions = resultSetContract.getString("agreement_conditions");
                            boolean renewable = resultSetContract.getBoolean("renewable");
                            ContractStatus statusDb = ContractStatus.valueOf(resultSetContract.getString("status"));
                            UUID partnerId = UUID.fromString(resultSetContract.getString("partner_id"));
                            DiscountType discountType = DiscountType.valueOf(resultSetContract.getString("discount_type"));
                            BigDecimal value = resultSetContract.getBigDecimal("value");

                            statementPartners.setObject(1, partnerId);
                            ResultSet resultSetPartner = statementPartners.executeQuery();
                            Partner partner = null;
                            if (resultSetPartner.next()) {
                                UUID partnerIdDb = UUID.fromString(resultSetPartner.getString("id"));
                                String companyName = resultSetPartner.getString("company_name");
                                String transporType = resultSetPartner.getString("transport_type");
                                String geographicalZone = resultSetPartner.getString("geographical_zone");
                                String specialConditions = resultSetPartner.getString("special_conditions");
                                String statusP = resultSetPartner.getString("status");
                                java.sql.Date creationDate = resultSetPartner.getDate("creation_date");

                                partner = new Partner(partnerIdDb, companyName, transporType, geographicalZone, specialConditions, statusP, creationDate);
                            }

                            contract = new Contract(contractIdDb, startDate, endDate, specialRate, agreementConditions, renewable, statusDb, partner, discountType, value);
                        }

                        Ticket ticket = new Ticket(ticketId, transportType, purchasePrice, sellingPrice, saleDate, status, discount, contract, null);
                        tickets.add(ticket);
                    }

                    Journey journey = new Journey(journeyId, departureTime, arrivalTime, departureStation, arrivalStation, tickets);
                    journeys.add(journey);
                }

                Favorite retrievedFavorite = new Favorite(favoriteId, client, journeys);
                return Optional.of(retrievedFavorite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
