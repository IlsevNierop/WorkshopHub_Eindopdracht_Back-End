package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;

import java.util.Set;

public class UserWorkshopOwnerInputDtoExclPassword {

    @NotBlank(message = "Firstname field shouldn't be empty.")
    public String firstName;
    @NotBlank (message = "Lastname field shouldn't be empty.")
    public String lastName;
    @NotBlank (message = "Email field shouldn't be empty.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "This email doesn't meet e-mail requirements (@ symbol and .com/nl etc)")
    public String email;

    @NotBlank (message = "Companyname field shouldn't be empty.")
    public String companyName;
    @Digits(integer = 10, fraction = 0, message = "Please enter a valid number")
    public String kvkNumber;

    public String vatNumber;

    // deze kan alleen geset worden via verify put methode met request param
//    public Boolean workshopOwnerVerified;

    @NotNull(message= "You should choose between workshop owner or customer.")
    public Boolean workshopOwner;

    @JsonSerialize
    public Set<Authority> authorities;
}
