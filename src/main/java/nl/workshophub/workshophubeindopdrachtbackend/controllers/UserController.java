package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.*;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.AuthenticationOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserCustomerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.UserWorkshopOwnerOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.services.UserService;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import nl.workshophub.workshophubeindopdrachtbackend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/customer/{customerId}")
    @Transactional
    public ResponseEntity<UserCustomerOutputDto> getCustomerById(@PathVariable Long customerId) {
        return new ResponseEntity<>(userService.getCustomerById(customerId), HttpStatus.OK);
    }

    @GetMapping("/workshopowner/{workshopOwnerId}")
    @Transactional
    public ResponseEntity<UserWorkshopOwnerOutputDto> getWorkshopOwnerById(@PathVariable Long workshopOwnerId) {
        return new ResponseEntity<>(userService.getWorkshopOwnerById(workshopOwnerId), HttpStatus.OK);
    }

    @GetMapping("/admin/workshopowners/")
    @Transactional
    public ResponseEntity<List<UserWorkshopOwnerOutputDto>> getWorkshopOwnersToVerify() {
        return new ResponseEntity<>(userService.getWorkshopOwnersToVerify(), HttpStatus.OK);
    }

    @GetMapping("/admin/")
    @Transactional
    public ResponseEntity<List<UserWorkshopOwnerOutputDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "admin/{userId}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUserAuthorities(userId));
    }


    @PostMapping("/customer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody UserCustomerInputDto customerInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (customerInputDto.workshopOwner) {
            return ResponseEntity.badRequest().body("To create a new account for a workshopowner you need to use a different link and more details are required.");
        }
        UserCustomerOutputDto customerOutputDto = userService.createCustomer(customerInputDto);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + customerOutputDto.id).toUriString());

        //return a token, so a user can be logged in directly on front-end side
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(customerOutputDto.email);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.created(uri).body(new AuthenticationOutputDto(jwt));
    }

    @PostMapping("/workshopowner")
    public ResponseEntity<Object> createWorkshopOwner(@Valid @RequestBody UserWorkshopOwnerInputDto workshopOwnerInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (!workshopOwnerInputDto.workshopOwner) {
            return ResponseEntity.badRequest().body("To create a new customer you need to use another link and less information is required.");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.createWorkshopOwner(workshopOwnerInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + workshopOwnerOutputDto.id).toUriString());

        //return a token, so a user can be logged in directly on front-end side
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(workshopOwnerOutputDto.email);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.created(uri).body(new AuthenticationOutputDto(jwt));
    }

    @PutMapping("/admin/{workshopOwnerId}")
    @Transactional
    public ResponseEntity<Object> verifyWorkshopOwnerByAdmin(@PathVariable Long workshopOwnerId, @RequestParam Boolean workshopOwnerVerified) {
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.verifyWorkshopOwnerByAdmin(workshopOwnerId, workshopOwnerVerified);
        return new ResponseEntity<>(workshopOwnerOutputDto, HttpStatus.OK);
    }

    @PutMapping("/customer/{customerId}")
    @Transactional
    public ResponseEntity<Object> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody UserCustomerInputDtoExclPassword customerInputDtoExclPassword, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (customerInputDtoExclPassword.workshopOwner) {
            return ResponseEntity.badRequest().body("With this link you can only update a customer's account. If you want to update/become a workshop owner you need to use another link and that requires more information.");
        }
        UserCustomerOutputDto customerOutputDto = userService.updateCustomer(customerId, customerInputDtoExclPassword);

        //return a token, so a user can be logged in directly on front-end side after updating user information. Jwt needs to be updated for it to be matching the details of the logged in user
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(customerOutputDto.email);

        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new AuthenticationOutputDto(jwt), HttpStatus.ACCEPTED);
    }


    @PutMapping("/workshopowner/{workshopOwnerId}")
    @Transactional
    public ResponseEntity<Object> updateWorkshopOwner(@PathVariable Long workshopOwnerId, @Valid @RequestBody UserWorkshopOwnerInputDtoExclPassword workshopOwnerInputDtoExclPassword, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        if (!workshopOwnerInputDtoExclPassword.workshopOwner) {
            return ResponseEntity.badRequest().body("With this link you can only update a workshop owner's account. If you want to update/become a customer you need to use another link and that requires less information.");
        }
        UserWorkshopOwnerOutputDto workshopOwnerOutputDto = userService.updateWorkshopOwner(workshopOwnerId, workshopOwnerInputDtoExclPassword);
        //return a token, so a user can be logged in directly on front-end side after updating user information. Jwt needs to be updated for it to be matching the details of the logged in user
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(workshopOwnerOutputDto.email);

        final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new AuthenticationOutputDto(jwt), HttpStatus.ACCEPTED);
    }

    @PutMapping ("/passwordrequest/{email}")
    public ResponseEntity<String> updatePassword(@PathVariable("email") String email, @Valid @RequestBody PasswordInputDto passwordInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(userService.updatePassword(email, passwordInputDto), HttpStatus.ACCEPTED);
    }

    @PutMapping ("/passwordupdaterequest/{email}")
    public ResponseEntity<Object> updatePasswordLoggedIn(@PathVariable("email") String email, @Valid @RequestBody PasswordInputDto passwordInputDto, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(userService.updatePasswordLoggedIn(email, passwordInputDto), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "admin/{email}/authorities")
    @Transactional
    public ResponseEntity<UserCustomerOutputDto> addUserAuthority(@PathVariable("email") String email, @RequestParam("authority") String authority) {
        UserCustomerOutputDto userCustomerOutputDto = userService.addUserAuthority(email, authority.toUpperCase());
        return new ResponseEntity<>(userCustomerOutputDto, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "admin/{userId}/authorities")
    public ResponseEntity<Object> removeAuthority(@PathVariable("userId") Long userId, @RequestParam("authority") String authority) {
        userService.removeAuthority(userId, authority.toUpperCase());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
