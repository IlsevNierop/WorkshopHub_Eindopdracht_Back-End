package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    //validatie gaat niet goed, check symbolen
    @NotBlank (message = "Wachtwoord kan niet leeg zijn")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\%\\^])[A-Za-z0-9!#%]{8,20}", message = "Wachtwoord moet aan de volgende eisen voldoen:" +
            "1. Minstens 1 kleine letter. 2. Minstens 1 hoofdletter. 3. Minstens 1 nummer 4. Minstens 1 van de volgende symbolen bevatten: !, # of %. 5. Het moet tussen de 8 en 20 karakters lang zijn.")

    public String password;

    @NotNull(message= "Je moet aangeven of je een workshop eigenaar of klant bent")
    public Boolean workshopOwner;
}
