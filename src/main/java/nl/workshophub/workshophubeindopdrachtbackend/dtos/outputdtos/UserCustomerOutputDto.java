package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCustomerOutputDto {

    public Long id;
    public String firstName;
    public String lastName;
    public String email;

    public Boolean workshopOwner;





}
