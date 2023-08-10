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
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @MockBean
    private WorkshopService workshopService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FileService fileService;

    Workshop workshop;
    WorkshopOutputDto workshopOutputDto;
    WorkshopOutputDto workshopOutputDtoFromInputDto1;
    WorkshopInputDto workshopInputDto1;
    User workshopOwner1;
    User customer1;



    @BeforeEach
    void setUp() {
        workshopOwner1 = new User();
        workshopOwner1.setId(1L);
        workshopOwner1.setCompanyName("Test bedrijf");
        workshopOwner1.setWorkshopOwnerVerified(true);
        workshopOwner1.setWorkshopOwner(true);
        workshopOwner1.setEmail("test@example.com");

        userRepository.save(workshopOwner1);

        customer1 = new User();
        customer1.setId(2L);

        workshop = new Workshop(10L, "Kaarsen maken test", LocalDate.of(2023, 9, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 45D, OUTDOORS, "Amsterdam", "Neem je regenjas mee", "Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee.", 10, "Koken", "Handwerk", true, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        List<Workshop> customerFavouriteWorkshops = new ArrayList<>(List.of(workshop));
        customer1.setWorkshops(customerFavouriteWorkshops);

        workshop.setWorkshopOwner(workshopOwner1);

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

        workshopOutputDto.workshopOwnerId = workshop.getWorkshopOwner().getId();
        workshopOutputDto.workshopOwnerCompanyName = workshop.getWorkshopOwner().getCompanyName();
        workshopOutputDto.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto.numberOfReviews = null;
        workshopOutputDto.amountOfFavsAndBookings = 0;
        workshopOutputDto.workshopPicUrl = workshop.getWorkshopPicUrl();


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

    }

    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithoutUser() throws Exception {
        workshopOutputDto.isFavourite = false;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(null)).willReturn(List.of(workshopOutputDto));

        mockMvc.perform(get("/workshops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
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
        workshopOutputDto.isFavourite = true;
        workshopOutputDto.amountOfFavsAndBookings = 1;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(customer1.getId())).willReturn(List.of(workshopOutputDto));

        mockMvc.perform(get("/workshops?userId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
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
        workshopOutputDto.isFavourite = false;
        workshopOutputDto.amountOfFavsAndBookings = 0;
        given(workshopService.addOrRemoveWorkshopFavourites(customer1.getId(), workshop.getId(), false)).willReturn(List.of(workshopOutputDto));

        mockMvc.perform(put("/workshops/favourite/2/10?favourite=false"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").value(10L))
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