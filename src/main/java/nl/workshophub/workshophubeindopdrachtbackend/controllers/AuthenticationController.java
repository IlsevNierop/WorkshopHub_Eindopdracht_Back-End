package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.AuthenticationInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.AuthenticationOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.services.CustomUserDetailsService;
import nl.workshophub.workshophubeindopdrachtbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
public class AuthenticationController {

    /*inject authentionManager, userDetailsService en jwtUtil*/
    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;


    public AuthenticationController(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


        // Deze methode geeft de principal (basis user gegevens) terug van de ingelogde gebruiker

    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    //om in te loggen
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthenticationInputDto authenticationInputDto) {

        String email = authenticationInputDto.getEmail();
        String password = authenticationInputDto.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        }
        catch (Exception e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(email);

        final String jwt = jwtUtil.generateToken(userDetails);

        //hier meer info toevoegen? usercustomeroutputdto? of als iemand ingelogd is - deze redirecten overzicht (home) get all workshops
        // of als iemand inlogt - dat hij/zij blijft op de pagina waar hij/zij is / heen wilde maar dan incl verificatie

        return ResponseEntity.ok(new AuthenticationOutputDto(jwt));
    }

}