package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Favorite;
import main.java.ma.wora.repositories.FavoriteRepository;

import java.util.Optional;

public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void mentionAsFavorite(Favorite favorite) {
        Optional<Favorite> existingFavorite = favoriteRepository.findById(favorite);

        if (existingFavorite.isPresent()) {
            System.out.println("This ticket is already marked as a favorite.");
        } else {
            favoriteRepository.mentionAsFavorite(favorite);
        }
    }
}
