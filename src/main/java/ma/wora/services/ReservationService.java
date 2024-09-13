package main.java.ma.wora.services;

import main.java.ma.wora.models.entities.Reservation;
import main.java.ma.wora.repositories.ClientRepository;
import main.java.ma.wora.repositories.ReservationRepository;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public void cancelReservation(Reservation reservation) {
          reservationRepository.CancelReservation( reservation);
    }
    public void confirmReservation(Reservation reservation) {
        reservationRepository.ConfirmReservation( reservation);
    }

}
