package main.java.ma.wora.presentation.ticket;

import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.TicketRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class TicketUi {
    private static final Scanner scanner = new Scanner(System.in);
    private final TicketRepository ticketRepository;

    public TicketUi(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void addTicket() {
        System.out.println("Enter Ticket ID (UUID):");
        UUID id = UUID.fromString(scanner.nextLine().trim());

        System.out.println("Enter Transport Type (e.g., BUS, TRAIN):");
        TransportType transportType = TransportType.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.println("Enter Purchase Price:");
        BigDecimal purchasePrice = new BigDecimal(scanner.nextLine().trim());

        System.out.println("Enter Selling Price:");
        BigDecimal sellingPrice = new BigDecimal(scanner.nextLine().trim());

        System.out.println("Enter Sale Date (YYYY-MM-DDTHH:MM:SS):");
        LocalDateTime saleDate = LocalDateTime.parse(scanner.nextLine().trim());

        System.out.println("Enter Status (e.g., SOLD, AVAILABLE):");
        TicketStatus status = TicketStatus.valueOf(scanner.nextLine().trim().toUpperCase());

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setTransportType(transportType);
        ticket.setPurchasePrice(purchasePrice);
        ticket.setSellingPrice(sellingPrice);
        ticket.setSaleDate(saleDate);
        ticket.setStatus(status);

        Ticket addedTicket = ticketRepository.add(ticket);
        if (addedTicket != null) {
            System.out.println("Ticket added successfully.");
            displayTicketDetails(addedTicket);
        } else {
            System.out.println("Failed to add ticket.");
        }
    }
    private void deleteTicket() {
        System.out.println("Enter Ticket ID to delete:");
        UUID ticketId = UUID.fromString(scanner.next().trim());
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket != null) {
            boolean deleted = ticketRepository.delete(ticket);
            if (deleted) {
                System.out.println("Ticket deleted successfully.");
            } else {
                System.out.println("Failed to delete ticket.");
            }
        } else {
            System.out.println("Ticket not found.");
        }
    }

    public void updateTicket() {
        System.out.println("Enter Ticket ID (UUID) to update:");
        UUID id = UUID.fromString(scanner.nextLine().trim());
        Ticket ticket = ticketRepository.findById(id);

        if (ticket != null) {
            System.out.println("Enter new Transport Type (e.g., BUS, TRAIN):");
            ticket.setTransportType(TransportType.valueOf(scanner.nextLine().trim().toUpperCase()));

            System.out.println("Enter new Purchase Price:");
            ticket.setPurchasePrice(new BigDecimal(scanner.nextLine().trim()));

            System.out.println("Enter new Selling Price:");
            ticket.setSellingPrice(new BigDecimal(scanner.nextLine().trim()));

            System.out.println("Enter new Sale Date (YYYY-MM-DDTHH:MM:SS):");
            ticket.setSaleDate(LocalDateTime.parse(scanner.nextLine().trim()));

            System.out.println("Enter new Status (e.g., SOLD, AVAILABLE):");
            ticket.setStatus(TicketStatus.valueOf(scanner.nextLine().trim().toUpperCase()));

            Ticket updatedTicket = ticketRepository.update(ticket);
            if (updatedTicket != null) {
                System.out.println("Ticket updated successfully.");
                displayTicketDetails(updatedTicket);
            } else {
                System.out.println("Failed to update ticket.");
            }
        } else {
            System.out.println("Ticket not found.");
        }
    }
    private void listAllTickets() {
        for (Ticket ticket : ticketRepository.findAll()) {
            TicketUi.displayTicketDetails(ticket);
        }

    }
    private void getTicketById() {
        System.out.println("Enter Ticket ID:");
        UUID ticketId = UUID.fromString(scanner.next().trim());
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket != null) {
            TicketUi.displayTicketDetails(ticket);
        } else {
            System.out.println("Ticket not found.");
        }
    }
    public static void displayTicketDetails(Ticket ticket) {
        System.out.println("Ticket Details:");
        System.out.println("ID: " + ticket.getId());
        System.out.println("Transport Type: " + ticket.getTransportType());
        System.out.println("Purchase Price: " + ticket.getPurchasePrice());
        System.out.println("Selling Price: " + ticket.getSellingPrice());
        System.out.println("Sale Date: " + ticket.getSaleDate());
        System.out.println("Status: " + ticket.getStatus());
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("That's not a valid number. Please try again.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    public void displayTicketMenu() {
        boolean running = true;

        while (running) {
            System.out.println("--- Ticket Management ---");
            System.out.println("1. Insert Ticket");
            System.out.println("2. Get Ticket by ID");
            System.out.println("3. Update Ticket");
            System.out.println("4. Delete Ticket");
            System.out.println("5. List All Tickets");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1 -> addTicket();
                case 2 -> getTicketById();
                case 3 -> updateTicket();
                case 4 -> deleteTicket();
                case 5 -> listAllTickets();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
