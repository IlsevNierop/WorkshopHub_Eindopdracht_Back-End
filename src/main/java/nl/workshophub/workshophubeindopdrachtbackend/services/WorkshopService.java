package nl.workshophub.workshophubeindopdrachtbackend.services;

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
        User customer = null;
        if (userId != null) {
            customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        }
        return processWorkshopsToWorkshopOutputDtos(workshops, customer);
    }

    public List<WorkshopOutputDto> getAllFavouriteWorkshopsUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the favourite workshops for this user.");
        }
        List<Workshop> favouriteWorkshops = new ArrayList<>(user.getFavouriteWorkshops());
        return processWorkshopsToWorkshopOutputDtos(favouriteWorkshops, user);

    }

    public WorkshopOutputDto getWorkshopByIdVerifiedAndPublish(Long workshopId, Long userId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
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
        User customer = null;
        if (userId != null) {
            customer = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        }
        return processWorkshopsToWorkshopOutputDtos(workshops, customer);
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
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the workshops from this workshopowner.");
        }
        List<Workshop> workshops = workshopRepository.findByWorkshopOwnerId(workshopOwnerId);
        return processWorkshopsToWorkshopOutputDtos(workshops, workshopOwner);
    }

    public List<WorkshopOutputDto> getAllWorkshopsToPublishFromWorkshopOwner(Long workshopOwnerId) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view the workshops from this workshopowner.");
        }
        List<Workshop> workshops = workshopRepository.findByWorkshopOwnerIdAndWorkshopVerifiedIsTrueAndPublishWorkshopIsNullOrPublishWorkshopIsFalseOrderByDate(workshopOwnerId);
        return processWorkshopsToWorkshopOutputDtos(workshops, workshopOwner);
    }

    public List<WorkshopOutputDto> getAllWorkshopsToVerify() {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsNullOrWorkshopVerifiedIsFalseOrderByDate(java.time.LocalDate.now());
        return processWorkshopsToWorkshopOutputDtos(workshops, null);
    }

    public List<WorkshopOutputDto> getAllWorkshops() {
        List<Workshop> workshops = workshopRepository.findAll();
        return processWorkshopsToWorkshopOutputDtos(workshops, null);
    }

    public WorkshopOutputDto getWorkshopById(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto createWorkshop(Long workshopOwnerId, WorkshopInputDto workshopInputDto) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to create a workshop from this workshopowner's account.");
        }
        if (workshopOwner.getWorkshopOwnerVerified() != Boolean.TRUE || !workshopOwner.getWorkshopOwner()) {
            throw new ForbiddenException("You're not allowed to create a new workshop, only a verified owner can create and publish workshops.");
        }
        Workshop workshop = transferWorkshopInputDtoToWorkshop(workshopInputDto, new Workshop());
        workshop.setWorkshopOwner(workshopOwner);
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshop.setFeedbackAdmin(null);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public List<WorkshopOutputDto> addOrRemoveWorkshopToFavourites(Long userId, Long workshopId, Boolean favourite) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to add favourites to this user's account.");
        }
        if (favourite) {
            user.getFavouriteWorkshops().add(workshop);
        } else {
            user.getFavouriteWorkshops().remove(workshop);
        }
        userRepository.save(user);
        List<Workshop> favouriteWorkshops = new ArrayList<>(user.getFavouriteWorkshops());
        return processWorkshopsToWorkshopOutputDtos(favouriteWorkshops, user);
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
        if (workshop.calculateAmountOfBookingsWorkshop() > workshopInputDto.amountOfParticipants) {
            throw new BadRequestException("You're trying to set the amount of participants to: " + workshopInputDto.amountOfParticipants + " while there are already " + workshop.calculateAmountOfBookingsWorkshop() + " bookings on this workshop.");
        }
        workshopInputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    @PutMapping
    public WorkshopOutputDto publishWorkshopByOwner(Long workshopId, Boolean publishWorkshop) {
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
        if (workshop.calculateAmountOfBookingsWorkshop() > workshopInputDto.amountOfParticipants) {
            throw new BadRequestException("You're trying to set the amount of participants to: " + workshopInputDto.amountOfParticipants + " while there are already " + workshop.calculateAmountOfBookingsWorkshop() + " bookings on this workshop.");
        }
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
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
        if (!workshop.getFavsUsers().isEmpty()) {
            for (User user : workshop.getFavsUsers()) {
                user.getFavouriteWorkshops().remove(workshop);
            }
        }
        workshopRepository.delete(workshop);

    }

    public List<WorkshopOutputDto> processWorkshopsToWorkshopOutputDtos(List<Workshop> workshops, User user) {
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        if (user != null) {
            for (Workshop w : workshops) {
                workshopOutputDtos.add(transferWorkshopToWorkshopOutputDto(w, user));
            }
        } else {
            for (Workshop w : workshops) {
                workshopOutputDtos.add(transferWorkshopToWorkshopOutputDto(w));
            }
        }
        return workshopOutputDtos;
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
        workshopOutputDto.amountOfFavsAndBookings = (workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop());
        workshopOutputDto.workshopPicUrl = workshop.getWorkshopPicUrl();
        return workshopOutputDto;
    }

    public WorkshopOutputDto transferWorkshopToWorkshopOutputDto(Workshop workshop, User user) {
        WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(workshop);
        workshopOutputDto.isFavourite = user.getFavouriteWorkshops().contains(workshop);
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
