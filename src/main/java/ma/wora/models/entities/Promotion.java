package main.java.ma.wora.models.entities;

import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.models.enums.PromotionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Promotion {
    private UUID id;
    private String offerName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private String conditions;
    private PromotionStatus status;
    private  UUID contract;

    public Promotion(UUID id , String offerName , String description, LocalDate startDate, LocalDate endDate, DiscountType discountType, BigDecimal discountValue, PromotionStatus status, String conditions , UUID contract) {
        this.id = id;
        this.offerName = offerName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.status = status;
        this.conditions = conditions;
        this.contract = contract;
    }



    public UUID getContract() {
        return contract;
    }

    public void setContract(UUID contract) {
        this.contract = contract;
    }



    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String name) {
        this.offerName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate date) {
        this.startDate = date;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate date) {
        this.endDate = date;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType type) {
        this.discountType = type;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal value) {
        this.discountValue = value;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        this.status = status;
    }

}
