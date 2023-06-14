package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    ModelMapper modelMapper = new ModelMapper();

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final WorkshopRepository workshopRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, WorkshopRepository workshopRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
    }
    public ReviewOutputDto getReviewById(Long id) throws RecordNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De review met ID " + id + " bestaat niet"));
        return transferReviewToReviewOutputDto(review);
    }


    //niet ingelogde users & gewone users: //eigenaar bedrijfsnaam toevoegen, user firstname en workshopnaam toevoegen
    //getmapping all reviews van 1 owner / user, waar approved = true, average rating teruggeven
    //getmapping ownerid /userid-  by id review approved = true, incl workshopnaam en date


    //admin
    public List<ReviewOutputDto> getAllReviews() throws RecordNotFoundException {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsToVerify() throws RecordNotFoundException {
        List<Review> reviews = reviewRepository.findByReviewVerifiedIsNull();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }


    //put
    // admin
   public ReviewOutputDto verifyReviewByAdmin(Long id, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De review met ID " + id + " bestaat niet"));

        review.setRating(reviewInputDto.rating);
        review.setReviewDescription(reviewInputDto.reviewDescription);
        if (reviewInputDto.reviewVerified != null) {
            review.setReviewVerified(reviewInputDto.reviewVerified);
        }
        if (reviewInputDto.feedbackAdmin != null){
            review.setFeedbackAdmin(reviewInputDto.feedbackAdmin);
        }

        reviewRepository.save(review);
        return transferReviewToReviewOutputDto(review);

    }
//    user
//    put public ReviewOutputDto updateReviewByUser check if user = customer

    //user:
    //postmapping als attended workshop==true & workshopdate is in past - begin van postmapping set true als date in past - user has booking on workshop

    public ReviewOutputDto createReview (Long workshopId, Long customerId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + workshopId + " bestaat niet"));
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID nummer " + customerId + " bestaat niet"));
        Review review = transferReviewInputDtoToReview(reviewInputDto);
        review.setWorkshop(workshop);
        review.setCustomer(customer);
        // als de owner gekoppeld is aan de workshop
//        review.setWorkshopOwner(workshop.getWorkshopOwner().getId());
        reviewRepository.save(review);

        return transferReviewToReviewOutputDto(review);
    }


    // admin
    public void deleteReview(Long id) throws RecordNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De review met ID " + id + " bestaat niet"));
        reviewRepository.delete(review);
    }





    public ReviewOutputDto transferReviewToReviewOutputDto(Review review) {
       ReviewOutputDto reviewOutputDto = new ReviewOutputDto();
       reviewOutputDto.id = review.getId();
       reviewOutputDto.rating = review.getRating();
       reviewOutputDto.reviewDescription = review.getReviewDescription();
       reviewOutputDto.reviewVerified = review.getReviewVerified();
       reviewOutputDto.feedbackAdmin = review.getFeedbackAdmin();
       reviewOutputDto.workshopTitle = review.getWorkshop().getTitle();
       reviewOutputDto.workshopDate = review.getWorkshop().getDate();
       reviewOutputDto.workshopLocation = review.getWorkshop().getLocation();
       reviewOutputDto.firstNameReviewer = review.getCustomer().getFirstName();
       reviewOutputDto.lastNameReviewer = review.getCustomer().getLastName();
//       reviewOutputDto.companyNameWorkshopOwner = review.getWorkshopOwner().getCompanyName();
       // average rating nog toevoegen

       return reviewOutputDto;

    }

    public Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto) {
        Review review = new Review();
        review.setRating(reviewInputDto.rating);
        review.setReviewDescription(reviewInputDto.reviewDescription);
        review.setReviewVerified(reviewInputDto.reviewVerified);
        review.setFeedbackAdmin(reviewInputDto.feedbackAdmin);
        User customer = userRepository.findById(reviewInputDto.customerId).orElseThrow(() -> new RecordNotFoundException("De klant met ID " + reviewInputDto.customerId + " bestaat niet."));
        review.setCustomer(customer);
        Workshop workshop = workshopRepository.findById(reviewInputDto.workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + reviewInputDto.workshopId + " bestaat niet, en er kan geen review achtergelaten worden zonder gekoppeld te zijn aan een workshop."));;
        review.setWorkshop(workshop);
        //workshopowner nog toevoegen na relatie met workshop
//        review.setWorkshopOwner(workshop.get);

        return review;

    }





}
