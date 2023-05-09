package nl.workshophub.workshophubeindopdrachtbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @NotNull
    private String title;

    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    @NotNull
    private double price;

    @NotNull
    private InOrOutdoors inOrOutdoors;

    @NotNull
    private String location;

    private String highlightedInfo;

    @NotNull
    private String description;

    @NotNull
    private int amountOfParticipants;

    private String workshopCategory;

// geen enumeratie, maar mogelijk een voorop gezette ArrayList - waarbij dan ALLEEN de admin nieuwe kan toevoegen en oude kan verwijderen (? beter niet mss ivm oude objecten? / editten?)
//    kan nog geen arraylist aanmaken
//    @NotNull
//    private ArrayList<String> workshopThemes;

//    // check of byte het juiste type variabele is voor image
//    @NotNull
//    private ArrayList<byte> imageWorkshop;

//    private ArrayList<Booking> workshopBookings;
//    private ArrayList<Review> workshopOwnerReviews;
    @NotNull
    private boolean workshopVerified;

    private String feedbackAdmin;










}
