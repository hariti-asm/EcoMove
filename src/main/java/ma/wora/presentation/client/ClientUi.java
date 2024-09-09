package main.java.ma.wora.presentation.client;

import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.services.ClientService;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ClientUi {
    private final ClientService clientService;
    private final Scanner scanner = new Scanner(System.in);

    public ClientUi(ClientService clientService) {
        this.clientService = clientService;
    }

    // Method to create a new client
    public boolean createClient() {
        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your phone number: ");
        String phoneNumber = scanner.nextLine();

        Client newClient = new Client(UUID.randomUUID(), firstName, lastName, email, phoneNumber);
        clientService.createClient(newClient);
        System.out.println("Account successfully created. Welcome, " + firstName + "!");

        return true;
    }

    public boolean authenticateUser() {
        System.out.println("Do you already have an account? (yes/no)");
        String hasAccount = scanner.nextLine();

        if (hasAccount.equalsIgnoreCase("yes")) {
            System.out.println("Please enter your last name: ");
            String lastName = scanner.nextLine();
            System.out.println("Please enter your email: ");
            String email = scanner.nextLine();

            Optional<Client> clientOpt = clientService.getClientByEmail(email);

            if (clientOpt.isEmpty()) {
                System.out.println("Email doesn't exist or is invalid.");
                return false;
            }

            Client client = clientOpt.get();
            if (client.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println("Welcome back, " + client.getFirstName() + "!");
                return true;
            } else {
                System.out.println("Authentication failed. Last name does not match.");
                return false;
            }
        } else if (hasAccount.equalsIgnoreCase("no")) {
            System.out.println("Would you like to create an account? (yes/no)");
            String createAccount = scanner.nextLine();

            if (createAccount.equalsIgnoreCase("yes")) {
                return createClient();
            } else {
                System.out.println("Authentication failed. No account found.");
                return false;
            }
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            return authenticateUser();
        }
    }

    public Client updateClient() {
        System.out.println("Please enter your email to update: ");
        String email = scanner.nextLine();
        Optional<Client> clientOpt = clientService.getClientByEmail(email);

        if (clientOpt.isEmpty()) {
            System.out.println("Invalid email address. Please try again.");
            return null;
        }

        Client client = clientOpt.get();

        System.out.println("Please enter your first name (or press Enter to keep current: " + client.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) {
            firstName = client.getFirstName();
        }

        System.out.println("Please enter your last name (or press Enter to keep current: " + client.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) {
            lastName = client.getLastName();
        }

        System.out.println("Please enter your email (or press Enter to keep current: " + client.getEmail() + "): ");
        String newEmail = scanner.nextLine();
        if (newEmail.isEmpty()) {
            newEmail = client.getEmail();
        }

        System.out.println("Please enter your phone number (or press Enter to keep current: " + client.getPhone() + "): ");
        String phoneNumber = scanner.nextLine();
        if (phoneNumber.isEmpty()) {
            phoneNumber = client.getPhone();
        }

        Client updatedClient = new Client(client.getId(), firstName, lastName, newEmail, phoneNumber);

        clientService.updateClient(updatedClient);
        System.out.println("Client updated successfully.");

        return updatedClient;
    }

    public Optional<Client> getClient() {
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        Optional<Client> clientOpt = clientService.getClientByEmail(email);

        if (clientOpt.isEmpty()) {
            System.out.println("Client not found.");
            return Optional.empty();
        }

        return clientOpt;
    }

    public void displayClientMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to ECOMOVE!");
            System.out.println("1. Update Your Account");
            System.out.println("2. Visit your account");
            System.out.println("3.Search Ticket");
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> updateClient();
                case 2 -> getClient();
                default -> {
                    System.out.println("Invalid option. Exiting...");
                    running = false;
                }
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
