package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;

//created this seperate class for the transfermethod - so workshopservice doesn't need to inject the reviewservice, but it can call it directly.
public class ReviewServiceTransferMethod {


    public static ReviewOutputDto transferReviewToReviewOutputDto(Review review) {
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

    public static Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto, Review review) {
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
