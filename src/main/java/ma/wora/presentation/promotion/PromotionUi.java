package main.java.ma.wora.presentation.promotion;

import main.java.ma.wora.models.entities.Promotion;
import main.java.ma.wora.repositories.PromotionRepository;

import java.sql.Date;import main.java.ma.wora.models.enums.PromotionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PromotionUi {
    private static final Scanner scanner = new Scanner(System.in);
    private final PromotionRepository promotionRepository;

    public PromotionUi(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void addPromotion() {
        System.out.println("Enter Promotion ID:");
        UUID id = UUID.fromString(scanner.nextLine());

        System.out.println("Enter Promotion Name:");
        String name = scanner.nextLine();

        System.out.println("Enter Promotion Description:");
        String description = scanner.nextLine();

        LocalDate startDate;
        LocalDate endDate;

        while (true) {
            try {
                System.out.println("Enter Start Date (YYYY-MM-DD):");
                startDate = LocalDate.parse(scanner.nextLine());
                System.out.println("Enter End Date (YYYY-MM-DD):");
                endDate = LocalDate.parse(scanner.nextLine());

                if (endDate.isAfter(startDate)) {
                    break;
                } else {
                    System.out.println("End date must be after start date. Please re-enter the dates.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        System.out.println("Enter Promotion Status (ACTIVE/ARCHIVED):");
        String statusString = scanner.nextLine();
        PromotionStatus status;
        try {
            status = PromotionStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid promotion status.");
            return;
        }

        Promotion promotion = new Promotion();
        promotion.setId(id);
        promotion.setOfferName(name);
        promotion.setDescription(description);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setStatus(status);

        try {
            promotionRepository.create(promotion);
            System.out.println("Promotion added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding promotion: " + e.getMessage());
        }
    }

    public void updatePromotion() {
        System.out.println("Enter Promotion ID to update:");
        UUID id = UUID.fromString(scanner.nextLine());

        Promotion promotion = promotionRepository.findById(id);
        if (promotion == null) {
            System.out.println("Promotion not found.");
            return;
        }

        System.out.println("Enter new Promotion Name (leave blank to keep current):");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            promotion.setOfferName(name);
        }

        System.out.println("Enter new Promotion Description (leave blank to keep current):");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            promotion.setDescription(description);
        }

        LocalDate startDate = null;
        LocalDate endDate = null;

        while (true) {
            try {
                System.out.println("Enter new Start Date (YYYY-MM-DD) (leave blank to keep current):");
                String startDateInput = scanner.nextLine();
                if (!startDateInput.isEmpty()) {
                    startDate = LocalDate.parse(startDateInput);
                    promotion.setStartDate(startDate);
                }

                System.out.println("Enter new End Date (YYYY-MM-DD) (leave blank to keep current):");
                String endDateInput = scanner.nextLine();
                if (!endDateInput.isEmpty()) {
                    endDate = LocalDate.parse(endDateInput);
                    promotion.setEndDate(endDate);
                }

                if (endDate.isAfter(startDate)) {
                    break;
                } else {
                    System.out.println("End date must be after start date. Please re-enter the dates.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        System.out.println("Enter new Promotion Status (ACTIVE/ARCHIVED) (leave blank to keep current):");
        String statusString = scanner.nextLine();
        if (!statusString.isEmpty()) {
            try {
                promotion.setStatus(PromotionStatus.valueOf(statusString.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid promotion status.");
                return;
            }
        }

        try {
            promotionRepository.update(promotion);
            System.out.println("Promotion updated successfully.");
        } catch (Exception e) {
            System.err.println("Error updating promotion: " + e.getMessage());
        }
    }

    public void deletePromotion() {
        System.out.println("Enter Promotion ID to delete:");
        UUID id = UUID.fromString(scanner.nextLine());

        Promotion promotion = promotionRepository.findById(id);
        if (promotion == null) {
            System.out.println("Promotion not found.");
            return;
        }

        try {
            promotionRepository.delete(promotion);
            System.out.println("Promotion deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting promotion: " + e.getMessage());
        }
    }

    public void getPromotionById() {
        System.out.println("Enter Promotion ID:");
        UUID id = UUID.fromString(scanner.nextLine());

        Promotion promotion = promotionRepository.findById(id);
        if (promotion == null) {
            System.out.println("Promotion not found.");
            return;
        }

        System.out.println("Promotion Details:");
        System.out.println("ID: " + promotion.getId());
        System.out.println("Name: " + promotion.getOfferName());
        System.out.println("Description: " + promotion.getDescription());
        System.out.println("Start Date: " + promotion.getStartDate());
        System.out.println("End Date: " + promotion.getEndDate());
        System.out.println("Status: " + promotion.getStatus());
    }

    public void listAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        if (promotions.isEmpty()) {
            System.out.println("No promotions found.");
            return;
        }

        for (Promotion promotion : promotions) {
            displayPromotionDetails(promotion);
        }
    }

    public void listArchivedPromotions() {
        List<Promotion> promotions = promotionRepository.findArchived();
        if (promotions.isEmpty()) {
            System.out.println("No archived promotions found.");
            return;
        }

        for (Promotion promotion : promotions) {
            displayPromotionDetails(promotion);
        }
    }

    private void displayPromotionDetails(Promotion promotion) {
        System.out.println("Promotion Details:");
        System.out.println("ID: " + promotion.getId());
        System.out.println("Name: " + promotion.getOfferName());
        System.out.println("Description: " + promotion.getDescription());
        System.out.println("Start Date: " + promotion.getStartDate());
        System.out.println("End Date: " + promotion.getEndDate());
        System.out.println("Status: " + promotion.getStatus());
        System.out.println();
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
