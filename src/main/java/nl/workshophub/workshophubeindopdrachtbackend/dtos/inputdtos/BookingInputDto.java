package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Min;

public class BookingInputDto {

    public String commentsCustomer;

    @Min(value=1, message = "Aantal plekjes moet meer dan 1 zijn")
    public int amount;

    public Long workshopId;




}
