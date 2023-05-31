package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class UserWorkshopOwnerInputDto {

    @NotBlank (message = "Voornaam kan niet leeg zijn")
    public String firstName;
    @NotBlank (message = "Achternaam kan niet leeg zijn")
    public String lastName;
    @NotBlank (message = "Email kan niet leeg zijn")
    @Email (regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    public String email;
    @NotBlank (message = "Wachtwoord kan niet leeg zijn")
    public String password;
    @NotBlank (message = "Bedrijfsnaam kan niet leeg zijn")
    public String companyName;
    @Positive(message = "KvK nummer kan niet leeg zijn")
    public int kvkNumber;
    @NotBlank (message = "BTW nummer kan niet leeg zijn")
    public String vatNumber;
    public Boolean workshopOwnerVerified;

    @NotNull
    public Boolean workshopOwner;
}
