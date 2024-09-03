package main.java.ma.wora.presentation.contract;

import main.java.ma.wora.models.entities.Contract;
import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.ContractStatus;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.repositories.ContractRepository;
import main.java.ma.wora.repositories.PartnerRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ContractUi {
    private static final Scanner scanner = new Scanner(System.in);

    private  final  ContractRepository contractRepository;
    private final  PartnerRepository partnerRepository;

    public ContractUi(ContractRepository contractRepository, PartnerRepository partnerRepository) {
        this.contractRepository = contractRepository;
        this.partnerRepository = partnerRepository;
    }

    public void displayContractMenu() {
        boolean running = true;

        while (running) {
            System.out.println("--- Contract Management ---");
            System.out.println("1. Insert contract");
            System.out.println("2. Get contract by ID");
            System.out.println("3. Get contracts by partner");
            System.out.println("4. Get terminated contracts");
            System.out.println("5. Update contract");
            System.out.println("6. Remove contract");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> addContract();
                case 2 -> getContractById();
                case 3 -> getContractsByPartner();
                case 4 -> getTerminatedContracts();
                case 5 -> updateContract();
                case 6 -> removeContract();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number. Please try again.");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public void addContract() {
        LocalDate startDate;
        LocalDate endDate;

        while (true) {
            try {
                System.out.println("Enter the start date (YYYY-MM-DD):");
                startDate = LocalDate.parse(scanner.nextLine());
                System.out.println("Enter the end date (YYYY-MM-DD):");
                endDate = LocalDate.parse(scanner.nextLine());

                if (endDate.isAfter(startDate)) {
                    break;
                } else {
                    System.out.println("End date must be greater than start date. Please re-enter the dates.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        System.out.println("Enter the special rate:");
        BigDecimal specialRate;
        try {
            specialRate = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for special rate.");
            return;
        }

        System.out.println("Enter the agreement conditions:");
        String agreementConditions = scanner.nextLine();

        System.out.println("Is the contract renewable? (true/false):");
        boolean renewable;
        try {
            renewable = Boolean.parseBoolean(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input for renewable status.");
            return;
        }

        System.out.println("Enter the contract status (e.g., ONGOING, EXPIRED):");
        ContractStatus status;
        try {
            status = ContractStatus.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid contract status.");
            return;
        }

        System.out.println("Enter the discount type (e.g., PERCENTAGE, FIXED_AMOUNT):");
        DiscountType discountType;
        try {
            discountType = DiscountType.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid discount type.");
            return;
        }

        System.out.println("Enter Partner ID:");
        UUID partnerId;
        try {
            partnerId = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Partner ID format.");
            return;
        }

        Partner partner = partnerRepository.findById(partnerId);

        if (partner == null) {
            System.out.println("Partner not found. Please check the ID.");
            return;
        }

        Contract contract = new Contract(
                        UUID.randomUUID(),
                        Date.valueOf(startDate),
                        Date.valueOf(endDate),
                        specialRate,
                        agreementConditions,
                        renewable,
                        status,
                        partner,
                        discountType
                );

        try {
            contractRepository.createContract(contract);
            System.out.println("Contract added successfully.");
        } catch (Exception e) {
            System.err.println("Error inserting contract: " + e.getMessage());
        }
    }
    public void getContractById() {
        System.out.println("Enter Contract ID:");
        String input = scanner.nextLine();
        System.out.print("Contract ID: " + input);

        if (input.isEmpty()) {
            System.out.println("Empty input. Please enter a valid Contract ID.");
            return;
        }

        UUID contractId;
        try {
            contractId = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Contract ID format.");
            return;
        }

        Contract contract = contractRepository.getContractById(contractId);

        if (contract == null) {
            System.out.println("Contract not found. Please check the ID.");
            return;
        }

        displayContractDetails(contract);
    }


    public void getContractsByPartner() {
        System.out.println("Enter Partner ID:");
        UUID partnerId;
        try {
            partnerId = UUID.fromString(scanner.next().trim());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Partner ID format.");
            return;
        }

        // Retrieve partner details
        Partner partner = partnerRepository.findById(partnerId);
        if (partner == null) {
            System.out.println("Partner not found.");
            return;
        }

        // Display partner details
        displayPartnerDetails(partner);

        // Retrieve and display contracts
        List<Contract> contracts = contractRepository.getContractsByPartner(partnerId);
        if (contracts.isEmpty()) {
            System.out.println("No contracts found for the given partner ID.");
        } else {
            System.out.println("Contracts for partner " + partner.getCompanyName() + ":");
            for (Contract contract : contracts) {
                displayContractDetails(contract);
            }
        }
    }
    private void displayPartnerDetails(Partner partner) {
        System.out.println("Partner Details:");
        System.out.println("ID: " + partner.getId());
        System.out.println("Company Name: " + partner.getCompanyName());
        System.out.println("Transport Type: " + partner.getTransportType());
        System.out.println("Geographical Zone: " + partner.getGeographicalZone());
        System.out.println("Special Conditions: " + partner.getSpecialConditions());
        System.out.println("Status: " + partner.getStatus());
        System.out.println("Creation Date: " + partner.getCreationDate());
    }
    public void getTerminatedContracts() {
        List<Contract> terminatedContracts = contractRepository.getTerminatedContracts();

        if (terminatedContracts.isEmpty()) {
            System.out.println("No terminated contracts found.");
            return;
        }

        for (Contract contract : terminatedContracts) {
            displayContractDetails(contract);
        }
    }

    public void updateContract() {
        System.out.println("Enter Contract ID to update:");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Empty input. Please enter a valid Contract ID.");
            return;
        }

        UUID contractId;
        try {
            contractId = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Contract ID format.");
            return;
        }

        Contract existingContract = contractRepository.getContractById(contractId);

        if (existingContract == null) {
            System.out.println("Contract not found. Please check the ID.");
            return;
        }

        System.out.println("Enter new start date (YYYY-MM-DD) or press Enter to keep the current value (" + existingContract.getStartDate() + "):");
        String startDateInput = scanner.nextLine();
        LocalDate startDate = startDateInput.isEmpty() ? existingContract.getStartDate().toLocalDate() : LocalDate.parse(startDateInput);

        System.out.println("Enter new end date (YYYY-MM-DD) or press Enter to keep the current value (" + existingContract.getEndDate() + "):");
        String endDateInput = scanner.nextLine();
        LocalDate endDate = endDateInput.isEmpty() ? existingContract.getEndDate().toLocalDate() : LocalDate.parse(endDateInput);

        System.out.println("Enter new special rate or press Enter to keep the current value (" + existingContract.getSpecialRate() + "):");
        String specialRateInput = scanner.nextLine();
        BigDecimal specialRate = specialRateInput.isEmpty() ? existingContract.getSpecialRate() : new BigDecimal(specialRateInput);

        System.out.println("Enter new agreement conditions or press Enter to keep the current value:");
        String agreementConditions = scanner.nextLine();
        if (agreementConditions.isEmpty()) agreementConditions = existingContract.getAgreementConditions();

        System.out.println("Is the contract renewable? (true/false) or press Enter to keep the current value (" + existingContract.isRenewable() + "):");
        String renewableInput = scanner.nextLine();
        boolean renewable = renewableInput.isEmpty() ? existingContract.isRenewable() : Boolean.parseBoolean(renewableInput);

        System.out.println("Enter new contract status (e.g., Active, terminated, suspended) or press Enter to keep the current value (" + existingContract.getStatus() + "):");
        String statusInput = scanner.nextLine();
        ContractStatus status = statusInput.isEmpty() ? existingContract.getStatus() : ContractStatus.valueOf(statusInput.toUpperCase());
        System.out.println("enter the discount type ( fixed_amount / percentage");
        String discountTypeInput = scanner.nextLine();
        DiscountType discountType = discountTypeInput.isEmpty() ? existingContract.getDiscountType() : DiscountType.valueOf(discountTypeInput.toUpperCase());
        Contract updatedContract = new Contract(
                        existingContract.getId(),
                        Date.valueOf(startDate),
                        Date.valueOf(endDate),
                        specialRate,
                        agreementConditions,
                        renewable,
                        status,
                        existingContract.getPartner(),
                        discountType
                );

        try {
            contractRepository.updateContract(updatedContract);
            System.out.println("Contract updated successfully.");
        } catch (Exception e) {
            System.err.println("Error updating contract: " + e.getMessage());
        }
    }

    public void removeContract() {
        System.out.println("Enter Contract ID to remove:");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Empty input. Please enter a valid Contract ID.");
            return;
        }

        UUID contractId;
        try {
            contractId = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Contract ID format.");
            return;
        }

        try {
            contractRepository.deleteContract(contractId);
            System.out.println("Contract removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing contract: " + e.getMessage());
        }
    }

    private void displayContractDetails(Contract contract) {
        System.out.println("Contract found:");
        System.out.println("ID: " + contract.getId());
        System.out.println("Start Date: " + contract.getStartDate());
        System.out.println("End Date: " + contract.getEndDate());
        System.out.println("Special Rate: " + contract.getSpecialRate());
        System.out.println("Agreement Conditions: " + contract.getAgreementConditions());
        System.out.println("Renewable: " + contract.isRenewable());
        System.out.println("Status: " + contract.getStatus());
    }
}
