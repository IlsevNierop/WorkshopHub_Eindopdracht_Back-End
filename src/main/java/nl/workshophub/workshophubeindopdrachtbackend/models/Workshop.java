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
    private String workshopCategory1;
    private String workshopCategory2;
    private Boolean workshopVerified;
    @Column(columnDefinition = "text")
    private String feedbackAdmin;
    private Boolean publishWorkshop;
    private String workshopPicUrl;
    private String fileName;
    @ManyToOne
    @JsonIgnore
    private User workshopOwner;
    @OneToMany(mappedBy = "workshop")
    @JsonIgnore
    private List<Booking> workshopBookings;

    @OneToMany(mappedBy = "workshop")
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
    public int calculateAmountOfBookingsWorkshop(){
        int bookingsWorkshop = 0;
        if (this.getWorkshopBookings() != null) {
            for (Booking booking : this.getWorkshopBookings()) {
                bookingsWorkshop += booking.getAmount();
            }
        }
        return bookingsWorkshop;
    }


    public int calculateAmountOfFavouritesWorkshop(){
        int amountOfFavourites = 0;
        if (this.getFavsUser() != null){
            amountOfFavourites += this.getFavsUser().size();
        }
        return amountOfFavourites;
    }


}
