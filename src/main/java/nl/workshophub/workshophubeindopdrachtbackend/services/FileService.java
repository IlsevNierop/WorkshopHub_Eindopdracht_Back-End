package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

@Service
public class FileService {
    @Value("${my.upload_location}")
    private final Path fileStoragePath;
    private final String fileStorageLocation;
    private final UserRepository userRepository;

    public FileService(@Value("${my.upload_location}") String fileStorageLocation, UserRepository userRepository) {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.userRepository = userRepository;

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

    }

    public String storeFile(MultipartFile file, String url, Long userId) throws RecordNotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));

        // TODO: 02/07/2023 when filenames have the same name, they get overwritten?

//        String fileNameAddition = StringUtils.cleanPath(Objects.requireNonNull(String.valueOf(Date.from(Instant.now()).getTime()))); // to prevent that files can have the same name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename() + String.valueOf(Date.from(Instant.now()).getTime())));

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

    public String updateProfilePic(MultipartFile file, String url, Long userId) throws RecordNotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(user.getFileName());

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred with deleting: " + user.getFileName());
        }


        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        Path filePath = Paths.get(fileStoragePath + "/" + fileName);

//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//
//        Path filePath = Paths.get(fileStoragePath + "/" + fileName + String.valueOf(Date.from(Instant.now()).getTime()));

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


    // TODO: 30/06/2023 werkt nog niet
    public Resource downLoadFile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("user niet gevonden"));

        if (user.getProfilePicUrl() == null){
            throw new BadRequestException("The file doesn't exist.");
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
}