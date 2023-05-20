package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;

import java.time.LocalDate;
import java.time.LocalTime;

//getters en setters nodig voor modelmapper
@Getter
@Setter
public class WorkshopOutputDtoDefault {

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
    public String workshopCategory;

    public Float averageRatingWorkshopOwner;

    // public workshopImage --> list of verschillende variabelen?
    //public ArrayList<Review> workshopOwnerReviews;


    // public String (?/) workshopTheme;


    //owner output & admin output
   //  public Boolean workshopVerified;
    //
    //    public String feedbackAdmin;
    //
    // public Boolean publishWorkshop;


}
