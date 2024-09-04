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
    private Contract contract;

    public Ticket() {
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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
