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

//    public Resource downloadProfilePic(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User with ID: " + userId + " doesn't exist."));
//
//        if (user.getProfilePicUrl() == null || user.getFileName() == null){
//            throw new RecordNotFoundException("The file doesn't exist.");
//        }
//        return downloadPic(user.getFileName());
//    }
    public Resource downloadProfilePic(String fileName) {
        return downloadPic(fileName);
    }

    public String uploadProfilePic(MultipartFile file, String url, Long userId, String fileName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to add a photo to this profile.");
        }
        if (user.getProfilePicUrl() != null) {
            Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(user.getFileName());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("A problem occurred with deleting: " + user.getFileName());
            }
        }
        storeFile(file, fileName);
        user.setProfilePicUrl(url);
        user.setFileName(fileName);
        userRepository.save(user);

        return url;
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

    public Resource downloadWorkshopPic(String fileName) {
        return downloadPic(fileName);
    }

    public String uploadWorkshopPic(MultipartFile file, String url, Long workshopId, String fileName) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        storeFile(file, fileName);
        workshop.setWorkshopPicUrl(url);
        workshop.setFileName(fileName);
        workshopRepository.save(workshop);
        return url;
    }

    public boolean deleteWorkshopPic(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID: " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to delete the photo of this workshop.");
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

    public Boolean storeFile(MultipartFile file, String fileName) {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .
        Path filePath = Paths.get(fileStoragePath + File.separator + fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        return true;
    }

    public String storeFile2(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime()))); // added the datefrom etc so files can't have the same name and overwrite .
        Path filePath = Paths.get(fileStoragePath + File.separator + fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
        return fileName;
    }
    public Resource downloadPic(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
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

}