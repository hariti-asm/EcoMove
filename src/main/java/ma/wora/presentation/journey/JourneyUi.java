package main.java.ma.wora.presentation.journey;

import main.java.ma.wora.services.JourenyService;
import main.java.ma.wora.services.PartnerService;

import java.util.Scanner;

public class JourneyUi {
    private final JourenyService journeyService;
    private final Scanner scanner = new Scanner(System.in);
    public JourneyUi(JourenyService journeyService) {
        this.journeyService = journeyService;
    }
}
