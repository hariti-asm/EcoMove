package main.java.ma.wora.presentation.promotion;

import main.java.ma.wora.models.entities.Contract;
import main.java.ma.wora.models.entities.Promotion;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.models.enums.PromotionStatus;
import main.java.ma.wora.services.PromotionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PromotionUi {
    private static final Scanner scanner = new Scanner(System.in);
    private final PromotionService promotionService;

    public PromotionUi(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public void addPromotion() {
        System.out.println("#------------ Add New Promotion : -------------#");

        UUID id = UUID.randomUUID();
        UUID contractId = getContractIdInput();
        String offerName = getPromotionNameInput();
        String description = getDescriptionInput();
        LocalDate startDate = getStartDateInput();
        LocalDate endDate = getEndDateInput(startDate);
        PromotionStatus offerStatus = getPromotionStatusInput();
        BigDecimal reductionValue = getPromotionDiscountValueInput();
        DiscountType reductionType = getDiscountTypeInput();
        String conditions = getConditionsInput();

        Promotion newPromotion = new Promotion(
                id,
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

        promotionService.createPromotion(newPromotion);
        System.out.println("New Promotion added successfully!");
    }

    private BigDecimal getPromotionDiscountValueInput() {
        while (true) {
            System.out.print("Enter discount value: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return new BigDecimal(input);
            }
            System.out.println("Discount value cannot be empty. Please try again.");
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

    private DiscountType getDiscountTypeInput() {
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
        System.out.print("Enter the ID of the promotional offer to update: ");
        String idString = scanner.nextLine().trim();

        try {
            UUID id = UUID.fromString(idString);
            Promotion offer = promotionService.getPromotionById(id);

            if (offer != null) {
                System.out.println("Enter new details (press Enter to keep current value):");

                System.out.print("Offer Name [" + offer.getOfferName() + "]: ");
                String offerName = scanner.nextLine();
                if (!offerName.isEmpty()) offer.setOfferName(offerName);

                System.out.print("Description [" + offer.getDescription() + "]: ");
                String description = scanner.nextLine();
                if (!description.isEmpty()) offer.setDescription(description);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                System.out.print("Start Date [" + offer.getStartDate() + "]: ");
                String startDateStr = scanner.nextLine();
                if (!startDateStr.isEmpty()) {
                    try {
                        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                        offer.setStartDate(startDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Keeping the current value.");
                    }
                }

                System.out.print("End Date [" + offer.getEndDate() + "]: ");
                String endDateStr = scanner.nextLine();
                if (!endDateStr.isEmpty()) {
                    try {
                        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                        offer.setEndDate(endDate);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Keeping the current value.");
                    }
                }

                System.out.print("Reduction Type [" + offer.getDiscountType() + "]: ");
                String reductionTypeStr = scanner.nextLine();
                if (!reductionTypeStr.isEmpty()) {
                    try {
                        DiscountType reductionType = DiscountType.valueOf(reductionTypeStr.toUpperCase());
                        offer.setDiscountType(reductionType);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid reduction type. Keeping the current value.");
                    }
                }

                System.out.print("Reduction Value [" + offer.getDiscountValue() + "]: ");
                String reductionValueStr = scanner.nextLine();
                if (!reductionValueStr.isEmpty()) {
                    try {
                        BigDecimal reductionValue = new BigDecimal(reductionValueStr);
                        offer.setDiscountValue(reductionValue);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format. Keeping the current value.");
                    }
                }

                System.out.print("Conditions [" + offer.getConditions() + "]: ");
                String conditions = scanner.nextLine();
                if (!conditions.isEmpty()) offer.setConditions(conditions);

                System.out.print("Status [" + offer.getStatus() + "]: ");
                String statusStr = scanner.nextLine();
                if (!statusStr.isEmpty()) {
                    try {
                        PromotionStatus status = PromotionStatus.valueOf(statusStr.toUpperCase());
                        offer.setStatus(status);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status. Keeping the current value.");
                    }
                }

                promotionService.updatePromotion(offer);
                System.out.println("Promotional offer updated successfully.");
            } else {
                System.out.println("No promotion found with ID: " + idString);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please try again.");
        }
    }

    public void deletePromotion() {
        System.out.println("Enter Promotion ID to delete:");
        UUID id = UUID.fromString(scanner.nextLine());

        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion == null) {
            System.out.println("Promotion not found.");
            return;
        }

        try {
            promotionService.deletePromotion(promotion);
            System.out.println("Promotion deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting promotion: " + e.getMessage());
        }
    }

    public void getPromotionById() {
        System.out.println("Enter Promotion ID:");
        UUID id = UUID.fromString(scanner.nextLine());

        Promotion promotion = promotionService.getPromotionById(id);
        if (promotion != null) {
            System.out.println("Promotion Details:");
            System.out.println("ID: " + promotion.getId());
            System.out.println("Offer Name: " + promotion.getOfferName());
            System.out.println("Description: " + promotion.getDescription());
            System.out.println("Start Date: " + promotion.getStartDate());
            System.out.println("End Date: " + promotion.getEndDate());
            System.out.println("Discount Type: " + promotion.getDiscountType());
            System.out.println("Discount Value: " + promotion.getDiscountValue());
            System.out.println("Status: " + promotion.getStatus());
            System.out.println("Conditions: " + promotion.getConditions());
            System.out.println("Contract ID: " + promotion.getContract());
        } else {
            System.out.println("No promotion found with ID: " + id);
        }
    }

    public void listAllPromotions() {
        System.out.println("Listing all promotions:");
        List<Promotion> promotions = promotionService.getAllPromotions();
        if (promotions.isEmpty()) {
            System.out.println("No promotions available.");
        } else {
            for (Promotion promotion : promotions) {
                System.out.println("ID: " + promotion.getId());
                System.out.println("Offer Name: " + promotion.getOfferName());
                System.out.println("Description: " + promotion.getDescription());
                System.out.println("Start Date: " + promotion.getStartDate());
                System.out.println("End Date: " + promotion.getEndDate());
                System.out.println("Discount Type: " + promotion.getDiscountType());
                System.out.println("Discount Value: " + promotion.getDiscountValue());
                System.out.println("Status: " + promotion.getStatus());
                System.out.println("Conditions: " + promotion.getConditions());
                System.out.println("Contract ID: " + promotion.getContract());
                System.out.println("------------------------------------");
            }
        }
    }



    public void getArchivedPromotions() {
        List<Promotion> archivedPromotions = promotionService.getArchivedPromotions();
        if (archivedPromotions.isEmpty()) {
            System.out.println("No archived promotions found.");
            return;
        }
        for (Promotion promotion : archivedPromotions) {
            System.out.println("ID: " + promotion.getId());
            System.out.println("Offer Name: " + promotion.getOfferName());
            System.out.println("Description: " + promotion.getDescription());
            System.out.println("Start Date: " + promotion.getStartDate());
            System.out.println("End Date: " + promotion.getEndDate());
            System.out.println("Discount Type: " + promotion.getDiscountType());
            System.out.println("Discount Value: " + promotion.getDiscountValue());
            System.out.println("Status: " + promotion.getStatus());
            System.out.println("Conditions: " + promotion.getConditions());
            System.out.println("Contract ID: " + promotion.getContract());

        }
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
                case 5 ->listAllPromotions();
                case 6 -> getArchivedPromotions();
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
