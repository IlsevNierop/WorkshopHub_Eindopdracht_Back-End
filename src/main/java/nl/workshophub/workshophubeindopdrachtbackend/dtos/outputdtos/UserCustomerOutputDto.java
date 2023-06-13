package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class UserCustomerOutputDto {

    public String firstName;
    public String lastName;
    public String email;

    public Boolean workshopOwner;





}
