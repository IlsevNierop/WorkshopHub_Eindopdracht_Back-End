package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDtoExclPassword;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDtoExclPassword;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTransferMethod {

    public static UserCustomerOutputDto transferUserToCustomerOutputDto(User customer) {
        UserCustomerOutputDto customerOutputDto = new UserCustomerOutputDto();
        customerOutputDto.id = customer.getId();
        customerOutputDto.firstName = customer.getFirstName();
        customerOutputDto.lastName = customer.getLastName();
        customerOutputDto.email = customer.getEmail();
        customerOutputDto.workshopOwner = customer.getWorkshopOwner();
        customerOutputDto.enabled = customer.isEnabled();
        customerOutputDto.authorities = customer.getAuthorities();
        customerOutputDto.profilePicUrl = customer.getProfilePicUrl();

        return customerOutputDto;
    }

    public static UserWorkshopOwnerOutputDto transferUserToWorkshopOwnerOutputDto(User workshopOwner) {
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
        if (workshopOwner.calculateAverageRatingAndNumberReviewsWorkshopOwner() != null) {
            workshopOwnerOutputDto.averageRatingReviews = workshopOwner.calculateAverageRatingAndNumberReviewsWorkshopOwner().get(0);
        }

        workshopOwnerOutputDto.authorities = workshopOwner.getAuthorities();
        workshopOwnerOutputDto.profilePicUrl = workshopOwner.getProfilePicUrl();


        return workshopOwnerOutputDto;
    }

    public static User transferWorkshopOwnerInputDtoToUser(User workshopOwner, UserWorkshopOwnerInputDto workshopOwnerInputDto, PasswordEncoder passwordEncoder) {
        workshopOwner.setPassword(passwordEncoder.encode(workshopOwnerInputDto.password));
        workshopOwner.setFirstName(workshopOwnerInputDto.firstName);
        workshopOwner.setLastName(workshopOwnerInputDto.lastName);
        workshopOwner.setEmail(workshopOwnerInputDto.email);
        workshopOwner.setCompanyName(workshopOwnerInputDto.companyName);
        workshopOwner.setKvkNumber(workshopOwnerInputDto.kvkNumber);
        workshopOwner.setVatNumber(workshopOwnerInputDto.vatNumber);
        workshopOwner.setWorkshopOwner(workshopOwnerInputDto.workshopOwner);
        //workshopowner verified is not set in this transfer method, because verifying takes place via the put method containing verify boolean as a request parameter (and only admin can do that)
        return workshopOwner;
    }

    public static User transferWorkshopOwnerInputDtoToUserExclPasswordToUser(User workshopOwner, UserWorkshopOwnerInputDtoExclPassword workshopOwnerInputDtoExclPassword) {
        workshopOwner.setFirstName(workshopOwnerInputDtoExclPassword.firstName);
        workshopOwner.setLastName(workshopOwnerInputDtoExclPassword.lastName);
        workshopOwner.setEmail(workshopOwnerInputDtoExclPassword.email);
        workshopOwner.setCompanyName(workshopOwnerInputDtoExclPassword.companyName);
        workshopOwner.setKvkNumber(workshopOwnerInputDtoExclPassword.kvkNumber);
        workshopOwner.setVatNumber(workshopOwnerInputDtoExclPassword.vatNumber);
        workshopOwner.setWorkshopOwner(workshopOwnerInputDtoExclPassword.workshopOwner);
        //workshopowner verified is not set in this transfer method, because verifying takes place via the put method containing verify boolean as a request parameter (and only admin can do that)
        return workshopOwner;
    }

    public static User transferCustomerInputDtoToUser(User customer, UserCustomerInputDto customerInputDto, PasswordEncoder passwordEncoder) {
        customer.setPassword(passwordEncoder.encode(customerInputDto.password));
        customer.setFirstName(customerInputDto.firstName);
        customer.setLastName(customerInputDto.lastName);
        customer.setEmail(customerInputDto.email);
        customer.setWorkshopOwner(customerInputDto.workshopOwner);

        return customer;
    }

    public static User transferCustomerInputDtoExclPasswordToUser(User customer, UserCustomerInputDtoExclPassword customerInputDtoExclPassword) {
        customer.setFirstName(customerInputDtoExclPassword.firstName);
        customer.setLastName(customerInputDtoExclPassword.lastName);
        customer.setEmail(customerInputDtoExclPassword.email);
        customer.setWorkshopOwner(customerInputDtoExclPassword.workshopOwner);

        return customer;
    }
}
