package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.CheckAuthorization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

@Service
public class FileService {
    @Value("${my.upload_location}")
    private final Path fileStoragePath;
    private final String fileStorageLocation;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    public FileService(@Value("${my.upload_location}") String fileStorageLocation, UserRepository userRepository, WorkshopRepository workshopRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

    }

    public String uploadProfilePic(MultipartFile file, String url, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to add a photo to this profile.");
        }

        // delete old profilepic if it exists, otherwise the server/folder gets filled up with a lot of unnecessary pictures
        if (user.getProfilePicUrl() != null) {

            Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(user.getFileName());

            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("A problem occurred with deleting: " + user.getFileName());
            }
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .

        Path filePath = Paths.get(fileStoragePath + File.separator + fileName);
//
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//
//        Path filePath = Paths.get(fileStoragePath + "/" + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }

        user.setProfilePicUrl(url);
        user.setFileName(fileName);
        userRepository.save(user);

        return fileName;
    }


    // TODO: 30/06/2023 nu staat het op permit all
    public Resource downloadProfilePic(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User with ID: " + userId + " doesn't exist."));

        if (user.getProfilePicUrl() == null || user.getFileName() == null){
            throw new RecordNotFoundException("The file doesn't exist.");
        }

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(user.getFileName());

//        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }

        if (!resource.exists() || !resource.isReadable()) {
            throw new BadRequestException("The file doesn't exist or is not readable.");
        }
        return resource;

    }

    public boolean deleteProfilePic(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to delete the photo of this profile.");
        }

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(user.getFileName());

        user.setProfilePicUrl(null);
        user.setFileName(null);
        userRepository.save(user);

        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred with deleting: " + user.getFileName());
        }


    }

    public String uploadWorkshopPic(MultipartFile file, String url, Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));

        //TODO check authentication? already checked in the workshopservice
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
//            throw new ForbiddenException("You're not allowed to add a photo to this profile.");
//        }


        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .

        Path filePath = Paths.get(fileStoragePath + File.separator + fileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }

        workshop.setWorkshopPicUrl(url);
        workshop.setFileName(fileName);
        workshopRepository.save(workshop);

        return fileName;
    }


    // TODO: 30/06/2023 nu staat het op permit all
    public Resource downloadWorkshopPic(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID: " + workshopId + " doesn't exist."));

        if (workshop.getWorkshopPicUrl() == null || workshop.getFileName() == null){
            throw new RecordNotFoundException("The file doesn't exist.");
        }

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(workshop.getFileName());

//        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);

        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }

        if (!resource.exists() || !resource.isReadable()) {
            throw new BadRequestException("The file doesn't exist or is not readable.");
        }
        return resource;

    }


    public boolean deleteWorkshopPic(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID: " + workshopId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to delete the photo of this profile.");
        }

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(workshop.getFileName());

        workshop.setWorkshopPicUrl(null);
        workshop.setFileName(null);
        workshopRepository.save(workshop);

        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred with deleting: " + workshop.getFileName());
        }


    }

}