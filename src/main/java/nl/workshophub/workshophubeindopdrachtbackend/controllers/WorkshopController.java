package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;
    private final FileService fileService;

    public WorkshopController(WorkshopService workshopService, FileService fileService) {
        this.workshopService = workshopService;
        this.fileService = fileService;
    }

    //open

    @GetMapping
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(@RequestParam(value = "userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(userId), HttpStatus.OK);
    }

    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopByIdVerifiedAndPublish(@PathVariable("workshopId") Long workshopId, @RequestParam(value = "userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getWorkshopByIdVerifiedAndPublish(workshopId, userId), HttpStatus.OK);
    }

    @GetMapping("/workshopowner/{workshopOwnerId}")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(@PathVariable Long workshopOwnerId, @RequestParam(value = "userId", required = false) Long userId) {
        return new ResponseEntity<>(workshopService.getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(workshopOwnerId, userId), HttpStatus.OK);
    }


    //owner
    @GetMapping("/workshopowner/workshop/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> getWorkshopByIdForWorkshopOwner(@PathVariable("workshopId") Long workshopId) {
        return new ResponseEntity<>(workshopService.getWorkshopByIdForWorkshopOwner(workshopId), HttpStatus.OK);
    }


    // filter at frontend on what to verify etc. show startdate today - but possibility to go back in time
    @GetMapping("/workshopowner/all/{workshopOwnerId}")
    public ResponseEntity<List<WorkshopOutputDto>> getAllWorkshopsFromWorkshopOwner(@PathVariable Long workshopOwnerId) {
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


    @PostMapping(value = "/workshopowner/{workshopOwnerId}", consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<Object> createWorkshop(
            @PathVariable Long workshopOwnerId,
            @RequestPart @Valid WorkshopInputDto workshopInputDto,
            BindingResult bindingResultWorkshopInputDto,
            @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        if (bindingResultWorkshopInputDto.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResultWorkshopInputDto));
        }

        WorkshopOutputDto workshopOutputDto = workshopService.createWorkshop(workshopOwnerId, workshopInputDto);
        if (file != null) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(workshopOutputDto.id.toString())).toUriString();

            String fileName = fileService.uploadWorkshopPic(file, url, workshopOutputDto.id);

//            fileController.uploadWorkshopPic(workshopOutputDto.id, file);
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(workshopOutputDto);
    }

    //TODO add pic to putmapping

    @PutMapping("/favourite/{userId}/{workshopId}")
    public ResponseEntity<List<WorkshopOutputDto>> addOrRemoveWorkshopFavourites(@PathVariable("userId") Long userId, @PathVariable("workshopId") Long workshopId, @RequestParam Boolean favourite) {
        return new ResponseEntity<>(workshopService.addOrRemoveWorkshopFavourites(userId, workshopId, favourite), HttpStatus.ACCEPTED);
    }


    @PutMapping(value= "/workshopowner/{workshopOwnerId}/{workshopId}", consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<Object> updateWorkshopByOwner(
            @PathVariable("workshopOwnerId") Long workshopOwnerId,
            @PathVariable("workshopId") Long workshopId,
            @RequestPart @Valid WorkshopInputDto workshopInputDto,
            BindingResult bindingResult,
            @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }

        WorkshopOutputDto workshopOutputDto = workshopService.verifyWorkshopByAdmin(workshopId, workshopInputDto);
        if (file != null) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(workshopOutputDto.id.toString())).toUriString();

            String fileName = fileService.uploadWorkshopPic(file, url, workshopOutputDto.id);

        }

        return new ResponseEntity<>(workshopOutputDto, HttpStatus.ACCEPTED);
    }


    @PutMapping("/workshopowner/verify/{workshopOwnerId}/{workshopId}")
    public ResponseEntity<WorkshopOutputDto> verifyWorkshopByOwner(@PathVariable("workshopOwnerId") Long workshopOwnerId, @PathVariable("workshopId") Long workshopId, @RequestParam Boolean publishWorkshop) {

        return new ResponseEntity<>(workshopService.verifyWorkshopByOwner(workshopOwnerId, workshopId, publishWorkshop), HttpStatus.ACCEPTED);
    }


    // admin:
    @PutMapping(value="/admin/{workshopId}", consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<Object> verifyWorkshopByAdmin(
            @PathVariable Long workshopId,
            @RequestPart @Valid WorkshopInputDto workshopInputDto,
            BindingResult bindingResult,
            @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        WorkshopOutputDto workshopOutputDto = workshopService.verifyWorkshopByAdmin(workshopId, workshopInputDto);
        if (file != null) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(workshopOutputDto.id.toString())).toUriString();

            String fileName = fileService.uploadWorkshopPic(file, url, workshopOutputDto.id);

        }
        return new ResponseEntity<>(workshopOutputDto, HttpStatus.ACCEPTED);
    }



    //owner mag ook eigenworkshop deleten - has role owner - check workshop.getWorkshopOwner().getId() != workshopOwner.getId()
    @DeleteMapping("/workshopowner/{workshopId}")
    public ResponseEntity<HttpStatus> deleteWorkshop(@PathVariable Long workshopId) {
        workshopService.deleteWorkshop(workshopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
