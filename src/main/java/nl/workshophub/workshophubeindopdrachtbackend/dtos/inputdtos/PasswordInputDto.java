package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PasswordInputDto {

    @NotBlank (message = "Password field shouldn't be empty.")
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[\\!\\#\\@\\$\\%\\&\\/\\(\\)\\=\\?\\*\\-\\+\\_\\.\\:\\;\\,\\{\\}\\^])[A-Za-z0-9!#@$%&/()=?*+-_.:;,{}]{8,20}", message = "Password needs to contain the following: " +
            "1. Minimum of 1 lowercase letter. 2. Minimum of 1 uppercase letter. 3. Minimum of 1 number 4. Minimum of 1 symbol. 5. It should be between 8 and 20 characters long.")
    public String newPassword;
}
