package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.PasswordInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.AuthenticationOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Authority;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.CheckAuthorization;
import nl.workshophub.workshophubeindopdrachtbackend.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public boolean userExists(String email) {

        return userRepository.existsByEmailIgnoreCase(email);
    }


    public UserCustomerOutputDto getCustomerById(Long customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(customer, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view this profile.");
        }
        if (customer.getWorkshopOwner() == true) {
            throw new RecordNotFoundException("The user with ID " + customerId + " is a workshop owner and not a customer.");
        }
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }

    public UserWorkshopOwnerOutputDto getWorkshopOwnerById(Long workshopOwnerId) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view this profile.");
        }
        if (workshopOwner.getWorkshopOwner() == false) {
            throw new RecordNotFoundException("The user with ID " + workshopOwnerId + " is a customer and not a workshop owner.");
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

    public Set<Authority> getUserAuthorities(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User with ID " + userId + " doesn't exist."));
        UserCustomerOutputDto userOutputDto = UserServiceTransferMethod.transferUserToCustomerOutputDto(user);
        // Don't want to communicate authorities with every outputdto - so only setting authorities in userOutputDto in this method.
        userOutputDto.authorities = user.getAuthorities();
        return userOutputDto.authorities;
    }

    public UserCustomerOutputDto createCustomer(UserCustomerInputDto customerInputDto) {
        if (userRepository.existsByEmailIgnoreCase(customerInputDto.email)) {
            throw new BadRequestException("Another user already exists with the email: " + customerInputDto.email);
        }
        User customer = new User();
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        customer = UserServiceTransferMethod.transferCustomerInputDtoToUser(customer, customerInputDto, passwordEncoder);
        customer.setApikey(randomString);
        userRepository.save(customer);
        customer.addAuthority(new Authority(customer.getId(), "ROLE_CUSTOMER"));
        userRepository.save(customer);


        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }

    public UserWorkshopOwnerOutputDto createWorkshopOwner(UserWorkshopOwnerInputDto workshopOwnerInputDto) {
        if (userRepository.existsByEmailIgnoreCase(workshopOwnerInputDto.email)) {
            throw new BadRequestException("Another user already exists with the email: " + workshopOwnerInputDto.email);
        }
        User workshopOwner = new User();
        workshopOwner = UserServiceTransferMethod.transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto, passwordEncoder);
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        workshopOwner.setApikey(randomString);
        userRepository.save(workshopOwner);
        workshopOwner.addAuthority(new Authority(workshopOwner.getId(), "ROLE_CUSTOMER"));
        userRepository.save(workshopOwner);
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public UserWorkshopOwnerOutputDto verifyWorkshopOwnerByAdmin(Long workshopOwnerId, Boolean
            workshopOwnerVerified) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist"));
        if (workshopOwner.getWorkshopOwner() == false) {
            throw new BadRequestException("This is a customer, not a workshop owner. The workshop owner should first enter all his/her company details & declare he/she is a workshopowner, before you can verify the account.");
        }
        workshopOwner.setWorkshopOwnerVerified(workshopOwnerVerified);
        if (workshopOwnerVerified == true) {
            workshopOwner.addAuthority(new Authority(workshopOwner.getId(), "ROLE_WORKSHOPOWNER"));
        }
        if (workshopOwnerVerified == false) {
            Authority authorityToRemove = workshopOwner.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase("ROLE_WORKSHOPOWNER")).findAny().get();
            workshopOwner.removeAuthority(authorityToRemove);
        }
        userRepository.save(workshopOwner);
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public UserCustomerOutputDto updateCustomer(Long customerId, UserCustomerInputDto customerInputDto) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The customer with ID " + customerId + " doesn't exist"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(customer, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to update this profile.");
        }
        if (!customer.getEmail().equals(customerInputDto.email)) {
            if (userRepository.existsByEmailIgnoreCase(customerInputDto.email)) {
                throw new BadRequestException("Another user already exists with the email: " + customerInputDto.email);
            }
        }
        customerInputDto.password = null;
        userRepository.save(UserServiceTransferMethod.transferCustomerInputDtoToUser(customer, customerInputDto, passwordEncoder));
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(customer);
    }


    public UserWorkshopOwnerOutputDto updateWorkshopOwner(Long workshopOwnerId, UserWorkshopOwnerInputDto
            workshopOwnerInputDto) {
        User workshopOwner = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The workshop owner with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(workshopOwner, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to update this profile.");
        }
        if (!workshopOwner.getEmail().equals(workshopOwnerInputDto.email)) {
            if (userRepository.existsByEmailIgnoreCase(workshopOwnerInputDto.email)) {
                throw new BadRequestException("Another user already exists with the email: " + workshopOwnerInputDto.email);
            }
        }
        userRepository.save(UserServiceTransferMethod.transferWorkshopOwnerInputDtoToUser(workshopOwner, workshopOwnerInputDto, passwordEncoder));
        return UserServiceTransferMethod.transferUserToWorkshopOwnerOutputDto(workshopOwner);
    }

    public String updatePassword(String email, PasswordInputDto passwordInputDto) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            throw new RecordNotFoundException("The user with email: " + email + " doesn't exist.");
        }
        User user = userRepository.findByEmailIgnoreCase(email);

        // in case of a logged in user that wants to change the password:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
                throw new ForbiddenException("You're not allowed to update the password for this account.");
            }
        }
        // TODO: 28/06/2023 in documentatie toevoegen:
        //in case of a forgotten password (email verification which happens in the 'real world' is too complex, so I will just reset the password to the new password) the password will be changed without verification.
        user.setPassword(passwordEncoder.encode(passwordInputDto.newPassword));
        userRepository.save(user);

        return "The password has been updated sucessfully.";
    }

    public UserCustomerOutputDto addUserAuthority(String email, String authority) {
        if (!userRepository.existsByEmailIgnoreCase(email)) {
            throw new RecordNotFoundException("The user with email: " + email + " doesn't exist.");
        }
        User user = userRepository.findByEmailIgnoreCase(email);
        for (Authority a : user.getAuthorities()) {
            if (a.getAuthority().equals("ROLE_" + authority)) {
                throw new BadRequestException("The user with email: " + email + " already has the authority: " + authority + ". You can't add an authority two times.");
            }
        }
        try {
            user.addAuthority(new Authority(user.getId(), "ROLE_" + authority));
        } catch (Exception e) {
            throw new BadRequestException("This authority can't be added");
        }
        userRepository.save(user);
        return UserServiceTransferMethod.transferUserToCustomerOutputDto(user);
    }

    public void removeAuthority(Long userId, String authority) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("User with ID: " + userId + " doesn't exist."));
        for (Authority a : user.getAuthorities()) {
            if (a.getAuthority().equals("ROLE_" + authority)) {
                user.getAuthorities().remove(a);
            }
        }
        userRepository.save(user);
    }


    public void deleteUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
            userRepository.delete(user);
        } catch (Exception e) {
            throw new BadRequestException("This user has a relation with either one or more workshop(s), review(s) and/or booking(s). You can't remove this user before removing the other items.");
        }
    }


}
