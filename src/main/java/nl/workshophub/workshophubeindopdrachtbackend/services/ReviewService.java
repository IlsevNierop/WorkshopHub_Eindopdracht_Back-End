package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    //getmapping goed te keuren
    //getmapping all reviews?

    //user:
    //postmapping als attended workshop==true & workshopdate is in past

    //put
    // check van workshop

    //delete
    // admin






    public ReviewOutputDto transferReviewToReviewOutputDto(Review review) {
        return modelMapper.map(review, ReviewOutputDto.class);

    }

    public Review transferReviewInputDtoToReview(ReviewInputDto reviewInputDto) {
        return modelMapper.map(reviewInputDto, Review.class);

    }





}
