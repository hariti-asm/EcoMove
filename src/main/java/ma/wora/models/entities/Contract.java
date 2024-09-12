package main.java.ma.wora.models.entities;

import main.java.ma.wora.models.enums.ContractStatus;
import main.java.ma.wora.models.enums.DiscountType;

import java.math.BigDecimal;
import java.sql.Date; // Use java.sql.Date for SQL operations
import java.util.UUID;

public class Contract {
    private UUID id;
    private Date startDate;
    private Date endDate;
    private BigDecimal specialRate;
    private String agreementConditions;
    private boolean renewable;
    private ContractStatus status;
    private Partner partner;
    private  DiscountType discountType;
private BigDecimal value;
    public Contract(UUID id, Date startDate, Date endDate, BigDecimal specialRate, String agreementConditions,
                    boolean renewable, ContractStatus status, Partner partner , DiscountType discountType , BigDecimal value) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.specialRate = specialRate;
        this.agreementConditions = agreementConditions;
        this.renewable = renewable;
        this.status = status;
        this.partner = partner;
        this.discountType = discountType;
        this.value = value;

    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSpecialRate() {
        return specialRate;
    }

    public void setSpecialRate(BigDecimal specialRate) {
        this.specialRate = specialRate;
    }

    public String getAgreementConditions() {
        return agreementConditions;
    }

    public void setAgreementConditions(String agreementConditions) {
        this.agreementConditions = agreementConditions;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
