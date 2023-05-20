package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDtoDefault;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.hibernate.jdbc.Work;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.modelmbean.ModelMBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkshopService {

    ModelMapper modelMapper = new ModelMapper();

    private final WorkshopRepository workshopRepository;

    public WorkshopService(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    public List<WorkshopOutputDtoDefault> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        List<Workshop> workshops = workshopRepository.findByDateAfterOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDtoDefault> workshopOutputDtoDefaults = new ArrayList<>();
        for (Workshop w : workshops) {
            if (w.getWorkshopVerified() != null && w.getPublishWorkshop() != null && w.getWorkshopVerified() == true && w.getPublishWorkshop() == true) {
                WorkshopOutputDtoDefault workshopOutputDtoDefault = transferWorkshopToWorkshopOutputDefault(w);
//            get average rating user - connected to workshop - set average rating owner to workshopoutput
                workshopOutputDtoDefaults.add(workshopOutputDtoDefault);
            }

        }
        return workshopOutputDtoDefaults;
    }

    public List<WorkshopOutputDtoDefault> testQueryGetAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now());
        List<WorkshopOutputDtoDefault> workshopOutputDtoDefaults = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDtoDefault workshopOutputDtoDefault = transferWorkshopToWorkshopOutputDefault(w);
//            get average rating user - connected to workshop - set average rating owner to workshopoutput
            workshopOutputDtoDefaults.add(workshopOutputDtoDefault);
        }
        return workshopOutputDtoDefaults;
    }

    public WorkshopOutputDtoDefault getWorkshopById(Long id)  throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));
        return transferWorkshopToWorkshopOutputDefault(workshop);

    }

    public WorkshopOutputDtoDefault getAllWorkshopsByWorkshopOwner




    public WorkshopOutputDtoDefault transferWorkshopToWorkshopOutputDefault(Workshop workshop) {
        return modelMapper.map(workshop, WorkshopOutputDtoDefault.class);
//        WorkshopOutputDtoDefault test = new WorkshopOutputDtoDefault();
//        test.title = workshop.getTitle();
//        return test;

    }

    public Workshop transferWorkshopOutputDefaultToWorkshop(WorkshopOutputDtoDefault workshopOutputDtoDefault) {
        return modelMapper.map(workshopOutputDtoDefault, Workshop.class);

    }
}
