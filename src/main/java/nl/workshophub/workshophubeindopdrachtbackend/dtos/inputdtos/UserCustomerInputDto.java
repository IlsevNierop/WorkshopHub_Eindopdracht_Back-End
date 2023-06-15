package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserCustomerInputDto {

    @NotBlank(message = "Voornaam kan niet leeg zijn")
    public String firstName;
    @NotBlank (message = "Achternaam kan niet leeg zijn")
    public String lastName;
    @NotBlank (message = "Email kan niet leeg zijn")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    public String email;
    @NotBlank (message = "Wachtwoord kan niet leeg zijn")
    //nog validatie toevoegen
    public String password;

    public Boolean workshopOwner;
}
