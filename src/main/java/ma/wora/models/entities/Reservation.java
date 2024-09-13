package main.java.ma.wora.models.entities;

import main.java.ma.wora.models.enums.ReservationStatus;

import java.util.List;
import java.util.UUID;

public class Reservation {
        private UUID id;
    private Client client;
    private List <Ticket> tickets;
    private ReservationStatus status;



    public Reservation( UUID id ,List<Ticket> tickets, Client client, ReservationStatus status) {
        this.id = id;
        this.tickets = tickets;
        this.client = client;
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

        public List<Ticket> getTickets() {
            return tickets;
        }

        public void setTickets(List<Ticket> tickets) {
            this.tickets = tickets;
        }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }


        @Override
        public String toString() {
            return "Reservation{" +
                    "client=" + client +
                    ", tickets=" + tickets +
                    ", status=" + status +
                    '}';
        }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
