package main.java.ma.wora.presentation.partner;

import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PartnerUi {
    private final PartnerRepository partnerRepository;
    private final Scanner scanner = new Scanner(System.in);

    public PartnerUi(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public void displayAllPartners() {
        List<Partner> partners = partnerRepository.findAll();

        if (partners.isEmpty()) {
            System.out.println("No partners found.");
        } else {
            for (Partner partner : partners) {
                showPartnerDetails(partner);
                System.out.println();
            }
        }
    }

    public void displayPartnerByName() {
        scanner.nextLine();
        System.out.println("Enter the partner name to search for:");
        String companyName = scanner.nextLine();

        Partner partner = partnerRepository.findByName(companyName);
        if (partner != null) {
            showPartnerDetails(partner);
        } else {
            System.out.println("Partner not found.");
        }
    }

    private void showPartnerDetails(Partner partner) {
        System.out.println("Partner Details:");
        System.out.println("ID: " + partner.getId());
        System.out.println("Company Name: " + partner.getCompanyName());
        System.out.println("Transport Type: " + partner.getTransportType());
        System.out.println("Geographical Zone: " + partner.getGeographicalZone());
        System.out.println("Special Conditions: " + partner.getSpecialConditions());
        System.out.println("Status: " + partner.getStatus());
        System.out.println("Creation Date: " + partner.getCreationDate());
    }

    public void addPartner() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();

        System.out.println("Enter the transport type:");
        String transportType = scanner.nextLine();

        System.out.println("Enter the geographical zone:");
        String geographicalZone = scanner.nextLine();

        System.out.println("Enter any special conditions:");
        String specialConditions = scanner.nextLine();

        System.out.println("Enter the status:");
        String status = scanner.nextLine();

        java.sql.Date creationDate = java.sql.Date.valueOf(LocalDate.now());
        Partner partner = new Partner(
                UUID.randomUUID(),
                companyName,
                transportType,
                geographicalZone,
                specialConditions,
                status,
                creationDate
        );

        partnerRepository.add(partner);
        System.out.println("Partner added successfully.");
        showPartnerDetails(partner);
    }

    public void updatePartner() {
        System.out.println("Enter the ID of the partner to update:");
        String idInput = scanner.nextLine();

        UUID id;
        try {
            id = UUID.fromString(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please enter a valid UUID.");
            return;
        }

        System.out.println("Enter the new company name:");
        String companyName = scanner.nextLine();

        System.out.println("Enter the new transport type:");
        String transportType = scanner.nextLine();

        System.out.println("Enter the new geographical zone:");
        String geographicalZone = scanner.nextLine();

        System.out.println("Enter any new special conditions:");
        String specialConditions = scanner.nextLine();

        System.out.println("Enter the new status:");
        String status = scanner.nextLine();

        LocalDate creationDate = LocalDate.now();

        Partner partner = new Partner(
                id,
                companyName,
                transportType,
                geographicalZone,
                specialConditions,
                status,
                Date.valueOf(creationDate)
        );

        Partner updatedPartner = partnerRepository.update(partner);

        if (updatedPartner != null) {
            System.out.println("Partner updated successfully.");
            showPartnerDetails(updatedPartner);
        } else {
            System.out.println("Partner update failed.");
        }
    }

    public void removePartner() {
        System.out.println("Enter the ID of the partner to remove:");
        String idInput = scanner.nextLine();

        UUID id;
        try {
            id = UUID.fromString(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please enter a valid UUID.");
            return;
        }

        boolean success = partnerRepository.remove(id);

        if (success) {
            System.out.println("Partner removed successfully.");
        } else {
            System.out.println("Partner with ID " + id + " not found.");
        }
    }

    public void changePartnerStatus() {
        System.out.println("Enter the ID of the partner to change status:");
        String idInput = scanner.nextLine();

        UUID id;
        try {
            id = UUID.fromString(idInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please enter a valid UUID.");
            return;
        }

        Partner existingPartner = partnerRepository.findById(id);
        if (existingPartner == null) {
            System.out.println("Partner with ID " + id + " not found.");
            return;
        }

        System.out.println("Enter the new status (e.g., ACTIVE, INACTIVE):");
        String statusInput = scanner.nextLine();

        PartnerStatus newStatus;
        try {
            newStatus = PartnerStatus.valueOf(statusInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter a valid status.");
            return;
        }

        boolean success = partnerRepository.changeStatus(id, newStatus);

        if (success) {
            System.out.println("Partner status changed successfully.");
            showPartnerDetails(existingPartner); // Display partner details after status change
        } else {
            System.out.println("Failed to change partner status.");
        }
    }

    public void displayPartnerMenu() {
        boolean running = true;

        while (running) {
            System.out.println("--- Partner Management ---");
            System.out.println("1. Display all partners");
            System.out.println("2. Display partner by name");
            System.out.println("3. Insert partner");
            System.out.println("4. Update partner");
            System.out.println("5. Remove partner");
            System.out.println("6. Change status of partner");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> displayAllPartners();
                case 2 -> displayPartnerByName();
                case 3 -> addPartner();
                case 4 -> updatePartner();
                case 5 -> removePartner();
                case 6 -> changePartnerStatus();
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
}
