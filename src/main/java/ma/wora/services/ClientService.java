package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

import java.util.Optional;

public class ClientService {
    private static ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        ClientService.clientRepository = clientRepository;
    }

    private static final String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private boolean isEmailValid(String email) {
        return email.matches(emailRegex);
    }

    public boolean createClient(Client client) {
        String email = client.getEmail();

        if (!isEmailValid(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

        return clientRepository.createClient(client);
    }

    public Optional<Client> updateClient(Client client) {
        String email = client.getEmail();

        if (!isEmailValid(email)) {
            System.out.println("Invalid email format.");
            return Optional.empty();
        }

        return Optional.ofNullable(clientRepository.updateClient(client));
    }

    public Optional<Client> getClientByEmail(String email) {
        if (!isEmailValid(email)) {
            System.out.println("Invalid email format.");
            return Optional.empty();
        }

        return clientRepository.getClientByEmail(email);
    }

    public Optional<Client> getClientByFirstName(String firstName) {
        return clientRepository.getClientByFirstName(firstName);
    }

    public Client authenticate(String email, String lastName) {
        if (!isEmailValid(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        return clientRepository.authenticate(email, lastName);
    }
}
