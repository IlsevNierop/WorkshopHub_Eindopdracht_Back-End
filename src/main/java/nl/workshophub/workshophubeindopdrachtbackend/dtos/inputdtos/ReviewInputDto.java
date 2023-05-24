package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewInputDto {

    @NotBlank
    public double rating;
    @NotBlank
    public String reviewDescription;

    private Boolean reviewVerified;
    private String feedbackAdmin;

    //private User customer;
    //private User workshopOwner;
    //private Workshop workshop;





}
