package nl.workshophub.workshophubeindopdrachtbackend.config;


import nl.workshophub.workshophubeindopdrachtbackend.filter.JwtRequestFilter;
import nl.workshophub.workshophubeindopdrachtbackend.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()

                //.................................open.................................
                .requestMatchers("/signin").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/customer").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/workshopowner").permitAll() //everyone can register //post
                .requestMatchers(HttpMethod.PUT, "/users/passwordrequest/{email}").permitAll() //no verification when password is forgotten, see userservice comments
                .requestMatchers(HttpMethod.GET, "/workshops").permitAll() //everyone can see the workshop calendar //get
                .requestMatchers(HttpMethod.GET, "/workshops/{workshopId}").permitAll() //everyone can see the workshop calendar //get
                .requestMatchers(HttpMethod.GET, "/workshops/workshopowner/{workshopOwnerId}").permitAll() //everyone can see the workshop calendar //get
                .requestMatchers(HttpMethod.GET, "/reviews/{reviewId}").permitAll() //get
                .requestMatchers(HttpMethod.GET, "/reviews/workshopowner/{workshopOwnerId}").permitAll() //everyone can see verified reviews //get
                .requestMatchers(HttpMethod.GET, "/downloadworkshoppic/{workshopId}").permitAll()
                .requestMatchers("/downloadprofilepic/{userId}").permitAll() //get

                //...............................authority: customer...............................

                .requestMatchers("/authenticated").authenticated()

                .requestMatchers("/users/customer/**").authenticated() //get, put
                .requestMatchers(HttpMethod.PUT, "/users/passwordupdaterequest/{email}").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/workshopowner/{workshopOwnerId}").authenticated() //get workshopowner
                .requestMatchers(HttpMethod.PUT, "/users/workshopowner/{workshopOwnerId}").authenticated() //put workshopowner

                .requestMatchers(HttpMethod.PUT, "/workshops/favourite/{userId}/{workshopId}").authenticated() //put
                .requestMatchers(HttpMethod.GET, "/workshops/favourites/{userId}").authenticated() //get

                .requestMatchers(HttpMethod.GET, "/bookings/user/{userId}").authenticated() //get
                .requestMatchers(HttpMethod.GET, "/bookings/{bookingId}").authenticated() //get
                .requestMatchers(HttpMethod.POST, "/bookings/{customerId}/{workshopId}").authenticated() //post
                .requestMatchers(HttpMethod.PUT, "/bookings/{bookingId}").authenticated() //put
                .requestMatchers(HttpMethod.DELETE, "/bookings/{bookingId}").authenticated() //delete

                .requestMatchers(HttpMethod.GET, "/reviews/customer/{customerId}").authenticated() //get
                .requestMatchers(HttpMethod.POST, "/reviews/{workshopId}/{customerId}").authenticated() //post
                .requestMatchers(HttpMethod.PUT, "/reviews/{customerId}/{reviewId}").authenticated() //put
                .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").authenticated() //delete

                .requestMatchers("/uploadprofilepic/{userId}").authenticated() //post
                .requestMatchers("/deleteprofilepic/{userId}").authenticated() //delete

                //.................................authority: owner.................................
                .requestMatchers("/users/workshopowner/**").hasAnyRole("WORKSHOPOWNER", "ADMIN") //get, put

                .requestMatchers("/workshops/workshopowner/**").hasAnyRole("WORKSHOPOWNER", "ADMIN")

                .requestMatchers("/bookings/workshop/**").hasAnyRole("WORKSHOPOWNER", "ADMIN")  //get
                .requestMatchers(HttpMethod.GET, "/bookings/workshopowner/{workshopOwnerId}").hasAnyRole("WORKSHOPOWNER", "ADMIN")  //get
                .requestMatchers(HttpMethod.GET, "/bookings/workshopowner/csv/**").hasAnyRole("WORKSHOPOWNER", "ADMIN")  //get

                .requestMatchers(HttpMethod.POST, "/uploadworkshoppic/{workshopId}").hasAnyRole("WORKSHOPOWNER", "ADMIN")
                .requestMatchers("/deleteworkshoppic/{workshopId}").hasAnyRole("WORKSHOPOWNER", "ADMIN") //delete

                //..............................authority: only-admin...............................
                .requestMatchers("/users/admin/**").hasRole("ADMIN") //get, get, put, post, delete, delete

                .requestMatchers("/workshops/admin/**").hasRole("ADMIN") //get, get, get, put

                .requestMatchers(HttpMethod.GET, "/bookings").hasAnyRole("ADMIN") //get
                .requestMatchers(HttpMethod.GET, "/bookings/admin/csv").hasRole("ADMIN") //get

                .requestMatchers("/reviews/admin/**").hasRole("ADMIN") // get, get, put, delete

                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}