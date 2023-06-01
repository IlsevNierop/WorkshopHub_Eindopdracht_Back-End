package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingOutputDto {

    public Long id;

    public LocalDate dateOrder;

    public String commentsCustomer;

    public int amount;

    // of gehele workshop teruggeven? straks als verschillende dto's dan de workshop dto teruggeven met sumiere info?

    public Long workshopId;
    public String workshopTitle;


}
