package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import java.time.LocalDate;


public class ReviewOutputDto {

    public Long id;
    public double rating;
    public String reviewDescription;
    public Boolean reviewVerified;
    public String feedbackAdmin;
    public String workshopTitle;
    public LocalDate workshopDate;
    public String firstNameReviewer;
    public String lastNameReviewer;
    public String companyNameWorkshopOwner;


}
