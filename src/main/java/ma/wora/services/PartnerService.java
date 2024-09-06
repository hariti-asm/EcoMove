package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.repositories.PartnerRepository;

import java.util.List;
import java.util.UUID;

public class PartnerService {
    private final PartnerRepository partnerRepository;
    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;

    }
    public Partner findPartnerById(UUID id) {
        return partnerRepository.findById(id);
    }
    public Partner findPartnerByName(String name) {
        return partnerRepository.findByName(name);
    }
    public Partner createPartner(Partner partner) {
        return partnerRepository.add(partner);
    }
    public Partner updatePartner(Partner partner) {
        return partnerRepository.update(partner);
    }
    public boolean removePartner(UUID id) {
        return partnerRepository.remove(id);
    }
   public List<String> findAllPartners() {
        return  partnerRepository.findAll();
   }
public boolean updatePartnerStatus( UUID id, PartnerStatus status) {
        return  partnerRepository.changeStatus(id, status);
}
}
