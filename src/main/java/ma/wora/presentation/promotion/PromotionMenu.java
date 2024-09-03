package main.java.ma.wora.presentation.promotion;

import java.util.Scanner;

public class PromotionMenu {

    private final PromotionUi promotionUi;
    private static final Scanner scanner = new Scanner(System.in);

    public PromotionMenu(PromotionUi promotionUi) {
        this.promotionUi = promotionUi;
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
                case 1 -> promotionUi.addPromotion();
                case 2 -> promotionUi.updatePromotion();
                case 3 -> promotionUi.deletePromotion();
                case 4 -> promotionUi.getPromotionById();
                case 5 -> promotionUi.listAllPromotions();
                case 6 -> promotionUi.listArchivedPromotions();
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
