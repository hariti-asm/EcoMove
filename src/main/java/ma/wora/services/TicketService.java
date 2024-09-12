package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Ticket;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.TicketRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<Ticket> searchTicketByDestination(String departurePoint, String arrivalPoint, LocalDate departureTime) {
        if (departurePoint == null || departurePoint.isEmpty()) {
            throw new IllegalArgumentException("Departure point cannot be empty");
        }
        if (arrivalPoint == null || arrivalPoint.isEmpty()) {
            throw new IllegalArgumentException("Arrival point cannot be empty");
        }
        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time cannot be null");
        }

        List<Ticket> tickets = ticketRepository.searchTicketByDestination(departurePoint, arrivalPoint, departureTime);

        return tickets.stream()
                .map(ticket -> {
                    BigDecimal calculatedTotalPrice = CalculatePrice(ticket);
                    return new Ticket(
                            ticket.getId(),
                            ticket.getTransportType(),
                            ticket.getPurchasePrice(),
                            calculatedTotalPrice,
                            ticket.getSaleDate(),
                            ticket.getStatus(),
                            ticket.getDiscount(),
                            ticket.getContract(),
                            ticket.getJourney()
                    );
                })
                .collect(Collectors.toList());
    }

    public BigDecimal CalculatePrice(Ticket ticket) {
        BigDecimal price = ticket.getSellingPrice();
        BigDecimal discountedPrice = price.subtract(ticket.getDiscount());

        if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
            discountedPrice = BigDecimal.ZERO;
        }

        DiscountType discountType = ticket.getContract().getDiscountType();
        BigDecimal discountValue = ticket.getContract().getValue();

        if (discountType == DiscountType.AMOUNT) {
            return discountedPrice.subtract(discountValue).max(BigDecimal.ZERO);
        } else if (discountType == DiscountType.PERCENTAGE) {
            BigDecimal percentageDiscount = price.multiply(discountValue).divide(BigDecimal.valueOf(100));
            return discountedPrice.subtract(percentageDiscount).max(BigDecimal.ZERO);
        }

        return discountedPrice;
    }

}
