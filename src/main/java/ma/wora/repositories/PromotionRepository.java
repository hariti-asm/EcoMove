package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Promotion;

import java.util.List;
import java.util.UUID;

public interface PromotionRepository {
Promotion findById(UUID id);
Promotion create(Promotion promotion);
Promotion update(Promotion promotion);
Promotion delete(Promotion promotion);
public List<Promotion> findAll();
public List<Promotion> findArchived();
}


