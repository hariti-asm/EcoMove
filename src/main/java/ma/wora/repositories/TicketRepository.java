package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketRepository {

    Ticket findById(UUID id);
    Ticket update(Ticket ticket);
    boolean delete(Ticket ticket);
    List<Ticket> findAll();
    Ticket add(Ticket ticket);
}
