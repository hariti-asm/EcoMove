package main.java.ma.wora.models.entities;

import main.java.ma.wora.models.enums.TicketStatus;
import main.java.ma.wora.models.enums.TransportType;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private TransportType transportType;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Date saleDate;  // Use java.sql.Date
    private TicketStatus status;
    private Integer discount;
    private UUID contract;
    private  Journey journey;
    public Ticket() {
    }

    public Ticket(UUID id, TransportType transportType, BigDecimal purchasePrice, BigDecimal sellingPrice, Date saleDate, TicketStatus status, int discount, UUID contract, Journey journey) {
        this.id = id;
        this.transportType = transportType;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.saleDate = saleDate;
        this.status = status;
        this.discount = discount;
        this.contract = contract;
        this.journey = journey;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Date getSaleDate() {
        return saleDate;  // No need for casting
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public UUID getContract() {
        return contract;
    }

    public void setContract(UUID contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {
        return "Ticket {" + "\n" +
                "  id: " + id + ",\n" +
                "  transportType: " + transportType + ",\n" +
                "  purchasePrice: " + purchasePrice + ",\n" +
                "  sellingPrice: " + sellingPrice + ",\n" +
                "  saleDate: " + saleDate + ",\n" +
                "  status: " + status + ",\n" +
                "  discount: " + discount + ",\n" +
                "  contract: " + contract + ",\n" +
                "  journey: " + journey + "\n" +
                "}";
    }

}
