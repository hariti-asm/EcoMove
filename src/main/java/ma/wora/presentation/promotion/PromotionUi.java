package main.java.ma.wora.presentation.promotion;

import main.java.ma.wora.models.entities.Promotion;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.repositories.PromotionRepository;

import java.math.BigDecimal;
import java.sql.Date;import main.java.ma.wora.models.enums.PromotionStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PromotionUi {
    private static final Scanner scanner = new Scanner(System.in);
    private final PromotionRepository promotionRepository;

    public PromotionUi(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void addPromotion(){
        System.out.println("#------------ Add New Promotion : -------------#");

        UUID id = UUID.randomUUID();
        UUID contractId = getContractIdInput();
        String offerName = getPromotionNameInput();
        String description = getDescriptionInput();
        LocalDate startDate = getStartDateInput();
        LocalDate endDate = getEndDateInput(startDate);
        PromotionStatus offerStatus = getPromotionStatusInput();
        BigDecimal reductionValue = getPromotionDiscountValueInput();
        DiscountType  reductionType =getDiscountTypeInput();
        String conditions = getConditionsInput();


        Promotion newPromotionalOffer = new Promotion(
                UUID.randomUUID(),
                offerName,

                description,
                startDate,
                endDate,
                reductionType,
                reductionValue,
                offerStatus,
                conditions,
                contractId
        );

        promotionRepository.create(newPromotionalOffer);
        System.out.println("New PromotionalOffer added successfully!");

    }

    private BigDecimal getPromotionDiscountValueInput() {
        while (true) {
            System.out.print("Enter discount value: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return new BigDecimal(input);
            }
            System.out.println("discount value cannot be empty. Please try again.");
        }

    }

    private UUID getContractIdInput() {
        while (true) {
            System.out.print("Enter Contract ID: ");
            String input = scanner.nextLine().trim();
            try {
                return UUID.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Please try again.");
            }
        }
    }

    private String getPromotionNameInput() {
        while (true) {
            System.out.print("Enter Offer Name: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Offer name cannot be empty. Please try again.");
        }
    }


    private String getDescriptionInput() {
        System.out.print("Enter Description: ");
        return scanner.nextLine().trim();
    }

    private LocalDate getStartDateInput() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    private LocalDate getEndDateInput(LocalDate startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate endDate = LocalDate.parse(input, formatter);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date must be after start date. Please try again.");
                } else {
                    return endDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }
 private  DiscountType getDiscountTypeInput(){
     while (true) {
         System.out.print("Enter Reduction Type (PERCENTAGE or FIXED_AMOUNT): ");
         String input = scanner.nextLine().trim().toUpperCase();
         try {
             return DiscountType.valueOf(input);
         } catch (IllegalArgumentException e) {
             System.out.println("Invalid reduction type. Please enter PERCENTAGE or FIXED_AMOUNT.");
         }
     }

 }


    private BigDecimal getDiscountValueInput(DiscountType DiscountType) {
        while (true) {
            System.out.print("Enter Reduction Value: ");
            String input = scanner.nextLine().trim();
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Reduction value must be positive. Please try again.");
                } else if (DiscountType == DiscountType.PERCENTAGE && value.compareTo(new BigDecimal("100")) > 0) {
                    System.out.println("Percentage reduction cannot exceed 100%. Please try again.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number.");
            }
        }
    }

    private String getConditionsInput() {
        System.out.print("Enter Conditions (optional): ");
        return scanner.nextLine().trim();
    }

    private PromotionStatus getPromotionStatusInput() {
        while (true) {
            System.out.print("Enter Promotion Status (ACTIVE or INACTIVE): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return PromotionStatus.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Promotion status. Please enter ACTIVE or INACTIVE.");
            }
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
    public void displayPromotionMenu() {
        boolean running = true;

        while (running) {
            System.out.println("--- Promotion Management ---");
            System.out.println("1. Add Promotion");
            System.out.println("2. Update Promotion");
            System.out.println("3. Delete Promotion");
            System.out.println("4. View Promotion by ID");
            System.out.println("5. List All Promotions");
            System.out.println("6. List Archived Promotions");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> addPromotion();
                case 2 -> updatePromotion();
                case 3 -> deletePromotion();
                case 4 -> getPromotionById();
                case 5 -> listAllPromotions();
                case 6 -> listArchivedPromotions();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
