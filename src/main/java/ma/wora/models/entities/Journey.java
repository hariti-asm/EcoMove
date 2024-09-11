package main.java.ma.wora.models.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Journey {

    private UUID id;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String departureStation;
    private String arrivalStation;
    private List<Ticket> tickets ;

    // Constructor
    public Journey(UUID id, Timestamp departureTime, Timestamp arrivalTime, String departureStation, String arrivalStation, List<Ticket> tickets) {
        this.id = id;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.tickets = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", departureStation='" + departureStation + '\'' +
                ", arrivalStation='" + arrivalStation + '\'' +
                '}';
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
