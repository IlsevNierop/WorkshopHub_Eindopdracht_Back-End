package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import nl.workshophub.workshophubeindopdrachtbackend.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewOutputDto> getReviewById(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }
    @GetMapping("/workshopowner/{workshopOwnerId}")
    public ResponseEntity <List<ReviewOutputDto>> getReviewsFromWorkshopOwnerVerified(@PathVariable Long workshopOwnerId) {
        return new ResponseEntity<>(reviewService.getReviewsFromWorkshopOwnerVerified(workshopOwnerId), HttpStatus.OK);
    }

    //principal
    @GetMapping("/customer/{customerId}")
    public ResponseEntity <List<ReviewOutputDto>> getReviewsFromCustomer(@PathVariable Long customerId) {
        return new ResponseEntity<>(reviewService.getReviewsFromCustomer(customerId), HttpStatus.OK);
    }

//admin
    @GetMapping("/admin/")
    public ResponseEntity<List<ReviewOutputDto>> getAllReviews(){
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);

    }

    @GetMapping("/admin/verify")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsToVerify(){
        return new ResponseEntity<>(reviewService.getReviewsToVerify(), HttpStatus.OK);

    }


    @PostMapping ("/{workshopId}/{customerId}")
    public ResponseEntity<Object> createReview (@PathVariable("workshopId") Long workshopId, @PathVariable ("customerId") Long customerId, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        ReviewOutputDto reviewOutputDto = reviewService.createReview(workshopId, customerId, reviewInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + reviewOutputDto.id).toUriString());
        return new ResponseEntity<>(reviewOutputDto, HttpStatus.ACCEPTED);
    }

    @PutMapping ("/admin/verify/{reviewId}")
    public ResponseEntity<Object> verifyReviewByAdmin(@PathVariable Long reviewId, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.verifyReviewByAdmin(reviewId, reviewInputDto), HttpStatus.ACCEPTED);
    }

    @PutMapping ("/{customerId}/{reviewId}")
    public ResponseEntity<Object> updateReviewByCustomer (@PathVariable Long reviewId, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.updateReviewByCustomer(reviewId, reviewInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
