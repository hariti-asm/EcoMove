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

        return Optional.empty();
    }


}
