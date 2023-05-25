package nl.workshophub.workshophubeindopdrachtbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String companyName;
    private int kvkNumber;
    private String vatNumber;
    private Boolean workshopOwnerVerified;
    private Boolean workshopOwner;

}
