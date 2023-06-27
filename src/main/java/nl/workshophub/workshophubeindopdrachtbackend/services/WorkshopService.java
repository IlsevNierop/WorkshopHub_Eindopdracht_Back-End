package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
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
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            if (userId != null) {
                User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The customer with ID " + userId + " doesn't exist."));
                workshopOutputDto.isFavourite = customer.getFavouriteWorkshops().contains(w);
            }
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto getWorkshopByIdVerifiedAndPublish(Long workshopId, Long userId) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        // using Boolean.TRUE because != true gives errors when the variable is null.
        if (workshop.getWorkshopVerified() != Boolean.TRUE || workshop.getPublishWorkshop() != Boolean.TRUE){
            throw new BadRequestException("You're not allowed to view this workshop.");
        }
        WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(workshop);
        if (userId != null) {
            User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The customer with ID " + userId + " doesn't exist."));
                workshopOutputDto.isFavourite = customer.getFavouriteWorkshops().contains(workshop);
                }
        return workshopOutputDto;
    }

    public List<WorkshopOutputDto> getAllWorkshopsFromWorkshopOwnerVerifiedAndPublish(Long workshopOwnerId, Long userId) {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now(), workshopOwnerId);
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            if (userId != null) {
                User customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The customer with ID " + userId + " doesn't exist."));
                workshopOutputDto.isFavourite = customer.getFavouriteWorkshops().contains(w);
            }
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }


    public WorkshopOutputDto getWorkshopByIdForWorkshopOwner( Long workshopId) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to view the workshop from this workshopowner.");
        }
        if (!workshop.getWorkshopOwner().getWorkshopOwner() || workshop.getWorkshopOwner().getWorkshopOwnerVerified() != Boolean.TRUE || workshop.getWorkshopOwner().getId() != workshop.getWorkshopOwner().getId()) {
            throw new BadRequestException("You're not allowed to view this workshop.");
        }
        return transferWorkshopToWorkshopOutputDto(workshop);
    }


    public List<WorkshopOutputDto> getAllWorkshopsFromWorkshopOwner(Long workshopOwnerId) {
        List<Workshop> workshops = workshopRepository.findByWorkshopOwnerId(workshopOwnerId);
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshopowner with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to view the workshops from this workshopowner.");
        }
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
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
        List<Workshop> workshops = workshopRepository.findAll();
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto getWorkshopById(Long workshopId) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto createWorkshop(Long workshopOwnerId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to create a workshops from this workshopowner's account.");
        }
        if (workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || !workshopOwner.getWorkshopOwner()) {
            throw new BadRequestException("You're not allowed to create a new workshop.");
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

    public List<WorkshopOutputDto> addOrRemoveWorkshopFavourites(Long userId, Long workshopId, Boolean favourite) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to add favourites to this user's account.");
        }
        if (workshop.getWorkshopVerified() != Boolean.TRUE || workshop.getPublishWorkshop() != Boolean.TRUE) {
            throw new BadRequestException("You're not allowed to view this workshop and add it to your favourites.");
        }
        if (favourite) {
            user.getFavouriteWorkshops().add(workshop);
        }
        else {
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

    public WorkshopOutputDto updateWorkshopByOwner(Long workshopOwnerId, Long workshopId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to update this workshop.");
        }

        if (!workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || workshop.getWorkshopOwner().getId() != workshopOwner.getId()) {
            throw new BadRequestException("You're not allowed to update this workshop.");
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
    public WorkshopOutputDto verifyWorkshopByOwner(Long workshopOwnerId, Long workshopId, Boolean publishWorkshop) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to publish this workshop.");
        }

        if (workshop.getWorkshopVerified() != Boolean.TRUE) {
            throw new BadRequestException("This workshop is not yet approved by the administrator, therefore it can't be published.");
        }
        if (!workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || workshop.getWorkshopOwner().getId() != workshopOwner.getId()) {
            throw new BadRequestException("You're not allowed to publish this workshop, only the verified owner can publish.");
        }
        workshop.setPublishWorkshop(publishWorkshop);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto verifyWorkshopByAdmin(Long workshopId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopId + " doesn't exist."));
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        // After verifying / disapproving the workshop by admin, publish workshop will automatically get a default value.
        workshop.setPublishWorkshop(null);
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public void deleteWorkshop(Long workshopId) throws BadRequestException, RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())){
            throw new BadRequestException("You're not allowed to delete this workshop.");
        }

        if (!workshop.getWorkshopBookings().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has bookings.");
        }
        if (!workshop.getWorkshopReviews().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has reviews.");
        }
        //can't remove workshop, if owner has set publish on true. Then owner needs to verify delete - by setting publish workshop on false.
        if (workshop.getPublishWorkshop() == Boolean.TRUE) {
            throw new BadRequestException("This workshop can't be removed, because the workshop owner has verified the workshop for publishing.");
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
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        workshopOutputDto.averageRatingWorkshopOwnerReviews = workshop.getWorkshopOwner().calculateAverageRatingWorkshopOwner();
        workshopOutputDto.amountOfFavsAndBookings = workshop.calculateAmountOfFavsAndBookingsWorkshop();

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
