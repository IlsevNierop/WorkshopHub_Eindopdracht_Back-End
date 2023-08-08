package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.ReviewInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Review;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
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
    Workshop workshop3;
    Workshop workshop4;

    User customer1;
    User customer2;
    User workshopOwner1;
    User workshopOwner2;

    Booking booking1;
    Booking booking2;

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
        customer1.setWorkshopOwner(false);

        customer2 = new User();
        customer2.setId(2L);
        customer2.setFirstName("Isabella");
        customer2.setLastName("Rossi");
        customer2.setEmail("isabella.rossi@gmail.com");
        customer2.setWorkshopOwner(false);

        workshopOwner1 = new User();
        workshopOwner1.setId(3L);
        workshopOwner1.setFirstName("Kees");
        workshopOwner1.setLastName("De Groot");
        workshopOwner1.setEmail("kees.de.groot@gmail.com");
        workshopOwner1.setWorkshopOwner(true);

        workshopOwner2 = new User();
        workshopOwner2.setId(4L);
        workshopOwner2.setFirstName("Samuel");
        workshopOwner2.setLastName("Smith");
        workshopOwner2.setEmail("samuel.smith@gmail.com");
        workshopOwner2.setWorkshopOwner(true);


        workshop1 = new Workshop();
        workshop1.setId(1L);
        workshop1.setWorkshopOwner(workshopOwner1);
        workshop1.setDate(LocalDate.of(2023, 4, 25));

        workshop2 = new Workshop();
        workshop2.setId(2L);
        workshop2.setWorkshopOwner(workshopOwner2);
        workshop2.setDate(LocalDate.of(2023, 9, 25));

        workshop3 = new Workshop();
        workshop3.setId(3L);
        workshop3.setWorkshopOwner(workshopOwner2);
        workshop3.setDate(LocalDate.of(2023, 4, 25));

        workshop4 = new Workshop();
        workshop4.setId(4L);
        workshop4.setWorkshopOwner(workshopOwner1);
        workshop4.setDate(LocalDate.of(2023, 10, 25));


        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop1, workshop4));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        List<Workshop> workshopOwner2Workshops = new ArrayList<>(List.of(workshop2, workshop3));
        workshopOwner2.setWorkshops(workshopOwner2Workshops);

        review1 = new Review(1L, 4.0D, "Test Review1 Description", null, "Test Feedback review1", workshop1, customer1);
        review2 = new Review(2L, 2.0D, "Test Review2 Description", true, "Test Feedback review2", workshop2, customer2);
        review3 = new Review(3L, 4.1D, "Test Review3 Description", true, "Test Feedback review3", workshop2, customer1);

        List<Review> customer1ReviewsList = new ArrayList<>(List.of(review1, review3));
        customer1.setCustomerReviews(customer1ReviewsList);

        List<Review> customer2ReviewsList = new ArrayList<>(List.of(review2));
        customer2.setCustomerReviews(customer2ReviewsList);

        List<Review> workshop1ReviewsList = new ArrayList<>(List.of(review1));
        workshop1.setWorkshopReviews(workshop1ReviewsList);

        List<Review> workshop2ReviewsList = new ArrayList<>(List.of(review2, review3));
        workshop2.setWorkshopReviews(workshop2ReviewsList);

        List<Review> workshop3ReviewsList = new ArrayList<>();
        workshop3.setWorkshopReviews(workshop3ReviewsList);

        List<Review> workshop4ReviewsList = new ArrayList<>();
        workshop4.setWorkshopReviews(workshop4ReviewsList);

        List<Review> workshop1ReviewList = new ArrayList<>(List.of(review1));
        workshop1.setWorkshopReviews(workshop1ReviewList);

        List<Review> workshop2ReviewList = new ArrayList<>(List.of(review2, review3));
        workshop2.setWorkshopReviews(workshop2ReviewList);

        booking1 = new Booking(1L, LocalDate.now(), "Ik heb er zin in", 3, 45D, workshop1, customer1);
        booking2 = new Booking(2L, LocalDate.now(), "Leuk workshop 2", 3, 5D, workshop2, customer1);
        booking2 = new Booking(3L, LocalDate.now(), "Leuk workshop 2", 3, 5D, workshop3, customer1);

        List<Booking> customer1BookingsList = new ArrayList<>(List.of(booking1, booking2));
        customer1.setBookings(customer1BookingsList);

        List<Booking> workshop1BookingsList = new ArrayList<>(List.of(booking1));
        workshop1.setWorkshopBookings(workshop1BookingsList);

        List<Booking> workshop2BookingsList = new ArrayList<>(List.of(booking2));
        workshop2.setWorkshopBookings(workshop2BookingsList);

        reviewOutputDto1 = new ReviewOutputDto();
        reviewOutputDto1.id = 1L;
        reviewOutputDto1.rating = 4.0D;
        reviewOutputDto1.reviewDescription = "Test Review1 Description";
        reviewOutputDto1.reviewVerified = null;
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

        reviewInputDto1 = new ReviewInputDto();
        reviewInputDto1.rating = 5.0D;
        reviewInputDto1.reviewDescription = "Test inputDto1";
        reviewInputDto1.reviewVerified = true;
        reviewInputDto1.feedbackAdmin = "Test feedbackAdmin 1";

        reviewInputDto2 = new ReviewInputDto();
        reviewInputDto2.rating = 3.0D;
        reviewInputDto2.reviewDescription = "Test inputDto2";
        reviewInputDto2.reviewVerified = true;
        reviewInputDto2.feedbackAdmin = "Test feedbackAdmin 2";

        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    void shouldReturnOneReviewByIdWithoutAuthenticationAndReviewVerifiedIsTrue() {
        //Arrange
        when(reviewRepository.findById(review2.getId())).thenReturn(Optional.of(review2));

        //        Act
        ReviewOutputDto reviewOutputDtoExpected = reviewService.getReviewById(review2.getId());

        //        Assert
        assertEquals(reviewOutputDto2.id, reviewOutputDtoExpected.id);
        assertEquals(reviewOutputDto2.rating, reviewOutputDtoExpected.rating);
        assertEquals(reviewOutputDto2.reviewDescription, reviewOutputDtoExpected.reviewDescription);
        assertEquals(reviewOutputDto2.reviewVerified, reviewOutputDtoExpected.reviewVerified);
        assertEquals(reviewOutputDto2.feedbackAdmin, reviewOutputDtoExpected.feedbackAdmin);
        assertEquals(reviewOutputDto2.workshopTitle, reviewOutputDtoExpected.workshopTitle);
        assertEquals(reviewOutputDto2.workshopDate, reviewOutputDtoExpected.workshopDate);
        assertEquals(reviewOutputDto2.firstNameReviewer, reviewOutputDtoExpected.firstNameReviewer);
        assertEquals(reviewOutputDto2.lastNameReviewer, reviewOutputDtoExpected.lastNameReviewer);
        assertEquals(reviewOutputDto2.companyNameWorkshopOwner, reviewOutputDtoExpected.companyNameWorkshopOwner);
    }

    @Test
    void shouldReturnOneReviewByIdWithAuthenticationAndReviewVerifiedIsFalse() {
        //Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        ReviewOutputDto reviewOutputDtoExpected = reviewService.getReviewById(review1.getId());

        //        Assert
        assertEquals(reviewOutputDto1.id, reviewOutputDtoExpected.id);
        assertEquals(reviewOutputDto1.rating, reviewOutputDtoExpected.rating);
        assertEquals(reviewOutputDto1.reviewDescription, reviewOutputDtoExpected.reviewDescription);
        assertEquals(reviewOutputDto1.reviewVerified, reviewOutputDtoExpected.reviewVerified);
        assertEquals(reviewOutputDto1.feedbackAdmin, reviewOutputDtoExpected.feedbackAdmin);
        assertEquals(reviewOutputDto1.workshopTitle, reviewOutputDtoExpected.workshopTitle);
        assertEquals(reviewOutputDto1.workshopDate, reviewOutputDtoExpected.workshopDate);
        assertEquals(reviewOutputDto1.firstNameReviewer, reviewOutputDtoExpected.firstNameReviewer);
        assertEquals(reviewOutputDto1.lastNameReviewer, reviewOutputDtoExpected.lastNameReviewer);
        assertEquals(reviewOutputDto1.companyNameWorkshopOwner, reviewOutputDtoExpected.companyNameWorkshopOwner);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInGetReviewById() {
        //Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.getReviewById(review1.getId()));
        assertEquals("You're not allowed to view this review.", exception.getMessage());
    }

    @Test
    void shouldReturnAllReviewsVerifiedFromWorkshopOwner() {
        //        Arrange
        List<ReviewOutputDto> reviewOutputDtos = List.of(reviewOutputDto2, reviewOutputDto3);
        when(userRepository.findById(workshopOwner2.getId())).thenReturn(Optional.of(workshopOwner2));

        //        Act
        List<ReviewOutputDto> getAllReviewsVerifiedFromWorkshopOwnerExpected = reviewService.getReviewsVerifiedFromWorkshopOwner(workshopOwner2.getId());

        //        Assert
        assertEquals(reviewOutputDtos.size(), getAllReviewsVerifiedFromWorkshopOwnerExpected.size());
        assertEquals(reviewOutputDtos.get(0).id, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).id);
        assertEquals(reviewOutputDtos.get(0).rating, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).rating);
        assertEquals(reviewOutputDtos.get(0).reviewDescription, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).reviewDescription);
        assertEquals(reviewOutputDtos.get(0).reviewVerified, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).reviewVerified);
        assertEquals(reviewOutputDtos.get(0).feedbackAdmin, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(0).workshopTitle, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).workshopTitle);
        assertEquals(reviewOutputDtos.get(0).workshopDate, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).workshopDate);
        assertEquals(reviewOutputDtos.get(0).firstNameReviewer, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(0).lastNameReviewer, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(0).companyNameWorkshopOwner, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(0).companyNameWorkshopOwner);
        assertEquals(reviewOutputDtos.get(1).id, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).id);
        assertEquals(reviewOutputDtos.get(1).rating, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).rating);
        assertEquals(reviewOutputDtos.get(1).reviewDescription, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).reviewDescription);
        assertEquals(reviewOutputDtos.get(1).reviewVerified, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).reviewVerified);
        assertEquals(reviewOutputDtos.get(1).feedbackAdmin, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(1).workshopTitle, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).workshopTitle);
        assertEquals(reviewOutputDtos.get(1).workshopDate, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).workshopDate);
        assertEquals(reviewOutputDtos.get(1).firstNameReviewer, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(1).lastNameReviewer, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(1).companyNameWorkshopOwner, getAllReviewsVerifiedFromWorkshopOwnerExpected.get(1).companyNameWorkshopOwner);
    }

    @Test
    void shouldReturnBadRequestExceptionWhenUserIsWorkshopOwnerIsFalse() {
        //Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        //        Act
        //        Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> reviewService.getReviewsVerifiedFromWorkshopOwner(customer1.getId()));
        assertEquals("This user is a customer, and not a workshop owner.", exception.getMessage());
    }

    @Test
    void shouldReturnReviewsFromCustomer() {
        //        Arrange
        List<ReviewOutputDto> reviewOutputDtos = List.of(reviewOutputDto1, reviewOutputDto3);
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(reviewRepository.findAllByCustomerId(customer1.getId())).thenReturn(List.of(review1, review3));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        List<ReviewOutputDto> getAllReviewsFromCustomerExpected = reviewService.getReviewsFromCustomer(customer1.getId());

        //        Assert
        assertEquals(reviewOutputDtos.size(), getAllReviewsFromCustomerExpected.size());
        assertEquals(reviewOutputDtos.get(0).id, getAllReviewsFromCustomerExpected.get(0).id);
        assertEquals(reviewOutputDtos.get(0).rating, getAllReviewsFromCustomerExpected.get(0).rating);
        assertEquals(reviewOutputDtos.get(0).reviewDescription, getAllReviewsFromCustomerExpected.get(0).reviewDescription);
        assertEquals(reviewOutputDtos.get(0).reviewVerified, getAllReviewsFromCustomerExpected.get(0).reviewVerified);
        assertEquals(reviewOutputDtos.get(0).feedbackAdmin, getAllReviewsFromCustomerExpected.get(0).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(0).workshopTitle, getAllReviewsFromCustomerExpected.get(0).workshopTitle);
        assertEquals(reviewOutputDtos.get(0).workshopDate, getAllReviewsFromCustomerExpected.get(0).workshopDate);
        assertEquals(reviewOutputDtos.get(0).firstNameReviewer, getAllReviewsFromCustomerExpected.get(0).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(0).lastNameReviewer, getAllReviewsFromCustomerExpected.get(0).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(0).companyNameWorkshopOwner, getAllReviewsFromCustomerExpected.get(0).companyNameWorkshopOwner);
        assertEquals(reviewOutputDtos.get(1).id, getAllReviewsFromCustomerExpected.get(1).id);
        assertEquals(reviewOutputDtos.get(1).rating, getAllReviewsFromCustomerExpected.get(1).rating);
        assertEquals(reviewOutputDtos.get(1).reviewDescription, getAllReviewsFromCustomerExpected.get(1).reviewDescription);
        assertEquals(reviewOutputDtos.get(1).reviewVerified, getAllReviewsFromCustomerExpected.get(1).reviewVerified);
        assertEquals(reviewOutputDtos.get(1).feedbackAdmin, getAllReviewsFromCustomerExpected.get(1).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(1).workshopTitle, getAllReviewsFromCustomerExpected.get(1).workshopTitle);
        assertEquals(reviewOutputDtos.get(1).workshopDate, getAllReviewsFromCustomerExpected.get(1).workshopDate);
        assertEquals(reviewOutputDtos.get(1).firstNameReviewer, getAllReviewsFromCustomerExpected.get(1).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(1).lastNameReviewer, getAllReviewsFromCustomerExpected.get(1).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(1).companyNameWorkshopOwner, getAllReviewsFromCustomerExpected.get(1).companyNameWorkshopOwner);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInGetReviewsFromCustomer() {
        //Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.getReviewsFromCustomer(customer1.getId()));
        assertEquals("You're not allowed to view reviews from this user account.", exception.getMessage());
    }

    @Test
    void shouldReturnAllReviews() {
        //       Assert
        List<ReviewOutputDto> reviewOutputDtos = List.of(reviewOutputDto1, reviewOutputDto2, reviewOutputDto3);
        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2, review3));

        //        Act
        List<ReviewOutputDto> getAllReviewsExpected = reviewService.getAllReviews();

        //        Assert
        assertEquals(reviewOutputDtos.size(), getAllReviewsExpected.size());
        assertEquals(reviewOutputDtos.get(0).id, getAllReviewsExpected.get(0).id);
        assertEquals(reviewOutputDtos.get(0).rating, getAllReviewsExpected.get(0).rating);
        assertEquals(reviewOutputDtos.get(0).reviewDescription, getAllReviewsExpected.get(0).reviewDescription);
        assertEquals(reviewOutputDtos.get(0).reviewVerified, getAllReviewsExpected.get(0).reviewVerified);
        assertEquals(reviewOutputDtos.get(0).feedbackAdmin, getAllReviewsExpected.get(0).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(0).workshopTitle, getAllReviewsExpected.get(0).workshopTitle);
        assertEquals(reviewOutputDtos.get(0).workshopDate, getAllReviewsExpected.get(0).workshopDate);
        assertEquals(reviewOutputDtos.get(0).firstNameReviewer, getAllReviewsExpected.get(0).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(0).lastNameReviewer, getAllReviewsExpected.get(0).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(0).companyNameWorkshopOwner, getAllReviewsExpected.get(0).companyNameWorkshopOwner);
        assertEquals(reviewOutputDtos.get(1).id, getAllReviewsExpected.get(1).id);
        assertEquals(reviewOutputDtos.get(1).rating, getAllReviewsExpected.get(1).rating);
        assertEquals(reviewOutputDtos.get(1).reviewDescription, getAllReviewsExpected.get(1).reviewDescription);
        assertEquals(reviewOutputDtos.get(1).reviewVerified, getAllReviewsExpected.get(1).reviewVerified);
        assertEquals(reviewOutputDtos.get(1).feedbackAdmin, getAllReviewsExpected.get(1).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(1).workshopTitle, getAllReviewsExpected.get(1).workshopTitle);
        assertEquals(reviewOutputDtos.get(1).workshopDate, getAllReviewsExpected.get(1).workshopDate);
        assertEquals(reviewOutputDtos.get(1).firstNameReviewer, getAllReviewsExpected.get(1).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(1).lastNameReviewer, getAllReviewsExpected.get(1).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(1).companyNameWorkshopOwner, getAllReviewsExpected.get(1).companyNameWorkshopOwner);
        assertEquals(reviewOutputDtos.get(2).id, getAllReviewsExpected.get(2).id);
        assertEquals(reviewOutputDtos.get(2).rating, getAllReviewsExpected.get(2).rating);
        assertEquals(reviewOutputDtos.get(2).reviewDescription, getAllReviewsExpected.get(2).reviewDescription);
        assertEquals(reviewOutputDtos.get(2).reviewVerified, getAllReviewsExpected.get(2).reviewVerified);
        assertEquals(reviewOutputDtos.get(2).feedbackAdmin, getAllReviewsExpected.get(2).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(2).workshopTitle, getAllReviewsExpected.get(2).workshopTitle);
        assertEquals(reviewOutputDtos.get(2).workshopDate, getAllReviewsExpected.get(2).workshopDate);
        assertEquals(reviewOutputDtos.get(2).firstNameReviewer, getAllReviewsExpected.get(2).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(2).lastNameReviewer, getAllReviewsExpected.get(2).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(2).companyNameWorkshopOwner, getAllReviewsExpected.get(2).companyNameWorkshopOwner);

    }

    @Test
    void shouldReturnReviewsToVerify() {
        //       Assert
        List<ReviewOutputDto> reviewOutputDtos = List.of(reviewOutputDto1);
        when(reviewRepository.findByReviewVerifiedIsNull()).thenReturn(List.of(review1));

        //        Act
        List<ReviewOutputDto> getAllReviewsToVerifyExpected = reviewService.getReviewsToVerify();

        //        Assert
        assertEquals(reviewOutputDtos.size(), getAllReviewsToVerifyExpected.size());
        assertEquals(reviewOutputDtos.get(0).id, getAllReviewsToVerifyExpected.get(0).id);
        assertEquals(reviewOutputDtos.get(0).rating, getAllReviewsToVerifyExpected.get(0).rating);
        assertEquals(reviewOutputDtos.get(0).reviewDescription, getAllReviewsToVerifyExpected.get(0).reviewDescription);
        assertEquals(reviewOutputDtos.get(0).reviewVerified, getAllReviewsToVerifyExpected.get(0).reviewVerified);
        assertEquals(reviewOutputDtos.get(0).feedbackAdmin, getAllReviewsToVerifyExpected.get(0).feedbackAdmin);
        assertEquals(reviewOutputDtos.get(0).workshopTitle, getAllReviewsToVerifyExpected.get(0).workshopTitle);
        assertEquals(reviewOutputDtos.get(0).workshopDate, getAllReviewsToVerifyExpected.get(0).workshopDate);
        assertEquals(reviewOutputDtos.get(0).firstNameReviewer, getAllReviewsToVerifyExpected.get(0).firstNameReviewer);
        assertEquals(reviewOutputDtos.get(0).lastNameReviewer, getAllReviewsToVerifyExpected.get(0).lastNameReviewer);
        assertEquals(reviewOutputDtos.get(0).companyNameWorkshopOwner, getAllReviewsToVerifyExpected.get(0).companyNameWorkshopOwner);
    }

    @Test
    void shouldCreateReviewAndReturnReviewOutPutDto() {
        //        Arrange
        when(workshopRepository.findById(workshop3.getId())).thenReturn(Optional.of(workshop3));
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(authentication.getName()).thenReturn(customer1.getEmail());
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

//        Act
        ReviewOutputDto reviewOutputDto = reviewService.createReview(workshop3.getId(), customer1.getId(), reviewInputDto1);

//        Assert
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();
        assertEquals(reviewInputDto1.rating, savedReview.getRating());
        assertEquals(reviewInputDto1.reviewDescription, savedReview.getReviewDescription());
        assertEquals(null, savedReview.getReviewVerified());
        assertEquals(null, savedReview.getFeedbackAdmin());
        assertEquals(customer1, savedReview.getCustomer());
        assertEquals(workshop3, savedReview.getWorkshop());
        assertEquals(reviewInputDto1.rating, reviewOutputDto.rating);
        assertEquals(reviewInputDto1.reviewDescription, reviewOutputDto.reviewDescription);
        assertEquals(workshop3.getTitle(), reviewOutputDto.workshopTitle);
        assertEquals(workshop3.getDate(), reviewOutputDto.workshopDate);
        assertEquals(workshop3.getWorkshopOwner().getCompanyName(), reviewOutputDto.companyNameWorkshopOwner);
        assertEquals(customer1.getFirstName(), reviewOutputDto.firstNameReviewer);
        assertEquals(customer1.getLastName(), reviewOutputDto.lastNameReviewer);
    }


    @Test
    void shouldReturnForbiddenExceptionWhenUserIsWorkshopOwnerOfReview() {
        //Arrange
        when(userRepository.findById(workshopOwner2.getId())).thenReturn(Optional.of(workshopOwner2));
        when(workshopRepository.findById(workshop3.getId())).thenReturn(Optional.of(workshop3));

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.createReview(workshop3.getId(), workshopOwner2.getId(), reviewInputDto1));
        assertEquals("You're not allowed to write a review about your own workshop.", exception.getMessage());
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInCreateReview() {
        //Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(workshopRepository.findById(workshop3.getId())).thenReturn(Optional.of(workshop3));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.createReview(workshop3.getId(), customer1.getId(), reviewInputDto1));
        assertEquals("You're not allowed to create a review from this user account.", exception.getMessage());
    }

    @Test
    void shouldReturnBadRequestExceptionWhenUserHasAlreadySubmittedReview() {
        //Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(workshopRepository.findById(workshop1.getId())).thenReturn(Optional.of(workshop1));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        //        Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> reviewService.createReview(workshop1.getId(), customer1.getId(), reviewInputDto1));
        assertEquals("You've already submitted a review for this workshop, you can only submit 1 review per attended workshop.", exception.getMessage());
    }

    @Test
    void shouldReturnForbiddenExceptionWhenWorkshopHasntTakenPlaceYet() {
        //Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(workshopRepository.findById(workshop4.getId())).thenReturn(Optional.of(workshop4));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.createReview(workshop4.getId(), customer1.getId(), reviewInputDto1));
        assertEquals("You're not allowed to create a review. Either you haven't attended the workshop or the workshop hasn't taken place yet.", exception.getMessage());
    }

    @Test
    void shouldVerifyAndUpdateReviewByAdmin() {
        //        Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewInputDto1.reviewVerified = false;
        reviewInputDto1.feedbackAdmin = "Test Verify Review";

//        Act
        ReviewOutputDto reviewOutputDto = reviewService.verifyReviewByAdmin(review1.getId(), reviewInputDto1);

//        Assert
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();
        assertEquals(reviewInputDto1.rating, savedReview.getRating());
        assertEquals(reviewInputDto1.reviewDescription, savedReview.getReviewDescription());
        assertEquals(reviewInputDto1.reviewVerified, savedReview.getReviewVerified());
        assertEquals(reviewInputDto1.feedbackAdmin, savedReview.getFeedbackAdmin());
        assertEquals(customer1, savedReview.getCustomer());
        assertEquals(workshop1, savedReview.getWorkshop());
        assertEquals(reviewInputDto1.rating, reviewOutputDto.rating);
        assertEquals(reviewInputDto1.reviewDescription, reviewOutputDto.reviewDescription);
        assertEquals(workshop1.getTitle(), reviewOutputDto.workshopTitle);
        assertEquals(workshop1.getDate(), reviewOutputDto.workshopDate);
        assertEquals(workshop1.getWorkshopOwner().getCompanyName(), reviewOutputDto.companyNameWorkshopOwner);
        assertEquals(customer1.getFirstName(), reviewOutputDto.firstNameReviewer);
        assertEquals(customer1.getLastName(), reviewOutputDto.lastNameReviewer);
    }

    @Test
    void shouldUpdateReviewByCustomer() {
        //        Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(authentication.getName()).thenReturn(customer1.getEmail());

//        Act
        ReviewOutputDto reviewOutputDto = reviewService.updateReviewByCustomer(review1.getId(), reviewInputDto1);

//        Assert
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();
        assertEquals(reviewInputDto1.rating, savedReview.getRating());
        assertEquals(reviewInputDto1.reviewDescription, savedReview.getReviewDescription());
        assertEquals(null, savedReview.getReviewVerified());
        assertEquals(reviewInputDto1.feedbackAdmin, savedReview.getFeedbackAdmin());
        assertEquals(customer1, savedReview.getCustomer());
        assertEquals(workshop1, savedReview.getWorkshop());
        assertEquals(reviewInputDto1.rating, reviewOutputDto.rating);
        assertEquals(reviewInputDto1.reviewDescription, reviewOutputDto.reviewDescription);
        assertEquals(workshop1.getTitle(), reviewOutputDto.workshopTitle);
        assertEquals(workshop1.getDate(), reviewOutputDto.workshopDate);
        assertEquals(workshop1.getWorkshopOwner().getCompanyName(), reviewOutputDto.companyNameWorkshopOwner);
        assertEquals(customer1.getFirstName(), reviewOutputDto.firstNameReviewer);
        assertEquals(customer1.getLastName(), reviewOutputDto.lastNameReviewer);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInUpdateReviewByCustomer() {
        //        Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.updateReviewByCustomer(review1.getId(), reviewInputDto1));
        assertEquals("You're not allowed to update this review.", exception.getMessage());
    }

    @Test
    void shouldDeleteReview() {
        //        Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        reviewService.deleteReview(review1.getId());

        //        Assert
        verify(reviewRepository).delete(review1);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInDeleteReview() {
        //        Arrange
        when(reviewRepository.findById(review1.getId())).thenReturn(Optional.of(review1));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> reviewService.deleteReview(review1.getId()));
        assertEquals("You're not allowed to delete this review.", exception.getMessage());
    }
}