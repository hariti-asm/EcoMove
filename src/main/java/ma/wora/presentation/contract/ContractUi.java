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
import java.util.Scanner;
import java.util.UUID;

public class ContractUi {
    private static final Scanner scanner = new Scanner(System.in);

    private final ContractRepository contractRepository;
    private final PartnerRepository partnerRepository;

    public void displayContractMenu() {
        boolean running = true;

        while (running) {
            System.out.println("--- Contract Management ---");
            System.out.println("1. Insert contract");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> addContract();
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
        return scanner.nextInt();
    }

    public ContractUi(ContractRepository contractRepository, PartnerRepository partnerRepository) {
        this.contractRepository = contractRepository;
        this.partnerRepository = partnerRepository;

    }

    public void addContract() {
        LocalDate startDate;
        LocalDate endDate;

        // Loop to ensure end date is after start date
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

        System.out.println("Enter the discount type (e.g., PERCENTAGE, AMOUNT):");
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
}
