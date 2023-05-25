package nl.workshophub.workshophubeindopdrachtbackend.repositories;

import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
