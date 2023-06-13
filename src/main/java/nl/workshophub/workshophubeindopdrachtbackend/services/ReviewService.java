package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    ModelMapper modelMapper = new ModelMapper();

    private final ReviewRepository reviewRepository;

    private final WorkshopRepository workshopRepository;

    public ReviewService(ReviewRepository reviewRepository, WorkshopRepository workshopRepository) {
        this.reviewRepository = reviewRepository;
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

    public ReviewOutputDto createReview (Long workshopId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + workshopId + " bestaat niet"));
        Review review = transferReviewInputDtoToReview(reviewInputDto);
        review.setWorkshop(workshop);
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
       reviewOutputDto.workshopId = review.getWorkshop().getId();
       reviewOutputDto.workshopTitle = review.getWorkshop().getTitle();

       return reviewOutputDto;

    }

    public Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto) {
        Review review = new Review();
        review.setRating(reviewInputDto.rating);
        review.setReviewDescription(reviewInputDto.reviewDescription);
        review.setReviewVerified(reviewInputDto.reviewVerified);
        review.setFeedbackAdmin(reviewInputDto.feedbackAdmin);
       //als je in de body van reviewinputdto de workshopId meegeeft, kun je hier, via de workshoprepository, ook nog de workshop setten.

        return review;

    }





}
