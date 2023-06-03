package nl.workshophub.workshophubeindopdrachtbackend.methods;

import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;

public class AvailableSpotsCalculation {

    public static int getAvailableSpotsWorkshop(Workshop workshop) {
        int spotsBooked = 0;
        for (Booking b : workshop.getWorkshopBookings()) {
            spotsBooked += b.getAmount();
        }
        return (workshop.getAmountOfParticipants() - spotsBooked);
    }
}

