package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private String email; //identifier

    private String password;
    private String companyName;
    private String kvkNumber;
    private String vatNumber;
    private Boolean workshopOwnerVerified;
    private Boolean workshopOwner;

    private String profilePicUrl;

    private String fileName;

    //relations

    @OneToMany (mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> customerReviews;
    @OneToMany (mappedBy = "workshopOwner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Workshop> workshops;

    @OneToMany (mappedBy = "customer", fetch = FetchType.LAZY)
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

    //moet dit nog in een dto? Nodig?
    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String apikey;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "userId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }


    public ArrayList<Double> calculateAverageRatingAndNumberReviewsWorkshopOwner() {
        if (this.getWorkshops() == null) {
            return null;
        }
        double sumRatings = 0;
        double numberReviews = 0;
        for (Workshop w : this.getWorkshops()) {
            if (w.getWorkshopReviews() != null) {
                for (Review r : w.getWorkshopReviews()) {
                    if (r.getReviewVerified() == Boolean.TRUE) {
                        sumRatings += r.getRating();
                        numberReviews++;
                    }
                }
            }
        }
        if (numberReviews == 0) {
            return null;
        }

        ArrayList<Double> avgRatingAndNumberReviews = new ArrayList<>();
        avgRatingAndNumberReviews.add(sumRatings / numberReviews);
        avgRatingAndNumberReviews.add(numberReviews);

        return avgRatingAndNumberReviews;

    }

}
