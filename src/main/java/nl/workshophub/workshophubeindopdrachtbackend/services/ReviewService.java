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
import nl.workshophub.workshophubeindopdrachtbackend.util.CheckAuthorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
        if (review.getReviewVerified() != Boolean.TRUE) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!CheckAuthorization.isAuthorized(review.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
                throw new BadRequestException("You're not allowed to view this review.");
            }
        }
        return ReviewServiceTransferMethod.transferReviewToReviewOutputDto(review);
    }

    public List<ReviewOutputDto> getReviewsFromWorkshopOwnerVerified(Long workshopOwnerId) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));
        if (workshopOwner.getWorkshopOwner() != true) {
            throw new BadRequestException("This user is a customer, and not a workshop owner.");
        }
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Workshop w : workshopOwner.getWorkshops()) {
            for (Review r : w.getWorkshopReviews()) {
                if (r.getReviewVerified() != null && r.getReviewVerified() == true) {
                    ReviewOutputDto reviewOutputDto = ReviewServiceTransferMethod.transferReviewToReviewOutputDto(r);
                    reviewOutputDtos.add(reviewOutputDto);
                }
            }
        }
        return reviewOutputDtos;
    }

    //check if user is customer
    public List<ReviewOutputDto> getReviewsFromCustomer(Long customerId) {
        List<Review> reviews = reviewRepository.findAllByCustomerId(customerId);

        //below code is so I don't have to get the user from the userrepository (maybe it's omslachtig?)
        if (!reviews.isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!CheckAuthorization.isAuthorized(reviews.get(0).getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
                throw new BadRequestException("You're not allowed to view reviews from this user account.");
            }
        }
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r : reviews) {
            ReviewOutputDto reviewOutputDto = ReviewServiceTransferMethod.transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }


    //admin
    public List<ReviewOutputDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r : reviews) {
            ReviewOutputDto reviewOutputDto = ReviewServiceTransferMethod.transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }

    public List<ReviewOutputDto> getReviewsToVerify() {
        List<Review> reviews = reviewRepository.findByReviewVerifiedIsNull();
        List<ReviewOutputDto> reviewOutputDtos = new ArrayList<>();
        for (Review r : reviews) {
            ReviewOutputDto reviewOutputDto = ReviewServiceTransferMethod.transferReviewToReviewOutputDto(r);
            reviewOutputDtos.add(reviewOutputDto);
        }
        return reviewOutputDtos;
    }


    public ReviewOutputDto createReview(Long workshopId, Long customerId, ReviewInputDto reviewInputDto) throws RecordNotFoundException, BadRequestException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));

        if (customer == workshop.getWorkshopOwner()){
            throw new BadRequestException("You're not allowed to write a review about your own workshop.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(customer, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new BadRequestException("You're not allowed to create a review from this user account.");
        }

        for (Review r : customer.getCustomerReviews()) {
            if (r.getWorkshop().getId() == workshop.getId()) {
                throw new BadRequestException("You've already submitted a review for this workshop, you can only submit 1 review per attended workshop.");
            }
        }
        for (Booking b : customer.getBookings()) {
            if (b.getWorkshop().getId() == workshop.getId() && b.getWorkshop().getDate().isBefore(LocalDate.now())) {
                Review review = new Review();
                ReviewServiceTransferMethod.transferReviewInputDtoToReview(reviewInputDto, review);
                review.setWorkshop(workshop);
                review.setCustomer(customer);
                // when creating new review by customer, reviewVerified and feedbackAdmin should get default values so admin can later verify and give feedback.
                review.setReviewVerified(null);
                review.setFeedbackAdmin(null);
                reviewRepository.save(review);
                return ReviewServiceTransferMethod.transferReviewToReviewOutputDto(review);
            }
        }
        throw new BadRequestException("You're not allowed to create a review. Either you haven't attended the workshop or the workshop hasn't taken place yet.");
    }

    public ReviewOutputDto verifyReviewByAdmin(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        ReviewServiceTransferMethod.transferReviewInputDtoToReview(reviewInputDto, review);
        reviewRepository.save(review);
        return ReviewServiceTransferMethod.transferReviewToReviewOutputDto(review);
    }

    //check if user is customer
    //principal
    public ReviewOutputDto updateReviewByCustomer(Long reviewId, ReviewInputDto reviewInputDto) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(review.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new BadRequestException("You're not allowed to view this review.");
        }

        // to prevent the owner from overwriting the feedback from the admin, I'll make sure the inputdto has the same feedback as the original review. This could be prevented with a different inputdto for customer (excluding feedbackadmin and verifyreview) and for admin.
        reviewInputDto.feedbackAdmin = review.getFeedbackAdmin();
        ReviewServiceTransferMethod.transferReviewInputDtoToReview(reviewInputDto, review);
        // after update review by customer, reviewVerified should get default value so admin can later verify.
        review.setReviewVerified(null);
        reviewRepository.save(review);
        return ReviewServiceTransferMethod.transferReviewToReviewOutputDto(review);
    }

    public void deleteReview(Long reviewId) throws RecordNotFoundException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RecordNotFoundException("The review with ID " + reviewId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(review.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new BadRequestException("You're not allowed to delete this review.");
        }

        reviewRepository.delete(review);
    }

}
