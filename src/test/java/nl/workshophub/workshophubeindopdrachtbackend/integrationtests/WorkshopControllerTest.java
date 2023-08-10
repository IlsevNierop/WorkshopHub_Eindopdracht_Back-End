package nl.workshophub.workshophubeindopdrachtbackend.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import nl.workshophub.workshophubeindopdrachtbackend.controllers.WorkshopController;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.INDOORS;
import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.OUTDOORS;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class WorkshopControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private WorkshopController workshopController;

    @MockBean
    private WorkshopService workshopService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private WorkshopRepository workshopRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FileService fileService;

    Workshop workshop5;
    Workshop workshop2;
    Workshop workshop3;
    Workshop workshop4;

    WorkshopOutputDto workshopOutputDto5;
    WorkshopOutputDto workshopOutputDtoFromInputDto1;

    WorkshopInputDto workshopInputDto1;
    User workshopOwner1;
    User customer1;

//    Authentication authentication;


    @BeforeEach
    void setUp() {
        workshopOwner1 = new User();
        workshopOwner1.setId(1L);
        workshopOwner1.setCompanyName("Test bedrijf");
        workshopOwner1.setWorkshopOwnerVerified(true);
        workshopOwner1.setWorkshopOwner(true);
        workshopOwner1.setEmail("test@example.com");

        Set<Authority> authoritiesWorkshopOwner1 = new HashSet<>();
        authoritiesWorkshopOwner1.add(new Authority(1L, "ROLE_WORKSHOPOWNER"));
        workshopOwner1.setAuthorities(authoritiesWorkshopOwner1);

        userRepository.save(workshopOwner1);

        customer1 = new User();
        customer1.setId(2L);

        workshop5 = new Workshop(5L, "Kaarsen maken test", LocalDate.of(2023, 9, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 45D, OUTDOORS, "Amsterdam", "Neem je regenjas mee", "Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee.", 10, "Koken", "Handwerk", true, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop2 = new Workshop(2L, "Kaarsen maken test2", LocalDate.of(2023, 5, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 20D, INDOORS, "Leiden", "We blijven binnen", "Een hele leuke workshop, nummer 2, met kaarsen maken binnen.", 10, "Creatief", "Handwerk", true, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop3 = new Workshop(3L, "Kaarsen maken test3", LocalDate.of(2023, 10, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 20D, INDOORS, "Leiden", "We blijven binnen", "Een hele leuke workshop, nummer 3, met kaarsen maken binnen.", 10, "Creatief", "Handwerk", null, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop4 = new Workshop(4L, "Kaarsen maken test4", LocalDate.of(2023, 11, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 25D, OUTDOORS, "Woerden", "We gaan naar buiten.", "Een hele leuke workshop, nummer 4, met kaarsen maken binnen.", 10, "Kaarsen", "Handwerk", true, "mag online", false, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop5, workshop2, workshop3, workshop4));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        List<Workshop> customerFavouriteWorkshops = new ArrayList<>(List.of(workshop5));
        customer1.setWorkshops(customerFavouriteWorkshops);

        workshop5.setWorkshopOwner(workshopOwner1);
        workshop2.setWorkshopOwner(workshopOwner1);
        workshop3.setWorkshopOwner(workshopOwner1);
        workshop4.setWorkshopOwner(workshopOwner1);

        workshopOutputDto5 = new WorkshopOutputDto();
        workshopOutputDto5.id = workshop5.getId();
        workshopOutputDto5.title = workshop5.getTitle();
        workshopOutputDto5.date = workshop5.getDate();
        workshopOutputDto5.startTime = workshop5.getStartTime();
        workshopOutputDto5.endTime = workshop5.getEndTime();
        workshopOutputDto5.price = workshop5.getPrice();
        workshopOutputDto5.inOrOutdoors = workshop5.getInOrOutdoors();
        workshopOutputDto5.location = workshop5.getLocation();
        workshopOutputDto5.highlightedInfo = workshop5.getHighlightedInfo();
        workshopOutputDto5.description = workshop5.getDescription();
        workshopOutputDto5.amountOfParticipants = workshop5.getAmountOfParticipants();
        workshopOutputDto5.spotsAvailable = workshop5.getAmountOfParticipants();
        workshopOutputDto5.workshopCategory1 = workshop5.getWorkshopCategory1();
        workshopOutputDto5.workshopCategory2 = workshop5.getWorkshopCategory2();
        workshopOutputDto5.workshopVerified = workshop5.getWorkshopVerified();
        workshopOutputDto5.feedbackAdmin = workshop5.getFeedbackAdmin();
        workshopOutputDto5.publishWorkshop = workshop5.getPublishWorkshop();
        workshopOutputDto5.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto5.workshopOwnerId = workshop5.getWorkshopOwner().getId();
        workshopOutputDto5.workshopOwnerCompanyName = workshop5.getWorkshopOwner().getCompanyName();
        workshopOutputDto5.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto5.numberOfReviews = null;
        workshopOutputDto5.amountOfFavsAndBookings = 0;
        workshopOutputDto5.workshopPicUrl = workshop5.getWorkshopPicUrl();


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

        workshopOutputDtoFromInputDto1 = new WorkshopOutputDto();
        workshopOutputDtoFromInputDto1.id = 1L;
        workshopOutputDtoFromInputDto1.title = workshopInputDto1.title;
        workshopOutputDtoFromInputDto1.date = workshopInputDto1.date;
        workshopOutputDtoFromInputDto1.startTime = workshopInputDto1.startTime;
        workshopOutputDtoFromInputDto1.endTime = workshopInputDto1.endTime;
        workshopOutputDtoFromInputDto1.price = workshopInputDto1.price;
        workshopOutputDtoFromInputDto1.inOrOutdoors = workshopInputDto1.inOrOutdoors;
        workshopOutputDtoFromInputDto1.location = workshopInputDto1.location;
        workshopOutputDtoFromInputDto1.highlightedInfo = workshopInputDto1.highlightedInfo;
        workshopOutputDtoFromInputDto1.description = workshopInputDto1.description;
        workshopOutputDtoFromInputDto1.amountOfParticipants = workshopInputDto1.amountOfParticipants;
        workshopOutputDtoFromInputDto1.spotsAvailable = workshopInputDto1.amountOfParticipants;
        workshopOutputDtoFromInputDto1.workshopCategory1 = workshopInputDto1.workshopCategory1;
        workshopOutputDtoFromInputDto1.workshopCategory2 = workshopInputDto1.workshopCategory2;
        workshopOutputDtoFromInputDto1.workshopVerified = workshopInputDto1.workshopVerified;
        workshopOutputDtoFromInputDto1.feedbackAdmin = workshopInputDto1.feedbackAdmin;
        workshopOutputDtoFromInputDto1.publishWorkshop = workshopInputDto1.publishWorkshop;
        workshopOutputDtoFromInputDto1.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDtoFromInputDto1.workshopOwnerId = workshopOwner1.getId();
        workshopOutputDtoFromInputDto1.workshopOwnerCompanyName = workshopOwner1.getCompanyName();
        workshopOutputDtoFromInputDto1.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDtoFromInputDto1.numberOfReviews = null;
        workshopOutputDtoFromInputDto1.amountOfFavsAndBookings = 0;

        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        workshopOutputDto3 = new WorkshopOutputDto();
//        workshopOutputDto3.id = workshop3.getId();
//        workshopOutputDto3.title = workshop3.getTitle();
//        workshopOutputDto3.date = workshop3.getDate();
//        workshopOutputDto3.startTime = workshop3.getStartTime();
//        workshopOutputDto3.endTime = workshop3.getEndTime();
//        workshopOutputDto3.price = workshop3.getPrice();
//        workshopOutputDto3.inOrOutdoors = workshop3.getInOrOutdoors();
//        workshopOutputDto3.location = workshop3.getLocation();
//        workshopOutputDto3.highlightedInfo = workshop3.getHighlightedInfo();
//        workshopOutputDto3.description = workshop3.getDescription();
//        workshopOutputDto3.amountOfParticipants = workshop3.getAmountOfParticipants();
//        workshopOutputDto3.spotsAvailable = workshop3.getAmountOfParticipants();
//        workshopOutputDto3.workshopCategory1 = workshop3.getWorkshopCategory1();
//        workshopOutputDto3.workshopCategory2 = workshop3.getWorkshopCategory2();
//        workshopOutputDto3.workshopVerified = workshop3.getWorkshopVerified();
//        workshopOutputDto3.feedbackAdmin = workshop3.getFeedbackAdmin();
//        workshopOutputDto3.publishWorkshop = workshop3.getPublishWorkshop();
//        workshopOutputDto3.workshopOwnerReviews = new ArrayList<>();
//
//        workshopOutputDto3.workshopOwnerId = workshop3.getWorkshopOwner().getId();
//        workshopOutputDto3.workshopOwnerCompanyName = workshop3.getWorkshopOwner().getCompanyName();
//        workshopOutputDto3.averageRatingWorkshopOwnerReviews = null;
//        workshopOutputDto3.numberOfReviews = null;
//        workshopOutputDto3.amountOfFavsAndBookings = 0;
//        workshopOutputDto3.workshopPicUrl = workshop3.getWorkshopPicUrl();
//
//
//        workshopOutputDto4 = new WorkshopOutputDto();
//        workshopOutputDto4.id = workshop4.getId();
//        workshopOutputDto4.title = workshop4.getTitle();
//        workshopOutputDto4.date = workshop4.getDate();
//        workshopOutputDto4.startTime = workshop4.getStartTime();
//        workshopOutputDto4.endTime = workshop4.getEndTime();
//        workshopOutputDto4.price = workshop4.getPrice();
//        workshopOutputDto4.inOrOutdoors = workshop4.getInOrOutdoors();
//        workshopOutputDto4.location = workshop4.getLocation();
//        workshopOutputDto4.highlightedInfo = workshop4.getHighlightedInfo();
//        workshopOutputDto4.description = workshop4.getDescription();
//        workshopOutputDto4.amountOfParticipants = workshop4.getAmountOfParticipants();
//        workshopOutputDto4.spotsAvailable = workshop4.getAmountOfParticipants();
//        workshopOutputDto4.workshopCategory1 = workshop4.getWorkshopCategory1();
//        workshopOutputDto4.workshopCategory2 = workshop4.getWorkshopCategory2();
//        workshopOutputDto4.workshopVerified = workshop4.getWorkshopVerified();
//        workshopOutputDto4.feedbackAdmin = workshop4.getFeedbackAdmin();
//        workshopOutputDto4.publishWorkshop = workshop4.getPublishWorkshop();
//        workshopOutputDto4.workshopOwnerReviews = new ArrayList<>();
//
//        workshopOutputDto4.workshopOwnerId = workshop4.getWorkshopOwner().getId();
//        workshopOutputDto4.workshopOwnerCompanyName = workshop4.getWorkshopOwner().getCompanyName();
//        workshopOutputDto4.averageRatingWorkshopOwnerReviews = null;
//        workshopOutputDto4.numberOfReviews = null;
//        workshopOutputDto4.amountOfFavsAndBookings = 0;
//        workshopOutputDto4.workshopPicUrl = workshop4.getWorkshopPicUrl();

    }

    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithoutUser() throws Exception {
        workshopOutputDto5.isFavourite = false;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(null)).willReturn(List.of(workshopOutputDto5));

        mockMvc.perform(get("/workshops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L))
                .andExpect(jsonPath("$[0].title").value("Kaarsen maken test"))
                .andExpect(jsonPath("$[0].date").value("2023-09-25"))
                .andExpect(jsonPath("$[0].startTime").value("16:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("18:00:00"))
                .andExpect(jsonPath("$[0].price").value("45.0"))
                .andExpect(jsonPath("$[0].inOrOutdoors").value("OUTDOORS"))
                .andExpect(jsonPath("$[0].location").value("Amsterdam"))
                .andExpect(jsonPath("$[0].highlightedInfo").value("Neem je regenjas mee"))
                .andExpect(jsonPath("$[0].description").value("Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee."))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(10))
                .andExpect(jsonPath("$[0].workshopCategory1").value("Koken"))
                .andExpect(jsonPath("$[0].workshopCategory2").value("Handwerk"))
                .andExpect(jsonPath("$[0].workshopVerified").value(true))
                .andExpect(jsonPath("$[0].feedbackAdmin").value("mag online"))
                .andExpect(jsonPath("$[0].publishWorkshop").value(true))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(1L))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value("Test bedrijf"))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").value(false))

                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$[0].workshopPicUrl").value("testurl"));
    }

    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithUser() throws Exception {
        workshopOutputDto5.isFavourite = true;
        workshopOutputDto5.amountOfFavsAndBookings = 1;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(customer1.getId())).willReturn(List.of(workshopOutputDto5));

        mockMvc.perform(get("/workshops?userId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L))
                .andExpect(jsonPath("$[0].title").value("Kaarsen maken test"))
                .andExpect(jsonPath("$[0].date").value("2023-09-25"))
                .andExpect(jsonPath("$[0].startTime").value("16:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("18:00:00"))
                .andExpect(jsonPath("$[0].price").value("45.0"))
                .andExpect(jsonPath("$[0].inOrOutdoors").value("OUTDOORS"))
                .andExpect(jsonPath("$[0].location").value("Amsterdam"))
                .andExpect(jsonPath("$[0].highlightedInfo").value("Neem je regenjas mee"))
                .andExpect(jsonPath("$[0].description").value("Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee."))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(10))
                .andExpect(jsonPath("$[0].workshopCategory1").value("Koken"))
                .andExpect(jsonPath("$[0].workshopCategory2").value("Handwerk"))
                .andExpect(jsonPath("$[0].workshopVerified").value(true))
                .andExpect(jsonPath("$[0].feedbackAdmin").value("mag online"))
                .andExpect(jsonPath("$[0].publishWorkshop").value(true))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(1L))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value("Test bedrijf"))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").value(true))
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value(1))
                .andExpect(jsonPath("$[0].workshopPicUrl").value("testurl"));
    }

    @Test
    void addOrRemoveWorkshopFavourites() throws Exception {
        workshopOutputDto5.isFavourite = false;
        workshopOutputDto5.amountOfFavsAndBookings = 0;
        given(workshopService.addOrRemoveWorkshopFavourites(customer1.getId(), workshop5.getId(), false)).willReturn(List.of(workshopOutputDto5));

        mockMvc.perform(put("/workshops/favourite/2/5?favourite=false"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").value(5L))
                .andExpect(jsonPath("$[0].title").value("Kaarsen maken test"))
                .andExpect(jsonPath("$[0].date").value("2023-09-25"))
                .andExpect(jsonPath("$[0].startTime").value("16:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("18:00:00"))
                .andExpect(jsonPath("$[0].price").value("45.0"))
                .andExpect(jsonPath("$[0].inOrOutdoors").value("OUTDOORS"))
                .andExpect(jsonPath("$[0].location").value("Amsterdam"))
                .andExpect(jsonPath("$[0].highlightedInfo").value("Neem je regenjas mee"))
                .andExpect(jsonPath("$[0].description").value("Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee."))
                .andExpect(jsonPath("$[0].amountOfParticipants").value(10))
                .andExpect(jsonPath("$[0].workshopCategory1").value("Koken"))
                .andExpect(jsonPath("$[0].workshopCategory2").value("Handwerk"))
                .andExpect(jsonPath("$[0].workshopVerified").value(true))
                .andExpect(jsonPath("$[0].feedbackAdmin").value("mag online"))
                .andExpect(jsonPath("$[0].publishWorkshop").value(true))
                .andExpect(jsonPath("$[0].workshopOwnerReviews").value(new ArrayList()))
                .andExpect(jsonPath("$[0].workshopOwnerId").value(1L))
                .andExpect(jsonPath("$[0].workshopOwnerCompanyName").value("Test bedrijf"))
                .andExpect(jsonPath("$[0].averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$[0].numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$[0].isFavourite").value(false))
                .andExpect(jsonPath("$[0].amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$[0].workshopPicUrl").value("testurl"));
    }

    @Test
    @Transactional
    void createWorkshopWithoutFileAndWithInputDto() throws Exception {

        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());
        System.out.println(workshopOutputDtoFromInputDto1.id);

        when(workshopService.createWorkshop(eq(workshopOwner1.getId()), eq(workshopInputDto1)))
                .thenReturn(workshopOutputDtoFromInputDto1);

        given(workshopService.createWorkshop(
                eq(workshopOwner1.getId()),
                argThat(dto -> workshopOutputDtoFromInputDto1.title.equals(workshopInputDto1.title))))
                .willReturn(workshopOutputDtoFromInputDto1);

        MockMultipartFile workshopInputDto = new MockMultipartFile(
                "workshopInputDto",
                "workshopInputDto.json",
                MediaType.APPLICATION_JSON_VALUE,
                asJsonString(workshopInputDto1).getBytes()
        );

        mockMvc.perform(multipart("/workshops/workshopowner/{workshopOwnerId}", workshopOwner1.getId())
                        .file(workshopInputDto))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern("http://localhost/workshops/workshopowner/1/1")))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Taart bakken"))
                .andExpect(jsonPath("$.date").value("2023-10-05"))
                .andExpect(jsonPath("$.startTime").value("16:00:00"))
                .andExpect(jsonPath("$.endTime").value("19:00:00"))
                .andExpect(jsonPath("$.price").value("99.0"))
                .andExpect(jsonPath("$.inOrOutdoors").value("INDOORS"))
                .andExpect(jsonPath("$.location").value("Utrecht"))
                .andExpect(jsonPath("$.highlightedInfo").value("Neem je lekkerste specerijen mee"))
                .andExpect(jsonPath("$.description").value("Een workshop taart bakken in Utrecht. Neem al je vrienden mee en bak de lekkerste taart die je ooit gemaakt hebt. Je mag de taart mee naar huis nemen. En als je je favoriete specerijen meeneemt, kunnen we een improvisatie taart maken."))
                .andExpect(jsonPath("$.amountOfParticipants").value(8))
                .andExpect(jsonPath("$.workshopCategory1").value("Bakken"))
                .andExpect(jsonPath("$.workshopCategory2").value("Koken"))
                .andExpect(jsonPath("$.workshopVerified").doesNotExist())
                .andExpect(jsonPath("$.feedbackAdmin").doesNotExist())
                .andExpect(jsonPath("$.publishWorkshop").doesNotExist())
                .andExpect(jsonPath("$.workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$.workshopOwnerId").value(1L))
                .andExpect(jsonPath("$.workshopOwnerCompanyName").value("Test bedrijf"))
                .andExpect(jsonPath("$.averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$.numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$.isFavourite").doesNotExist())
                .andExpect(jsonPath("$.amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$.workshopPicUrl").doesNotExist());
    }

    @Test
    @Transactional
    void createWorkshopWithFileAndWithInputDto() throws Exception {

        workshopOutputDtoFromInputDto1.workshopPicUrl = "http://localhost8080/downloadworkshoppic/1";

        when(authentication.getName()).thenReturn(workshopOwner1.getEmail());
        System.out.println(workshopOutputDtoFromInputDto1.id);

        when(workshopService.createWorkshop(eq(workshopOwner1.getId()), eq(workshopInputDto1)))
                .thenReturn(workshopOutputDtoFromInputDto1);

        when(fileService.uploadWorkshopPic(any(MultipartFile.class), anyString(), anyLong()))
                .thenReturn("mockedFileName");

        given(workshopService.createWorkshop(
                eq(workshopOwner1.getId()),
                argThat(dto -> workshopOutputDtoFromInputDto1.title.equals(workshopInputDto1.title))))
                .willReturn(workshopOutputDtoFromInputDto1);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        MockMultipartFile workshopInputDto = new MockMultipartFile(
                "workshopInputDto", // Part name
                "workshopInputDto.json", // Original filename
                MediaType.APPLICATION_JSON_VALUE, // Content type
                asJsonString(workshopInputDto1).getBytes() // JSON data as bytes
        );

        mockMvc.perform(multipart("/workshops/workshopowner/{workshopOwnerId}", workshopOwner1.getId())
                        .file(workshopInputDto)
                        .file(file)
                        .contentType("multipart/form-data"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Taart bakken"))
                .andExpect(jsonPath("$.date").value("2023-10-05"))
                .andExpect(jsonPath("$.startTime").value("16:00:00"))
                .andExpect(jsonPath("$.endTime").value("19:00:00"))
                .andExpect(jsonPath("$.price").value("99.0"))
                .andExpect(jsonPath("$.inOrOutdoors").value("INDOORS"))
                .andExpect(jsonPath("$.location").value("Utrecht"))
                .andExpect(jsonPath("$.highlightedInfo").value("Neem je lekkerste specerijen mee"))
                .andExpect(jsonPath("$.description").value("Een workshop taart bakken in Utrecht. Neem al je vrienden mee en bak de lekkerste taart die je ooit gemaakt hebt. Je mag de taart mee naar huis nemen. En als je je favoriete specerijen meeneemt, kunnen we een improvisatie taart maken."))
                .andExpect(jsonPath("$.amountOfParticipants").value(8))
                .andExpect(jsonPath("$.workshopCategory1").value("Bakken"))
                .andExpect(jsonPath("$.workshopCategory2").value("Koken"))
                .andExpect(jsonPath("$.workshopVerified").doesNotExist())
                .andExpect(jsonPath("$.feedbackAdmin").doesNotExist())
                .andExpect(jsonPath("$.publishWorkshop").doesNotExist())
                .andExpect(jsonPath("$.workshopOwnerReviews").value(new ArrayList<>()))
                .andExpect(jsonPath("$.workshopOwnerId").value(1L))
                .andExpect(jsonPath("$.workshopOwnerCompanyName").value("Test bedrijf"))
                .andExpect(jsonPath("$.averageRatingWorkshopOwnerReviews").doesNotExist())
                .andExpect(jsonPath("$.numberOfReviews").doesNotExist())
                .andExpect(jsonPath("$.isFavourite").doesNotExist())
                .andExpect(jsonPath("$.amountOfFavsAndBookings").value(0))
                .andExpect(jsonPath("$.workshopPicUrl").value("http://localhost8080/downloadworkshoppic/1"));
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