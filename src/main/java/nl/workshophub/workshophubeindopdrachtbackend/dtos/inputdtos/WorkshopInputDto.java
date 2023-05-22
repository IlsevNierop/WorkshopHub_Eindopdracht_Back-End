package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;

import java.time.LocalDate;
import java.time.LocalTime;

//getters en setters nodig voor modelmapper
@Getter
@Setter
public class WorkshopInputDto {

    @NotBlank(message = "Workshop titel kan niet leeg zijn")
    public String title;
    @NotNull(message = "Datum mag niet leeg zin")
    @Future(message = "Datum moet in de toekomst liggen")
    public LocalDate date;

    @NotNull(message = "Starttijd kan niet leeg zijn")
    public LocalTime startTime;
    @NotNull(message = "Eindtijd kan niet leeg zijn")
    public LocalTime endTime;

    @Positive(message = "Prijs kan niet leeg zijn")
    public double price;

    //validatie annotation toevoegen voor enumeration
//    @NotNull (message = "Vul in of het binnen of buiten is")
    public InOrOutdoors inOrOutdoors;

    @NotBlank(message = "Locatie kan niet leeg zijn")
    public String location;
    public String highlightedInfo;

    @NotBlank(message = "Omschrijving kan niet leeg zijn")
    @Size(min=50, max=400, message= "Omschrijving moet minimaal 50 en maximaal 400 karakters bevatten.")
    public String description;

    @Positive(message = "Aantal deelnemers kan niet leeg zijn")
    public int amountOfParticipants;
    public Boolean publishWorkshop;

    @NotBlank (message = "Er moet minstens 1 workshop thema gevuld zijn")
    public String workshopCategory1;
    public String workshopCategory2;

//     admin input: --> extra input dto klasse maken - nice to have
      public Boolean workshopVerified;
      public String feedbackAdmin;






}
