package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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
