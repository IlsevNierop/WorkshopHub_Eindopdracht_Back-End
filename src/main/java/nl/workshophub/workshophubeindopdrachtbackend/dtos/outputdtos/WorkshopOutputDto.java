package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//getters en setters nodig voor modelmapper
@Getter
@Setter
public class WorkshopOutputDto {

    public Long id;
    public String title;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public double price;
    public InOrOutdoors inOrOutdoors;
    public String location;
    public String highlightedInfo;
    public String description;
    public int amountOfParticipants;

    private int spotsAvailable;
    public String workshopCategory1;
    public String workshopCategory2;


    // public workshopImage --> list of verschillende variabelen?


    //owner output & admin output -- later in extra dto klasse plaatsen? nice to have
    public Boolean workshopVerified;

    public String feedbackAdmin;

    public Boolean publishWorkshop;

    public List<Booking> workshopBookings;

    // niet gehele reviews teruggeven? Alleen de rating, firstname, workshoptitle?
    public List<Review> workshopOwnerReviews;
//    public Float averageRatingWorkshopOwnerReviews;

    // ook sumiere output dto maken voor de kalender overview - daar ook bedrijfsnaam toevoegen?


}
