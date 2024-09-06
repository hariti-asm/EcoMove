package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Promotion;
import main.java.ma.wora.repositories.PromotionRepository;

import java.util.List;
import java.util.UUID;

public class PromotionService {
    private final PromotionRepository promotionRepository;
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public Promotion createPromotion(Promotion promotion) {
        return  promotionRepository.create(promotion);
    }
    public Promotion updatePromotion(Promotion promotion) {
        return promotionRepository.update(promotion);
    }
    public Promotion deletePromotion(Promotion promotion) {
        return promotionRepository.delete(promotion);
    }
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    public Promotion getPromotionById(UUID id) {
        return promotionRepository.findById(id);
    }
    public List<Promotion> getArchivedPromotion() {
        return promotionRepository.findArchived();
    }
    public  List<Promotion> getArchivedPromotions() {
        return promotionRepository.findArchived();
    }
}
