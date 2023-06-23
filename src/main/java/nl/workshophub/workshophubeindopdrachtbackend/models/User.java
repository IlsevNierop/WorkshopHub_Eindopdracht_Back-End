package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //relations

    @OneToMany (mappedBy = "customer")
    @JsonIgnore
    private List<Review> customerReviews;
    @OneToMany (mappedBy = "workshopOwner")
    @JsonIgnore
    private List<Workshop> workshops;

    @OneToMany (mappedBy = "customer")
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "user_favourite_workshop",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "workshop_id")

    )
    private  Set<Workshop> favouriteWorkshops = new HashSet<>();

    //security
    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String apikey;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

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
