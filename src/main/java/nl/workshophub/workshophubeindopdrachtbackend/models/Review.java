package nl.workshophub.workshophubeindopdrachtbackend.models;

import jakarta.persistence.*;
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

    @ManyToOne
    private Workshop workshop;

    //private User customer;
    //private User workshopOwner;


    //private Boolean attendedWorkshop? --> methode om bookings te checken van de workshop?





}
