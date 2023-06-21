package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique=true)
    private String email;
    private String password;
    private String companyName;
    private Integer kvkNumber;
    private String vatNumber;
    private Boolean workshopOwnerVerified;
    private Boolean workshopOwner;

    @OneToMany (mappedBy = "customer")
    @JsonIgnore
    private List<Review> customerReviews;
    @OneToMany (mappedBy = "workshopOwner")
    @JsonIgnore
    private List<Workshop> workshops;

    @OneToMany (mappedBy = "customer")
    @JsonIgnore
    private List<Booking> bookings;

    //possibly cascade all - cause user needs to be able to be removed, even if it has workshops as favourites.
    @ManyToMany
    @JoinTable(
            name = "users_favourite_workshops",
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn
    )
    private List<Workshop> favouriteWorkshops;


    public Double calculateAverageRatingWorkshopOwner() {
        if (this.getWorkshops() == null) {
            return null;
        }
        double sumRatings = 0;
        double numberReviews = 0;
        for (Workshop w : this.getWorkshops()) {
            if (w.getWorkshopReviews() != null) {
                for (Review r : w.getWorkshopReviews()) {
                    sumRatings += r.getRating();
                    numberReviews++;
                }
            }
        }
        if (numberReviews == 0) {
            return null;
        }

        return sumRatings / numberReviews;

    }

}
