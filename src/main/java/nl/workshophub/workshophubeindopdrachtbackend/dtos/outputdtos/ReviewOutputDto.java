package nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewOutputDto {

    public Long id;

    public double rating;

    public String reviewDescription;

    private Boolean reviewVerified;
    private String feedbackAdmin;

    //private User customer;
    //private User workshopOwner;
    //private Workshop workshop;


}
