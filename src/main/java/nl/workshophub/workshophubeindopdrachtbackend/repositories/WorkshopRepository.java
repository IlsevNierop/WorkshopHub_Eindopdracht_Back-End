package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    List<Workshop> findByDateAfterAndWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(LocalDate date, Long workshopOwnerId);

    List<Workshop> findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(LocalDate date);

    List<Workshop> findByWorkshopOwnerId(Long workshopOwnerId);

    List<Workshop> findByWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsNullOrPublishWorkshopIsFalseOrderByDate(Long workshopOwnerId);

    List<Workshop> findByDateAfterAndWorkshopVerifiedIsNullOrWorkshopVerifiedIsFalseOrderByDate(LocalDate date);


}
