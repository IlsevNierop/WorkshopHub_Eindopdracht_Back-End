package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;

import java.util.Set;

public class UserWorkshopOwnerInputDto {

    @NotBlank (message = "Firstname field shouldn't be empty.")
    public String firstName;
    @NotBlank (message = "Lastname field shouldn't be empty.")
    public String lastName;
    @NotBlank (message = "Email field shouldn't be empty.")
    @Email (regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "This email doesn't meet e-mail requirements (@ symbol and .com/nl etc)")
    public String email;

//    @NotBlank (message = "Password field shouldn't be empty.")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\@\\$\\%\\&\\/\\(\\)\\=\\?\\*\\-\\+\\_\\.\\:\\;\\,\\{\\}\\^])[A-Za-z0-9!#@$%&/()=?*+-_.:;,{}]{8,20}", message = "Password needs to contain the following: " +
            "1. Minimum of 1 lowercase letter. 2. Minimum of 1 uppercase letter. 3. Minimum of 1 number 4. Minimum of 1 symbol. 5. It should be between 8 and 20 characters long.")
    public String password;
    @NotBlank (message = "Companyname field shouldn't be empty.")
    public String companyName;
    @Positive(message = "KvK number shouldn't be empty.")
    public int kvkNumber;
    @NotBlank (message = "BTW number shouldn't be empty.")
    public String vatNumber;

    // deze kan alleen geset worden via verify put methode met request param
//    public Boolean workshopOwnerVerified;

    @NotNull(message= "You should choose between workshop owner or customer.")
    public Boolean workshopOwner;

    //security
    public Boolean enabled;
    public String apikey;

    @JsonSerialize
    public Set<Authority> authorities;
}
