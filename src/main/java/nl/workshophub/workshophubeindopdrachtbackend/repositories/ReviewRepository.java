package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository <Review, Long> {
}
