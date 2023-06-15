package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "workshops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate date;
    private LocalTime startTime;

    //end time verwijderen
    private LocalTime endTime;
    //    zet hier de tijdsduur neer - duration
    private double price;
    @Enumerated(EnumType.STRING)
    private InOrOutdoors inOrOutdoors;
    private String location;

    // bij opsomming mogelijk met \n werken en dan in frontend goed weergeven
    private String highlightedInfo;

    @Column(columnDefinition = "text")
    private String description;
    private int amountOfParticipants;

    // geen enumeratie, maar mogelijk een extra klasse maken (incl service, etc) met een voorop gezette ArrayList - waarbij dan ALLEEN de admin nieuwe strings kan toevoegen die dan ook toegevoegd worden aan de database en oude kan verwijderen (? beter niet mss ivm oude objecten? / editten?)
    private String workshopCategory1;
    private String workshopCategory2;

    //mogelijk enumeratie van maken? met zelfde waardes als publishworkshop.
    private Boolean workshopVerified;

    @Column(columnDefinition = "text")
    private String feedbackAdmin;

    private Boolean publishWorkshop;


    // check of byte het juiste type variabele is voor image - even checken hoe dit verwerkt wordt - lijst of niet?

//    private ArrayList<byte> workshopImage;

    @ManyToOne
    private User workshopOwner;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> workshopBookings;

    @OneToMany(mappedBy = "workshop", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> workshopReviews;


}
