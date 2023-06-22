package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import java.time.LocalDate;


public class BookingOutputDto {

    public Long id;
    public LocalDate dateOrder;
    public String commentsCustomer;
    public int amount;

    public Long workshopId;
    public String workshopTitle;
    public UserCustomerOutputDto customerOutputDto;


}
