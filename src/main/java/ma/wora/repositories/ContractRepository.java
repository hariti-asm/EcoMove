package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Contract;

import java.util.List;
import java.util.UUID;

public interface ContractRepository {
    Contract createContract(Contract contract);
     Contract getContractById(UUID id);
    boolean updateContract(UUID contractId, Contract updatedContract);
    boolean deleteContract(UUID id);
    List<Contract> getAllContracts();
    List<Contract> getTerminatedContracts();
    List<Contract> getContractsByPartner(UUID partnerId);
}