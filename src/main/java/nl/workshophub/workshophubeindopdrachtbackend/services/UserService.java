package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    ModelMapper modelMapper = new ModelMapper();

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCustomerOutputDto getCustomerById(Long id) throws RecordNotFoundException {
        User customer = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID " + id + " bestaat niet"));
        if (customer.getWorkshopOwner() == true){
            throw new RecordNotFoundException("De gebruiker met ID " + id + " is een workshopeigenaar");
        }
        return transferUserToCustomerOutputDto(customer);
    }
    public UserWorkshopOwnerOutputDto getWorkshopOwnerById(Long id) throws RecordNotFoundException {
        User workshopOwner = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID " + id + " bestaat niet"));
        if (workshopOwner.getWorkshopOwner() == false){
            throw new RecordNotFoundException("De gebruiker met ID " + id + " is geen workshopeigenaar");
        }
        return transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }
    public List<UserWorkshopOwnerOutputDto> getWorkshopOwnersToVerify() throws RecordNotFoundException{
        List<User> workshopowners = userRepository.findByWorkshopOwnerIsTrueAndWorkshopOwnerVerifiedIsNull();
        if (workshopowners.isEmpty()){
            throw new RecordNotFoundException("Er zijn momenteel geen goed te keuren workshopeigenaren");
        }
        List<UserWorkshopOwnerOutputDto> workshopOwnerOutputDtos = new ArrayList<>();
        for (User workshopowner: workshopowners) {
            UserWorkshopOwnerOutputDto workshopOwnerOutputDto = transferUserToWorkshopOwnerOutputDto(workshopowner);
            workshopOwnerOutputDtos.add(workshopOwnerOutputDto);
        }
        return workshopOwnerOutputDtos;

    }



    public UserCustomerOutputDto transferUserToCustomerOutputDto(User customer) {
        return modelMapper.map(customer, UserCustomerOutputDto.class);
    }

    public UserWorkshopOwnerOutputDto transferUserToWorkshopOwnerOutputDto(User workshopOwner) {
        return modelMapper.map(workshopOwner, UserWorkshopOwnerOutputDto.class);
    }

    public User transferWorkshopOwnerInputDtoToUser(UserWorkshopOwnerInputDto workshopOwnerInputDto) {
        return modelMapper.map(workshopOwnerInputDto, User.class);
    }
    public User transferCustomerInputDtoToUser(UserCustomerInputDto customerInputDto) {
        return modelMapper.map(customerInputDto, User.class);
    }
}
