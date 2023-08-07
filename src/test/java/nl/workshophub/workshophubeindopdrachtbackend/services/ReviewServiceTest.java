package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    @Mock
    WorkshopRepository workshopRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ReviewService reviewService;

    @Captor
    ArgumentCaptor<Review> reviewCaptor;


    Review review1;
    Review review2;
    Review review3;

    Workshop workshop1;
    Workshop workshop2;

    User customer1;
    User customer2;
    User workshopOwner1;
    User workshopOwner2;

    ReviewOutputDto reviewOutputDto1;
    ReviewOutputDto reviewOutputDto2;
    ReviewOutputDto reviewOutputDto3;

    ReviewInputDto reviewInputDto1;
    ReviewInputDto reviewInputDto2;
    Authentication authentication;

    @BeforeEach
    void setUp() {


        customer1 = new User();
        customer1.setId(1L);
        customer1.setFirstName("Henk");
        customer1.setLastName("Janssen");
        customer1.setEmail("henk.janssen@gmail.com");

        customer2 = new User();
        customer2.setId(2L);
        customer2.setFirstName("Isabella");
        customer2.setLastName("Rossi");
        customer2.setEmail("isabella.rossi@gmail.com");

        workshopOwner1 = new User();
        workshopOwner1.setId(3L);
        workshopOwner1.setFirstName("Kees");
        workshopOwner1.setLastName("De Groot");
        workshopOwner1.setEmail("kees.de.groot@gmail.com");

        workshopOwner1 = new User();
        workshopOwner1.setId(4L);
        workshopOwner1.setFirstName("Samuel");
        workshopOwner1.setLastName("Smith");
        workshopOwner1.setEmail("samuel.smith@gmail.com");

        workshop1 = new Workshop();
        workshop1.setId(1L);
        workshop1.setWorkshopOwner(workshopOwner1);
        workshop2 = new Workshop();
        workshop2.setId(2L);
        workshop2.setWorkshopOwner(workshopOwner2);

        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop1));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        List<Workshop> workshopOwner2Workshops = new ArrayList<>(List.of(workshop2));
        workshopOwner2.setWorkshops(workshopOwner2Workshops);

        review1 = new Review(1L, 4.0D, "Test Review1 Description", false, "Test Feedback review1", workshop1, customer1);
        review2 = new Review(2L, 2.0D, "Test Review2 Description", true, "Test Feedback review2", workshop2, customer2);
        review3 = new Review(3L, 4.1D, "Test Review3 Description", true, "Test Feedback review3", workshop2, customer1);

        List<Review> workshop1ReviewList = new ArrayList<>(List.of(review1));
        workshop1.setWorkshopReviews(workshop1ReviewList);

        List<Review> workshop2ReviewList = new ArrayList<>(List.of(review2, review3));
        workshop2.setWorkshopReviews(workshop2ReviewList);

        reviewOutputDto1 = new ReviewOutputDto();
        reviewOutputDto1.id = 1L;
        reviewOutputDto1.rating = 4.0D;
        reviewOutputDto1.reviewDescription = "Test Review1 Description";
        reviewOutputDto1.reviewVerified = false;
        reviewOutputDto1.feedbackAdmin = "Test Feedback review1";
        reviewOutputDto1.workshopTitle = workshop1.getTitle();
        reviewOutputDto1.workshopDate = workshop1.getDate();
        reviewOutputDto1.firstNameReviewer = customer1.getFirstName();
        reviewOutputDto1.lastNameReviewer = customer1.getLastName();
        reviewOutputDto1.companyNameWorkshopOwner = workshop1.getWorkshopOwner().getCompanyName();

        reviewOutputDto2 = new ReviewOutputDto();
        reviewOutputDto2.id = 2L;
        reviewOutputDto2.rating = 2.0D;
        reviewOutputDto2.reviewDescription = "Test Review2 Description";
        reviewOutputDto2.reviewVerified = true;
        reviewOutputDto2.feedbackAdmin = "Test Feedback review2";
        reviewOutputDto2.workshopTitle = workshop2.getTitle();
        reviewOutputDto2.workshopDate = workshop2.getDate();
        reviewOutputDto2.firstNameReviewer = customer2.getFirstName();
        reviewOutputDto2.lastNameReviewer = customer2.getLastName();
        reviewOutputDto2.companyNameWorkshopOwner = workshop2.getWorkshopOwner().getCompanyName();

        reviewOutputDto3 = new ReviewOutputDto();
        reviewOutputDto3.id = 3L;
        reviewOutputDto3.rating = 4.1D;
        reviewOutputDto3.reviewDescription = "Test Review3 Description";
        reviewOutputDto3.reviewVerified = true;
        reviewOutputDto3.feedbackAdmin = "Test Feedback review3";
        reviewOutputDto3.workshopTitle = workshop2.getTitle();
        reviewOutputDto3.workshopDate = workshop2.getDate();
        reviewOutputDto3.firstNameReviewer = customer1.getFirstName();
        reviewOutputDto3.lastNameReviewer = customer1.getLastName();
        reviewOutputDto3.companyNameWorkshopOwner = workshop2.getWorkshopOwner().getCompanyName();



    }

    @Test
    void getReviewById() {
    }

    @Test
    void getReviewsFromWorkshopOwnerVerified() {
    }

    @Test
    void getReviewsFromCustomer() {
    }

    @Test
    void getAllReviews() {
    }

    @Test
    void getReviewsToVerify() {
    }

    @Test
    void createReview() {
    }

    @Test
    void verifyReviewByAdmin() {
    }

    @Test
    void updateReviewByCustomer() {
    }

    @Test
    void deleteReview() {
    }
}