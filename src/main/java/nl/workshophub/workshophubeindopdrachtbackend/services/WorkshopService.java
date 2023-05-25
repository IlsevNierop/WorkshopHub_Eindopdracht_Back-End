package nl.workshophub.workshophubeindopdrachtbackend.services;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ValidationException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;


@Service
public class WorkshopService {

    ModelMapper modelMapper = new ModelMapper();

    private final WorkshopRepository workshopRepository;

    public WorkshopService(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    public List<WorkshopOutputDto> getAllWorkshopsVerifiedAndPublishFromCurrentDateOnwardsOrderByDate() throws RecordNotFoundException {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsTrueAndPublishWorkshopIsTrueOrderByDate(java.time.LocalDate.now());
        if (workshops.isEmpty()) {
            throw new RecordNotFoundException("Er zijn momenteel geen workshops beschikbaar");
        }
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
//            get average rating user - connected to workshop - set average rating owner to workshopoutput
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto getWorkshopById(Long id) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public List<WorkshopOutputDto> getAllWorkshopsToVerify() throws RecordNotFoundException {
        List<Workshop> workshops = workshopRepository.findByDateAfterAndWorkshopVerifiedIsNullOrderByDate(java.time.LocalDate.now());
        if (workshops.isEmpty()) {
            throw new RecordNotFoundException("Er zijn momenteel geen goed te keuren workshops");
        }
        List<WorkshopOutputDto> workshopOutputDtos = new ArrayList<>();
        for (Workshop w : workshops) {
            WorkshopOutputDto workshopOutputDto = transferWorkshopToWorkshopOutputDto(w);
//            get average rating user - connected to workshop - set average rating owner to workshopoutput
            workshopOutputDtos.add(workshopOutputDto);
        }
        return workshopOutputDtos;
    }

    public WorkshopOutputDto createWorkshop(WorkshopInputDto workshopInputDto) {
        Workshop workshop = transferWorkshopInputDtoToWorkshop(workshopInputDto);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    public WorkshopOutputDto updateWorkshopByOwner(Long id, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));
        workshop.setTitle(workshopInputDto.title);
        workshop.setDate(workshopInputDto.date);
        workshop.setStartTime(workshopInputDto.startTime);
        workshop.setEndTime(workshopInputDto.endTime);
        workshop.setPrice(workshopInputDto.price);
        //check default waarde!
        if (workshopInputDto.inOrOutdoors != null) {
            workshop.setInOrOutdoors(workshopInputDto.inOrOutdoors);
        }
        workshop.setLocation(workshopInputDto.location);
        if (workshopInputDto.highlightedInfo != null) {
            workshop.setHighlightedInfo(workshopInputDto.highlightedInfo);
        }
        workshop.setDescription(workshopInputDto.description);
        workshop.setAmountOfParticipants(workshopInputDto.amountOfParticipants);
        workshop.setWorkshopCategory1(workshopInputDto.workshopCategory1);
        if (workshopInputDto.workshopCategory2 != null) {
            workshop.setWorkshopCategory2(workshopInputDto.workshopCategory2);
        }
        // na het wijzigen van een workshop wordt de status automatisch op geverifieerd null en publish null gezet:
        workshop.setPublishWorkshop(null);
        workshop.setWorkshopVerified(null);
        // owner kan feedback niet wijzigen, dus die wordt ook niet met deze put gewijzigd.
        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    @PutMapping
    public WorkshopOutputDto verifyWorkshopByOwner(Long id, Boolean publishWorkshop) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));

        workshop.setPublishWorkshop(publishWorkshop);
        workshopRepository.save(workshop);
        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    //admin:

    public WorkshopOutputDto verifyWorkshopByAdmin(Long id, WorkshopInputDto workshopInputDto) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));
        workshop.setTitle(workshopInputDto.title);
        workshop.setDate(workshopInputDto.date);
        workshop.setStartTime(workshopInputDto.startTime);
        workshop.setEndTime(workshopInputDto.endTime);
        workshop.setPrice(workshopInputDto.price);
        //check default waarde!
        if (workshopInputDto.inOrOutdoors != null) {
            workshop.setInOrOutdoors(workshopInputDto.inOrOutdoors);
        }
        workshop.setLocation(workshopInputDto.location);
        if (workshopInputDto.highlightedInfo != null) {
            workshop.setHighlightedInfo(workshopInputDto.highlightedInfo);
        }
        workshop.setDescription(workshopInputDto.description);
        workshop.setAmountOfParticipants(workshopInputDto.amountOfParticipants);
        workshop.setWorkshopCategory1(workshopInputDto.workshopCategory1);
        if (workshopInputDto.workshopCategory2 != null) {
            workshop.setWorkshopCategory2(workshopInputDto.workshopCategory2);
        }
        if (workshopInputDto.workshopVerified != null) {
            workshop.setWorkshopVerified(workshopInputDto.workshopVerified);
        }
        if (workshopInputDto.feedbackAdmin != null) {
            workshop.setFeedbackAdmin(workshopInputDto.feedbackAdmin);
        }

        // na het wijzigen van een workshop wordt de status automatisch publish null gezet:
        workshop.setPublishWorkshop(null);

        workshopRepository.save(workshop);

        return transferWorkshopToWorkshopOutputDto(workshop);
    }

    //create exception for
    public void deleteWorkshop(Long id) throws ValidationException, RecordNotFoundException{
        Workshop workshop = workshopRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De workshop met ID " + id + " bestaat niet"));
        //admin kan niet verwijderen als owner op publish heeft gezet
        if (workshop.getPublishWorkshop() == true) {
            throw new ValidationException("Deze workshop kan niet verwijderd worden omdat deze door de eigenaar geaccordeerd is.");
        }
            //wat als er relaties zijn? boekingen kan niet verwijderen.
            workshopRepository.delete(workshop);

    }


    public WorkshopOutputDto transferWorkshopToWorkshopOutputDto(Workshop workshop) {
        return modelMapper.map(workshop, WorkshopOutputDto.class);

    }

    public Workshop transferWorkshopInputDtoToWorkshop(WorkshopInputDto workshopInputDto) {
        return modelMapper.map(workshopInputDto, Workshop.class);

    }

//    // als voorbeeld voor het uitsluiten van bepaalde properties als bepaalde properties waarde null hebben. helemaal onderaan het skippen van properties.
//    public Workshop updateWorkshopTransferWorkshopInputDtoToWorkshop(WorkshopInputDto workshopInputDto) {
//        TypeMap<WorkshopInputDto, Workshop> propertyMapper = modelMapper.createTypeMap(WorkshopInputDto.class, Workshop.class);
//        propertyMapper.addMappings(modelMapper -> modelMapper.when(Conditions.isNull()).skip(WorkshopInputDto::getHighlightedInfo, Workshop::setHighlightedInfo));
//        propertyMapper.addMappings(modelMapper -> modelMapper.when(Conditions.isNull()).skip(WorkshopInputDto::getPublishWorkshop, Workshop::setPublishWorkshop));
//        propertyMapper.addMappings(modelMapper -> modelMapper.when(Conditions.isNull()).skip(WorkshopInputDto::getWorkshopCategory2, Workshop::setWorkshopCategory2));
//        propertyMapper.addMappings(modelMapper -> modelMapper.when(Conditions.isNull()).skip(WorkshopInputDto::getWorkshopVerified, Workshop::setWorkshopVerified));
//        propertyMapper.addMappings(modelMapper -> modelMapper.when(Conditions.isNull()).skip(WorkshopInputDto::getFeedbackAdmin, Workshop::setFeedbackAdmin));
//        propertyMapper.addMappings(modelMapper -> modelMapper.skip(Workshop::setId));
//
//        return modelMapper.map(workshopInputDto, Workshop.class);
//
//    }

//    TypeMap<Game, GameDTO> propertyMapper = this.mapper.createTypeMap(Game.class, GameDTO.class);
//    propertyMapper.addMappings(mapper -> mapper.skip(GameDTO::setId));
}
