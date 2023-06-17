package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.services.UserService;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FieldErrorHandling fieldErrorHandling;
    public UserController(UserService userService, FieldErrorHandling fieldErrorHandling) {
        this.userService = userService;
        this.fieldErrorHandling = fieldErrorHandling;
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<UserCustomerOutputDto> getCustomerById(@PathVariable Long customerId) {
        return new ResponseEntity<>(userService.getCustomerById(customerId), HttpStatus.OK);
    }
    @GetMapping("/workshopowner/{workshopOwnerId}")
    public ResponseEntity<UserWorkshopOwnerOutputDto> getWorkshopOwnerById(@PathVariable Long workshopOwnerId) {
        return new ResponseEntity<>(userService.getWorkshopOwnerById(workshopOwnerId), HttpStatus.OK);
    }

    @GetMapping("/admin/workshopowners/")
    public ResponseEntity<List<UserWorkshopOwnerOutputDto>> getWorkshopOwnersToVerify() {
        return new ResponseEntity<>(userService.getWorkshopOwnersToVerify(), HttpStatus.OK);
    }

    @PostMapping ("/customer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody UserCustomerInputDto customerInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (customerInputDto.workshopOwner == true) {
            return ResponseEntity.badRequest().body("To create a new account for a workshopowner you need to use a different link and more details are required.");
        }
        UserCustomerOutputDto customerOutputDto = userService.createCustomer(customerInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + customerOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(customerOutputDto);
    }
    @PostMapping ("/workshopOwner")
    public ResponseEntity<Object> createWorkshopOwner(@Valid @RequestBody UserWorkshopOwnerInputDto workshopOwnerInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (workshopOwnerInputDto.workshopOwner == false) {
            return ResponseEntity.badRequest().body("To create a new customer you need to use another link and less information is required.");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.createWorkshopOwner(workshopOwnerInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(workshopOwnerOutputDto);
    }

    @PutMapping ("/admin/{workshopOwnerId}")
    public ResponseEntity <Object> verifyWorkshopOwnerByAdmin(@PathVariable Long workshopOwnerId,@RequestParam Boolean workshopOwnerVerified) throws BadRequestException {
//        //how to check if incoming parameter is correct? Now getting a 400 error if boolean is not true or false. Seems like the error is being created even before it hits the controller. Below code is not working:
//        if (workshopOwnerVerified != true || workshopOwnerVerified != false){
//            throw new BadRequestException("You should either verify (set workshopOwnerVerified to true) this workshop owner or disapprove (set workshopOwnerVerified to false) this workshop owner.");
//        }
//        if (bindingResult.hasFieldErrors()){
//            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
//        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.verifyWorkshopOwnerByAdmin(workshopOwnerId, workshopOwnerVerified);
        return new ResponseEntity<>(workshopOwnerOutputDto, HttpStatus.OK);
    }

 @PutMapping ("/customer/{customerId}")
    public ResponseEntity <Object> updateCustomer(@PathVariable Long customerId,@RequestBody UserCustomerInputDto customerInputDto, BindingResult bindingResult){
     if (bindingResult.hasFieldErrors()){
         return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
     }
     if (customerInputDto.workshopOwner == true) {
         return ResponseEntity.badRequest().body("With this link you can only update a customer's account. If you want to update/become a workshop owner you need to use another link and that requires more information.");
     }
        UserCustomerOutputDto customerOutputDto = userService.updateCustomer (customerId, customerInputDto);

        return new ResponseEntity<>(customerOutputDto, HttpStatus.OK);
    }

    @PutMapping ("/workshopowner/{workshopOwnerId}")
    public ResponseEntity <Object> updateWorkshopOwner(@PathVariable Long workshopOwnerId,@RequestBody UserWorkshopOwnerInputDto workshopOwnerInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()){
            return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (workshopOwnerInputDto.workshopOwner == false) {
            return ResponseEntity.badRequest().body("With this link you can only update a workshop owner's account. If you want to update/become a customer you need to use another link and that requires less information.");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.updateWorkshopOwner(workshopOwnerId, workshopOwnerInputDto);
        return new ResponseEntity<>(workshopOwnerOutputDto, HttpStatus.OK);
    }

    @DeleteMapping ("admin/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
