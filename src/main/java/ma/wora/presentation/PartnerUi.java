package main.java.ma.wora.presentation;

import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PartnerUi {
    private static final Scanner scanner = new Scanner(System.in);

    private final PartnerRepository repository;

    public PartnerUi(PartnerRepository repository) {
        this.repository = repository;
    }

    public void displayAllPartners() {
        List<String> partnerNames = repository.findAll()
                .stream()
                .map(Partner::getCompanyName)
                .toList();

        if (partnerNames.isEmpty()) {
            System.out.println("No partners found.");
        } else {
            partnerNames.forEach(System.out::println);
        }

    }

    public void displayPartnerByName() {
System.out.println("Enter the partner name to search for:");
String companyName = scanner.nextLine();
    Partner partner = repository.findByName(companyName);
    if(partner !=null)

    {
        System.out.println("partner " + partner.getCompanyName() +" found:\n" );

    }
else

    {
        System.out.println("partner not found");
    }
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

        repository.add(partner); // Assuming you have an add method in your repository

        System.out.println("Partner added successfully.");
    }

    public void updatePartner() {
        System.out.println("Enter the ID of the partner to update:");
        String idInput = scanner.nextLine();

        // Validate UUID format
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

        // You can set creationDate to the current date or another value
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

        Partner updatedPartner = repository.update(partner);

        if (updatedPartner != null) {
            System.out.println("Partner updated successfully.");
        } else {
            System.out.println("Partner update failed.");
        }
    }

}