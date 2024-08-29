package main.java.ma.wora;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private TransportType transportType;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private LocalDateTime saleDate;
    private TicketStatus status;
    private Promotion promotion;
    private Integer discount;
    UUID getId() {
        return id;
    }

    TransportType getTransportType() {
        return transportType;
    }

    void setTransportType(TransportType type) {
        this.transportType = type;
    }

    BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    void setPurchasePrice(BigDecimal price) {
        this.purchasePrice = price;
    }

    BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    void setSellingPrice(BigDecimal price) {
        this.sellingPrice = price;
    }

    LocalDateTime getSaleDate() {
        return saleDate;
    }

    void setSaleDate(LocalDateTime date) {
        this.saleDate = date;
    }

    TicketStatus getStatus() {
        return status;
    }

    void setStatus(TicketStatus status) {
        this.status = status;
    }

    Promotion getPromotion() {
        return promotion;
    }

    void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    BigDecimal calculateProfit() {
        return sellingPrice.subtract(purchasePrice);
    }
}
