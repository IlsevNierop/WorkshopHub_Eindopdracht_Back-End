package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {


    private final WorkshopRepository repos;

    public WorkshopController(WorkshopRepository repos) {
        this.repos = repos;
    }

    @GetMapping
    public ResponseEntity<List<Workshop>> getAllWorkshopsFromCurrentDateOnwards() {

        return new ResponseEntity<>(repos.findByDateAfter(java.time.LocalDate.now()), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Workshop> getWorkshopById(@PathVariable Long id) throws RecordNotFoundException {
        Optional<Workshop> optionalWorkshop = repos.findById(id);
        if (optionalWorkshop.isEmpty()) {
            throw new RecordNotFoundException("De workshop met ID " + id + " bestaat niet");
        }
        Workshop workshop = optionalWorkshop.get();
        return new ResponseEntity<>(workshop, HttpStatus.OK);
    }

//    @GetMapping ("/workshopowner")
//    public ResponseEntity<List<Workshop>> getWorkshopsByWorkshopOwner (@RequestParam User workshopOwner) throws RecordNotFoundException {
//        //Nog toevoegen: check of workshopOwner uberhaupt bestaat in de database
//        if (repos.findByWorkshopOwner(workshopOwner).isEmpty()){
//            throw RecordNotFoundException("De workshopeigenaar met naam " + workshopOwner.getFullName() + " heeft geen workshops geregistreerd staan");
//        }
//        return new ResponseEntity<>(repos.findByWorkshopOwner(workshopOwner), HttpStatus.OK);
//    }
}
