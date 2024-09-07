package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Client;

public interface ClientRepository {
    Client createClient(Client client);
    Client authenticate(String last_name, String email);
    Client getClientByEmail(String email);
    Client updateClient(Client client);
}
