package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDtoDefault;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.VariableCannotBeEmptyException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {

    // nog checken of identieke workshop al bestaat? Gebaseerd op datum, tijdstip, title, locatie & workshopowner?

    //adres waar workshop plaatsvindt - komt alleen in bevestigingsmail - moet wel ingevoerd worden?


    private final WorkshopService workshopService;
    private final WorkshopRepository repos;

    public WorkshopController(WorkshopService workshopService, WorkshopRepository repos) {
        this.workshopService = workshopService;
        this.repos = repos;
    }

    @GetMapping
    public ResponseEntity<List<WorkshopOutputDtoDefault>> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(), HttpStatus.OK);
    }

    //werkt!
    @GetMapping("/test")
    public ResponseEntity<List<WorkshopOutputDtoDefault>> testQueryGetAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        return new ResponseEntity<>(workshopService.testQueryGetAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopOutputDtoDefault> getWorkshopById(@PathVariable Long id) {
        return new ResponseEntity<>(workshopService.getWorkshopById(id), HttpStatus.OK);
    }

// Mogelijk niet nodig?
//    @GetMapping("/findbytitle")
//    public ResponseEntity<List<Workshop>> getWorkshopsByTitleSubstring(@RequestParam String title) throws RecordNotFoundException {
//        if (repos.findByTitleContainingIgnoreCase(title).isEmpty()) {
//            throw new RecordNotFoundException("Er bestaan geen workshops met het woord " + title + " in de titel");
//        }
//        return new ResponseEntity<>(repos.findByTitleContainingIgnoreCase(title), HttpStatus.OK);
//    }

//   // Mogelijk niet nodig?
//    @GetMapping("/findbydate")
//    public ResponseEntity<List<Workshop>> getWorkshopsByDate(@RequestParam LocalDate date) throws RecordNotFoundException {
//        if (repos.findByDate(date).isEmpty()) {
//            throw new RecordNotFoundException("Er bestaan geen workshops op datum " + date);
//        }
//        return new ResponseEntity<>(repos.findByDate(date), HttpStatus.OK);
//    }

    //owner getmappings:
    // goed te keuren workshops van owner - verified==true
    // afgekeurde workshops van owner - verified == false - incl feedback
    // published workshops van owner after date (?)
    // published workshops owner allemaal (?)
    // get by workshopid --> incl list bookings
//    @GetMapping ("/{workshopowner}/goedkeuren")
//    public ResponseEntity<List<Workshop>> getAllWorkshopsByWorkshopOwner (@RequestParam User workshopOwner) throws RecordNotFoundException {
//        //Nog toevoegen: check of workshopOwner uberhaupt bestaat in de database
//        if (repos.findByWorkshopOwner(workshopOwner).isEmpty()){
//            throw new RecordNotFoundException("De workshopeigenaar met naam " + workshopOwner.getFullName() + " heeft geen workshops geregistreerd staan");
//        }
//        return new ResponseEntity<>(repos.findByWorkshopOwner(workshopOwner), HttpStatus.OK);
//    }

    // admin getmappings:
    // alle goed te keuren workshops (verified == null)
    // alle openstaande afgekeurde workshops (verified == false)
    // alle workshops incl alle variabelen (bookings, feedback, verified etc)
    // alle goedgekeurde en published incl alle variabelen
    // by id - alle variabelen
    // by ownerid


    // post maken voor alleen goedkeuren door owner? pathvariable workshop & input met alleen boolean publish?
    @PostMapping
    public ResponseEntity<Workshop> createWorkshop(@RequestBody Workshop workshop) throws VariableCannotBeEmptyException {
        repos.save(workshop);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshop.getId()).toUriString());
        return ResponseEntity.created(uri).body(workshop);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Workshop> updateWorkshop(@PathVariable Long id, @RequestBody Workshop w) throws RecordNotFoundException, VariableCannotBeEmptyException {
        Optional<Workshop> optionalWorkshop = repos.findById(id);
        if (optionalWorkshop.isEmpty()) {
            throw new RecordNotFoundException("De workshop met ID " + id + " bestaat niet");
        }
        Workshop workshop = optionalWorkshop.get();
        if (w.getTitle() != null) {
            workshop.setTitle(w.getTitle());
        }
        if (w.getDate() != null) {
            workshop.setDate(w.getDate());
        }
        if (w.getStartTime() != null) {
            workshop.setStartTime(w.getStartTime());
        }
        if (w.getEndTime() != null) {
            workshop.setEndTime(w.getEndTime());
        }
        if (w.getPrice() != 0.0) {
            workshop.setPrice(w.getPrice());
        }
        //check default waarde!
        if (w.getInOrOutdoors() != null) {
            workshop.setInOrOutdoors(w.getInOrOutdoors());
        }
        if (w.getLocation() != null) {
            workshop.setLocation(w.getLocation());
        }
//        if (!w.getHighlightedInfo().isEmtpy()) {
//            workshop.setHighlightedInfo(w.getHighlightedInfo());
//        }
        if (w.getDescription() != null) {
            workshop.setDescription(w.getDescription());
        }
        if (w.getAmountOfParticipants() != 0) {
            workshop.setAmountOfParticipants(w.getAmountOfParticipants());
        }
        if (w.getWorkshopCategory() != null) {
            workshop.setWorkshopCategory(w.getWorkshopCategory());
        }
//        if (!w.getWorkshopTheme().isEmtpy()) {
//            workshop.setWorkshopTheme(w.getWorkshopTheme());
//        }

//        if (!w.getWorkshopImage().isEmtpy()) {
//            workshop.setWorkshopImage(w.getWorkshopImage());
//        }
        if (w.getWorkshopVerified() != null) {
            workshop.setWorkshopVerified(w.getWorkshopVerified());
        }
        if (w.getFeedbackAdmin() != null) {
            workshop.setFeedbackAdmin(w.getFeedbackAdmin());
        }
        repos.save(workshop);
        return new ResponseEntity<>(workshop, HttpStatus.ACCEPTED);
    }

    //delete mapping admin: alleen als publish == false
    // delete mapping owner (kan niet als bookings heeft)
    @DeleteMapping("/{id}")
    public ResponseEntity<Workshop> deleteWorkshop(@PathVariable Long id) throws RecordNotFoundException {
        Optional<Workshop> optionalWorkshop = repos.findById(id);
        if (optionalWorkshop.isEmpty()) {
            throw new RecordNotFoundException("De workshop met id " + id + " bestaat niet");
        }
        Workshop workshop = optionalWorkshop.get();
        repos.delete(workshop);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
