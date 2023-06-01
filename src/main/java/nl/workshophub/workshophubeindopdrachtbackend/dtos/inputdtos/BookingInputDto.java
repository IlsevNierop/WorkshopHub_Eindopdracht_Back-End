package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingInputDto {

    // check of automatisch toegewezen kan worden aan vandaag
    @NotNull (message = "Datum van boeking mag niet leeg zijn")
    public LocalDate dateOrder;

    public String commentsCustomer;

    @Min(value=1, message = "Aantal plekjes moet meer dan 1 zijn")
    public int amount;

    public Long workshopId;


}
