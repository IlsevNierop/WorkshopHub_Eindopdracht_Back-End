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

    //open

    @GetMapping
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(@RequestParam(value="userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(userId), HttpStatus.OK);
    }

    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopByIdVerifiedAndPublish(@PathVariable("workshopId") Long workshopId, @RequestParam(value="userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getWorkshopByIdVerifiedAndPublish(workshopId, userId), HttpStatus.OK);
    }

    @GetMapping("/workshopOwner/{workshopOwnerId}")
    public ResponseEntity<List<WorkshopOutputDto>>  getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(@PathVariable Long workshopOwnerId,  @RequestParam(value="userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(workshopOwnerId, userId), HttpStatus.OK);
    }


    //owner
    @GetMapping("/workshopOwner/{workshopOwnerId}/workshop/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopByWorkshopOwnerId(@PathVariable("workshopOwnerId") Long workshopId, @PathVariable("workshopId") Long workshopOwnerId) {
        return new ResponseEntity<>(workshopService.getWorkshopByWorkshopOwnerId(workshopId, workshopOwnerId), HttpStatus.OK);
    }


    // filter at frontend on what to verify etc. show startdate today - but possibility to go back in time
    @GetMapping("/workshopOwner/all/{workshopOwnerId}")
    public ResponseEntity<List<WorkshopOutputDto>>  getAllWorkshopsFromWorkshopOwner(@PathVariable Long workshopOwnerId) {
        return new ResponseEntity<>(workshopService.getAllWorkshopsFromWorkshopOwner(workshopOwnerId), HttpStatus.OK);
    }



    // admin getmappings:
    @GetMapping("/admin/verify")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsToVerify() {
        return new ResponseEntity<>(workshopService.getAllWorkshopsToVerify(), HttpStatus.OK);
    }

    @GetMapping("/admin/")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshops() {
        return new ResponseEntity<>(workshopService.getAllWorkshops(), HttpStatus.OK);
    }

    @GetMapping("/admin/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopById(@PathVariable Long workshopId) {
        return new ResponseEntity<>(workshopService.getWorkshopById(workshopId), HttpStatus.OK);
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

    @PutMapping ("favourite/{userId}/{workshopId}")
    public ResponseEntity <List<WorkshopOutputDto>> addOrRemoveWorkshopFavourites(@PathVariable("userId") Long userId, @PathVariable("workshopId") Long workshopId, @RequestParam Boolean favourite){
        return new ResponseEntity<>(workshopService.addOrRemoveWorkshopFavourites(userId, workshopId, favourite), HttpStatus.ACCEPTED);
    }


    @PutMapping ("/{workshopOwnerId}/{workshopId}")
    public ResponseEntity<Object> updateWorkshopByOwner (@PathVariable("workshopOwnerId") Long workshopOwnerId, @PathVariable("workshopId") Long workshopId,  @Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(workshopService.updateWorkshopByOwner(workshopOwnerId, workshopId, workshopInputDto), HttpStatus.ACCEPTED);
    }


    @PutMapping ("/verify/{workshopOwnerId}/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> verifyWorkshopByOwner (@PathVariable("workshopOwnerId") Long workshopOwnerId, @PathVariable("workshopId") Long workshopId, @RequestParam Boolean publishWorkshop) {

        return new ResponseEntity<>(workshopService.verifyWorkshopByOwner(workshopOwnerId, workshopId, publishWorkshop), HttpStatus.ACCEPTED);
    }

//    putmapping: favourites

    // admin:
    @PutMapping ("/admin/{workshopId}")
    public ResponseEntity<Object> verifyWorkshopByAdmin (@PathVariable Long workshopId, @Valid @RequestBody WorkshopInputDto workshopInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(workshopService.verifyWorkshopByAdmin(workshopId, workshopInputDto), HttpStatus.ACCEPTED);
    }

    //owner mag ook eigenworkshop deleten - has role owner - check workshop.getWorkshopOwner().getId() != workshopOwner.getId()
    @DeleteMapping("/{workshopId}")
    public ResponseEntity<HttpStatus> deleteWorkshop(@PathVariable Long workshopId) {
        workshopService.deleteWorkshop(workshopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
