package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;

import java.util.Set;

public class UserCustomerOutputDto {

    public Long id;
    public String firstName;
    public String lastName;
    public String email;

    public Boolean workshopOwner;

    public String profilePicUrl;

    //security
    public Boolean enabled;
//    public String apikey;

    @JsonSerialize
    public Set<Authority> authorities;





}
