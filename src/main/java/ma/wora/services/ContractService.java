package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Contract;
import main.java.ma.wora.repositories.ContractRepository;

import java.util.List;
import java.util.UUID;

public class ContractService {

    private  final ContractRepository contractRepository;
    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }
    public Contract getContractById(UUID id) {
        return contractRepository.getContractById(id);
    }
    public List<Contract> getAllContracts() {
        return contractRepository.getAllContracts();
    }
    public Contract createContract(Contract contract) {
        return contractRepository.createContract(contract);
    }
    public boolean deleteContract(UUID id) {
        return  contractRepository.deleteContract(id);
    }
    public boolean updateContract(UUID id, Contract updatedContract) {
        // Fetch the existing contract from the repository
        Contract existingContract = contractRepository.getContractById(id);

        if (existingContract == null) {
            System.out.println("Contract not found with ID: " + id);
            return false;
        }

        // Update fields only if they are not null or empty in the updatedContract
        existingContract.setStartDate(updatedContract.getStartDate() != null ? updatedContract.getStartDate() : existingContract.getStartDate());
        existingContract.setEndDate(updatedContract.getEndDate() != null ? updatedContract.getEndDate() : existingContract.getEndDate());
        existingContract.setSpecialRate(updatedContract.getSpecialRate() != null ? updatedContract.getSpecialRate() : existingContract.getSpecialRate());
        existingContract.setAgreementConditions(updatedContract.getAgreementConditions() != null ? updatedContract.getAgreementConditions() : existingContract.getAgreementConditions());

        // Update the renewable field if it's explicitly changed
        if (updatedContract.isRenewable() != existingContract.isRenewable()) {
            existingContract.setRenewable(updatedContract.isRenewable());
        }

        existingContract.setStatus(updatedContract.getStatus() != null ? updatedContract.getStatus() : existingContract.getStatus());
        existingContract.setPartner(updatedContract.getPartner() != null ? updatedContract.getPartner() : existingContract.getPartner());
        existingContract.setDiscountType(updatedContract.getDiscountType() != null ? updatedContract.getDiscountType() : existingContract.getDiscountType());

        // Save the updated contract back to the repository
        return contractRepository.updateContract(id, existingContract);
    }
}
