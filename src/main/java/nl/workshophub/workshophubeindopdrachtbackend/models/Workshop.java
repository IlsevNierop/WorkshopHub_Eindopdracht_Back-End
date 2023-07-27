package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private LocalTime endTime;
    private double price;
    @Enumerated(EnumType.STRING)
    private InOrOutdoors inOrOutdoors;
    private String location;
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


    // TODO make this several pictures
    private String workshopPicUrl;

    private String fileName;

    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    private User workshopOwner;

    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> workshopBookings;

    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> workshopReviews;

    @ManyToMany (mappedBy = "favouriteWorkshops")
    @JsonIgnore
    private Set<User> favsUser  = new HashSet<>();


    public int getAvailableSpotsWorkshop() {
        int spotsBooked = 0;
        if (this.getWorkshopBookings() != null){
            for (Booking b : this.getWorkshopBookings()) {
                spotsBooked += b.getAmount();
            }}
        return (this.getAmountOfParticipants() - spotsBooked);
    }

    // TODO: 12/07/2023 doesn't take into account reviews 
    public int calculateAmountOfFavsAndBookingsWorkshop(){
        int popularityNumber = 0;
        popularityNumber += this.getFavsUser().size();
        if (this.getWorkshopBookings() != null) {
            for (Booking booking : this.getWorkshopBookings()) {
                popularityNumber += booking.getAmount();
            }
        }
        return popularityNumber;
    }


}
