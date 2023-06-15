package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserCustomerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.UserWorkshopOwnerInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.WorkshopInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
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
    //customer
    @GetMapping("/customer/{id}")
    public ResponseEntity<UserCustomerOutputDto> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getCustomerById(id), HttpStatus.OK);
    }
    //owner
    @GetMapping("/workshopowner/{id}")
    public ResponseEntity<UserWorkshopOwnerOutputDto> getWorkshopOwnerById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getWorkshopOwnerById(id), HttpStatus.OK);
    }

    //admin
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
            return ResponseEntity.badRequest().body("Om een nieuw account voor workshopeigenaar aan te maken, moet je een andere link gebruiken en meer details invullen");
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
            return ResponseEntity.badRequest().body("Om een nieuw account voor een klant aan te maken, moet je een andere link gebruiken en is er minder informatie nodig");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.createWorkshopOwner(workshopOwnerInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(workshopOwnerOutputDto);
    }


    // bij het verifieren van een workshopowner - dan ook automatisch rol workshopowner toekennen.




}
