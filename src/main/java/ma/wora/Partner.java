package main.java.ma.wora;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Partner {
    private UUID id;
    private String companyName;
    private TransportType transportType;
    private String geographicalZone;
    private String specialConditions;
    private PartnerStatus status;
    private Date creationDate;
    private List<Contract> contracts; // List to store contracts

    public Partner() {
        this.contracts = new ArrayList<>();
    }

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

    public void addContract(Contract contract) {
        contracts.add(contract);
    }

    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }

    public List<Contract> getContracts() {
        return contracts;
    }


}
