package main.java.ma.wora.repositories;


import main.java.ma.wora.models.entities.Partner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface PartnerRepository {
    List<Partner>findAll();
    Partner findByName(String companyName  );
    Partner add(Partner partner);
}