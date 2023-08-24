package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Date;
import java.time.Instant;
import java.util.Objects;

@RestController
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/downloadprofilepic/{fileName}")
    public ResponseEntity<Object> downloadProfilePic(@PathVariable String fileName) {

        Resource resource = fileService.downloadProfilePic(fileName);

        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }
    @PostMapping("/uploadprofilepic/{userId}")
    public ResponseEntity<Object> uploadProfilePic(@PathVariable Long userId, @RequestParam("file") MultipartFile file){

        // next line makes url. example "http://localhost:8080/downloadprofilepic/{fileName}"
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadprofilepic/").path(Objects.requireNonNull(fileName)).toUriString();
        fileService.uploadProfilePic(file, url, userId, fileName);

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
    @GetMapping("/downloadworkshoppic/{fileName}")
    public ResponseEntity<Object> downloadWorkshopPic(@PathVariable String fileName) {

        Resource resource = fileService.downloadWorkshopPic(fileName);

        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }

    @PostMapping("/uploadworkshoppic/{workshopId}")
    public ResponseEntity<Object> uploadWorkshopPic(@PathVariable Long workshopId, @RequestParam("file") MultipartFile file){
        // next line makes url. example "http://localhost:8080/downloadworkshoppic/{fileName}"
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(fileName)).toUriString();

        fileService.uploadWorkshopPic(file, url, workshopId, fileName);

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

