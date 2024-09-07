package main.java.ma.wora.presentation.partner;

import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.PartnerRepository;
import main.java.ma.wora.services.PartnerService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PartnerUi {
    private final PartnerService partnerService;
    private final Scanner scanner = new Scanner(System.in);

    public PartnerUi(PartnerService partnerService) {
        this.partnerService = partnerService;
    }



    public void displayPartnerByName() {
        scanner.nextLine();
        System.out.println("Enter the partner name to search for:");
        String companyName = scanner.nextLine();

        Partner partner =partnerService.findPartnerByName(companyName);
        if (partner != null) {
            showPartnerDetails(partner);
        } else {
            System.out.println("Partner not found.");
        }
    }

    private void showPartnerDetails(Partner partner) {
        System.out.println("Partner Details:");
        System.out.println("ID: " + (partner.getId() != null ? partner.getId() : "N/A"));
        System.out.println("Company Name: " + (partner.getCompanyName() != null ? partner.getCompanyName() : "N/A"));
        System.out.println("Transport Type: " + (partner.getTransportType() != null ? partner.getTransportType() : "N/A"));
        System.out.println("Geographical Zone: " + (partner.getGeographicalZone() != null ? partner.getGeographicalZone() : "N/A"));
        System.out.println("Special Conditions: " + (partner.getSpecialConditions() != null ? partner.getSpecialConditions() : "N/A"));
        System.out.println("Status: " + (partner.getStatus() != null ? partner.getStatus() : "N/A"));
        System.out.println("Creation Date: " + (partner.getCreationDate() != null ? partner.getCreationDate() : "N/A"));
    }
    public void displayAllPartners() {
        List<String> partnerNames =partnerService.findAllPartners();
        if (partnerNames.isEmpty()) {
            System.out.println("No partners found!");
        } else {
            System.out.println("#------------- All Partners -------------#");
            int count = 0;
            for (String name : partnerNames) {
                System.out.println("Company Name " + ++count + ": " + name);
            }
        }
    }
    public void UpdatePartnerStatus(){

        System.out.println("#-------------- Update Partner Status --------------# ");

        System.out.print("Enter ID of partner :");
        String partnerIdInput = scanner.nextLine();
        UUID partnerId;
        try {
            partnerId = UUID.fromString(partnerIdInput);

        } catch (IllegalArgumentException e){
            System.out.println("invalid uuid format !");
            return;

        }

        System.out.println("Enter New status ");
        String StatusInput = scanner.nextLine().trim().toUpperCase();

        PartnerStatus newStatus ;
        try {
            newStatus = PartnerStatus.valueOf(StatusInput);

        } catch (IllegalArgumentException e) {
            System.out.println("invalid status !");
            return;
        }

       partnerService.updatePartnerStatus(partnerId, newStatus);

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

       partnerService.createPartner(partner);
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

        Partner existingPartner =partnerService.findPartnerById(id);
        if (existingPartner == null) {
            System.out.println("Partner not found.");
            return;
        }

        System.out.println("Enter the new company name (press Enter to keep current: " + existingPartner.getCompanyName() + "):");
        String companyName = scanner.nextLine();
        if (companyName.isEmpty()) {
            companyName = existingPartner.getCompanyName();
        }

        System.out.println("Enter the new transport type (press Enter to keep current: " + existingPartner.getTransportType() + "):");
        String transportType = scanner.nextLine();
        if (transportType.isEmpty()) {
            transportType = String.valueOf(existingPartner.getTransportType());
        }

        System.out.println("Enter the new geographical zone (press Enter to keep current: " + existingPartner.getGeographicalZone() + "):");
        String geographicalZone = scanner.nextLine();
        if (geographicalZone.isEmpty()) {
            geographicalZone = existingPartner.getGeographicalZone();
        }

        System.out.println("Enter any new special conditions (press Enter to keep current: " + existingPartner.getSpecialConditions() + "):");
        String specialConditions = scanner.nextLine();
        if (specialConditions.isEmpty()) {
            specialConditions = existingPartner.getSpecialConditions();
        }

        System.out.println("Enter the new status (press Enter to keep current: " + existingPartner.getStatus() + "):");
        String status = scanner.nextLine();
        if (status.isEmpty()) {
            status = String.valueOf(existingPartner.getStatus());
        }

        java.util.Date creationDate = existingPartner.getCreationDate();

        Partner updatedPartner = new Partner(
                id,
                companyName,
                transportType,
                geographicalZone,
                specialConditions,
                status,
                Date.valueOf(String.valueOf(creationDate))
        );

        Partner result =partnerService.updatePartner(updatedPartner);

        if (result != null) {
            System.out.println("Partner updated successfully.");
            showPartnerDetails(result);
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

        boolean success =partnerService.removePartner(id);

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

        Partner existingPartner =partnerService.findPartnerById(id);
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

        boolean success =partnerService.updatePartnerStatus(id, newStatus);

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
