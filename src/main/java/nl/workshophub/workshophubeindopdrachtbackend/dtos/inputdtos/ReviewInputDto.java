package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.*;

public class ReviewInputDto {

    @Max(value=5, message="Rating can't be higher than 5")
    @Positive (message = "Rating can't be empty or below 0")
    public double rating;
    @NotBlank (message = "Description can't be empty")
    public String reviewDescription;

    public Boolean reviewVerified;
    public String feedbackAdmin;











}
