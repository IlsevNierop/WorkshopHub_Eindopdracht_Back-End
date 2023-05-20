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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private double price;
    @Enumerated(EnumType.STRING)
    private InOrOutdoors inOrOutdoors;
    private String location;

    // bij opsomming mogelijk met \n werken en dan in frontend goed weergeven
private String highlightedInfo;

    @Column(columnDefinition = "text")
    private String description;
    private int amountOfParticipants;

    private String workshopCategory;


    private Boolean workshopVerified;

    @Column(columnDefinition = "text")
    private String feedbackAdmin;

    private Boolean publishWorkshop;


    //    private User workshopOwner

    // geen enumeratie, maar mogelijk een voorop gezette ArrayList - waarbij dan ALLEEN de admin nieuwe kan toevoegen en oude kan verwijderen (? beter niet mss ivm oude objecten? / editten?)
//    kan nog geen arraylist aanmaken - hoe werkt dit als het een lijst met strings is? Kan dit een arraylist zijn?
//    @NotNull
//    private ArrayList<String> workshopTheme;

//    // check of byte het juiste type variabele is voor image
//    @NotNull
//    private ArrayList<byte> workshopImage;

//    private ArrayList<Booking> workshopBookings;
//    private ArrayList<Review> workshopOwnerReviews;










}
