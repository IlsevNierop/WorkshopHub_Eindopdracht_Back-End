package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationInputDto {

    public String email;
    public String password;

    public AuthenticationInputDto() {
    }
    public AuthenticationInputDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
