package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;

import java.util.Set;

public class UserWorkshopOwnerOutputDto {

    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String companyName;
    public String kvkNumber;
    public String vatNumber;
    public Boolean workshopOwnerVerified;

    public Boolean workshopOwner;
    public Double averageRatingReviews;
    public String profilePicUrl;

    @JsonSerialize
    public Set<Authority> authorities;

}
