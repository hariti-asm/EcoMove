package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

import java.util.Optional;
import java.util.UUID;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
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

        return clientRepository.updateClient(client);
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

    public Optional<Client> authenticate(String email, String lastName) {
        if (!isEmailValid(email)) {
            System.out.println("Invalid email format.");
            return Optional.empty();
        }

        return clientRepository.authenticate(lastName, email);
    }
    public Optional<Client> getClientById(UUID clientId) {
        return clientRepository.getClientById(clientId);
    }
}
