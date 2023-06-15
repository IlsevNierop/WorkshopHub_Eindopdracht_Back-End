package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWorkshopOwnerOutputDto {

    public Long id;

    public String firstName;
    public String lastName;
    public String email;
//    public String password;
    public String companyName;
    public int kvkNumber;
    public String vatNumber;
    public Boolean workshopOwnerVerified;

    public Boolean workshopOwner;
    public Double averageRatingReviews;

}
