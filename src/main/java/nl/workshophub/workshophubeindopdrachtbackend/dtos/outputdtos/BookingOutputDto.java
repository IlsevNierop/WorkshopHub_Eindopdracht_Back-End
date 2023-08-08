package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import java.time.LocalDate;


public class BookingOutputDto {

    public Long id;
    public LocalDate dateOrder;
    public String commentsCustomer;
    public int amount;
    public double totalPrice;

    public Long workshopId;
    public int spotsAvailableWorkshop;
    public String workshopTitle;
    public LocalDate workshopDate;

    public Long customerId;
    public String firstNameCustomer;

    public String lastNameCustomer;

    public String emailCustomer;

    public Boolean reviewCustomerWritten;


}
