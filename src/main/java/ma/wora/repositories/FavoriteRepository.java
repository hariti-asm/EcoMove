package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Favorite;
import main.java.ma.wora.models.entities.Journey;
import main.java.ma.wora.models.entities.Reservation;

public interface FavoriteRepository {
    Void mentionAsFavorite(Favorite favorite);

}
