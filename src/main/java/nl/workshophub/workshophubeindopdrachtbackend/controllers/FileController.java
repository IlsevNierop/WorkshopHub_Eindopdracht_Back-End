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

    //    post for single upload


    @PostMapping("/uploadprofilepic/{userId}")
    public ResponseEntity<Object> singleFileUpload(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {

        // next line makes url. example "http://localhost:8080/downloadprofilepic/userId"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadprofilepic/").path(Objects.requireNonNull(userId.toString())).toUriString();

        String fileName = fileService.storeFile(file, url, userId);

//        String fileName = fileManagerService.storeFile(file);
//        URI uri = StringGenerator.uriGenerator(env.getProperty("apiPrefix") + "/files/" + fileName);
//        return ResponseEntity.created(uri).body("File stored");
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());



//        String contentType = file.getContentType();
//
//        String fileName = fileService.storeFile(file, url);

        return ResponseEntity.ok("File uploaded" );
    }

    @PutMapping("/updateprofilepic/{userId}")
    public ResponseEntity<Object> updateProfilePic(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {

        // next line makes url. example "http://localhost:8080/downloadprofilepic/userId"
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadprofilepic/").path(Objects.requireNonNull(userId.toString())).toUriString();

        String fileName = fileService.updateProfilePic(file, url, userId);

//        String fileName = fileManagerService.storeFile(file);
//        URI uri = StringGenerator.uriGenerator(env.getProperty("apiPrefix") + "/files/" + fileName);
//        return ResponseEntity.created(uri).body("File stored");
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());



//        String contentType = file.getContentType();
//
//        String fileName = fileService.storeFile(file, url);

        return ResponseEntity.ok("File uploaded" );
    }


    @GetMapping("/downloadprofilepic/{userId}")
    public ResponseEntity<Object> downloadProfilePic(@PathVariable Long userId, HttpServletRequest request) {

        Resource resource = fileService.downLoadFile(userId);

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

        }
        if (fileService.deleteProfilePic(userId)) {
            return ResponseEntity.ok("Profile picture of user with ID : " + userId + " is deleted");
        } else {
            throw new BadRequestException("file does not exist in the system");
        }


    }






}

