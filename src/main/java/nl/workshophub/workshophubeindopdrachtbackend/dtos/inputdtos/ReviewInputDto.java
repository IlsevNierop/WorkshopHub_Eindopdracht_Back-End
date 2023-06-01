package nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ReviewInputDto {

    @Min(value=0, message= "Beoordeling mag niet lager dan 0 zijn")
    @Max(value=5, message="Beoordeling mag niet hoger dan 5 zijn")
    public double rating;
    @NotBlank (message = "Omschrijving mag niet leeg zijn")
    public String reviewDescription;

    public Boolean reviewVerified;
    public String feedbackAdmin;

    public Long workshopId;

    //public User customer;
    //public User workshopOwner;






}
