package nl.workshophub.workshophubeindopdrachtbackend.integrationtests;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.services.FileService;
import nl.workshophub.workshophubeindopdrachtbackend.services.WorkshopService;
import org.hamcrest.collection.ArrayAsIterableMatcher;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.INDOORS;
import static nl.workshophub.workshophubeindopdrachtbackend.models.InOrOutdoors.OUTDOORS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@RunWith(SpringRunner.class)
//@WebMvcTest(WorkshopController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("test")

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class WorkshopControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private WorkshopService workshopService;

    @MockBean
    private FileService fileService;

    Workshop workshop1;
    Workshop workshop2;
    Workshop workshop3;
    Workshop workshop4;

    WorkshopOutputDto workshopOutputDto1;
    WorkshopOutputDto workshopOutputDto2;
    WorkshopOutputDto workshopOutputDto3;
    WorkshopOutputDto workshopOutputDto4;


    User workshopOwner1;
    User customer1;

    @BeforeEach
    void setUp() {
        workshopOwner1 = new User();
        workshopOwner1.setId(1L);
        workshopOwner1.setCompanyName("Test bedrijf");

        customer1 = new User();
        customer1.setId(2L);

        workshop1 = new Workshop(1L, "Kaarsen maken test", LocalDate.of(2023, 9, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 45D, OUTDOORS, "Amsterdam", "Neem je regenjas mee", "Een hele leuke workshop, nummer 1, met kaarsen maken en een regenjas mee.", 10, "Koken", "Handwerk", true, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop2 = new Workshop(2L, "Kaarsen maken test2", LocalDate.of(2023, 5, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 20D, INDOORS, "Leiden", "We blijven binnen", "Een hele leuke workshop, nummer 2, met kaarsen maken binnen.", 10, "Creatief", "Handwerk", true, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop3 = new Workshop(3L, "Kaarsen maken test3", LocalDate.of(2023, 10, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 20D, INDOORS, "Leiden", "We blijven binnen", "Een hele leuke workshop, nummer 3, met kaarsen maken binnen.", 10, "Creatief", "Handwerk", null, "mag online", true, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        workshop4 = new Workshop(4L, "Kaarsen maken test4", LocalDate.of(2023, 11, 25), (LocalTime.of(16, 0)), (LocalTime.of(18, 0)), 25D, OUTDOORS, "Woerden", "We gaan naar buiten.", "Een hele leuke workshop, nummer 4, met kaarsen maken binnen.", 10, "Kaarsen", "Handwerk", true, "mag online", false, "testurl", "fotovanworkshop", workshopOwner1, null, null, null);

        List<Workshop> workshopOwner1Workshops = new ArrayList<>(List.of(workshop1, workshop2, workshop3, workshop4));
        workshopOwner1.setWorkshops(workshopOwner1Workshops);

        List<Workshop> customerFavouriteWorkshops = new ArrayList<>(List.of(workshop1));
        customer1.setWorkshops(customerFavouriteWorkshops);

        workshop1.setWorkshopOwner(workshopOwner1);
        workshop2.setWorkshopOwner(workshopOwner1);
        workshop3.setWorkshopOwner(workshopOwner1);
        workshop4.setWorkshopOwner(workshopOwner1);

        workshopOutputDto1 = new WorkshopOutputDto();
        workshopOutputDto1.id = workshop1.getId();
        workshopOutputDto1.title = workshop1.getTitle();
        workshopOutputDto1.date = workshop1.getDate();
        workshopOutputDto1.startTime = workshop1.getStartTime();
        workshopOutputDto1.endTime = workshop1.getEndTime();
        workshopOutputDto1.price = workshop1.getPrice();
        workshopOutputDto1.inOrOutdoors = workshop1.getInOrOutdoors();
        workshopOutputDto1.location = workshop1.getLocation();
        workshopOutputDto1.highlightedInfo = workshop1.getHighlightedInfo();
        workshopOutputDto1.description = workshop1.getDescription();
        workshopOutputDto1.amountOfParticipants = workshop1.getAmountOfParticipants();
        workshopOutputDto1.spotsAvailable = workshop1.getAmountOfParticipants();
        workshopOutputDto1.workshopCategory1 = workshop1.getWorkshopCategory1();
        workshopOutputDto1.workshopCategory2 = workshop1.getWorkshopCategory2();
        workshopOutputDto1.workshopVerified = workshop1.getWorkshopVerified();
        workshopOutputDto1.feedbackAdmin = workshop1.getFeedbackAdmin();
        workshopOutputDto1.publishWorkshop = workshop1.getPublishWorkshop();
        workshopOutputDto1.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto1.workshopOwnerId = workshop1.getWorkshopOwner().getId();
        workshopOutputDto1.workshopOwnerCompanyName = workshop1.getWorkshopOwner().getCompanyName();
        workshopOutputDto1.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto1.numberOfReviews = null;
        workshopOutputDto1.amountOfFavsAndBookings = 0;
        workshopOutputDto1.workshopPicUrl = workshop1.getWorkshopPicUrl();


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
        workshopOutputDto2.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto2.workshopOwnerId = workshop2.getWorkshopOwner().getId();
        workshopOutputDto2.workshopOwnerCompanyName = workshop2.getWorkshopOwner().getCompanyName();
        workshopOutputDto2.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto2.numberOfReviews = null;
        workshopOutputDto2.amountOfFavsAndBookings = 0;
        workshopOutputDto2.workshopPicUrl = workshop2.getWorkshopPicUrl();


        workshopOutputDto3 = new WorkshopOutputDto();
        workshopOutputDto3.id = workshop3.getId();
        workshopOutputDto3.title = workshop3.getTitle();
        workshopOutputDto3.date = workshop3.getDate();
        workshopOutputDto3.startTime = workshop3.getStartTime();
        workshopOutputDto3.endTime = workshop3.getEndTime();
        workshopOutputDto3.price = workshop3.getPrice();
        workshopOutputDto3.inOrOutdoors = workshop3.getInOrOutdoors();
        workshopOutputDto3.location = workshop3.getLocation();
        workshopOutputDto3.highlightedInfo = workshop3.getHighlightedInfo();
        workshopOutputDto3.description = workshop3.getDescription();
        workshopOutputDto3.amountOfParticipants = workshop3.getAmountOfParticipants();
        workshopOutputDto3.spotsAvailable = workshop3.getAmountOfParticipants();
        workshopOutputDto3.workshopCategory1 = workshop3.getWorkshopCategory1();
        workshopOutputDto3.workshopCategory2 = workshop3.getWorkshopCategory2();
        workshopOutputDto3.workshopVerified = workshop3.getWorkshopVerified();
        workshopOutputDto3.feedbackAdmin = workshop3.getFeedbackAdmin();
        workshopOutputDto3.publishWorkshop = workshop3.getPublishWorkshop();
        workshopOutputDto3.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto3.workshopOwnerId = workshop3.getWorkshopOwner().getId();
        workshopOutputDto3.workshopOwnerCompanyName = workshop3.getWorkshopOwner().getCompanyName();
        workshopOutputDto3.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto3.numberOfReviews = null;
        workshopOutputDto3.amountOfFavsAndBookings = 0;
        workshopOutputDto3.workshopPicUrl = workshop3.getWorkshopPicUrl();


        workshopOutputDto4 = new WorkshopOutputDto();
        workshopOutputDto4.id = workshop4.getId();
        workshopOutputDto4.title = workshop4.getTitle();
        workshopOutputDto4.date = workshop4.getDate();
        workshopOutputDto4.startTime = workshop4.getStartTime();
        workshopOutputDto4.endTime = workshop4.getEndTime();
        workshopOutputDto4.price = workshop4.getPrice();
        workshopOutputDto4.inOrOutdoors = workshop4.getInOrOutdoors();
        workshopOutputDto4.location = workshop4.getLocation();
        workshopOutputDto4.highlightedInfo = workshop4.getHighlightedInfo();
        workshopOutputDto4.description = workshop4.getDescription();
        workshopOutputDto4.amountOfParticipants = workshop4.getAmountOfParticipants();
        workshopOutputDto4.spotsAvailable = workshop4.getAmountOfParticipants();
        workshopOutputDto4.workshopCategory1 = workshop4.getWorkshopCategory1();
        workshopOutputDto4.workshopCategory2 = workshop4.getWorkshopCategory2();
        workshopOutputDto4.workshopVerified = workshop4.getWorkshopVerified();
        workshopOutputDto4.feedbackAdmin = workshop4.getFeedbackAdmin();
        workshopOutputDto4.publishWorkshop = workshop4.getPublishWorkshop();
        workshopOutputDto4.workshopOwnerReviews = new ArrayList<>();

        workshopOutputDto4.workshopOwnerId = workshop4.getWorkshopOwner().getId();
        workshopOutputDto4.workshopOwnerCompanyName = workshop4.getWorkshopOwner().getCompanyName();
        workshopOutputDto4.averageRatingWorkshopOwnerReviews = null;
        workshopOutputDto4.numberOfReviews = null;
        workshopOutputDto4.amountOfFavsAndBookings = 0;
        workshopOutputDto4.workshopPicUrl = workshop4.getWorkshopPicUrl();

    }

    //een test met user en een test zonder user
    @Test
    void getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDateWithoutUser() throws Exception {
        workshopOutputDto1.isFavourite = false;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(null)).willReturn(List.of(workshopOutputDto1));

        mockMvc.perform(get("/workshops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
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
        workshopOutputDto1.isFavourite = true;
        workshopOutputDto1.amountOfFavsAndBookings = 1;

        given(workshopService.getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate(customer1.getId())).willReturn(List.of(workshopOutputDto1));

        mockMvc.perform(get("/workshops?userId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
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
        workshopOutputDto1.isFavourite = false;
        workshopOutputDto1.amountOfFavsAndBookings = 0;
        given(workshopService.addOrRemoveWorkshopFavourites(customer1.getId(), workshop1.getId(), false)).willReturn(List.of(workshopOutputDto1));

        mockMvc.perform(put("/workshops/favourite/2/1?favourite=false"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").value(1L))
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
}