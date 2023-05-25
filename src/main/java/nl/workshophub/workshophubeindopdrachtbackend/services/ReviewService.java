package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ValidationException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    ModelMapper modelMapper = new ModelMapper();

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
        if (reviews.isEmpty()){
            throw new RecordNotFoundException("Er zijn geen reviews");
        }
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsToVerify() throws RecordNotFoundException {
        List<Review> reviews = reviewRepository.findByReviewVerifiedIsNull();
        if (reviews.isEmpty()){
            throw new RecordNotFoundException("Er zijn momenteel geen goed te keuren reviews");
        }
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }





    //user:
    //postmapping als attended workshop==true & workshopdate is in past - begin van postmapping set true als date in past

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
//    public ReviewOutputDto updateReviewByUser check if user = customer


    // admin
    public void deleteReview(Long id) throws RecordNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De review met ID " + id + " bestaat niet"));
        reviewRepository.delete(review);
    }





    public ReviewOutputDto transferReviewToReviewOutputDto(Review review) {
        return modelMapper.map(review, ReviewOutputDto.class);

    }

    public Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto) {
        return modelMapper.map(reviewInputDto, Review.class);

    }





}
