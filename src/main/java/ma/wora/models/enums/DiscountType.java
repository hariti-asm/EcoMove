package main.java.ma.wora.models.enums;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public enum DiscountType {

PERCENTAGE,FIXED_AMOUNT;

    public static class Contract {
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

        public static class Partner {
            private UUID id;
            private String companyName;
            private TransportType transportType;
            private String geographicalZone;
            private String specialConditions;
            private PartnerStatus status;
            private Date creationDate;

            public UUID getId() {
                return id;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String name) {
                this.companyName = name;
            }

            public String getCommercialContact() {
                return null;
            }

            public void setCommercialContact(String contact) {
            }

            public TransportType getTransportType() {
                return transportType;
            }

            public void setTransportType(TransportType type) {
                this.transportType = type;
            }

            public String getGeographicalZone() {
                return geographicalZone;
            }

            public void setGeographicalZone(String zone) {
                this.geographicalZone = zone;
            }

            public String getSpecialConditions() {
                return specialConditions;
            }

            public void setSpecialConditions(String conditions) {
                this.specialConditions = conditions;
            }

            public PartnerStatus getStatus() {
                return status;
            }

            public void setStatus(PartnerStatus status) {
                this.status = status;
            }

            public Date getCreationDate() {
                return creationDate;
            }




        }
    }
}
