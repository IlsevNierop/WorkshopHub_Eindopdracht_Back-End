package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final BookingService bookingService;

    public WorkshopService(WorkshopRepository workshopRepository, UserRepository userRepository, ReviewService reviewService, BookingService bookingService) {
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
        this.bookingService = bookingService;
    }

    public List<WorkshopOutputDto> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    // add in output all reviews about workshopowner to getworkshop by id? List of reviewoutputdtos?
    public WorkshopOutputDto getWorkshopById(Long id) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + id + " doesn't exist."));
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public List<WorkshopOutputDto> getAllWorkshopsToVerify() throws RecordNotFoundException {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsNullOrWorkshopVerifiedIsFalseOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
//            get average rating user - connected to workshop - set average rating owner to workshopoutput
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto createWorkshop(Long workshopOwnerId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("De workshop eigenaar met ID " + workshopOwnerId + " bestaat niet"));
        if (workshopOwner.getWorkshopOwnerVerified() == null || !workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() == false) {
            throw new BadRequestException("Je bent niet gemachtigd een nieuwe workshop aan te maken");
        }
        Workshop workshop = new Workshop();
        workshop = transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        workshop.setWorkshopOwner(workshopOwner);
        // bij aanmaken van nieuwe workshop moeten publishWorkshop, workshopVerified en FeedbackAdmin default waardes krijgen.
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshop.setFeedbackAdmin(null);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto updateWorkshopByOwner(Long workshopOwnerId, Long workshopId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + workshopId + " bestaat niet"));
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("De workshop eigenaar met ID " + workshopOwnerId + " bestaat niet"));
        if (!workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() != true) {
            throw new BadRequestException("Je bent niet gemachtigd een workshop aan te passen");
        }
        // om te voorkomen dat de owner de feedback van de admin in de workshopInputDto heeft aangepast, en dit in de transfer methode wordt overgenomen, pas ik hier de inputdto feedback aan naar de eventuele originele feedback.
        workshopInputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);
        // na het wijzigen van een workshop wordt de status automatisch op geverifieerd null en publish null gezet:
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    @PutMapping
    public WorkshopOutputDto verifyWorkshopByOwner(Long workshopOwnerId, Long workshopId, Boolean publishWorkshop) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + workshopId + " bestaat niet"));
        if (workshop.getWorkshopVerified() != true) {
            throw new BadRequestException("Deze workshop is nog niet goedgekeurd door de administrator, dus deze workshop kan nog niet op publiceren gezet worden.");
        }
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("De workshop eigenaar met ID " + workshopOwnerId + " bestaat niet"));
        if (!workshopOwner.getWorkshopOwner() || workshopOwner.getWorkshopOwnerVerified() != true) {
            throw new BadRequestException("Je bent niet gemachtigd deze workshop te verifieren");
        }
        workshop.setPublishWorkshop(publishWorkshop);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto verifyWorkshopByAdmin(Long workshopId, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + workshopId + " bestaat niet"));
        transferWorkshopInputDtoToWorkshop(workshopInputDto, workshop);

        // na het wijzigen van een workshop door admin, wordt de publish workshop automatisch op null gezet, zodat owner kan accorderen:
        workshop.setPublishWorkshop(null);

        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public void deleteWorkshop(Long workshopId) throws BadRequestException, RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        if (!workshop.getWorkshopBookings().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has bookings.");
        }
        if (!workshop.getWorkshopReviews().isEmpty()) {
            throw new BadRequestException("This workshop can't be removed, since it already has reviews.");
        }
        //can't remove workshop, if owner has set publish on true. Then owner needs to verify deleten - by setting publish workshop on false.
        if (workshop.getPublishWorkshop() != null && workshop.getPublishWorkshop() == true) {
            throw new BadRequestException("This workshop can't be removes, because the workshop owner has verified the workshop for publishing.");
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
        workshopOutputDto.workshopBookings = getAllBookingOutPutDtosFromWorkshop(workshop);
        workshopOutputDto.workshopOwnerReviews = getAllReviewOutPutDtosFromWorkshopOwner(workshop);
        workshopOutputDto.spotsAvailable = workshop.getAvailableSpotsWorkshop();
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        workshopOutputDto.averageRatingWorkshopOwnerReviews = workshop.getWorkshopOwner().calculateAverageRatingWorkshopOwner();

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

        // bij update functies mogen de volgende waardes niet altijd gewijzigd worden, daarom bij die mappings dit overschreven.
        if (workshopInputDto.workshopVerified != null) {
            workshop.setWorkshopVerified(workshopInputDto.workshopVerified);
        }
        if (workshopInputDto.feedbackAdmin != null) {
            workshop.setFeedbackAdmin(workshopInputDto.feedbackAdmin);
        }
        workshop.setPublishWorkshop(workshopInputDto.publishWorkshop);

        return workshop;

    }

    public List<ReviewOutputDto> getAllReviewOutPutDtosFromWorkshopOwner(Workshop workshop) {
        List<ReviewOutputDto> allReviewOutputDtosFromWorkshopOwner = new ArrayList<>();
        if (workshop.getWorkshopOwner().getWorkshops() != null) {
            for (Workshop w : workshop.getWorkshopOwner().getWorkshops()) {
                if (w.getWorkshopReviews() != null) {
                    for (Review r : w.getWorkshopReviews()) {
                        allReviewOutputDtosFromWorkshopOwner.add(reviewService.transferReviewToReviewOutputDto(r));
                    }
                }
            }
        }
        return allReviewOutputDtosFromWorkshopOwner;
    }

    public List<BookingOutputDto> getAllBookingOutPutDtosFromWorkshop(Workshop workshop) {
        List<BookingOutputDto> allBookingOutPutDtosFromWorkshop = new ArrayList<>();
        if (workshop.getWorkshopBookings() != null) {
            for (Booking b : workshop.getWorkshopBookings()) {
                allBookingOutPutDtosFromWorkshop.add(bookingService.transferBookingToBookingOutputDto(b));
            }
        }
        return allBookingOutPutDtosFromWorkshop;
    }


}
