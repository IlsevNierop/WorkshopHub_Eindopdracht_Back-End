package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;

import java.time.LocalDate;
import java.time.LocalTime;

//getters en setters nodig voor modelmapper
@Getter
@Setter
public class WorkshopInputDtoOwner {

    @NotBlank(message = "Workshop titel kan niet leeg zijn")
    public String title;
    @NotBlank(message = "Datum mag niet leeg zin")
    @Future(message = "Datum moet in de toekomst liggen")
    public LocalDate date;

    @NotBlank(message = "Starttijd kan niet leeg zijn")
    public LocalTime startTime;
    @NotBlank(message = "Eindtijd kan niet leeg zijn")
    public LocalTime endTime;

    @NotBlank(message = "Prijs kan niet leeg zijn")
    public double price;

    @NotBlank(message = "Binnen/buiten kan niet leeg zijn")
    public InOrOutdoors inOrOutdoors;

    @NotBlank(message = "Locatie kan niet leeg zijn")
    public String location;
    public String highlightedInfo;

    @NotBlank(message = "Omschrijving kan niet leeg zijn")
    @Size(min=50, max=400, message= "Omschrijving moet minimaal 50 en maximaal 400 karakters bevatten.")
    public String description;

    @NotBlank(message = "Aantal deelnemers kan niet leeg zijn")
    public int amountOfParticipants;
    public String workshopCategory;

    public Boolean publishWorkshop;

    // admin input:
    // public String (?) workshopTheme;
    //  public Boolean workshopVerified;
    //
    //    public String feedbackAdmin;






}
