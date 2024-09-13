package main.java.ma.wora.models.entities;

import java.util.List;
import java.util.UUID;

public class Client {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Reservation> reservations;

    public Client(UUID id, String firstName, String lastName, String email, String phone , List<Reservation> reservations) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.reservations = reservations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
