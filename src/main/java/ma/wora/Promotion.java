package main.java.ma.wora;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Promotion {
    private UUID id;
    private String offerName;
    private String description;
    private Date startDate;
    private Date endDate;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private String conditions;
    private PromotionStatus status;
    private Partner partner;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date date) {
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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public boolean isActive() {
        Date currentDate = new Date();
        return startDate != null && endDate != null && currentDate.after(startDate) && currentDate.before(endDate);
    }
}
