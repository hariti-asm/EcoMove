package main.java.ma.wora.presentation.client;

import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

import java.util.Scanner;

public class ClientUi {
final ClientRepository clientRepository;
public ClientUi(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
}
    private final Scanner scanner = new Scanner(System.in);

public Client createClient( Client client ) {
    System.out.println("#-------------- WELCOME TO ECOMOVE PLATEFORME --------------# ");
    System.out.println("DO YOU ALREADY HAVE AN ACCOUNT ? (type yes or no");
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("yes")) {
        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
      clientRepository.authenticate(lastName, email);
    }
    if (input.equalsIgnoreCase("no")) {
        System.out.println("Please enter your first  name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your phone number:: ");
        String phoneNumber = scanner.nextLine();
        clientRepository.createClient(client);
    }
    return  null;

}

}
