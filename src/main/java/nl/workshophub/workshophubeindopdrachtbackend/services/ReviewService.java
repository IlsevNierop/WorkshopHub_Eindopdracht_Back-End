package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, WorkshopRepository workshopRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.workshopRepository = workshopRepository;
    }
    public ReviewOutputDto getReviewById(Long reviewId) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        return transferReviewToReviewOutputDto(review);
    }

    public List<ReviewOutputDto> getReviewsFromWorkshopOwner(Long workshopOwnerId) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));
        if (workshopOwner.getWorkshopOwner() != true){
            throw new BadRequestException("This user is a customer, and not a workshop owner.");
        }
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Workshop w: workshopOwner.getWorkshops()) {
            for (Review r: w.getWorkshopReviews()) {
                if (r.getReviewVerified() != null && r.getReviewVerified() == true) {
                    ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
                    reviewOutputDtos.add(reviewOutputDto);
                }
            }
        }
        return reviewOutputDtos;
    }

    //check if user is customer
    public List<ReviewOutputDto> getReviewsFromCustomer(Long customerId) {
        List<Review> reviews = reviewRepository.findAllByCustomerId(customerId);
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }


    //admin
    public List<ReviewOutputDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsToVerify() {
        List<Review> reviews = reviewRepository.findByReviewVerifiedIsNull();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r: reviews) {
            ReviewOutputDto reviewOutputDto = transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }


    //check if user is customer
    public ReviewOutputDto createReview (Long workshopId, Long customerId, ReviewInputDto reviewInputDto) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));
        for (Review r: customer.getCustomerReviews()){
            if (r.getWorkshop().getId() == workshop.getId()){
                throw new BadRequestException("You've already submitted a review for this workshop, you can only submit 1 review per attended workshop.");
            }
        }
        for (Booking b: customer.getBookings()){
            if (b.getWorkshop().getId() == workshop.getId() && b.getWorkshop().getDate().isBefore(LocalDate.now())){
                Review review = new Review();
                transferReviewInputDtoToReview(reviewInputDto, review);
                review.setWorkshop(workshop);
                review.setCustomer(customer);
                // when creating new review by customer, reviewVerified and feedbackAdmin should get default values so admin can later verify and give feedback.
                review.setReviewVerified(null);
                review.setFeedbackAdmin(null);
                reviewRepository.save(review);
                return transferReviewToReviewOutputDto(review);
            }
        }
        throw new BadRequestException("You're not allowed to create a review. Either you haven't attended the workshop or the workshop hasn't taken place yet.");
    }

   public ReviewOutputDto verifyReviewByAdmin(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        transferReviewInputDtoToReview(reviewInputDto, review);
        reviewRepository.save(review);
        return transferReviewToReviewOutputDto(review);
    }

    //check if user is customer
    public ReviewOutputDto updateReviewByCustomer(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        transferReviewInputDtoToReview(reviewInputDto, review);
        // after update review by customer, reviewVerified and feedbackAdmin should get default values so admin can later verify and give feedback.
        review.setReviewVerified(null);
        review.setFeedbackAdmin(null);
        reviewRepository.save(review);
        return transferReviewToReviewOutputDto(review);
    }

    public void deleteReview(Long reviewId) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
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
       reviewOutputDto.firstNameReviewer = review.getCustomer().getFirstName();
       reviewOutputDto.lastNameReviewer = review.getCustomer().getLastName();
       reviewOutputDto.companyNameWorkshopOwner = review.getWorkshop().getWorkshopOwner().getCompanyName();


       return reviewOutputDto;

    }

    public Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto, Review review) {
        review.setRating(reviewInputDto.rating);
        review.setReviewDescription(reviewInputDto.reviewDescription);
        if (reviewInputDto.reviewVerified != null) {
            review.setReviewVerified(reviewInputDto.reviewVerified);
        }
        if (reviewInputDto.feedbackAdmin != null){
            review.setFeedbackAdmin(reviewInputDto.feedbackAdmin);
        }

        return review;

    }





}
