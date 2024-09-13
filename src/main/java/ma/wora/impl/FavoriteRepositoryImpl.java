package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Favorite;
import main.java.ma.wora.models.entities.Journey;
import main.java.ma.wora.models.enums.ReservationStatus;
import main.java.ma.wora.repositories.FavoriteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FavoriteRepositoryImpl implements FavoriteRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "favorites";

    public FavoriteRepositoryImpl() throws SQLException {
    }

    @Override
    public Void mentionAsFavorite(Favorite favorite) {
        String query = "INSERT INTO " + tableName + " (id, client_id, journey_id) VALUES (?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            for (Journey journey : favorite.getJourneys()) {
                pst.setObject(1, favorite.getId());
                pst.setObject(2, favorite.getClient().getId());
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
}
