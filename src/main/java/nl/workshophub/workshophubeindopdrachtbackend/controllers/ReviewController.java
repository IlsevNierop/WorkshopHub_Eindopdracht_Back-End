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

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewOutputDto> getReviewById(@PathVariable Long id) {
        return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<ReviewOutputDto>> getAllReviews(){
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);

    }

    @GetMapping("/admin/")
    public ResponseEntity<List<ReviewOutputDto>> getReviewsToVerify(){
        return new ResponseEntity<>(reviewService.getReviewsToVerify(), HttpStatus.OK);

    }

    @PutMapping ("/admin/{id}")
    public ResponseEntity<Object> verifyReviewByAdmin (@PathVariable Long id, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(reviewService.verifyReviewByAdmin(id, reviewInputDto), HttpStatus.ACCEPTED);
    }

    //userId nog toevoegen na relatie leggen
    @PostMapping ("/{workshopId}/{customerId}")
    public ResponseEntity<Object> createReview (@PathVariable("workshopId") Long workshopId, @PathVariable ("customerId") Long customerId, @Valid @RequestBody ReviewInputDto reviewInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        //uri toevoegen
        return new ResponseEntity<>(reviewService.createReview(workshopId, customerId, reviewInputDto), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
