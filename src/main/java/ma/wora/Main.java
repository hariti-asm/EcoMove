package main.java.ma.wora;

import main.java.ma.wora.impl.PartnerRepositoryImpl;
import main.java.ma.wora.presentation.Menu;
import main.java.ma.wora.presentation.PartnerUi;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static PartnerRepository repository = null;

    static {
        try {
            repository = new PartnerRepositoryImpl();
        } catch (SQLException e) {

        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final Menu menu = new Menu();
        final PartnerUi partnerUi = new PartnerUi(repository); // Reuse the static repository
        boolean running = true;

        while (running) {
            menu.displayMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 6 : partnerUi.changePartnerStatus();
                case 5 : partnerUi.removePartner();
                case 4 : partnerUi.updatePartner();
                case 3: partnerUi.addPartner();
                case 2: partnerUi.displayPartnerByName();
                case 1:
                    partnerUi.displayAllPartners();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number. Please try again.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
