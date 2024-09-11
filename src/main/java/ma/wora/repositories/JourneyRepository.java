package main.java.ma.wora.repositories;

import main.java.ma.wora.models.entities.Journey;

import java.time.LocalDate;
import java.util.List;

public interface JourneyRepository {
    void createJourney(Journey journey);
    List<Journey> displayAllJourneys();
    List<Journey> searchJourneys(String startLocation, String endLocation, LocalDate departureDate);

}
