package main.java.ma.wora.repositories;


import main.java.ma.wora.models.entities.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    boolean createClient(Client client);
    Optional<Client> authenticate(String lastName, String email);
    Optional<Client> getClientByEmail(String email);
    Optional<Client> updateClient(Client client);
    Optional<Client> getClientByFirstName(String firstName);
    Optional<Client> getClientById(UUID id);
}
