package nl.workshophub.workshophubeindopdrachtbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    private double rating;

    private String reviewDescription;

    private Boolean reviewVerified;
    private String feedbackAdmin;

    //private User customer;
    //private User workshopOwner;
    //private Workshop workshop;

    //private Boolean attendedWorkshop? --> methode om bookings te checken van de workshop?





}
