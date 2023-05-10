package nl.workshophub.workshophubeindopdrachtbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
@Table(name = "workshops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workshop {

    @Id
    @GeneratedValue
    private Long id;

//    @NotNull
//    private User workshopOwner

    // even de NotNull annotatie verwijderd, want dit zorgde ervoor dat ik de error meldingen niet zelf kon meegeven.
    //Nu zit de NotNull check in de controller
//    @NotNull(message = "Titel van de workshop mag niet leeg zijn")
    private String title;

//    @NotNull
    private LocalDate date;
//    @NotNull
    private LocalTime startTime;
//    @NotNull
    private LocalTime endTime;

//    @NotNull
    private double price;

//    @NotNull
    @Enumerated(EnumType.STRING)
    private InOrOutdoors inOrOutdoors;

//    @NotNull
    private String location;

//    // moet opsomming worden, daarom een arraylist van gemaakt
//    private ArrayList<String> highlightedInfo;

//    @NotNull
    @Column(columnDefinition = "text")
    private String description;

//    @NotNull
    private int amountOfParticipants;

    private String workshopCategory;

// geen enumeratie, maar mogelijk een voorop gezette ArrayList - waarbij dan ALLEEN de admin nieuwe kan toevoegen en oude kan verwijderen (? beter niet mss ivm oude objecten? / editten?)
//    kan nog geen arraylist aanmaken
//    @NotNull
//    private ArrayList<String> workshopTheme;

//    // check of byte het juiste type variabele is voor image
//    @NotNull
//    private ArrayList<byte> workshopImage;

    // dit komt in de koppeltabel - deze niet toegevoegd in de standaard put mapping
//    private ArrayList<Booking> workshopBookings;
//    private ArrayList<Review> workshopOwnerReviews;
//    @NotNull
    private Boolean workshopVerified;

    @Column(columnDefinition = "text")
    private String feedbackAdmin;










}
