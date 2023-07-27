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

    //needs fetchtype lazy, otherwise get bookings from customer, shows double bookings
    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    private Workshop workshop;

    //needs fetchtype lazy, otherwise booking amounts are being counted double
    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnore
    private User customer;
    
}
