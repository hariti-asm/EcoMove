package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.models.entities.Reservation;
import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.ReservationStatus;
import main.java.ma.wora.repositories.ReservationRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationRepositoryImpl  implements ReservationRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "reservations";

    public ReservationRepositoryImpl() throws SQLException {
    }


    @Override
    public Void ConfirmReservation(Reservation reservation) {
        String query = "INSERT INTO " + tableName + " (id, client_id, ticket_id, reservation_status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            reservation.setStatus(ReservationStatus.CONFIRMED);

            pst.setObject(1, reservation.getId());
            pst.setObject(2, reservation.getClient().getId());
            pst.setObject(3, reservation.getTickets().get(0).getId());
            pst.setObject(4, reservation.getStatus().name(), java.sql.Types.OTHER); // Ensure correct type

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Reservation confirmed and inserted successfully: " + reservation.getId());
            } else {
                System.out.println("Failed to insert reservation: " + reservation.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to confirm reservation: " + reservation.getId());
        }
        return null;
    }

    @Override
    public Void CancelReservation(Reservation reservation) {
        String query = "INSERT INTO " + tableName + " (id, client_id, ticket_id, reservation_status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            reservation.setStatus(ReservationStatus.CANCELLED);

            pst.setObject(1, reservation.getId());
            pst.setObject(2, reservation.getClient().getId());
            pst.setObject(3, reservation.getTickets().get(0).getId());
            pst.setString(4, reservation.getStatus().name());

             int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Reservation cancelled and inserted successfully: " + reservation.getId());
            } else {
                System.out.println("Failed to insert reservation: " + reservation.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to cancel reservation: " + reservation.getId());
        }
        return null;
    }

}
