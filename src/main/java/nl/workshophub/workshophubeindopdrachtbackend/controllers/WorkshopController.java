package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.VariableCannotBeEmptyException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final WorkshopRepository repos;

    public WorkshopController(WorkshopService workshopService, WorkshopRepository repos) {
        this.workshopService = workshopService;
        this.repos = repos;
    }

    @GetMapping
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopById(@PathVariable Long id) {
        return new ResponseEntity<>(workshopService.getWorkshopById(id), HttpStatus.OK);
    }


    //owner getmappings:
    // goed te keuren workshops van owner - verified==true
    // afgekeurde workshops van owner - verified == false - incl feedback
    // published workshops van owner after date (?)
    // published workshops owner allemaal (?)

//    @GetMapping ("/{workshopowner}/goedkeuren")
//    public ResponseEntity<List<Workshop>> getAllWorkshopsByWorkshopOwner (@RequestParam User workshopOwner) throws RecordNotFoundException {
//        //Nog toevoegen: check of workshopOwner uberhaupt bestaat in de database
//        if (repos.findByWorkshopOwner(workshopOwner).isEmpty()){
//            throw new RecordNotFoundException("De workshopeigenaar met naam " + workshopOwner.getFullName() + " heeft geen workshops geregistreerd staan");
//        }
//        return new ResponseEntity<>(repos.findByWorkshopOwner(workshopOwner), HttpStatus.OK);
//    }

    // admin getmappings:
    @GetMapping("/admin/goedkeuren")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsToVerify() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsToVerify(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createWorkshop(@Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(errorToStringHandling(bindingResult));
        }
        WorkshopOutputDto workshopOutputDto = workshopService.createWorkshop(workshopInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOutputDto).toUriString());
        return ResponseEntity.created(uri).body(workshopOutputDto);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Workshop> updateWorkshop(@PathVariable Long id, @RequestBody Workshop w) throws RecordNotFoundException, VariableCannotBeEmptyException {
//        Optional<Workshop> optionalWorkshop = repos.findById(id);
//        if (optionalWorkshop.isEmpty()) {
//            throw new RecordNotFoundException("De workshop met ID " + id + " bestaat niet");
//        }
//        Workshop workshop = optionalWorkshop.get();
//        if (w.getTitle() != null) {
//            workshop.setTitle(w.getTitle());
//        }
//        if (w.getDate() != null) {
//            workshop.setDate(w.getDate());
//        }
//        if (w.getStartTime() != null) {
//            workshop.setStartTime(w.getStartTime());
//        }
//        if (w.getEndTime() != null) {
//            workshop.setEndTime(w.getEndTime());
//        }
//        if (w.getPrice() != 0.0) {
//            workshop.setPrice(w.getPrice());
//        }
//        //check default waarde!
//        if (w.getInOrOutdoors() != null) {
//            workshop.setInOrOutdoors(w.getInOrOutdoors());
//        }
//        if (w.getLocation() != null) {
//            workshop.setLocation(w.getLocation());
//        }
////        if (!w.getHighlightedInfo().isEmtpy()) {
////            workshop.setHighlightedInfo(w.getHighlightedInfo());
////        }
//        if (w.getDescription() != null) {
//            workshop.setDescription(w.getDescription());
//        }
//        if (w.getAmountOfParticipants() != 0) {
//            workshop.setAmountOfParticipants(w.getAmountOfParticipants());
//        }
//        if (w.getWorkshopCategory() != null) {
//            workshop.setWorkshopCategory(w.getWorkshopCategory());
//        }
////        if (!w.getWorkshopTheme().isEmtpy()) {
////            workshop.setWorkshopTheme(w.getWorkshopTheme());
////        }
//
////        if (!w.getWorkshopImage().isEmtpy()) {
////            workshop.setWorkshopImage(w.getWorkshopImage());
////        }
//        if (w.getWorkshopVerified() != null) {
//            workshop.setWorkshopVerified(w.getWorkshopVerified());
//        }
//        if (w.getFeedbackAdmin() != null) {
//            workshop.setFeedbackAdmin(w.getFeedbackAdmin());
//        }
//        repos.save(workshop);
//        return new ResponseEntity<>(workshop, HttpStatus.ACCEPTED);
//    }

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

    public String errorToStringHandling (BindingResult bindingResult){
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : bindingResult.getFieldErrors()){
            sb.append(fe.getField() + ": ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }


}
