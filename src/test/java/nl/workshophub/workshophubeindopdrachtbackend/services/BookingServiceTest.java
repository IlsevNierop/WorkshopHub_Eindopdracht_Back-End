package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.models.*;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.BookingRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.ReviewRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    WorkshopRepository workshopRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookingService bookingService;
    BookingOutputDto bookingOutputDto1;
    BookingOutputDto bookingOutputDto2;
    BookingOutputDto bookingOutputDto3;
    BookingOutputDto bookingOutputDto4;
    Booking booking1;
    Booking booking2;
    Booking booking3;
    Booking booking4;
    User customer1;
    User customer2;
    User workshopOwner1;
    Workshop workshop1;
    Workshop workshop2;
    Review review1;
    Review review2;

    Authentication authentication;


    @BeforeEach
    void setUp() {
        workshop1 = new Workshop();
        workshop1.setId(1L);
        workshop1.setAmountOfParticipants(10);
        workshop1.setTitle("Kaarsen maken");
        workshop1.setDate(LocalDate.of(2023, 10, 15));

        workshop2 = new Workshop();
        workshop2.setId(2L);
        workshop2.setAmountOfParticipants(9);
        workshop2.setTitle("Brood bakken");
        workshop2.setDate(LocalDate.of(2023, 9, 25));

        customer1 = new User();
        customer1.setId(1L);
        customer1.setFirstName("Henk");
        customer1.setLastName("Janssen");
        customer1.setEmail("henk.janssen@gmail.com");

        Set<Authority> authoritiesCustomer1 = new HashSet<>();
        authoritiesCustomer1.add(new Authority(1L, "ROLE_CUSTOMER"));
        customer1.setAuthorities(authoritiesCustomer1);

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

        workshop1.setWorkshopOwner(customer2);
        List<Workshop> customer2WorkshopsAsWorkshopOwner = new ArrayList<>(List.of(workshop1));
        customer2.setWorkshops(customer2WorkshopsAsWorkshopOwner);

        workshop2.setWorkshopOwner(workshopOwner1);
        List<Workshop> workshopOwnersWorkshops = new ArrayList<>(List.of(workshop2));
        workshopOwner1.setWorkshops(workshopOwnersWorkshops);

        Set<Authority> authoritiesCustomer2 = new HashSet<>();
        authoritiesCustomer2.add(new Authority(2L, "ROLE_CUSTOMER"));
        authoritiesCustomer2.add(new Authority(2L, "ROLE_WORKSHOPOWNER"));
        customer2.setAuthorities(authoritiesCustomer2);

        booking1 = new Booking(1L, LocalDate.now(), "Ik heb er zin in", 3, 45D, workshop1, customer1);
        booking2 = new Booking(2L, LocalDate.now(), "Leuk workshop 2", 3, 5D, workshop2, customer1);
        booking3 = new Booking(3L, LocalDate.now(), "Ik ben vega", 4, 6D, workshop1, customer2);
        booking4 = new Booking(4L, LocalDate.now(), "We komen met zn allen uit Groningen", 2, 15D, workshop2, customer2);

        review1 = new Review(1L, 4.3D, "test", true, "mag online", workshop1, customer1);
        review2 = new Review(2L, 4.3D, "test2", false, "mag online", workshop2, customer1);

        workshopRepository.save(workshop1);
        workshopRepository.save(workshop2);
        userRepository.save(customer1);
        userRepository.save(customer2);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Booking> customer1BookingsList = new ArrayList<>(List.of(booking1, booking2));
        customer1.setBookings(customer1BookingsList);

        List<Booking> customer2BookingsList = new ArrayList<>(List.of(booking3, booking4));
        customer2.setBookings(customer2BookingsList);

        List<Booking> workshop1BookingsList = new ArrayList<>(List.of(booking1, booking3));
        workshop1.setWorkshopBookings(workshop1BookingsList);

        List<Booking> workshop2BookingsList = new ArrayList<>(List.of(booking2, booking4));
        workshop2.setWorkshopBookings(workshop2BookingsList);

        List<Review> customer1ReviewsList = new ArrayList<>(List.of(review1, review2));
        customer1.setCustomerReviews(customer1ReviewsList);

        List<Review> workshop1ReviewsList = new ArrayList<>(List.of(review1));
        workshop1.setWorkshopReviews(workshop1ReviewsList);

        List<Review> workshop2ReviewsList = new ArrayList<>(List.of(review2));
        workshop2.setWorkshopReviews(workshop2ReviewsList);

        bookingOutputDto1 = new BookingOutputDto();
        bookingOutputDto1.id = 1L;
        bookingOutputDto1.dateOrder = LocalDate.now();
        bookingOutputDto1.commentsCustomer = "Ik heb er zin in";
        bookingOutputDto1.amount = 3;
        bookingOutputDto1.totalPrice = 45D;
        bookingOutputDto1.workshopId = 1L;
        bookingOutputDto1.spotsAvailableWorkshop = 3;
        bookingOutputDto1.workshopTitle = "Kaarsen maken";
        bookingOutputDto1.workshopDate = LocalDate.of(2023, 10, 15);
        bookingOutputDto1.customerId = 1L;
        bookingOutputDto1.firstNameCustomer = "Henk";
        bookingOutputDto1.lastNameCustomer = "Janssen";
        bookingOutputDto1.emailCustomer = "henk.janssen@gmail.com";
        bookingOutputDto1.reviewCustomerWritten = true;

        bookingOutputDto2 = new BookingOutputDto();
        bookingOutputDto2.id = 2L;
        bookingOutputDto2.dateOrder = LocalDate.now();
        bookingOutputDto2.commentsCustomer = "Leuk workshop 2";
        bookingOutputDto2.amount = 3;
        bookingOutputDto2.totalPrice = 5D;
        bookingOutputDto2.workshopId = 2L;
        bookingOutputDto2.spotsAvailableWorkshop = 4;
        bookingOutputDto2.workshopTitle = "Brood bakken";
        bookingOutputDto2.workshopDate = LocalDate.of(2023, 9, 25);
        bookingOutputDto2.customerId = 1L;
        bookingOutputDto2.firstNameCustomer = "Henk";
        bookingOutputDto2.lastNameCustomer = "Janssen";
        bookingOutputDto2.emailCustomer = "henk.janssen@gmail.com";
        bookingOutputDto2.reviewCustomerWritten = true;

        bookingOutputDto3 = new BookingOutputDto();
        bookingOutputDto3.id = 3L;
        bookingOutputDto3.dateOrder = LocalDate.now();
        bookingOutputDto3.commentsCustomer = "Ik ben vega";
        bookingOutputDto3.amount = 4;
        bookingOutputDto3.totalPrice = 6D;
        bookingOutputDto3.workshopId = 1L;
        bookingOutputDto3.spotsAvailableWorkshop = 3;
        bookingOutputDto3.workshopTitle = "Kaarsen maken";
        bookingOutputDto3.workshopDate = LocalDate.of(2023, 10, 15);
        bookingOutputDto3.customerId = 2L;
        bookingOutputDto3.firstNameCustomer = "Isabella";
        bookingOutputDto3.lastNameCustomer = "Rossi";
        bookingOutputDto3.emailCustomer = "isabella.rossi@gmail.com";
        bookingOutputDto3.reviewCustomerWritten = false;

        bookingOutputDto4 = new BookingOutputDto();
        bookingOutputDto4.id = 4L;
        bookingOutputDto4.dateOrder = LocalDate.now();
        bookingOutputDto4.commentsCustomer = "We komen met zn allen uit Groningen";
        bookingOutputDto4.amount = 2;
        bookingOutputDto4.totalPrice = 15D;
        bookingOutputDto4.workshopId = 2L;
        bookingOutputDto4.spotsAvailableWorkshop = 4;
        bookingOutputDto4.workshopTitle = "Brood bakken";
        bookingOutputDto4.workshopDate = LocalDate.of(2023, 9, 25);
        bookingOutputDto4.customerId = 2L;
        bookingOutputDto4.firstNameCustomer = "Isabella";
        bookingOutputDto4.lastNameCustomer = "Rossi";
        bookingOutputDto4.emailCustomer = "isabella.rossi@gmail.com";
        bookingOutputDto4.reviewCustomerWritten = false;

        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // ook opslaan in repository om te testen of het verwijderen werkt.

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldReturnAllBookingsFromUser() {
        //        Arrange
        List<BookingOutputDto> bookingOutputDtos = List.of(bookingOutputDto1, bookingOutputDto2);

        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        //Dit is om te checken of de juiste persoon de juiste gegevens opvraagt
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        List<BookingOutputDto> getAllBookingsFromCustomer1 = bookingService.getAllBookingsFromUser(customer1.getId());

        //        Assert
        assertEquals(bookingOutputDtos.get(0).id, getAllBookingsFromCustomer1.get(0).id);
        assertEquals(bookingOutputDtos.get(0).totalPrice, getAllBookingsFromCustomer1.get(0).totalPrice);
        assertEquals(bookingOutputDtos.get(0).spotsAvailableWorkshop, getAllBookingsFromCustomer1.get(0).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(1).customerId, getAllBookingsFromCustomer1.get(1).customerId);
        assertEquals(bookingOutputDtos.get(1).reviewCustomerWritten, getAllBookingsFromCustomer1.get(1).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).workshopId, getAllBookingsFromCustomer1.get(1).workshopId);
        assertEquals(bookingOutputDtos.get(1).dateOrder, getAllBookingsFromCustomer1.get(1).dateOrder);
        assertEquals(bookingOutputDtos.get(1).commentsCustomer, getAllBookingsFromCustomer1.get(1).commentsCustomer);
        assertEquals(bookingOutputDtos.get(1).amount, getAllBookingsFromCustomer1.get(1).amount);
        assertEquals(bookingOutputDtos.get(1).totalPrice, getAllBookingsFromCustomer1.get(1).totalPrice);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectWhenRequestingAllBookingsFromUser() {
        //        Arrange
        when(userRepository.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        //customer2 is logged in (while requesting data about customer1)
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.getAllBookingsFromUser(customer1.getId()));
        assertEquals("You're not allowed to view bookings from this user.", exception.getMessage());

    }

    @Test
    void shouldReturnAllBookingsFromWorkshop() {
        //        Arrange
        List<BookingOutputDto> bookingOutputDtos = List.of(bookingOutputDto1, bookingOutputDto3);

        when(workshopRepository.findById(workshop1.getId())).thenReturn(Optional.of(workshop1));

        //customer2 is workshopowner of workshop1
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        List<BookingOutputDto> getAllBookingsFromWorkshop1 = bookingService.getAllBookingsFromWorkshop(workshop1.getId());

        //        Assert
        assertEquals(bookingOutputDtos.get(0).id, getAllBookingsFromWorkshop1.get(0).id);
        assertEquals(bookingOutputDtos.get(0).dateOrder, getAllBookingsFromWorkshop1.get(0).dateOrder);
        assertEquals(bookingOutputDtos.get(0).commentsCustomer, getAllBookingsFromWorkshop1.get(0).commentsCustomer);
        assertEquals(bookingOutputDtos.get(0).amount, getAllBookingsFromWorkshop1.get(0).amount);
        assertEquals(bookingOutputDtos.get(0).totalPrice, getAllBookingsFromWorkshop1.get(0).totalPrice);
        assertEquals(bookingOutputDtos.get(0).workshopId, getAllBookingsFromWorkshop1.get(0).workshopId);
        assertEquals(bookingOutputDtos.get(0).spotsAvailableWorkshop, getAllBookingsFromWorkshop1.get(0).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(0).workshopTitle, getAllBookingsFromWorkshop1.get(0).workshopTitle);
        assertEquals(bookingOutputDtos.get(0).workshopDate, getAllBookingsFromWorkshop1.get(0).workshopDate);
        assertEquals(bookingOutputDtos.get(0).customerId, getAllBookingsFromWorkshop1.get(0).customerId);
        assertEquals(bookingOutputDtos.get(0).firstNameCustomer, getAllBookingsFromWorkshop1.get(0).firstNameCustomer);
        assertEquals(bookingOutputDtos.get(0).lastNameCustomer, getAllBookingsFromWorkshop1.get(0).lastNameCustomer);
        assertEquals(bookingOutputDtos.get(0).emailCustomer, getAllBookingsFromWorkshop1.get(0).emailCustomer);
        assertEquals(bookingOutputDtos.get(0).reviewCustomerWritten, getAllBookingsFromWorkshop1.get(0).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).customerId, getAllBookingsFromWorkshop1.get(1).customerId);
        assertEquals(bookingOutputDtos.get(1).reviewCustomerWritten, getAllBookingsFromWorkshop1.get(1).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).workshopId, getAllBookingsFromWorkshop1.get(1).workshopId);
        assertEquals(bookingOutputDtos.get(1).dateOrder, getAllBookingsFromWorkshop1.get(1).dateOrder);
        assertEquals(bookingOutputDtos.get(1).commentsCustomer, getAllBookingsFromWorkshop1.get(1).commentsCustomer);
        assertEquals(bookingOutputDtos.get(1).amount, getAllBookingsFromWorkshop1.get(1).amount);
        assertEquals(bookingOutputDtos.get(1).totalPrice, getAllBookingsFromWorkshop1.get(1).totalPrice);
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectWhenRequestingAllBookingsFromWorkshop() {
        //        Arrange
        when(workshopRepository.findById(workshop1.getId())).thenReturn(Optional.of(workshop1));

        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.getAllBookingsFromWorkshop(workshop1.getId()));
        assertEquals("You're not allowed to view bookings from this workshop, since you're not the owner.", exception.getMessage());

    }

    @Test
    void shouldReturnAllBookingsFromWorkshopsFromWorkshopOwner() {
        //        Arrange
        List<BookingOutputDto> bookingOutputDtos = List.of(bookingOutputDto1, bookingOutputDto3);

        when(userRepository.findById(customer2.getId())).thenReturn(Optional.of(customer2));
        when(workshopRepository.findByWorkshopOwnerId(customer2.getId())).thenReturn(List.of(workshop1));

        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        List<BookingOutputDto> getAllBookingsFromWorkshopOwnerCustomer2 = bookingService.getAllBookingsFromWorkshopsFromWorkshopOwner(customer2.getId());

        //        Assert
        assertEquals(bookingOutputDtos.get(0).id, getAllBookingsFromWorkshopOwnerCustomer2.get(0).id);
        assertEquals(bookingOutputDtos.get(0).dateOrder, getAllBookingsFromWorkshopOwnerCustomer2.get(0).dateOrder);
        assertEquals(bookingOutputDtos.get(0).commentsCustomer, getAllBookingsFromWorkshopOwnerCustomer2.get(0).commentsCustomer);
        assertEquals(bookingOutputDtos.get(0).amount, getAllBookingsFromWorkshopOwnerCustomer2.get(0).amount);
        assertEquals(bookingOutputDtos.get(0).totalPrice, getAllBookingsFromWorkshopOwnerCustomer2.get(0).totalPrice);
        assertEquals(bookingOutputDtos.get(0).workshopId, getAllBookingsFromWorkshopOwnerCustomer2.get(0).workshopId);
        assertEquals(bookingOutputDtos.get(0).spotsAvailableWorkshop, getAllBookingsFromWorkshopOwnerCustomer2.get(0).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(0).workshopTitle, getAllBookingsFromWorkshopOwnerCustomer2.get(0).workshopTitle);
        assertEquals(bookingOutputDtos.get(0).workshopDate, getAllBookingsFromWorkshopOwnerCustomer2.get(0).workshopDate);
        assertEquals(bookingOutputDtos.get(0).customerId, getAllBookingsFromWorkshopOwnerCustomer2.get(0).customerId);
        assertEquals(bookingOutputDtos.get(0).firstNameCustomer, getAllBookingsFromWorkshopOwnerCustomer2.get(0).firstNameCustomer);
        assertEquals(bookingOutputDtos.get(0).lastNameCustomer, getAllBookingsFromWorkshopOwnerCustomer2.get(0).lastNameCustomer);
        assertEquals(bookingOutputDtos.get(0).emailCustomer, getAllBookingsFromWorkshopOwnerCustomer2.get(0).emailCustomer);
        assertEquals(bookingOutputDtos.get(0).reviewCustomerWritten, getAllBookingsFromWorkshopOwnerCustomer2.get(0).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).customerId, getAllBookingsFromWorkshopOwnerCustomer2.get(1).customerId);
        assertEquals(bookingOutputDtos.get(1).reviewCustomerWritten, getAllBookingsFromWorkshopOwnerCustomer2.get(1).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).workshopId, getAllBookingsFromWorkshopOwnerCustomer2.get(1).workshopId);
        assertEquals(bookingOutputDtos.get(1).dateOrder, getAllBookingsFromWorkshopOwnerCustomer2.get(1).dateOrder);
        assertEquals(bookingOutputDtos.get(1).commentsCustomer, getAllBookingsFromWorkshopOwnerCustomer2.get(1).commentsCustomer);
        assertEquals(bookingOutputDtos.get(1).amount, getAllBookingsFromWorkshopOwnerCustomer2.get(1).amount);
        assertEquals(bookingOutputDtos.get(1).totalPrice, getAllBookingsFromWorkshopOwnerCustomer2.get(1).totalPrice);
    }

    //    @Test
//    void shouldReturnRecordNotFoundExceptionWhenUserIsNotFoundInGetAllBookingsFromWorkshopsFromWorkshopOwner() {
//        //        Arrange
//        when(workshopRepository.findById(workshop1.getId())).thenReturn(Optional.of(workshop1));
//
//        when(authentication.getName()).thenReturn(customer1.getEmail());
//
//        //        Act
//        ForbiddenException exception = assertThrows(ForbiddenException.class,
//                () -> bookingService.getAllBookingsFromWorkshop(workshop1.getId()));
//
//        //        Assert
//        assertEquals("You're not allowed to view bookings from this workshop, since you're not the owner.", exception.getMessage());
//
//    }
    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInGetAllBookingsFromWorkshopsFromWorkshopOwner() {
        //        Arrange
        when(userRepository.findById(customer2.getId())).thenReturn(Optional.of(customer2));

        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.getAllBookingsFromWorkshopsFromWorkshopOwner(customer2.getId()));
        assertEquals("You're not allowed to view bookings from this user.", exception.getMessage());

    }

    @Test
    void shouldReturnAllBookings() {
        //        Arrange
        List<BookingOutputDto> bookingOutputDtos = List.of(bookingOutputDto1, bookingOutputDto2, bookingOutputDto3, bookingOutputDto4);

        when(bookingRepository.findAll()).thenReturn(List.of(booking1, booking2, booking3, booking4));

        //        Act
        List<BookingOutputDto> getAllBookings = bookingService.getAllBookings();

        //        Assert
        assertEquals(bookingOutputDtos.size(), getAllBookings.size());
        assertEquals(bookingOutputDtos.get(0).id, getAllBookings.get(0).id);
        assertEquals(bookingOutputDtos.get(1).customerId, getAllBookings.get(1).customerId);
        assertEquals(bookingOutputDtos.get(1).reviewCustomerWritten, getAllBookings.get(1).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).workshopId, getAllBookings.get(1).workshopId);
        assertEquals(bookingOutputDtos.get(1).dateOrder, getAllBookings.get(1).dateOrder);
        assertEquals(bookingOutputDtos.get(1).commentsCustomer, getAllBookings.get(1).commentsCustomer);
        assertEquals(bookingOutputDtos.get(1).amount, getAllBookings.get(1).amount);
        assertEquals(bookingOutputDtos.get(1).totalPrice, getAllBookings.get(1).totalPrice);
        assertEquals(bookingOutputDtos.get(2).workshopId, getAllBookings.get(2).workshopId);
        assertEquals(bookingOutputDtos.get(2).spotsAvailableWorkshop, getAllBookings.get(2).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(2).workshopTitle, getAllBookings.get(2).workshopTitle);
        assertEquals(bookingOutputDtos.get(0).workshopDate, getAllBookings.get(0).workshopDate);
        assertEquals(bookingOutputDtos.get(3).lastNameCustomer, getAllBookings.get(3).lastNameCustomer);
        assertEquals(bookingOutputDtos.get(3).emailCustomer, getAllBookings.get(3).emailCustomer);
        assertEquals(bookingOutputDtos.get(0).spotsAvailableWorkshop, getAllBookings.get(0).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(1).spotsAvailableWorkshop, getAllBookings.get(1).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(2).spotsAvailableWorkshop, getAllBookings.get(2).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(3).spotsAvailableWorkshop, getAllBookings.get(3).spotsAvailableWorkshop);
        assertEquals(bookingOutputDtos.get(0).reviewCustomerWritten, getAllBookings.get(0).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(1).reviewCustomerWritten, getAllBookings.get(1).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(2).reviewCustomerWritten, getAllBookings.get(2).reviewCustomerWritten);
        assertEquals(bookingOutputDtos.get(3).reviewCustomerWritten, getAllBookings.get(3).reviewCustomerWritten);


    }

    @Test
    void shouldGetOneBookingById() {
//        Arrange
        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        BookingOutputDto bookingOutputDtoExpected = bookingService.getOneBookingById(booking1.getId());

//        Assert
        assertEquals(bookingOutputDto1.id, bookingOutputDtoExpected.id);
        assertEquals(bookingOutputDto1.dateOrder, bookingOutputDtoExpected.dateOrder);
        assertEquals(bookingOutputDto1.commentsCustomer, bookingOutputDtoExpected.commentsCustomer);
        assertEquals(bookingOutputDto1.amount, bookingOutputDtoExpected.amount);
        assertEquals(bookingOutputDto1.totalPrice, bookingOutputDtoExpected.totalPrice);
        assertEquals(bookingOutputDto1.workshopId, bookingOutputDtoExpected.workshopId);
        assertEquals(bookingOutputDto1.spotsAvailableWorkshop, bookingOutputDtoExpected.spotsAvailableWorkshop);
        assertEquals(bookingOutputDto1.workshopTitle, bookingOutputDtoExpected.workshopTitle);
        assertEquals(bookingOutputDto1.workshopDate, bookingOutputDtoExpected.workshopDate);
        assertEquals(bookingOutputDto1.customerId, bookingOutputDtoExpected.customerId);
        assertEquals(bookingOutputDto1.firstNameCustomer, bookingOutputDtoExpected.firstNameCustomer);
        assertEquals(bookingOutputDto1.lastNameCustomer, bookingOutputDtoExpected.lastNameCustomer);
        assertEquals(bookingOutputDto1.emailCustomer, bookingOutputDtoExpected.emailCustomer);
        assertEquals(bookingOutputDto1.reviewCustomerWritten, bookingOutputDtoExpected.reviewCustomerWritten);

    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInGetOneBookingById() {
        //        Arrange
        when(bookingRepository.findById(booking2.getId())).thenReturn(Optional.of(booking2));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.getOneBookingById(booking2.getId()));
        assertEquals("You're not allowed to view bookings from this user.", exception.getMessage());
    }

    @Test
    void shouldGenerateAndDownloadCsvWorkshopOwner() {
        //        Arrange
        List<Booking> workshop1Bookings = new ArrayList<>(List.of(booking2, booking4));
        when(userRepository.findById(workshopOwner1.getId())).thenReturn(Optional.of(workshopOwner1));
        when(workshopRepository.findByWorkshopOwnerId(workshopOwner1.getId())).thenReturn(List.of(workshop2));

        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());

        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Booking ID,Date booking, Amount, First name customer, Last name customer, Email customer, Comments customer, Total Price, Workshop ID, Title workshop, Workshop date");

        for (Booking booking : workshop1Bookings) {
            csvContent.append(System.lineSeparator())
                    .append(booking.getId()).append(",")
                    .append(booking.getDateOrder()).append(",")
                    .append(booking.getAmount()).append(",")
                    .append(booking.getCustomer().getFirstName()).append(",")
                    .append(booking.getCustomer().getLastName()).append(",")
                    .append(booking.getCustomer().getEmail()).append(",")
                    .append(booking.getCommentsCustomer()).append(",")
                    .append(booking.getTotalPrice()).append(",")
                    .append(booking.getWorkshop().getId()).append(",")
                    .append(booking.getWorkshop().getTitle()).append(",")
                    .append(booking.getWorkshop().getDate()).append(",");
        }
        byte[] content = csvContent.toString().getBytes();
        ByteArrayResource byteArrayResourceExpected = new ByteArrayResource(content);

//        Act
        ByteArrayResource getCsvContent = bookingService.generateAndDownloadCsvWorkshopOwner(workshopOwner1.getId());

//        Assert
        assertEquals(byteArrayResourceExpected, getCsvContent);
        assertEquals(byteArrayResourceExpected.contentLength(), getCsvContent.contentLength());
    }

    @Test
    void shouldReturnForbiddenExceptionWhenUserIsIncorrectInGenerateAndDownloadCsvWorkshopOwner() {
        //        Arrange
        when(userRepository.findById(workshopOwner1.getId())).thenReturn(Optional.of(workshopOwner1));
        when(authentication.getName()).thenReturn(customer1.getEmail());

        //        Act
        //        Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> bookingService.generateAndDownloadCsvWorkshopOwner(workshopOwner1.getId()));
        assertEquals("You're not allowed to view bookings from this user.", exception.getMessage());
    }

//    @Test
//    void shouldReturnIOExceptionWhenIssueInFileWriterInGenerateAndDownloadCsvWorkshopOwner() throws IOException {
//        //        Arrange
//        when(userRepository.findById(workshopOwner1.getId())).thenReturn(Optional.of(workshopOwner1));
//        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());
//
//        // Mock the FileWriter to throw IOException when writing the file
//        FileWriter fileWriter = org.mockito.Mockito.mock(FileWriter.class);
//        try {
//            org.mockito.Mockito.doThrow(new IOException("Test IOException")).when(fileWriter).write(anyString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Use reflection to set the mocked FileWriter in the bookingService
//        try {
//            java.lang.reflect.Field field = bookingService.getClass().getDeclaredField("writer");
//            field.setAccessible(true);
//            field.set(bookingService, fileWriter);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> bookingService.generateAndDownloadCsvWorkshopOwner(workshopOwner1.getId()));
//    }


    @Test
    void shouldGenerateAndDownloadCsvWorkshop() {
        //        Arrange
        //booking 1 en 3 workshop 1
        List<Booking> workshop1Bookings = new ArrayList<>(List.of(booking1, booking3));
        when(workshopRepository.findById(workshop1.getId())).thenReturn(Optional.of(workshop1));
        when(authentication.getName()).thenReturn(customer2.getEmail());

        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Booking ID,Date booking, Amount, First name customer, Last name customer, Email customer, Comments customer, Total Price, Workshop ID, Title workshop, Workshop date");

        for (Booking booking : workshop1Bookings) {
            csvContent.append(System.lineSeparator())
                    .append(booking.getId()).append(",")
                    .append(booking.getDateOrder()).append(",")
                    .append(booking.getAmount()).append(",")
                    .append(booking.getCustomer().getFirstName()).append(",")
                    .append(booking.getCustomer().getLastName()).append(",")
                    .append(booking.getCustomer().getEmail()).append(",")
                    .append(booking.getCommentsCustomer()).append(",")
                    .append(booking.getTotalPrice()).append(",")
                    .append(booking.getWorkshop().getId()).append(",")
                    .append(booking.getWorkshop().getTitle()).append(",")
                    .append(booking.getWorkshop().getDate()).append(",");
        }
        byte[] content = csvContent.toString().getBytes();
        ByteArrayResource byteArrayResourceExpected = new ByteArrayResource(content);

//        Act
        ByteArrayResource getCsvContent = bookingService.generateAndDownloadCsvWorkshop(workshop1.getId());

//        Assert
        assertEquals(byteArrayResourceExpected, getCsvContent);
        assertEquals(byteArrayResourceExpected.contentLength(), getCsvContent.contentLength());

    }

    @Test
    @Disabled
    void generateAndDownloadCsv() {
    }

    @Test
    @Disabled
    void createBooking() {
    }

    @Test
    @Disabled
    void updateBooking() {
    }

    @Test
    @Disabled
    void deleteBooking() {
    }

    @Test
    @Disabled
    void transferBookingToBookingOutputDto() {
    }

    @Test
    @Disabled
    void transferBookingInputDtoToBooking() {
    }

    @Test
    @Disabled
    void getReviewCustomerWritten() {
    }
}