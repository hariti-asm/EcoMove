package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Reservation;

import java.util.UUID;

public interface ReservationRepository {
    Void ConfirmReservation(Reservation reservation);
    Void CancelReservation(Reservation reservation);
}

