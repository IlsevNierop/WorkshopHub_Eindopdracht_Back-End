package nl.workshophub.workshophubeindopdrachtbackend.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.INDOORS;
import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.OUTDOORS;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class WorkshopControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkshopRepository workshopRepository;
    @MockBean
    private Authentication authentication;

    Workshop workshop;
    Workshop workshop2;
    WorkshopOutputDto workshopOutputDto;
    WorkshopOutputDto workshopOutputDto2;
    WorkshopInputDto workshopInputDto1;
    User workshopOwner1;
    User customer1;

    @BeforeEach
    void setUp() {
        workshopOwner1 = new User();
        workshopOwner1.setCompanyName("Test bedrijf");
        workshopOwner1.setWorkshopOwnerVerified(true);
        workshopOwner1.setWorkshopOwner(true);
        workshopOwner1.setEmail("test@example.com");

        customer1 = new User();
        customer1.setEmail("customer1@example.com");

        workshop = new Workshop();
        workshop.setTitle("Kaarsen maken test");
        workshop.setDate(LocalDate.of(2023, 9, 25));
        workshop.setStartTime((LocalTime.of(16, 0)));
        workshop.setEndTime((LocalTime.of(18, 0)));
        workshop.setPrice(45D);
        workshop.setInOrOutdoors(OUTDOORS);
        workshop.setLocation("Amsterdam");
        workshop.setHighlightedInfo("Neem je regenjas mee");
        workshop.setDescription("Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee.");
        workshop.setAmountOfParticipants(10);
        workshop.setWorkshopCategory1("Koken");
        workshop.setWorkshopCategory2("Handwerk");
        workshop.setWorkshopVerified(true);
        workshop.setFeedbackAdmin("mag online");
        workshop.setPublishWorkshop(true);
        workshop.setWorkshopPicUrl("testurl");
        workshop.setFileName("fotovanworkshop");
        workshop.setWorkshopBookings(null);
        workshop.setWorkshopReviews(null);
        workshop.setFavsUsers(null);

        workshop2 = new Workshop();
        workshop2.setTitle("Test workshop 2");
        workshop2.setDate(LocalDate.of(2023, 10, 23));
        workshop2.setStartTime((LocalTime.of(16, 0)));
        workshop2.setEndTime((LocalTime.of(18, 0)));
        workshop2.setPrice(45D);
        workshop2.setInOrOutdoors(OUTDOORS);
        workshop2.setLocation("Amsterdam");
        workshop2.setHighlightedInfo("Neem je regenjas mee");
        workshop2.setDescription("Een hele leuke workshop, nummer 2, met kaarsen maken en een regenjas mee.");
        workshop2.setAmountOfParticipants(10);
        workshop2.setWorkshopCategory1("Koken");
        workshop2.setWorkshopCategory2("Handwerk");
        workshop2.setWorkshopVerified(true);
        workshop2.setFeedbackAdmin("mag online");
        workshop2.setPublishWorkshop(true);
        workshop2.setWorkshopPicUrl("testurl");
        workshop2.setFileName("fotovanworkshop");
        workshop2.setWorkshopBookings(null);
        workshop2.setWorkshopReviews(null);
        workshop2.setFavsUsers(null);

        userRepository.save(workshopOwner1);
        userRepository.save(customer1);

        workshop.setWorkshopOwner(workshopOwner1);
        workshop.setFavsUsers(new HashSet<>(List.of(customer1)));
        workshop2.setWorkshopOwner(workshopOwner1);

        workshopRepository.save(workshop);
        workshopRepository.save(workshop2);

        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop, workshop2));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        userRepository.save(workshopOwner1);

        Set<Workshop> customerFavouriteWorkshops = new HashSet<>(List.of(workshop));
        customer1.setFavouriteWorkshops(customerFavouriteWorkshops);
        userRepository.save(customer1);


        workshopOutputDto = new WorkshopOutputDto();
        workshopOutputDto.id = workshop.getId();
        workshopOutputDto.title = workshop.getTitle();
        workshopOutputDto.date = workshop.getDate();
        workshopOutputDto.startTime = workshop.getStartTime();
        workshopOutputDto.endTime = workshop.getEndTime();
        workshopOutputDto.price = workshop.getPrice();
        workshopOutputDto.inOrOutdoors = workshop.getInOrOutdoors();
        workshopOutputDto.location = workshop.getLocation();
        workshopOutputDto.highlightedInfo = workshop.getHighlightedInfo();
        workshopOutputDto.description = workshop.getDescription();
        workshopOutputDto.amountOfParticipants = workshop.getAmountOfParticipants();
        workshopOutputDto.spotsAvailable = workshop.getAmountOfParticipants();
        workshopOutputDto.workshopCategory1 = workshop.getWorkshopCategory1();
        workshopOutputDto.workshopCategory2 = workshop.getWorkshopCategory2();
        workshopOutputDto.workshopVerified = workshop.getWorkshopVerified();
        workshopOutputDto.feedbackAdmin = workshop.getFeedbackAdmin();
        workshopOutputDto.publishWorkshop = workshop.getPublishWorkshop();
        workshopOutputDto.workshopOwnerReviews = new ArrayList<>();
        workshopOutputDto.isFavourite = true;

        workshopOutputDto.workshopOwnerId = workshop.getWorkshopOwner().getId();
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        workshopOutputDto.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto.numberOfReviews = null;
        workshopOutputDto.amountOfFavsAndBookings = (workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop());
        workshopOutputDto.workshopPicUrl = workshop.getWorkshopPicUrl();

        workshopOutputDto2 = new WorkshopOutputDto();
        workshopOutputDto2.id = workshop2.getId();
        workshopOutputDto2.title = workshop2.getTitle();
        workshopOutputDto2.date = workshop2.getDate();
        workshopOutputDto2.startTime = workshop2.getStartTime();
        workshopOutputDto2.endTime = workshop2.getEndTime();
        workshopOutputDto2.price = workshop2.getPrice();
        workshopOutputDto2.inOrOutdoors = workshop2.getInOrOutdoors();
        workshopOutputDto2.location = workshop2.getLocation();
        workshopOutputDto2.highlightedInfo = workshop2.getHighlightedInfo();
        workshopOutputDto2.description = workshop2.getDescription();
        workshopOutputDto2.amountOfParticipants = workshop2.getAmountOfParticipants();
        workshopOutputDto2.spotsAvailable = workshop2.getAmountOfParticipants();
        workshopOutputDto2.workshopCategory1 = workshop2.getWorkshopCategory1();
        workshopOutputDto2.workshopCategory2 = workshop2.getWorkshopCategory2();
        workshopOutputDto2.workshopVerified = workshop2.getWorkshopVerified();
        workshopOutputDto2.feedbackAdmin = workshop2.getFeedbackAdmin();
        workshopOutputDto2.publishWorkshop = workshop2.getPublishWorkshop();
        workshopOutputDto2.isFavourite = true;
        workshopOutputDto2.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto2.workshopOwnerId = workshop2.getWorkshopOwner().getId();
        workshopOutputDto2.workshopOwnerCompanyName = workshop2.getWorkshopOwner().getCompanyName();
        workshopOutputDto2.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto2.numberOfReviews = null;
        workshopOutputDto2.amountOfFavsAndBookings = 1;
        workshopOutputDto2.workshopPicUrl = workshop2.getWorkshopPicUrl();

        workshopInputDto1 = new WorkshopInputDto();
        workshopInputDto1.title = "Taart bakken";
        workshopInputDto1.date = LocalDate.of(2023, 10, 5);
        workshopInputDto1.startTime = (LocalTime.of(16, 0));
        workshopInputDto1.endTime = (LocalTime.of(19, 0));
        workshopInputDto1.price = 99.0;
        workshopInputDto1.inOrOutdoors = INDOORS;
        workshopInputDto1.location = "Utrecht";
        workshopInputDto1.highlightedInfo = "Neem je lekkerste specerijen mee";
        workshopInputDto1.description = "Een workshop taart bakken in Utrecht. Neem al je vrienden mee en bak de lekkerste taart die je ooit gemaakt hebt. Je mag de taart mee naar huis nemen. En als je je favoriete specerijen meeneemt, kunnen we een improvisatie taart maken.";
        workshopInputDto1.amountOfParticipants = 8;
        workshopInputDto1.workshopCategory1 = "Bakken";
        workshopInputDto1.workshopCategory2 = "Koken";
        workshopInputDto1.workshopVerified = null;
        workshopInputDto1.feedbackAdmin = null;
        workshopInputDto1.publishWorkshop = null;

        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        for (User user : userRepository.findAll()) {
            user.setFavouriteWorkshops(null);
            userRepository.save(user);
        }

        for (Workshop w : workshopRepository.findAll()) {
            w.setWorkshopOwner(null);
            workshopRepository.save(w);
        }

        userRepository.deleteAll();
        workshopRepository.deleteAll();
    }


    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithoutUser() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        mockMvc.perform(get("/workshops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(workshop.getId()))
                .andExpect(jsonPath("$[0].title").value(workshop.getTitle()))
                .andExpect(jsonPath("$[0].date").value(workshop.getDate().toString()))
                .andExpect(jsonPath("$[0].startTime").value(workshop.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(workshop.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].price").value(Double.toString(workshop.getPrice())))
                .andExpect(jsonPath("$[0].inOrOutdoors").value(workshop.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[0].location").value(workshop.getLocation()))
                .andExpect(jsonPath("$[0].highlightedInfo").value(workshop.getHighlightedInfo()))
                .andExpect(jsonPath("$[0].description").value(workshop.getDescription()))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(workshop.getAmountOfParticipants()))
                .andExpect(jsonPath("$[0].workshopCategory1").value(workshop.getWorkshopCategory1()))
                .andExpect(jsonPath("$[0].workshopCategory2").value(workshop.getWorkshopCategory2()))
                .andExpect(jsonPath("$[0].workshopVerified").value(workshop.getWorkshopVerified()))
                .andExpect(jsonPath("$[0].feedbackAdmin").value(workshop.getFeedbackAdmin()))
                .andExpect(jsonPath("$[0].publishWorkshop").value(workshop.getPublishWorkshop()))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value(workshop.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").doesNotExist())
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value((workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop())))
                .andExpect(jsonPath("$[0].workshopPicUrl").value(workshop.getWorkshopPicUrl()))
                .andExpect(jsonPath("$[0].id").value(workshop.getId()))
                .andExpect(jsonPath("$[0].title").value(workshop.getTitle()))
                .andExpect(jsonPath("$[0].date").value(workshop.getDate().toString()))
                .andExpect(jsonPath("$[0].startTime").value(workshop.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(workshop.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].price").value(Double.toString(workshop.getPrice())))
                .andExpect(jsonPath("$[0].inOrOutdoors").value(workshop.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[0].location").value(workshop.getLocation()))
                .andExpect(jsonPath("$[0].highlightedInfo").value(workshop.getHighlightedInfo()))
                .andExpect(jsonPath("$[0].description").value(workshop.getDescription()))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(workshop.getAmountOfParticipants()))
                .andExpect(jsonPath("$[0].workshopCategory1").value(workshop.getWorkshopCategory1()))
                .andExpect(jsonPath("$[0].workshopCategory2").value(workshop.getWorkshopCategory2()))
                .andExpect(jsonPath("$[0].workshopVerified").value(workshop.getWorkshopVerified()))
                .andExpect(jsonPath("$[0].feedbackAdmin").value(workshop.getFeedbackAdmin()))
                .andExpect(jsonPath("$[0].publishWorkshop").value(workshop.getPublishWorkshop()))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value(workshop.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").doesNotExist())
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value((workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop())))
                .andExpect(jsonPath("$[0].workshopPicUrl").value(workshop.getWorkshopPicUrl()))
                .andExpect(jsonPath("$[1].id").value(workshop2.getId()))
                .andExpect(jsonPath("$[1].title").value(workshop2.getTitle()))
                .andExpect(jsonPath("$[1].date").value(workshop2.getDate().toString()))
                .andExpect(jsonPath("$[1].startTime").value(workshop2.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[1].endTime").value(workshop2.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[1].price").value(Double.toString(workshop2.getPrice())))
                .andExpect(jsonPath("$[1].inOrOutdoors").value(workshop2.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[1].location").value(workshop2.getLocation()))
                .andExpect(jsonPath("$[1].highlightedInfo").value(workshop2.getHighlightedInfo()))
                .andExpect(jsonPath("$[1].description").value(workshop2.getDescription()))
                .andExpect(jsonPath("$[1].amountOfParticipants").value(workshop2.getAmountOfParticipants()))
                .andExpect(jsonPath("$[1].workshopCategory1").value(workshop2.getWorkshopCategory1()))
                .andExpect(jsonPath("$[1].workshopCategory2").value(workshop2.getWorkshopCategory2()))
                .andExpect(jsonPath("$[1].workshopVerified").value(workshop2.getWorkshopVerified()))
                .andExpect(jsonPath("$[1].feedbackAdmin").value(workshop2.getFeedbackAdmin()))
                .andExpect(jsonPath("$[1].publishWorkshop").value(workshop2.getPublishWorkshop()))
                .andExpect(jsonPath("$[1].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[1].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[1].workshopOwnerCompanyName").value(workshop2.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[1].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[1].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[1].isFavourite").doesNotExist())
                .andExpect(jsonPath("$[1].amountOfFavsAndBookings").value((workshop2.calculateAmountOfBookingsWorkshop() + workshop2.calculateAmountOfFavouritesWorkshop())))
                .andExpect(jsonPath("$[1].workshopPicUrl").value(workshop2.getWorkshopPicUrl()));

    }

    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithUser() throws Exception {
        when(authentication.getName()).thenReturn(customer1.getEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        mockMvc.perform(get("/workshops?userId={userId}", customer1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(workshop.getId()))
                .andExpect(jsonPath("$[0].title").value(workshop.getTitle()))
                .andExpect(jsonPath("$[0].date").value(workshop.getDate().toString()))
                .andExpect(jsonPath("$[0].startTime").value(workshop.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(workshop.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].price").value(Double.toString(workshop.getPrice())))
                .andExpect(jsonPath("$[0].inOrOutdoors").value(workshop.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[0].location").value(workshop.getLocation()))
                .andExpect(jsonPath("$[0].highlightedInfo").value(workshop.getHighlightedInfo()))
                .andExpect(jsonPath("$[0].description").value(workshop.getDescription()))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(workshop.getAmountOfParticipants()))
                .andExpect(jsonPath("$[0].workshopCategory1").value(workshop.getWorkshopCategory1()))
                .andExpect(jsonPath("$[0].workshopCategory2").value(workshop.getWorkshopCategory2()))
                .andExpect(jsonPath("$[0].workshopVerified").value(workshop.getWorkshopVerified()))
                .andExpect(jsonPath("$[0].feedbackAdmin").value(workshop.getFeedbackAdmin()))
                .andExpect(jsonPath("$[0].publishWorkshop").value(workshop.getPublishWorkshop()))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value(workshop.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").value(customer1.getFavouriteWorkshops().contains(workshop)))
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value((workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop())))
                .andExpect(jsonPath("$[0].workshopPicUrl").value(workshop.getWorkshopPicUrl()))
                .andExpect(jsonPath("$[0].id").value(workshop.getId()))
                .andExpect(jsonPath("$[0].title").value(workshop.getTitle()))
                .andExpect(jsonPath("$[0].date").value(workshop.getDate().toString()))
                .andExpect(jsonPath("$[0].startTime").value(workshop.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(workshop.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].price").value(Double.toString(workshop.getPrice())))
                .andExpect(jsonPath("$[0].inOrOutdoors").value(workshop.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[0].location").value(workshop.getLocation()))
                .andExpect(jsonPath("$[0].highlightedInfo").value(workshop.getHighlightedInfo()))
                .andExpect(jsonPath("$[0].description").value(workshop.getDescription()))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(workshop.getAmountOfParticipants()))
                .andExpect(jsonPath("$[0].workshopCategory1").value(workshop.getWorkshopCategory1()))
                .andExpect(jsonPath("$[0].workshopCategory2").value(workshop.getWorkshopCategory2()))
                .andExpect(jsonPath("$[0].workshopVerified").value(workshop.getWorkshopVerified()))
                .andExpect(jsonPath("$[0].feedbackAdmin").value(workshop.getFeedbackAdmin()))
                .andExpect(jsonPath("$[0].publishWorkshop").value(workshop.getPublishWorkshop()))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value(workshop.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").value(customer1.getFavouriteWorkshops().contains(workshop)))
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value((workshop.calculateAmountOfBookingsWorkshop() + workshop.calculateAmountOfFavouritesWorkshop())))
                .andExpect(jsonPath("$[0].workshopPicUrl").value(workshop.getWorkshopPicUrl()))
                .andExpect(jsonPath("$[1].id").value(workshop2.getId()))
                .andExpect(jsonPath("$[1].title").value(workshop2.getTitle()))
                .andExpect(jsonPath("$[1].date").value(workshop2.getDate().toString()))
                .andExpect(jsonPath("$[1].startTime").value(workshop2.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[1].endTime").value(workshop2.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[1].price").value(Double.toString(workshop2.getPrice())))
                .andExpect(jsonPath("$[1].inOrOutdoors").value(workshop2.getInOrOutdoors().toString()))
                .andExpect(jsonPath("$[1].location").value(workshop2.getLocation()))
                .andExpect(jsonPath("$[1].highlightedInfo").value(workshop2.getHighlightedInfo()))
                .andExpect(jsonPath("$[1].description").value(workshop2.getDescription()))
                .andExpect(jsonPath("$[1].amountOfParticipants").value(workshop2.getAmountOfParticipants()))
                .andExpect(jsonPath("$[1].workshopCategory1").value(workshop2.getWorkshopCategory1()))
                .andExpect(jsonPath("$[1].workshopCategory2").value(workshop2.getWorkshopCategory2()))
                .andExpect(jsonPath("$[1].workshopVerified").value(workshop2.getWorkshopVerified()))
                .andExpect(jsonPath("$[1].feedbackAdmin").value(workshop2.getFeedbackAdmin()))
                .andExpect(jsonPath("$[1].publishWorkshop").value(workshop2.getPublishWorkshop()))
                .andExpect(jsonPath("$[1].workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$[1].workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$[1].workshopOwnerCompanyName").value(workshop2.getWorkshopOwner().getCompanyName()))
                .andExpect(jsonPath("$[1].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[1].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[1].isFavourite").value(customer1.getFavouriteWorkshops().contains(workshop2)))
                .andExpect(jsonPath("$[1].amountOfFavsAndBookings").value(workshop2
                        .calculateAmountOfFavouritesWorkshop()))
                .andExpect(jsonPath("$[1].workshopPicUrl").value(workshop2.getWorkshopPicUrl()));

    }


    @Test
    void addOrRemoveWorkshopFavourites() throws Exception {
        when(authentication.getName()).thenReturn(customer1.getEmail());

        MvcResult result = mockMvc.perform(put("/workshops/favourite/{userId}/{workshopId}?favourite=true", customer1.getId(), workshop2.getId()))
                .andExpect(status().isAccepted())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        List<WorkshopOutputDto> actualOutput = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        List<WorkshopOutputDto> expectedOutput = new ArrayList<>(List.of(workshopOutputDto, workshopOutputDto2));

        assertEquals(expectedOutput.size(), actualOutput.size());

       WorkshopOutputDto sutWorkshopOutputDto = actualOutput.stream()
                .filter(item -> item.title.equals(workshopOutputDto.title))
                .findFirst().get();

        WorkshopOutputDto sutWorkshopOutputDto2 = actualOutput.stream()
                .filter(item -> item.title.equals(workshopOutputDto2.title))
                .findFirst().get();


        // TODO: 14/08/2023   // test de required fields alleen - want in de service test test je of de save en transfer goed gaat. test hier alleen de gehele lijn.
                    assertEquals(workshopOutputDto.date, sutWorkshopOutputDto.date);
                    assertEquals(workshopOutputDto.startTime, sutWorkshopOutputDto.startTime);
                    assertEquals(workshopOutputDto.endTime, sutWorkshopOutputDto.endTime);
                    assertEquals(workshopOutputDto.description, sutWorkshopOutputDto.description);
                    assertEquals(workshopOutputDto.price, sutWorkshopOutputDto.price);
                    assertEquals(workshopOutputDto.inOrOutdoors, sutWorkshopOutputDto.inOrOutdoors);
                    assertEquals(workshopOutputDto.location, sutWorkshopOutputDto.location);
                    assertEquals(workshopOutputDto.description, sutWorkshopOutputDto.description);
                    assertEquals(workshopOutputDto.amountOfParticipants, sutWorkshopOutputDto.amountOfParticipants);
                    assertEquals(workshopOutputDto.workshopCategory1, sutWorkshopOutputDto.workshopCategory1);


                    assertEquals(workshopOutputDto2.date, sutWorkshopOutputDto2.date);
                    assertEquals(workshopOutputDto2.startTime, sutWorkshopOutputDto2.startTime);
                    assertEquals(workshopOutputDto2.endTime, sutWorkshopOutputDto2.endTime);
                    assertEquals(workshopOutputDto2.description, sutWorkshopOutputDto2.description);
                    assertEquals(workshopOutputDto2.price, sutWorkshopOutputDto2.price);
                    assertEquals(workshopOutputDto2.inOrOutdoors, sutWorkshopOutputDto2.inOrOutdoors);
                    assertEquals(workshopOutputDto2.location, sutWorkshopOutputDto2.location);
                    assertEquals(workshopOutputDto2.description, sutWorkshopOutputDto2.description);
                    assertEquals(workshopOutputDto2.amountOfParticipants, sutWorkshopOutputDto2.amountOfParticipants);
                    assertEquals(workshopOutputDto2.workshopCategory1, sutWorkshopOutputDto2.workshopCategory1);

                }


    @Test
    @Transactional
    void createWorkshopWithoutFileAndWithInputDto() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());

        MockMultipartFile workshopInputDto = new MockMultipartFile(
                "workshopInputDto",
                "workshopInputDto.json",
                MediaType.APPLICATION_JSON_VALUE,
                asJsonString(workshopInputDto1).getBytes()
        );

        mockMvc.perform(multipart("/workshops/workshopowner/{workshopOwnerId}", workshopOwner1.getId())
                        .file(workshopInputDto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.title").value(workshopInputDto1.title))
                .andExpect(jsonPath("$.date").value(workshopInputDto1.date.toString()))
                .andExpect(jsonPath("$.startTime").value(workshopInputDto1.startTime.format(formatter)))
                .andExpect(jsonPath("$.endTime").value(workshopInputDto1.endTime.format(formatter)))
                .andExpect(jsonPath("$.price").value(Double.toString(workshopInputDto1.price)))
                .andExpect(jsonPath("$.inOrOutdoors").value(workshopInputDto1.inOrOutdoors.toString()))
                .andExpect(jsonPath("$.location").value(workshopInputDto1.location))
                .andExpect(jsonPath("$.highlightedInfo").value(workshopInputDto1.highlightedInfo))
                .andExpect(jsonPath("$.description").value(workshopInputDto1.description))
                .andExpect(jsonPath("$.amountOfParticipants").value(workshopInputDto1.amountOfParticipants))
                .andExpect(jsonPath("$.workshopCategory1").value(workshopInputDto1.workshopCategory1))
                .andExpect(jsonPath("$.workshopCategory2").value(workshopInputDto1.workshopCategory2))
                .andExpect(jsonPath("$.workshopVerified").doesNotExist())
                .andExpect(jsonPath("$.feedbackAdmin").doesNotExist())
                .andExpect(jsonPath("$.publishWorkshop").doesNotExist())
                .andExpect(jsonPath("$.workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$.workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$.workshopOwnerCompanyName").value(workshopOwner1.getCompanyName()))
                .andExpect(jsonPath("$.averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$.numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$.isFavourite").doesNotExist())
                .andExpect(jsonPath("$.amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$.workshopPicUrl").doesNotExist());
    }

    @Test
    @Transactional
    void createWorkshopWithFileAndWithInputDto() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        MockMultipartFile workshopInputDto = new MockMultipartFile(
                "workshopInputDto",
                "workshopInputDto.json",
                MediaType.APPLICATION_JSON_VALUE,
                asJsonString(workshopInputDto1).getBytes()
        );

        mockMvc.perform(multipart("/workshops/workshopowner/{workshopOwnerId}", workshopOwner1.getId())
                        .file(workshopInputDto)
                        .file(file)
                        .contentType("multipart/form-data"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.title").value(workshopInputDto1.title))
                .andExpect(jsonPath("$.date").value(workshopInputDto1.date.toString()))
                .andExpect(jsonPath("$.startTime").value(workshopInputDto1.startTime.format(formatter)))
                .andExpect(jsonPath("$.endTime").value(workshopInputDto1.endTime.format(formatter)))
                .andExpect(jsonPath("$.price").value(Double.toString(workshopInputDto1.price)))
                .andExpect(jsonPath("$.inOrOutdoors").value(workshopInputDto1.inOrOutdoors.toString()))
                .andExpect(jsonPath("$.location").value(workshopInputDto1.location))
                .andExpect(jsonPath("$.highlightedInfo").value(workshopInputDto1.highlightedInfo))
                .andExpect(jsonPath("$.description").value(workshopInputDto1.description))
                .andExpect(jsonPath("$.amountOfParticipants").value(workshopInputDto1.amountOfParticipants))
                .andExpect(jsonPath("$.workshopCategory1").value(workshopInputDto1.workshopCategory1))
                .andExpect(jsonPath("$.workshopCategory2").value(workshopInputDto1.workshopCategory2))
                .andExpect(jsonPath("$.workshopVerified").doesNotExist())
                .andExpect(jsonPath("$.feedbackAdmin").doesNotExist())
                .andExpect(jsonPath("$.publishWorkshop").doesNotExist())
                .andExpect(jsonPath("$.workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$.workshopOwnerId").value(workshopOwner1.getId()))
                .andExpect(jsonPath("$.workshopOwnerCompanyName").value(workshopOwner1.getCompanyName()))
                .andExpect(jsonPath("$.averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$.numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$.isFavourite").doesNotExist())
                .andExpect(jsonPath("$.amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$.workshopPicUrl").value(notNullValue()));
    }


    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper =
                    new ObjectMapper().registerModule(new JavaTimeModule())
                            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}