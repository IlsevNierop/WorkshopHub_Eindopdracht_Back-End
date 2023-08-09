package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;

import java.util.Set;

public class UserCustomerInputDtoExclPassword {
    @NotBlank(message = "Firstname field shouldn't be empty.")
    public String firstName;
    @NotBlank (message = "Lastname field shouldn't be empty.")
    public String lastName;

    @NotBlank (message = "Email field shouldn't be empty.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "This email doesn't meet e-mail requirements (@ symbol and .com/nl etc)")
    public String email;

    @NotNull(message= "You should choose between workshop owner or customer.")
    public Boolean workshopOwner;


    @JsonSerialize
    public Set<Authority> authorities;
}
