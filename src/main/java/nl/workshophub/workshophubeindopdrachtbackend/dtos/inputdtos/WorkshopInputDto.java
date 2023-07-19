package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkshopInputDto {

    @NotBlank(message = "Workshop title can't be empty.")
    public String title;
    @NotNull(message = "Date can't be empty.")
    @Future(message = "Date needs to be in the future.")
    @DateTimeFormat(pattern="YYYY-MM-DD")
//    @Pattern(regexp = "YYYY-MM-DD", message = "Date format should be YYYY-MM-DD.") - not working, check validation date and time
    public LocalDate date;
    @NotNull(message = "Start time can't be empty.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTime startTime;
    @NotNull(message = "End time can't be empty.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTime endTime;
    @Positive(message = "Price can't be emtpy")
    public double price;
    @Enumerated(EnumType.STRING)
    public InOrOutdoors inOrOutdoors;
    @NotBlank(message = "Location can't be empty.")
    public String location;
    public String highlightedInfo;
    @NotBlank(message = "Description can't be empty.")
    @Size(min = 50, max = 2000, message = "Description needs to have a minimum of 50 characters and maximum of 2000.")
    public String description;
    @Positive(message = "Amount of participants can't be empty.")
    public int amountOfParticipants;
    @NotBlank(message = "At least one workshop category should be filled in.")
    public String workshopCategory1;
    public String workshopCategory2;
    public Boolean workshopVerified;
    public String feedbackAdmin;
    public Boolean publishWorkshop;



}
