package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;

    private final FieldErrorHandling fieldErrorHandling;

    public WorkshopController(WorkshopService workshopService, FieldErrorHandling fieldErrorHandling) {
        this.workshopService = workshopService;
        this.fieldErrorHandling = fieldErrorHandling;
    }

    @GetMapping
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopById(@PathVariable Long id) {
        return new ResponseEntity<>(workshopService.getWorkshopById(id), HttpStatus.OK);
    }

    //getmapping om per bedrijf alle workshops te zien? voor niet ingelogde users?


    //owner getmappings:
    // goed te keuren workshops van owner - verified==true
    // afgekeurde workshops van owner - verified == false - incl feedback
    // published workshops van owner after date (?)
    // published workshops owner allemaal (?)

//    @GetMapping ("/{workshopowner}/")
//    public ResponseEntity<List<Workshop>> getAllWorkshopsByWorkshopOwner (@RequestParam User workshopOwner) throws RecordNotFoundException {
//        //Nog toevoegen: check of workshopOwner uberhaupt bestaat in de database
//        if (repos.findByWorkshopOwner(workshopOwner).isEmpty()){
//            throw new RecordNotFoundException("De workshopeigenaar met naam " + workshopOwner.getFullName() + " heeft geen workshops geregistreerd staan");
//        }
//        return new ResponseEntity<>(repos.findByWorkshopOwner(workshopOwner), HttpStatus.OK);
//    }

    // admin getmappings:
    @GetMapping("/admin/")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsToVerify() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsToVerify(), HttpStatus.OK);
    }

    @PostMapping("/{workshopOwnerId}")
    public ResponseEntity<Object> createWorkshop(@PathVariable Long workshopOwnerId, @Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        WorkshopOutputDto workshopOutputDto = workshopService.createWorkshop(workshopOwnerId, workshopInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(workshopOutputDto);
    }


    //put mapping: owner can edit everything but not feedback and approve --> if some variables are edited automatisch verified == null. If only publish is edited --> dan verified niet wijzigen en andere dingen niet wijzigen.
    // aparte put voor alleen verifieren door owner - met een request param?
    //owner id nog toevoegen {workshopownerid}/
    @PutMapping ("/{workshopOwnerId}/{workshopId}")
    public ResponseEntity<Object> updateWorkshopByOwner (@PathVariable("workshopOwnerId") Long workshopOwnerId, @PathVariable("id") Long workshopId,  @Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(workshopService.updateWorkshopByOwner(workshopOwnerId, workshopId, workshopInputDto), HttpStatus.ACCEPTED);
    }


    @PutMapping ("/verify/{workshopOwnerId}/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> verifyWorkshopByOwner (@PathVariable("workshopOwnerId") Long workshopOwnerId, @PathVariable Long workshopId, @RequestParam Boolean publishWorkshop) {

        return new ResponseEntity<>(workshopService.verifyWorkshopByOwner(workshopOwnerId, workshopId, publishWorkshop), HttpStatus.ACCEPTED);
    }

    // admin:
    // put mapping: admin can edit and add feedback and approve
    @PutMapping ("/admin/{workshopId}")
    public ResponseEntity<Object> verifyWorkshopByAdmin (@PathVariable Long workshopId, @Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(workshopService.verifyWorkshopByAdmin(workshopId, workshopInputDto), HttpStatus.ACCEPTED);
    }


    //delete mapping admin: alleen als publish == false
    // delete mapping owner (kan niet als bookings heeft)
    @DeleteMapping("/admin/{workshopId}")
    public ResponseEntity<HttpStatus> deleteWorkshop(@PathVariable Long workshopId) {
        workshopService.deleteWorkshop(workshopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
