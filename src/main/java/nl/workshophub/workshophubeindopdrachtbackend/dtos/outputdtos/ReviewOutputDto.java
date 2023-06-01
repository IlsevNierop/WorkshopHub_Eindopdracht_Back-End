package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewOutputDto {

    public Long id;
    public double rating;
    public String reviewDescription;
    public Boolean reviewVerified;
    public String feedbackAdmin;
    public String firstNameReviewer;
    public String companyNameWorkshopOwner;
    public double averageRatingWorkshopOwner;
    public Long workshopId;
    public String workshopTitle;

    //public User customer;
    //public User workshopOwner;
//    public String workshopOwnerCompanyName;
//    public Long workshopOwnerId;





}
