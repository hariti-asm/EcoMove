package main.java.ma.wora.models.entities;

import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.models.enums.TransportType;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Partner {
    private UUID id;
    private String companyName;
    private TransportType transportType;
    private String geographicalZone;
    private String specialConditions;
    private PartnerStatus status;
    private Date creationDate;
    public Partner(UUID id, String companyName, TransportType transportType,
                   String geographicalZone, String specialConditions,
                   PartnerStatus status, Date creationDate) {
        this.id = id;
        this.companyName = companyName;
        this.transportType = transportType;
        this.geographicalZone = geographicalZone;
        this.specialConditions = specialConditions;
        this.status = status;
        this.creationDate = creationDate;
    }

    public Partner(UUID id, String companyName, String transportType, String geographicalZone, String specialConditions, String status, java.sql.Date creationDate) {
        this.id = id;
        this.companyName = companyName;
        this.transportType = TransportType.valueOf(transportType);
        this.geographicalZone = geographicalZone;
        this.specialConditions = specialConditions;
        this.status = PartnerStatus.valueOf(status);
        this.creationDate = creationDate;
    }




    public UUID getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public String getGeographicalZone() {
        return geographicalZone;
    }

    public String getSpecialConditions() {
        return specialConditions;
    }

    public PartnerStatus getStatus() {
        return status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public void setGeographicalZone(String geographicalZone) {
        this.geographicalZone = geographicalZone;
    }

    public void setSpecialConditions(String specialConditions) {
        this.specialConditions = specialConditions;
    }

    public void setStatus(PartnerStatus status) {
        this.status = status;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
