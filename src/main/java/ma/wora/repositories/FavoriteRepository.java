package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Favorite;
import main.java.ma.wora.models.entities.Journey;
import main.java.ma.wora.models.entities.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository {
    Void mentionAsFavorite(Favorite favorite);
    Optional<Favorite> findById(Favorite favorite);

}
