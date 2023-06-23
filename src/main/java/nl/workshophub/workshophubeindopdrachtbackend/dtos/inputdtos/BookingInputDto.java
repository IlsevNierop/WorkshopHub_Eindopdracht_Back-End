package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Min;

public class BookingInputDto {

    public String commentsCustomer;

    @Min(value=1, message = "You need to book at least 1 spot.")
    public int amount;

    public Long workshopId;




}
