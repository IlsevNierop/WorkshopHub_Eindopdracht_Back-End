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
        if (customer.getWorkshopOwner() == true) {
            throw new RecordNotFoundException("De gebruiker met ID " + id + " is een workshopeigenaar en geen klant");
        }
        return transferUserToCustomerOutputDto(customer);
    }

    public UserWorkshopOwnerOutputDto getWorkshopOwnerById(Long id) throws RecordNotFoundException {
        User workshopOwner = userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID " + id + " bestaat niet"));
        if (workshopOwner.getWorkshopOwner() == false) {
            throw new RecordNotFoundException("De gebruiker met ID " + id + " is geen workshopeigenaar, maar een klant");
        }
        return transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public List<UserWorkshopOwnerOutputDto> getWorkshopOwnersToVerify() throws RecordNotFoundException {
        List<User> workshopowners = userRepository.findByWorkshopOwnerIsTrueAndWorkshopOwnerVerifiedIsNull();
        List<UserWorkshopOwnerOutputDto> workshopOwnerOutputDtos = new ArrayList<>();
        for (User workshopowner : workshopowners) {
            UserWorkshopOwnerOutputDto workshopOwnerOutputDto = transferUserToWorkshopOwnerOutputDto(workshopowner);
            workshopOwnerOutputDtos.add(workshopOwnerOutputDto);
        }
        return workshopOwnerOutputDtos;

    }


    public UserCustomerOutputDto transferUserToCustomerOutputDto(User customer) {
        UserCustomerOutputDto customerOutputDto = new UserCustomerOutputDto();
        customerOutputDto.firstName = customer.getFirstName();
        customerOutputDto.lastName = customer.getLastName();
        customerOutputDto.email = customer.getEmail();
//        customerOutputDto.password = customer.getPassword();
        customerOutputDto.workshopOwner = customer.getWorkshopOwner();

        return customerOutputDto;
    }

    public UserWorkshopOwnerOutputDto transferUserToWorkshopOwnerOutputDto(User workshopOwner) {
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = new UserWorkshopOwnerOutputDto();
        workshopOwnerOutputDto.id = workshopOwner.getId();
        workshopOwnerOutputDto.firstName = workshopOwner.getFirstName();
        workshopOwnerOutputDto.lastName = workshopOwner.getLastName();
        workshopOwnerOutputDto.email = workshopOwner.getEmail();
//        workshopOwnerOutputDto.password = workshopOwner.getPassword();
        workshopOwnerOutputDto.companyName = workshopOwner.getCompanyName();
        workshopOwnerOutputDto.kvkNumber = workshopOwner.getKvkNumber();
        workshopOwnerOutputDto.vatNumber = workshopOwner.getVatNumber();
        workshopOwnerOutputDto.workshopOwnerVerified = workshopOwner.getWorkshopOwnerVerified();
        workshopOwnerOutputDto.workshopOwner = workshopOwner.getWorkshopOwner();

        return workshopOwnerOutputDto;
    }

    public User transferWorkshopOwnerInputDtoToUser(UserWorkshopOwnerInputDto workshopOwnerInputDto) {
        User workshopOwner = new User();
        workshopOwner.setFirstName(workshopOwnerInputDto.firstName);
        workshopOwner.setLastName(workshopOwnerInputDto.lastName);
        workshopOwner.setEmail(workshopOwnerInputDto.email);
        workshopOwner.setPassword(workshopOwnerInputDto.password);
        workshopOwner.setCompanyName(workshopOwnerInputDto.companyName);
        workshopOwner.setKvkNumber(workshopOwnerInputDto.kvkNumber);
        workshopOwner.setVatNumber(workshopOwnerInputDto.vatNumber);
        workshopOwner.setWorkshopOwnerVerified(workshopOwnerInputDto.workshopOwnerVerified);
        workshopOwner.setWorkshopOwner(workshopOwnerInputDto.workshopOwner);

        return workshopOwner;
    }

    public User transferCustomerInputDtoToUser(UserCustomerInputDto customerInputDto) {
        User customer = new User();
        customer.setFirstName(customerInputDto.firstName);
        customer.setLastName(customerInputDto.lastName);
        customer.setEmail(customerInputDto.email);
        customer.setPassword(customerInputDto.password);
        customer.setWorkshopOwner(customerInputDto.workshopOwner);

        return customer;
    }
}
