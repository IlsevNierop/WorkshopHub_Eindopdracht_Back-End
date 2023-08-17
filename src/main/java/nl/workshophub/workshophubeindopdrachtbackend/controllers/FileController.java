package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@RestController
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/downloadprofilepic/{userId}")
    public ResponseEntity<Object> downloadProfilePic(@PathVariable Long userId) {

        Resource resource = fileService.downloadProfilePic(userId);

        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }
    @PostMapping("/uploadprofilepic/{userId}")
    public ResponseEntity<Object> uploadProfilePic(@PathVariable Long userId, @RequestParam("file") MultipartFile file){

        // next line makes url. example "http://localhost:8080/downloadprofilepic/{userId}"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadprofilepic/").path(Objects.requireNonNull(userId.toString())).toUriString();
        String fileName = fileService.uploadProfilePic(file, url, userId);

        return ResponseEntity.ok(url);
    }


    @DeleteMapping("/deleteprofilepic/{userId}")
    public ResponseEntity<Object> deleteProfilePic(@PathVariable Long userId) {

        if (fileService.deleteProfilePic(userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new BadRequestException("file does not exist in the system");
        }


    }
    @GetMapping("/downloadworkshoppic/{workshopId}")
    public ResponseEntity<Object> downloadWorkshopPic(@PathVariable Long workshopId) {

        Resource resource = fileService.downloadWorkshopPic(workshopId);

        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }

    @PostMapping("/uploadworkshoppic/{workshopId}")
    public ResponseEntity<Object> uploadWorkshopPic(@PathVariable Long workshopId, @RequestParam("file") MultipartFile file){
        // next line makes url. example "http://localhost:8080/downloadworkshoppic/{workshopId}"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(workshopId.toString())).toUriString();

        String fileName = fileService.uploadWorkshopPic(file, url, workshopId);

        return ResponseEntity.ok(url);
    }

    @DeleteMapping("/deleteworkshoppic/{workshopId}")
    public ResponseEntity<Object> deleteWorkshopPic(@PathVariable Long workshopId) {

        if (fileService.deleteWorkshopPic(workshopId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new BadRequestException("file does not exist in the system");
        }
    }
}

