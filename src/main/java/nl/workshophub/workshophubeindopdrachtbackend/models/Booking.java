package nl.workshophub.workshophubeindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dateOrder;

    private String commentsCustomer;

    private int amount;

    private double totalPrice;

    @ManyToOne
    @JsonIgnore
    private Workshop workshop;

    @ManyToOne
    @JsonIgnore
    private User customer;
    
}
