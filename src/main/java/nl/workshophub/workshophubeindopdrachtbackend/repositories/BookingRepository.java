package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
