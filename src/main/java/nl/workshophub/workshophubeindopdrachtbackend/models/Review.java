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
//    private Boolean attendedWorkshop;

    @ManyToOne
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "workshop_owner_id")
    private User workshopOwner;


    //private Boolean attendedWorkshop? --> methode om bookings te checken van de workshop?





}
