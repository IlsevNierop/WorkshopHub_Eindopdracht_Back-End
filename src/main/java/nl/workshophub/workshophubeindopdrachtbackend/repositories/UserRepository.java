package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByWorkshopOwnerIsTrueAndWorkshopOwnerVerifiedIsNullOrWorkshopOwnerVerifiedIsFalse();
    Boolean existsByEmail(String email);
}
