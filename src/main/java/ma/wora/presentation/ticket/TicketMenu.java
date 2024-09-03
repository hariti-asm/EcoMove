package main.java.ma.wora.presentation.ticket;

import main.java.ma.wora.models.entities.Ticket;

public class TicketMenu {
    public void displayTicketMenu(Ticket ticket) {
        System.out.println("--- Ticket Management ---");
        System.out.println("1. Insert Ticket");
        System.out.println("2. Get Ticket by ID");
        System.out.println("3. Update Ticket");
        System.out.println("4. Delete Ticket");
        System.out.println("5. List All Tickets");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }
    }








