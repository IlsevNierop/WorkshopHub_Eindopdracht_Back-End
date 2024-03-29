package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


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
    public int spotsAvailable;
    public String workshopCategory1;
    public String workshopCategory2;
    public Boolean workshopVerified;
    public String feedbackAdmin;
    public Boolean publishWorkshop;
    public Long workshopOwnerId;
    public String workshopOwnerCompanyName;
    public Double averageRatingWorkshopOwnerReviews;
    public Double numberOfReviews;
    public Boolean isFavourite;
    public int amountOfFavsAndBookings;
    public String workshopPicUrl;
    public List<ReviewOutputDto> workshopOwnerReviews;

}
