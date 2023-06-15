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
    private String email;
    private String password;
    private String companyName;
    private Integer kvkNumber;
    private String vatNumber;
    private Boolean workshopOwnerVerified;
    private Boolean workshopOwner;

    @OneToMany (mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> customerReviews;

    @OneToMany (mappedBy = "workshopOwner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> workshopOwnerReviews;

    @OneToMany (mappedBy = "workshopOwner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Workshop> workshops;

}
