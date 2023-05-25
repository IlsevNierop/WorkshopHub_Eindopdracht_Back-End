package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;

import java.time.LocalDate;
import java.time.LocalTime;

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
    public Float averageRatingWorkshopOwner;

    public String workshopCategory1;
    public String workshopCategory2;


    // public workshopImage --> list of verschillende variabelen?
    //public ArrayList<Review> workshopOwnerReviews;

    //owner output & admin output -- later in extra dto klasse plaatsen? nice to have
    public Boolean workshopVerified;

    public String feedbackAdmin;

    public Boolean publishWorkshop;


}