package nl.workshophub.workshophubeindopdrachtbackend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.CheckAuthorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;

    public WorkshopService(WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
    }

    public List<WorkshopOutputDto> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(Long userId) {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();

        if (userId != null) {
            User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
            for (Workshop w : workshops) {
                WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w, customer);
                workshopOutputDtos.add(workshopOutputDto);
            }
        } else {
            for (Workshop w : workshops) {
                WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
                workshopOutputDtos.add(workshopOutputDto);
            }
        }

        return workshopOutputDtos;
    }

    public List<WorkshopOutputDto> getAllFavouriteWorkshopsUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the favourite workshops for this user.");
        }

        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        //TODO nu ook workshops uit het verleden? In documentatie noemen?
        for (Workshop w : user.getFavouriteWorkshops()) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w, user);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto getWorkshopByIdVerifiedAndPublish(Long workshopId, Long userId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        // using Boolean.TRUE because != true gives errors when the variable is null.
        if (workshop.getWorkshopVerified() != Boolean.TRUE || workshop.getPublishWorkshop() != Boolean.TRUE) {
            throw new ForbiddenException("You're not allowed to view this workshop.");
        }
        if (userId != null) {
            User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
            return transferWorkshopToWorkshopOutputDto(workshop, customer);
        }
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public List<WorkshopOutputDto> getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(Long workshopOwnerId, Long userId) {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now(), workshopOwnerId);
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        if (userId != null) {
            User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
            for (Workshop w : workshops) {
                WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w, customer);
                workshopOutputDtos.add(workshopOutputDto);
            }
        } else {
            for (Workshop w : workshops) {
                WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
                workshopOutputDtos.add(workshopOutputDto);
            }
        }
        return workshopOutputDtos;
    }


    public WorkshopOutputDto getWorkshopByIdForWorkshopOwner(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the workshop from this workshopowner.");
        }
        if (!workshop.getWorkshopOwner().getWorkshopOwner() || workshop.getWorkshopOwner().getWorkshopOwnerVerified() != Boolean.TRUE || workshop.getWorkshopOwner().getId() != workshop.getWorkshopOwner().getId()) {
            throw new ForbiddenException("You're not allowed to view this workshop.");
        }
        return transferWorkshopToWorkshopOutputDto(workshop, workshop.getWorkshopOwner());
    }


    public List<WorkshopOutputDto> getAllWorkshopsFromWorkshopOwnerByWorkshopOwner(Long workshopOwnerId) {
        List<Workshop> workshops = workshopRepository.findByWorkshopOwnerId(workshopOwnerId);
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the workshops from this workshopowner.");
        }
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w, workshopOwner);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public List<WorkshopOutputDto> getAllWorkshopsToVerifyFromWorkshopOwner(Long workshopOwnerId) {
        List<Workshop> workshops = workshopRepository.findByWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsNullOrPublishWorkshopIsFalseOrderByDate(workshopOwnerId);
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the workshops from this workshopowner.");
        }
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w, workshopOwner);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }


    public List<WorkshopOutputDto> getAllWorkshopsToVerify() {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsNullOrWorkshopVerifiedIsFalseOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public List<WorkshopOutputDto> getAllWorkshops() {
        //admin - while being logged in as admin - doesn't see favourite with this method - frontend admin can switch to user/ workshopowner view.
        List<Workshop> workshops = workshopRepository.findAll();
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto getWorkshopById(Long workshopId) {
        //admin - while being logged in as admin - doesn't see favourite with this method - frontend admin can switch to user/ workshopowner view.
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto createWorkshop(Long workshopOwnerId, WorkshopInputDto workshopInputDto) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to create a workshops from this workshopowner's account.");
        }
        if (workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || !workshopOwner.getWorkshopOwner()) {
            throw new ForbiddenException("You're not allowed to create a new workshop, only a verified owner can publish.");
        }
        Workshop workshop = new Workshop();
        workshop = transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        workshop.setWorkshopOwner(workshopOwner);
        // when creating a new workshop, publishWorkshop, workshopVerified and feedbackAdmin need to get default values.
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshop.setFeedbackAdmin(null);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    // incl file and string
//    public WorkshopOutputDto createWorkshop(Long workshopOwnerId, String workshopInputDto) {
//        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
//            throw new ForbiddenException("You're not allowed to create a workshops from this workshopowner's account.");
//        }
//        if (workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || !workshopOwner.getWorkshopOwner()) {
//            throw new ForbiddenException("You're not allowed to create a new workshop, only a verified owner can publish.");
//        }
//
//        WorkshopInputDto workshopInputDto1 = new WorkshopInputDto();
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            workshopInputDto1 = objectMapper.readValue(workshopInputDto, WorkshopInputDto.class);
//        }
//        catch (IOException err) {
//            System.out.println("Error " + err.toString());
//        }
//
//
//        Workshop workshop = new Workshop();
//        workshop = transferWorkshopInputDtoToWorkshop(workshopInputDto1, workshop);
//        workshop.setWorkshopOwner(workshopOwner);
//        // when creating a new workshop, publishWorkshop, workshopVerified and feedbackAdmin need to get default values.
//        workshop.setPublishWorkshop(null);
//        workshop.setWorkshopVerified(null);
//        workshop.setFeedbackAdmin(null);
//        workshopRepository.save(workshop);
//        return transferWorkshopToWorkshopOutputDto(workshop);
//    }

    public List<WorkshopOutputDto> addOrRemoveWorkshopFavourites(Long userId, Long workshopId, Boolean favourite) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to add favourites to this user's account.");
        }
        if (workshop.getWorkshopVerified() != Boolean.TRUE || workshop.getPublishWorkshop() != Boolean.TRUE) {
            throw new ForbiddenException("You're not allowed to view this workshop and add it to your favourites.");
        }
        if (favourite) {
            user.getFavouriteWorkshops().add(workshop);
        } else {
            user.getFavouriteWorkshops().remove(workshop);
        }
        userRepository.save(user);

        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : user.getFavouriteWorkshops()) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            workshopOutputDto.isFavourite = true;
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto updateWorkshopByOwner(Long workshopOwnerId, Long workshopId, WorkshopInputDto workshopInputDto) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to update this workshop.");
        }

        if (!workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || workshop.getWorkshopOwner().getId() != workshopOwner.getId()) {
            throw new ForbiddenException("You're not allowed to update this workshop.");
        }
        // to prevent the owner from overwriting the feedback from the admin, I'll make sure the inputdto has the same feedback as the original workshop. This could be prevented with a different inputdto for workshopowner (excluding feedbackadmin and verifyworkshop) and for admin.
        workshopInputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        // after updating a workshop, publish and verify will be automatically set to default values.
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    @PutMapping
    public WorkshopOutputDto verifyWorkshopByOwner(Long workshopId, Boolean publishWorkshop) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to publish this workshop.");
        }

        if (workshop.getWorkshopVerified() != Boolean.TRUE) {
            throw new BadRequestException("This workshop is not yet approved by the administrator, therefore it can't be published.");
        }
        if (!workshop.getWorkshopOwner().getWorkshopOwner() || workshop.getWorkshopOwner().getWorkshopOwnerVerified() != Boolean.TRUE) {
            throw new ForbiddenException("You're not allowed to publish this workshop, only the verified owner can publish.");
        }
        workshop.setPublishWorkshop(publishWorkshop);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto verifyWorkshopByAdmin(Long workshopId, WorkshopInputDto workshopInputDto) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        // After verifying / disapproving the workshop by admin, publish workshop will automatically get a default value.
        workshop.setPublishWorkshop(null);
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public void deleteWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to delete this workshop.");
        }

        if (!workshop.getWorkshopBookings().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has one or more relation with bookings.");
        }
        if (!workshop.getWorkshopReviews().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has one or more relation with reviews.");
        }
        // workshop is referencing entity in manytomany relation with user, regarding favourites, the workshop should be able to be deleted, even if users have it listed as favourite, therefore delete all those associations first:
        if (!workshop.getFavsUser().isEmpty()) {
            for (User user : workshop.getFavsUser()) {
                user.getFavouriteWorkshops().remove(workshop);
            }
        }
        workshopRepository.delete(workshop);

    }

    public WorkshopOutputDto transferWorkshopToWorkshopOutputDto(Workshop workshop) {
        WorkshopOutputDto workshopOutputDto = new WorkshopOutputDto();
        workshopOutputDto.id = workshop.getId();
        workshopOutputDto.title = workshop.getTitle();
        workshopOutputDto.date = workshop.getDate();
        workshopOutputDto.startTime = workshop.getStartTime();
        workshopOutputDto.endTime = workshop.getEndTime();
        workshopOutputDto.price = workshop.getPrice();
        workshopOutputDto.inOrOutdoors = workshop.getInOrOutdoors();
        workshopOutputDto.location = workshop.getLocation();
        workshopOutputDto.highlightedInfo = workshop.getHighlightedInfo();
        workshopOutputDto.description = workshop.getDescription();
        workshopOutputDto.amountOfParticipants = workshop.getAmountOfParticipants();
        workshopOutputDto.workshopCategory1 = workshop.getWorkshopCategory1();
        workshopOutputDto.workshopCategory2 = workshop.getWorkshopCategory2();
        workshopOutputDto.workshopVerified = workshop.getWorkshopVerified();
        workshopOutputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        workshopOutputDto.publishWorkshop = workshop.getPublishWorkshop();
        workshopOutputDto.workshopOwnerReviews = createReviewOutPutDtosFromWorkshopOwner(workshop);
        workshopOutputDto.spotsAvailable = workshop.getAvailableSpotsWorkshop();
        workshopOutputDto.workshopOwnerId = workshop.getWorkshopOwner().getId();
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        if (workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner() != null) {
            workshopOutputDto.averageRatingWorkshopOwnerReviews = workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner().get(0);
            workshopOutputDto.numberOfReviews = workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner().get(1);
        }
        workshopOutputDto.amountOfFavsAndBookings = workshop.calculateAmountOfFavsAndBookingsWorkshop();
        workshopOutputDto.workshopPicUrl = workshop.getWorkshopPicUrl();

        return workshopOutputDto;
    }

    //Overloading, in case there is a user the variable isfavourite is made.
    public WorkshopOutputDto transferWorkshopToWorkshopOutputDto(Workshop workshop, User user) {
        //Could also call the other transfer method in stead of having duplicated code, but this is officially 'better' practice.
        //TODO check met Paul
        WorkshopOutputDto workshopOutputDto = new WorkshopOutputDto();
        workshopOutputDto.id = workshop.getId();
        workshopOutputDto.title = workshop.getTitle();
        workshopOutputDto.date = workshop.getDate();
        workshopOutputDto.startTime = workshop.getStartTime();
        workshopOutputDto.endTime = workshop.getEndTime();
        workshopOutputDto.price = workshop.getPrice();
        workshopOutputDto.inOrOutdoors = workshop.getInOrOutdoors();
        workshopOutputDto.location = workshop.getLocation();
        workshopOutputDto.highlightedInfo = workshop.getHighlightedInfo();
        workshopOutputDto.description = workshop.getDescription();
        workshopOutputDto.amountOfParticipants = workshop.getAmountOfParticipants();
        workshopOutputDto.workshopCategory1 = workshop.getWorkshopCategory1();
        workshopOutputDto.workshopCategory2 = workshop.getWorkshopCategory2();
        workshopOutputDto.workshopVerified = workshop.getWorkshopVerified();
        workshopOutputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        workshopOutputDto.publishWorkshop = workshop.getPublishWorkshop();
        workshopOutputDto.workshopOwnerReviews = createReviewOutPutDtosFromWorkshopOwner(workshop);
        workshopOutputDto.spotsAvailable = workshop.getAvailableSpotsWorkshop();
        workshopOutputDto.workshopOwnerId = workshop.getWorkshopOwner().getId();
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        if (workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner() != null) {
            workshopOutputDto.averageRatingWorkshopOwnerReviews = workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner().get(0);
            workshopOutputDto.numberOfReviews = workshop.getWorkshopOwner().calculateAverageRatingAndNumberReviewsWorkshopOwner().get(1);
        }
        workshopOutputDto.amountOfFavsAndBookings = workshop.calculateAmountOfFavsAndBookingsWorkshop();
        workshopOutputDto.isFavourite = user.getFavouriteWorkshops().contains(workshop);
        workshopOutputDto.workshopPicUrl = workshop.getWorkshopPicUrl();

        return workshopOutputDto;
    }

    public Workshop transferWorkshopInputDtoToWorkshop(WorkshopInputDto workshopInputDto, Workshop workshop) {
        workshop.setTitle(workshopInputDto.title);
        workshop.setDate(workshopInputDto.date);
        workshop.setStartTime(workshopInputDto.startTime);
        workshop.setEndTime(workshopInputDto.endTime);
        workshop.setPrice(workshopInputDto.price);
        workshop.setInOrOutdoors(workshopInputDto.inOrOutdoors);
        workshop.setLocation(workshopInputDto.location);
        if (workshopInputDto.highlightedInfo != null) {
            workshop.setHighlightedInfo(workshopInputDto.highlightedInfo);
        }
        workshop.setDescription(workshopInputDto.description);
        workshop.setAmountOfParticipants(workshopInputDto.amountOfParticipants);
        workshop.setWorkshopCategory1(workshopInputDto.workshopCategory1);
        if (workshopInputDto.workshopCategory2 != null) {
            workshop.setWorkshopCategory2(workshopInputDto.workshopCategory2);
        }
        // this can be set (by admin) via de workshopinputdto, since the admin doesn't only verify, but can also update the whole workshop
        if (workshopInputDto.workshopVerified != null) {
            workshop.setWorkshopVerified(workshopInputDto.workshopVerified);
        }
        if (workshopInputDto.feedbackAdmin != null) {
            workshop.setFeedbackAdmin(workshopInputDto.feedbackAdmin);
        }
        workshop.setPublishWorkshop(workshopInputDto.publishWorkshop);

        return workshop;

    }

    public List<ReviewOutputDto> createReviewOutPutDtosFromWorkshopOwner(Workshop workshop) {
        List<ReviewOutputDto> allReviewOutputDtosFromWorkshopOwner = new ArrayList<>();
        if (workshop.getWorkshopOwner().getWorkshops() != null) {
            for (Workshop w : workshop.getWorkshopOwner().getWorkshops()) {
                if (w.getWorkshopReviews() != null) {
                    for (Review r : w.getWorkshopReviews()) {
                        if (r.getReviewVerified() == Boolean.TRUE) {
                            allReviewOutputDtosFromWorkshopOwner.add(ReviewServiceTransferMethod.transferReviewToReviewOutputDto(r));
                        }
                    }
                }
            }
        }
        return allReviewOutputDtosFromWorkshopOwner;
    }


}
