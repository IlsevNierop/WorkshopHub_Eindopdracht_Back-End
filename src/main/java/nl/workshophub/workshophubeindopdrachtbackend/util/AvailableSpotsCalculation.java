package nl.workshophub.workshophubeindopdrachtbackend.util;

import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import org.springframework.stereotype.Component;

@Component
public class AvailableSpotsCalculation {

    public int getAvailableSpotsWorkshop(Workshop workshop) {
        int spotsBooked = 0;
        if (workshop.getWorkshopBookings() != null){
        for (Booking b : workshop.getWorkshopBookings()) {
            spotsBooked += b.getAmount();
        }}
        return (workshop.getAmountOfParticipants() - spotsBooked);
    }
}

