package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserCustomerOutputDto getCustomerById(Long customerId) throws RecordNotFoundException {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));
        if (customer.getWorkshopOwner() == true) {
            throw new RecordNotFoundException("The user with ID " + customerId + " is a workshop owner and not a customer.");
        }
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }

    public UserWorkshopOwnerOutputDto getWorkshopOwnerById(Long workshopOwnerId) throws RecordNotFoundException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        if (workshopOwner.getWorkshopOwner() == false) {
            throw new RecordNotFoundException("The user with ID "  + workshopOwnerId + " is a customer and not a workshop owner.");
        }
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public List<UserWorkshopOwnerOutputDto> getWorkshopOwnersToVerify() {
        List<User> workshopOwners = userRepository.findByWorkshopOwnerIsTrueAndWorkshopOwnerVerifiedIsNullOrWorkshopOwnerVerifiedIsFalse();
        List<UserWorkshopOwnerOutputDto> workshopOwnerOutputDtos = new ArrayList<>();
        for (User workshopOwner : workshopOwners) {
            workshopOwnerOutputDtos.add(UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner));
        }
        return workshopOwnerOutputDtos;
    }

    public UserCustomerOutputDto createCustomer(UserCustomerInputDto customerInputDto) throws BadRequestException {
        if (userRepository.existsByEmail(customerInputDto.email)){
            throw new BadRequestException("Invalid request: another user exists with the email: " + customerInputDto.email);
        }
        User customer = new User();
        userRepository.save(UserServiceTransferMethod.transferCustomerInputDtoToUser(customer, customerInputDto));
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }
    public UserWorkshopOwnerOutputDto createWorkshopOwner(UserWorkshopOwnerInputDto workshopOwnerInputDto) throws BadRequestException {
        if (userRepository.existsByEmail(workshopOwnerInputDto.email)){
            throw new BadRequestException("Invalid request: another user exists with the email: " + workshopOwnerInputDto.email);
        }
        User workshopOwner = new User();
        userRepository.save(UserServiceTransferMethod.transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto));
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public UserWorkshopOwnerOutputDto verifyWorkshopOwnerByAdmin(Long workshopOwnerId, Boolean workshopOwnerVerified) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist"));
        if (workshopOwner.getWorkshopOwner() == false){
            throw new BadRequestException("This is a customer, not a workshop owner. The workshop owner should first enter all his/her company details & declare he/she is a workshopowner, before you can verify the account.");
        }
//
        workshopOwner.setWorkshopOwnerVerified(workshopOwnerVerified);
        if (workshopOwnerVerified == true){
            // hier de rol toevoegen
        }
//        if (workshopOwnerVerified == false && workshopOwner heeft de rol workshopowner - dan die verwijderen)

        userRepository.save(workshopOwner);
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public UserCustomerOutputDto updateCustomer(Long customerId, UserCustomerInputDto customerInputDto) throws RecordNotFoundException, BadRequestException {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The customer with ID " + customerId + " doesn't exist"));
        if (customer.getWorkshopOwner() == true){
            throw new BadRequestException("The account with ID " + customerId + " is a workshop owner and not a customer.");
        }
        if (!customer.getEmail().equals(customerInputDto.email)){
            if (userRepository.existsByEmail(customerInputDto.email)){
                throw new BadRequestException("Invalid request: another user exists with the email: " + customerInputDto.email);
            }
        }
        userRepository.save(UserServiceTransferMethod.transferCustomerInputDtoToUser(customer, customerInputDto));
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }

    public UserWorkshopOwnerOutputDto updateWorkshopOwner(Long workshopOwnerId, UserWorkshopOwnerInputDto workshopOwnerInputDto) throws RecordNotFoundException, BadRequestException {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));
        if (workshopOwner.getWorkshopOwner() == false){
            throw new BadRequestException("The account with ID " + workshopOwnerId + " is a customer and not a workshop owner.");
        }
        if (!workshopOwner.getEmail().equals(workshopOwnerInputDto.email)){
            if (userRepository.existsByEmail(workshopOwnerInputDto.email)){
                throw new BadRequestException("Invalid request: another user exists with the email: " + workshopOwnerInputDto.email);
            }
        }
        userRepository.save(UserServiceTransferMethod.transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto));
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public void deleteUser(Long userId) throws RecordNotFoundException, BadRequestException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
            userRepository.delete(user);
        }
        catch (Exception e){
            throw new BadRequestException("This user has a relation with either one or more workshop(s), review(s) and/or booking(s). You can't remove this user before removing the other items.");
        }
    }


}
