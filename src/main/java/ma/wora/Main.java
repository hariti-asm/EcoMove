package main.java.ma.wora;

import main.java.ma.wora.impl.*;
import main.java.ma.wora.presentation.contract.ContractUi;
import main.java.ma.wora.presentation.partner.PartnerUi;
import main.java.ma.wora.impl.ContractRepositoryImpl;
import main.java.ma.wora.impl.PartnerRepositoryImpl;
import main.java.ma.wora.presentation.promotion.PromotionUi;
import main.java.ma.wora.presentation.client.ClientUi;

import main.java.ma.wora.presentation.ticket.TicketUi;
import main.java.ma.wora.repositories.*;
import main.java.ma.wora.services.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static PartnerRepository partnerRepository;
    private static PartnerService partnerService;
    private static ContractRepository contractRepository;
    private static ContractService contractService;
    private static TicketService ticketService;
    private static TicketRepository ticketRepository;
    private static PromotionService promotionService;
    private static ClientRepository clientRepository;
    private static ClientService clientService;
    private static ReservationRepository reservationRepository;
    private static ReservationService reservationService;
    private static FavoriteRepository favoriteRepository;
    private static FavoriteService favoriteService;

    static {
        try {
            partnerRepository = new PartnerRepositoryImpl();
            clientRepository = new ClientRepositoryImpl();
            reservationRepository = new ReservationRepositoryImpl();
            contractRepository = new ContractRepositoryImpl(partnerRepository);
            partnerService = new PartnerService(partnerRepository);
            contractService = new ContractService(contractRepository);
            clientService = new ClientService(clientRepository);
            ticketRepository = new TicketRepositoryImpl(contractRepository);
            ticketService = new TicketService(ticketRepository);
            PromotionRepository promotionRepository = new PromotionRepositoryImpl();
            promotionService = new PromotionService(promotionRepository);
            favoriteRepository = new FavoriteRepositoryImpl(clientService);  // Add this line
            favoriteService = new FavoriteService(favoriteRepository);  // Add this line

        } catch (SQLException e) {
            System.err.println("Failed to initialize repositories: " + e.getMessage());
            System.exit(1);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final PartnerUi partnerUi = new PartnerUi(partnerService);
        final ContractUi contractUi = new ContractUi(contractRepository, partnerRepository, contractService);
        final ReservationService reservationService = new ReservationService(reservationRepository);
        final FavoriteService favoriteService = new FavoriteService(favoriteRepository);
        final TicketUi ticketUi = new TicketUi(ticketService, contractService, reservationService, clientService , favoriteService);
        final ClientUi clientUi = new ClientUi(clientService, ticketUi);
        final PromotionUi promotionUi = new PromotionUi(promotionService);

        boolean authenticated = false;

        while (!authenticated) {
            authenticated = clientUi.authenticateUser();
        }

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> partnerUi.displayPartnerMenu();
                case 2 -> contractUi.displayContractMenu();
                case 3 -> ticketUi.displayTicketMenu();
                case 4 -> promotionUi.displayPromotionMenu();
                case 5 -> clientUi.displayClientMenu();
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
        System.out.println("5. Client Management");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}
