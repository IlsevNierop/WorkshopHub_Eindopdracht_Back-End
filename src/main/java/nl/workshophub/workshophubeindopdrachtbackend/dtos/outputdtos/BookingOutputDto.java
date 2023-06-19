package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;

import java.time.LocalDate;

@Getter
@Setter
public class BookingOutputDto {

    public Long id;
    public LocalDate dateOrder;
    public String commentsCustomer;
    public int amount;

    public Long workshopId;
    // add extra information? Maybe add another workshopoutputdto that contains few variables
    public String workshopTitle;
    public UserCustomerOutputDto customerOutputDto;


}
