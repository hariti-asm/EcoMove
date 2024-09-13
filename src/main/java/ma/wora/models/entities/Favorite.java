package main.java.ma.wora.models.entities;

import java.util.List;
import java.util.UUID;

public class Favorite {
    private UUID id;
    private Client client;
    private List<Journey> journeys;

    public Favorite(UUID id, Client client, List<Journey> journeys) {
        this.id = id;
        this.client = client;
        this.journeys = journeys;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
    }
}
