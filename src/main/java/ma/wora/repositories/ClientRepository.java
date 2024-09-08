package main.java.ma.wora.repositories;


import main.java.ma.wora.models.entities.Client;

import java.util.Optional;

public interface ClientRepository {
    boolean createClient(Client client);
    Client authenticate(String last_name, String email);
    Optional<Client> getClientByEmail(String email);
    Client updateClient(Client client);
    Optional<Client> getClientByFirstName(String first_name);
}
