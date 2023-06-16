package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.AverageRatingWorkshopOwnerCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AverageRatingWorkshopOwnerCalculator averageRatingWorkshopOwnerCalculator;


    public UserService(UserRepository userRepository, AverageRatingWorkshopOwnerCalculator averageRatingWorkshopOwnerCalculator) {
        this.userRepository = userRepository;
        this.averageRatingWorkshopOwnerCalculator = averageRatingWorkshopOwnerCalculator;
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

    public UserCustomerOutputDto createCustomer(UserCustomerInputDto customerInputDto) throws BadRequestException {
        if (userRepository.existsByEmail(customerInputDto.email)){
            throw new BadRequestException("Er bestaat al een gebruiker met het emailadres: " + customerInputDto.email);
        }
        User customer = new User();
        transferCustomerInputDtoToUser(customer, customerInputDto);
        userRepository.save(customer);
        return transferUserToCustomerOutputDto(customer);
    }
    public UserWorkshopOwnerOutputDto createWorkshopOwner(UserWorkshopOwnerInputDto workshopOwnerInputDto) throws BadRequestException {
        if (userRepository.existsByEmail(workshopOwnerInputDto.email)){
            throw new BadRequestException("Er bestaat al een gebruiker met het emailadres: " + workshopOwnerInputDto.email);
        }
        User workshopOwner = new User();
        transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto);
        userRepository.save(workshopOwner);
        return transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    // ook automatisch rol workshopowner toevoegen na verificatie
    public UserWorkshopOwnerOutputDto verifyWorkshopOwnerByAdmin(Long workshopOwnerId, Boolean workshopOwnerVerified) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("De workshop eigenaar met ID " + workshopOwnerId + " bestaat niet"));
        if (workshopOwner.getWorkshopOwner() == false){
            throw new BadRequestException("Dit is een klant en geen workshopeigenaar, laat de workshopeigenaar eerst al zijn/haar bedrijfsgegevens invullen en aangeven dat hij/zij workshopeigenaar is, voordat je kunt verifieren");
        }
        workshopOwner.setWorkshopOwnerVerified(workshopOwnerVerified);
        if (workshopOwnerVerified == true){
            // hier de rol toevoegen
        }
//        if (workshopOwnerVerified == false && workshopOwner heeft de rol workshopowner - dan die verwijderen)

        userRepository.save(workshopOwner);
        return transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public UserCustomerOutputDto updateCustomer(Long customerId, UserCustomerInputDto customerInputDto) throws RecordNotFoundException {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("De klant met ID " + customerId + " bestaat niet"));
        System.out.println(customer.getEmail());
        if (!customer.getEmail().equals(customerInputDto.email)){
            if (userRepository.existsByEmail(customerInputDto.email)){
                throw new BadRequestException("Er bestaat al een andere gebruiker met het emailadres: " + customerInputDto.email);
            }
        }
        transferCustomerInputDtoToUser(customer, customerInputDto);
        userRepository.save(customer);
        return transferUserToCustomerOutputDto(customer);
    }



    public UserWorkshopOwnerOutputDto updateWorkshopOwner(Long workshopOwnerId, UserWorkshopOwnerInputDto workshopOwnerInputDto) throws RecordNotFoundException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("De workshop eigenaar met ID " + workshopOwnerId + " bestaat niet"));
        if (!workshopOwner.getEmail().equals(workshopOwnerInputDto.email)){
            if (userRepository.existsByEmail(workshopOwnerInputDto.email)){
                throw new BadRequestException("Er bestaat al een andere gebruiker met het emailadres: " + workshopOwnerInputDto.email);
            }
        }
        transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto);
        userRepository.save(workshopOwner);
        return transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }


    public UserCustomerOutputDto transferUserToCustomerOutputDto(User customer) {
        UserCustomerOutputDto customerOutputDto = new UserCustomerOutputDto();
        customerOutputDto.id = customer.getId();
        customerOutputDto.firstName = customer.getFirstName();
        customerOutputDto.lastName = customer.getLastName();
        customerOutputDto.email = customer.getEmail();
        customerOutputDto.workshopOwner = customer.getWorkshopOwner();

        return customerOutputDto;
    }

    public UserWorkshopOwnerOutputDto transferUserToWorkshopOwnerOutputDto(User workshopOwner) {
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = new UserWorkshopOwnerOutputDto();
        workshopOwnerOutputDto.id = workshopOwner.getId();
        workshopOwnerOutputDto.firstName = workshopOwner.getFirstName();
        workshopOwnerOutputDto.lastName = workshopOwner.getLastName();
        workshopOwnerOutputDto.email = workshopOwner.getEmail();
        workshopOwnerOutputDto.companyName = workshopOwner.getCompanyName();
        workshopOwnerOutputDto.kvkNumber = workshopOwner.getKvkNumber();
        workshopOwnerOutputDto.vatNumber = workshopOwner.getVatNumber();
        workshopOwnerOutputDto.workshopOwnerVerified = workshopOwner.getWorkshopOwnerVerified();
        workshopOwnerOutputDto.workshopOwner = workshopOwner.getWorkshopOwner();
        workshopOwnerOutputDto.averageRatingReviews = averageRatingWorkshopOwnerCalculator.calculateAverageRatingWorkshopOwner(workshopOwner);


        return workshopOwnerOutputDto;
    }

    public User transferWorkshopOwnerInputDtoToUser(User workshopOwner, UserWorkshopOwnerInputDto workshopOwnerInputDto) {
        workshopOwner.setFirstName(workshopOwnerInputDto.firstName);
        workshopOwner.setLastName(workshopOwnerInputDto.lastName);
        workshopOwner.setEmail(workshopOwnerInputDto.email);
        workshopOwner.setPassword(workshopOwnerInputDto.password);
        workshopOwner.setCompanyName(workshopOwnerInputDto.companyName);
        workshopOwner.setKvkNumber(workshopOwnerInputDto.kvkNumber);
        workshopOwner.setVatNumber(workshopOwnerInputDto.vatNumber);
        workshopOwner.setWorkshopOwner(workshopOwnerInputDto.workshopOwner);

        //bewust workshopverified hieruit gelaten, omdat het verifyen alleen via de put verify methode gaat - en dat kan alleen de admin doen

        return workshopOwner;
    }

    public User transferCustomerInputDtoToUser(User customer, UserCustomerInputDto customerInputDto) {
        customer.setFirstName(customerInputDto.firstName);
        customer.setLastName(customerInputDto.lastName);
        customer.setEmail(customerInputDto.email);
        customer.setPassword(customerInputDto.password);
        customer.setWorkshopOwner(customerInputDto.workshopOwner);

        return customer;
    }
}
