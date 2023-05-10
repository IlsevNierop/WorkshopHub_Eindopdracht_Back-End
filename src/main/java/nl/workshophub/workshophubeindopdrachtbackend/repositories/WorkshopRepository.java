package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

//    List<Workshop> findByWorkshopOwner (User workshopOwner);

    List<Workshop> findByDateAfter(LocalDate date);


}
