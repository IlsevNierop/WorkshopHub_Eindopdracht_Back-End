package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    //open
    List<Workshop> findByDateAfterAndWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate (LocalDate date, Long workshopOwnerId);
    List<Workshop> findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(LocalDate date);

    //owner
    List<Workshop> findByWorkshopOwnerId (Long workshopOwnerId);

    //admin
    List<Workshop> findByDateAfterAndWorkshopVerifiedIsNullOrWorkshopVerifiedIsFalseOrderByDate(LocalDate date);


}
