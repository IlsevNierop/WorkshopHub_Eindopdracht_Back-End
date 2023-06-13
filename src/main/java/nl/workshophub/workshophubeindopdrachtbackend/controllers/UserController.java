package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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

    //getmapping om per bedrijf alle workshops te zien? voor niet ingelogde users?

    //admin

    @GetMapping("/admin/workshopowner/")
    public ResponseEntity<List<UserWorkshopOwnerOutputDto>> getWorkshopOwnersToVerify() {
        return new ResponseEntity<>(userService.getWorkshopOwnersToVerify(), HttpStatus.OK);
    }

    // bij het verifieren van een workshopowner - dan ook automatisch rol workshopowner toekennen.




}
