package main.java.ma.wora.repositories;

import main.java.ma.wora.models.enums.DiscountType;

import java.util.ArrayList;
import java.util.List;

public class ContractRepository {
private final List<DiscountType.Contract> contracts= new ArrayList<DiscountType.Contract>();


    public void addContract(DiscountType.Contract contract) {
        contracts.add(contract);
    }

    public void removeContract(DiscountType.Contract contract) {
        contracts.remove(contract);
    }

    public List<DiscountType.Contract> getContracts() {
        return contracts;
    }
}
