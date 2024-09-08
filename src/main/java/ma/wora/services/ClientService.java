package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

public class ClientService {
    private static ClientRepository clientRepository;
    public ClientService( ClientRepository clientRepository) {
        ClientService.clientRepository = clientRepository;
    }

    public boolean createClient(Client client) {
        return  clientRepository.createClient(client);
    }
    public Client updateClient(Client client) {
        return clientRepository.updateClient(client);
    }
    public Client getClientByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }
    public Client authenticate(String email, String last_name) {
        return clientRepository.authenticate(email, last_name);
    }
}
