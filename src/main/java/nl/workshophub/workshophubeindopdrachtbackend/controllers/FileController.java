package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.transaction.Transactional;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Objects;

@RestController
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    //TODO cleanup this controller


    @PostMapping("/uploadprofilepic/{userId}")
    public ResponseEntity<Object> uploadProfilePic(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {

        // next line makes url. example "http://localhost:8080/downloadprofilepic/userId"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadprofilepic/").path(Objects.requireNonNull(userId.toString())).toUriString();

        String fileName = fileService.uploadProfilePic(file, url, userId);

//        String fileName = fileManagerService.storeFile(file);
//        URI uri = StringGenerator.uriGenerator(env.getProperty("apiPrefix") + "/files/" + fileName);
//        return ResponseEntity.created(uri).body("File stored");
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());


//        String contentType = file.getContentType();
//
//        String fileName = fileService.storeFile(file, url);

        return ResponseEntity.ok(url);
    }

    @GetMapping("/downloadprofilepic/{userId}")
    public ResponseEntity<Object> downloadProfilePic(@PathVariable Long userId, HttpServletRequest request) {

        Resource resource = fileService.downloadProfilePic(userId);

//        this mediaType decides witch type you accept if you only accept 1 type
        MediaType contentType = MediaType.IMAGE_JPEG;
//        this is going to accept multiple types

//        String mimeType;
//
//        try{
//            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException e) {
//            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//        }


//        for download attachment use next line
//        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename()).body(resource);
//        for showing image in browser
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }

    @DeleteMapping("/deleteprofilepic/{userId}")
    public ResponseEntity<Object> deleteProfilePic(@PathVariable Long userId) {

        if (fileService.deleteProfilePic(userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new BadRequestException("file does not exist in the system");
        }


    }


    //TODO check of het tegelijk met aanmaken workshop kan (in requestbody van volledige workshop?)
    @PostMapping("/uploadworkshoppic/{workshopId}")
    public ResponseEntity<Object> uploadWorkshopPic(@PathVariable Long workshopId, @RequestParam("file") MultipartFile file) throws IOException {

        // next line makes url. example "http://localhost:8080/downloadworkshoppic/workshopId"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadworkshoppic/").path(Objects.requireNonNull(workshopId.toString())).toUriString();

        String fileName = fileService.uploadWorkshopPic(file, url, workshopId);

        return ResponseEntity.ok(url);
    }

    @GetMapping("/downloadworkshoppic/{workshopId}")
    public ResponseEntity<Object> downloadWorkshopPic(@PathVariable Long workshopId, HttpServletRequest request) {

        Resource resource = fileService.downloadWorkshopPic(workshopId);

        MediaType contentType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(contentType).header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename()).body(resource);
    }

    // TODO: 05/07/2023 Delete nog testen met postman

    @DeleteMapping("/deleteworkshoppic/{workshopId}")
    public ResponseEntity<Object> deleteWorkshopPic(@PathVariable Long workshopId) {

        if (fileService.deleteWorkshopPic(workshopId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new BadRequestException("file does not exist in the system");
        }


    }


}

