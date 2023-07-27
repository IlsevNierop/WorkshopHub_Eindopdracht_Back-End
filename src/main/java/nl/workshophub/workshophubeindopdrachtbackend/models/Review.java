package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @JoinColumn(name = "workshop_id")
    private Workshop workshop;

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private User customer;


    //private Boolean attendedWorkshop? --> methode om bookings te checken van de workshop?





}
