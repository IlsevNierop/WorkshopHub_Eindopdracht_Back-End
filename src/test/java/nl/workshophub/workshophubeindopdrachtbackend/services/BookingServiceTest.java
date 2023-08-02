package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.BookingRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    WorkshopRepository workshopRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookingService bookingService;

    BookingInputDto bookingInputDto;
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
    Workshop workshop1;
    Workshop workshop2;


    @BeforeEach
    void setUp() {
        booking1 = new Booking(1L, LocalDate.now(), "Ik heb er zin in", 3, 45D, workshop1, customer1);
        booking2 = new Booking(2L, LocalDate.now(), "Leuk workshop 2", 3, 5D, workshop2, customer1);
        booking3 = new Booking(3L, LocalDate.now(), "Ik ben vega", 4, 6D, workshop1, customer2);
        booking4 = new Booking(4L, LocalDate.now(), "We komen met zn allen uit Groningen", 2, 15D, workshop2, customer2);

        workshop1.setId(1L);
        workshop1.setAmountOfParticipants(10);
        workshop1.setTitle("Kaarsen maken");
        workshop1.setDate(LocalDate.of(2023, 10, 15));

        workshop2.setId(2L);
        workshop2.setAmountOfParticipants(9);
        workshop2.setTitle("Brood bakken");
        workshop2.setDate(LocalDate.of(2023, 9, 25));


        customer1.setId(1L);
        customer1.setFirstName("Henk");
        customer1.setLastName("Janssen");
        customer1.setEmail("henk.janssen@gmail.com");

        customer2.setId(2L);
        customer2.setFirstName("Isabella");
        customer2.setLastName("Rossi");
        customer2.setEmail("isabella.rossi@gmail.com");

        //methode available spots & reviewcustomerwritten

        bookingOutputDto1.id= 1L;
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

        bookingOutputDto2.id= 2L;
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

        bookingOutputDto3.id= 3L;
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
        bookingOutputDto3.reviewCustomerWritten = true;

        bookingOutputDto4.id= 4L;
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
        bookingOutputDto4.reviewCustomerWritten = true;



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void getAllBookingsFromUser() {
    }

    @Test
    @Disabled
    void getAllBookingsFromWorkshop() {
    }

    @Test
    @Disabled
    void getAllBookingsFromWorkshopsFromWorkshopOwner() {
    }

    @Test
    @Disabled
    void getAllBookings() {
        //        Arrange

//        Act

//        Assert
    }

    @Test
    @Disabled
    void getOneBookingById() {
//        Arrange

//        Act

//        Assert
    }

    @Test
    @Disabled
    void generateAndDownloadCsvWorkshopOwner() {
    }

    @Test
    @Disabled
    void generateAndDownloadCsvWorkshop() {
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