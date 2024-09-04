package main.java.ma.wora.repositories;


import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;

import java.util.List;
import java.util.UUID;

public interface PartnerRepository {
    List<String> findAll();
    Partner findByName(String companyName  );
    Partner add(Partner partner);
    Partner update(Partner partner);
    boolean remove(UUID id);
    boolean changeStatus(UUID id, PartnerStatus newStatus);
    Partner findById(UUID id);
    void UpdatePartnerStatus(UUID partnerId, PartnerStatus newStatus);
}