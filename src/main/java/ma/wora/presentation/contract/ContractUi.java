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
        System.out.print("Enter Contract ID: ");
        String contractIdInput = scanner.nextLine();

        try {
            UUID contractId = UUID.fromString(contractIdInput);

            Contract existingContract = contractRepository.getContractById(contractId);
            if (existingContract == null) {
                System.out.println("No contract found with this ID.");
                return;
            }

            System.out.println("Current Partner ID: " + existingContract.getPartner().getId());
            System.out.print("Enter new Partner ID (or press Enter to keep current): ");
            String partnerIdInput = scanner.nextLine();
            UUID newPartnerId = partnerIdInput.isEmpty() ? existingContract.getPartner().getId() : UUID.fromString(partnerIdInput);

            // Assuming you fetch the partner details from the database using the newPartnerId
            Partner newPartner = partnerRepository.findById(newPartnerId);
            if (newPartner == null) {
                System.out.println("No partner found with this ID.");
                return;
            }

            System.out.println("Current Start Date: " + existingContract.getStartDate());
            System.out.print("Enter new Start Date (YYYY-MM-DD) (or press Enter to keep current): ");
            String startDateInput = scanner.nextLine();
            LocalDate newStartDate = startDateInput.isEmpty() ? existingContract.getStartDate().toLocalDate() : LocalDate.parse(startDateInput);

            System.out.println("Current End Date: " + existingContract.getEndDate());
            System.out.print("Enter new End Date (YYYY-MM-DD) (or press Enter to keep current): ");
            String endDateInput = scanner.nextLine();
            LocalDate newEndDate = endDateInput.isEmpty() ? existingContract.getEndDate().toLocalDate() : LocalDate.parse(endDateInput);

            System.out.println("Current Special Rate: " + existingContract.getSpecialRate());
            System.out.print("Enter new Special Rate (or press Enter to keep current): ");
            String specialRateInput = scanner.nextLine();
            BigDecimal newSpecialRate = specialRateInput.isEmpty() ? existingContract.getSpecialRate() : new BigDecimal(specialRateInput);

            System.out.println("Current Agreement Conditions: " + existingContract.getAgreementConditions());
            System.out.print("Enter new Agreement Conditions (or press Enter to keep current): ");
            String agreementConditionsInput = scanner.nextLine();
            String newAgreementConditions = agreementConditionsInput.isEmpty() ? existingContract.getAgreementConditions() : agreementConditionsInput;

            System.out.println("Current Renewable: " + (existingContract.isRenewable() ? "Yes" : "No"));
            System.out.print("Is it Renewable? (Yes/No) (or press Enter to keep current): ");
            String renewableInput = scanner.nextLine();
            boolean newRenewable = renewableInput.isEmpty() ? existingContract.isRenewable() : renewableInput.equalsIgnoreCase("Yes");

            System.out.println("Current Status: " + existingContract.getStatus());
            System.out.print("Enter new Status (ACTIVE/TERMINATED/SUSPENDED) (or press Enter to keep current): ");
            String statusInput = scanner.nextLine();
            ContractStatus newStatus = statusInput.isEmpty() ? existingContract.getStatus() : ContractStatus.valueOf(statusInput);

            System.out.println("Current Discount Type: " + existingContract.getDiscountType());
            System.out.print("Enter new Discount Type (PERCENTAGE/FIXED_AMOUNT) (or press Enter to keep current): ");
            String discountTypeInput = scanner.nextLine();
            DiscountType newDiscountType = discountTypeInput.isEmpty() ? existingContract.getDiscountType() : DiscountType.valueOf(discountTypeInput);

            // Create an updated contract object
            Contract updatedContract = new Contract(
                    existingContract.getId(), // assuming the ID remains the same
                    Date.valueOf(newStartDate),
                    Date.valueOf(newEndDate),
                    newSpecialRate,
                    newAgreementConditions,
                    newRenewable,
                    newStatus,
                    newPartner,  // Using the new partner object
                    newDiscountType
            );

            boolean success = contractRepository.updateContract(contractId, updatedContract);
            if (success) {
                System.out.println("Contract updated successfully.");
            } else {
                System.out.println("Contract update failed.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Contract ID format.");
        }
    }
    public boolean removeContract() {
        System.out.println("Enter Contract ID to remove:");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Empty input. Please enter a valid Contract ID.");
            return false;  // Return false if input is empty
        }

        UUID contractId;
        try {
            contractId = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Contract ID format.");
            return false;  // Return false if the input format is invalid
        }

        try {
            boolean isDeleted = contractRepository.deleteContract(contractId);
            if (isDeleted) {
                System.out.println("Contract removed successfully.");
                return true;  // Return true if the contract was successfully removed
            } else {
                System.out.println("Contract not found or wasn't removed.");
                return false;  // Return false if the contract wasn't found or couldn't be removed
            }
        } catch (Exception e) {
            System.err.println("Error removing contract: " + e.getMessage());
            return false;  // Return false if there was an error during the removal process
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
