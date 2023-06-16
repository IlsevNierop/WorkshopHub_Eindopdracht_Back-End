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
            return ResponseEntity.badRequest().body("Dit is de endpoint voor de workshopeigenaar, om een nieuw account voor een klant aan te maken, moet je een andere link gebruiken en is er minder informatie nodig");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.createWorkshopOwner(workshopOwnerInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(workshopOwnerOutputDto);
    }

    @PutMapping ("/admin/{workshopOwnerId}")
    public ResponseEntity <UserWorkshopOwnerOutputDto> verifyWorkshopOwnerByAdmin(@PathVariable Long workshopOwnerId,@RequestParam Boolean workshopOwnerVerified){
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.verifyWorkshopOwnerByAdmin(workshopOwnerId, workshopOwnerVerified);
        return new ResponseEntity<>(workshopOwnerOutputDto, HttpStatus.OK);
    }

 @PutMapping ("/customer/{customerId}")
    public ResponseEntity <Object> updateCustomer(@PathVariable Long customerId,@RequestBody UserCustomerInputDto customerInputDto, BindingResult bindingResult){
     if (bindingResult.hasFieldErrors()){
         return ResponseEntity.badRequest().body(fieldErrorHandling.getErrorToStringHandling(bindingResult));
     }
     if (customerInputDto.workshopOwner == true) {
         return ResponseEntity.badRequest().body("Hier kun je een account wijzien voor een klant, als je voor een workshopeigenaar het account wilt wijzigen, moet je een andere link gebruiken en meer details invullen");
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
            return ResponseEntity.badRequest().body("Dit is de endpoint voor de workshopeigenaar, om een nieuw account voor een klant aan te maken, moet je een andere link gebruiken en is er minder informatie nodig");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.updateWorkshopOwner(workshopOwnerId, workshopOwnerInputDto);
        return new ResponseEntity<>(workshopOwnerOutputDto, HttpStatus.OK);
    }


    //deletemapping (relaties?) - moet je een user kunnen verwijderen als die workshops / boekingen / reviews heeft? privacy wise moet je die kunnen verwijderen - maar als er boekingen en reviews zijn, dan wil je die behouden - anders kan owner zijn/haar account verwijderen en van scratch opnieuw beginnen na slechte reviews.

    @DeleteMapping ("admin/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
