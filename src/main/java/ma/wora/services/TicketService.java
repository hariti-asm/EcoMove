package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.TicketRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TicketService {

    private  final TicketRepository ticketRepository;
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket addTicket(Ticket ticket) {
        return ticketRepository.add(ticket);
    }
    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.update(ticket);
    }
    public boolean deleteTicket(Ticket ticket) {
        return  ticketRepository.delete(ticket);
    }
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    public Ticket getTicketById(UUID id) {
        return ticketRepository.findById(id);
    }
    public Ticket updateTicket(UUID id, String transportType, BigDecimal purchasePrice, BigDecimal sellingPrice, String saleDate, String status) {
        Ticket ticket = ticketRepository.findById(id);

        if (ticket != null) {
            if (transportType != null && !transportType.isEmpty()) {
                ticket.setTransportType(TransportType.valueOf(transportType.toUpperCase()));
            }
            if (purchasePrice != null) {
                ticket.setPurchasePrice(purchasePrice);
            }
            if (sellingPrice != null) {
                ticket.setSellingPrice(sellingPrice);
            }
            if (saleDate != null && !saleDate.isEmpty()) {
                ticket.setSaleDate(Date.valueOf(LocalDate.parse(saleDate)));
            }
            if (status != null && !status.isEmpty()) {
                ticket.setStatus(TicketStatus.valueOf(status.toUpperCase()));
            }

            return ticketRepository.update(ticket);
        }
        return null;
    }
}
