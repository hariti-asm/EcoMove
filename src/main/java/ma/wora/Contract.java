package main.java.ma.wora;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Contract {
    UUID id;
    Date startDate;
    Date endDate;
    BigDecimal specialRate;
    String agreementConditions;
    boolean renewable;
    ContractStatus status;
    Partner partner;
    DiscountType discountType;
    UUID getId() {
        return id;
    }

    Date getStartDate() {
        return startDate;
    }

    void setStartDate(Date date) {
        this.startDate = date;
    }

    Date getEndDate() {
        return endDate;
    }

    void setEndDate(Date date) {
        this.endDate = date;
    }

    BigDecimal getSpecialRate() {
        return specialRate;
    }

    void setSpecialRate(BigDecimal rate) {
        this.specialRate = rate;
    }

    String getAgreementConditions() {
        return agreementConditions;
    }

    void setAgreementConditions(String conditions) {
        this.agreementConditions = conditions;
    }

    boolean isRenewable() {
        return renewable;
    }

    void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    ContractStatus getStatus() {
        return status;
    }

    void setStatus(ContractStatus status) {
        this.status = status;
    }

    Partner getPartner() {
        return partner;
    }

    void setPartner(Partner partner) {
        this.partner = partner;
    }

}
