package main.java.ma.wora.impl;

import com.sun.source.tree.TryTree;
import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Journey;
import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.repositories.JourneyRepository;
import main.java.ma.wora.repositories.TicketRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JourneyRepositoryImpl implements JourneyRepository {
    final private Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
final String tableName = "journey";
  final private TicketRepository ticketRepository ;
    public JourneyRepositoryImpl(TicketRepository ticketRepository) throws SQLException {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void createJourney(Journey journey) {
        String query = "INSERT INTO " + tableName + " VALUES (?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement pst = connection.prepareStatement(query)){
            pst.setObject(1, journey.getId());
            pst.setString( 2, journey.getDepartureStation() );
            pst.setString( 3, journey.getArrivalStation() );
            pst.setTimestamp(4, journey.getDepartureTime());
            pst.setTimestamp(5, journey.getArrivalTime());
            pst.setString(4, journey.getDepartureStation());
            pst.setString(5, journey.getArrivalStation());
           int affectedRows= pst.executeUpdate();
            if( affectedRows > 0){
                System.out.println("Journey created successfully");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            }

    @Override
    public List<Journey> displayAllJourneys() {
        List<Journey> journeys = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet resultSet = pst.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String departureStation = resultSet.getString("departure_station");
                String arrivalStation = resultSet.getString("arrival_station");
                Timestamp departureTime = resultSet.getTimestamp("departure_time");
                Timestamp arrivalTime = resultSet.getTimestamp("arrival_time");
                List<Ticket> tickets = new ArrayList<>();

                Journey journey = new Journey(id, departureTime, arrivalTime, departureStation, arrivalStation, tickets );
                tickets = journey.getTickets();
                journey.setTickets(tickets);
                journeys.add(journey);
            }
            return journeys;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving journeys", e);
        }

        return journeys;
    }
    @Override
    public List<Journey> searchJourneys(String startLocation, String endLocation, LocalDate departureDate) {
        return List.of();
    }
}
