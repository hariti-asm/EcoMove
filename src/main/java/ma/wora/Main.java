package main.java.ma.wora;

import main.java.ma.wora.impl.PromotionRepositoryImpl;
import main.java.ma.wora.impl.TicketRepositoryImpl;
import main.java.ma.wora.presentation.contract.ContractUi;
import main.java.ma.wora.presentation.partner.PartnerUi;
import main.java.ma.wora.impl.ContractRepositoryImpl;
import main.java.ma.wora.impl.PartnerRepositoryImpl;
import main.java.ma.wora.presentation.promotion.PromotionMenu;
import main.java.ma.wora.presentation.promotion.PromotionUi;
import main.java.ma.wora.presentation.ticket.TicketUi;
import main.java.ma.wora.repositories.ContractRepository;
import main.java.ma.wora.repositories.PartnerRepository;
import main.java.ma.wora.repositories.PromotionRepository;
import main.java.ma.wora.repositories.TicketRepository;

import java.sql.SQLException;
import java.util.Scanner;



public class Main {

    private static PartnerRepository partnerRepository;
    private static ContractRepository contractRepository;
    private static PromotionRepository promotionRepository;
    private static TicketRepository ticketRepository;

    static {
        try {
            partnerRepository = new PartnerRepositoryImpl();
            contractRepository = new ContractRepositoryImpl(partnerRepository);
            ticketRepository = new TicketRepositoryImpl();
            promotionRepository = new PromotionRepositoryImpl();
        } catch (SQLException e) {
            System.err.println("Failed to initialize repositories: " + e.getMessage());
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        final PartnerUi partnerUi = new PartnerUi(partnerRepository);
        final ContractUi contractUi = new ContractUi(contractRepository, partnerRepository);
          final TicketUi ticketUi = new TicketUi(ticketRepository);
          final  PromotionUi promotionUi = new PromotionUi(promotionRepository);
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> partnerUi.displayPartnerMenu();
                case 2 -> contractUi.displayContractMenu();
                case 3 -> ticketUi.displayTicketMenu();
                case 4 -> promotionUi.displayPromotionMenu();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
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
    private static void displayMainMenu() {
        System.out.println("--- Management System ---");
        System.out.println("1. Partner Management");
        System.out.println("2. Contract Management");
        System.out.println("3. Ticket Management");
        System.out.println("4. Promotion Management");


        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}
