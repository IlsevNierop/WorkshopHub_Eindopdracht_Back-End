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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }


    // PasswordEncoderBean. Deze kun je overal in je applicatie injecteren waar nodig.
    // Je kunt dit ook in een aparte configuratie klasse zetten.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()
//                .requestMatchers("/**").permitAll()
                //authentication
                .requestMatchers("/authenticated").authenticated()
                .requestMatchers("/signin").permitAll()
                //open
                .requestMatchers(HttpMethod.POST, "/users/customer").permitAll() //everyone can register //post
                .requestMatchers(HttpMethod.POST, "/users/workshopowner").permitAll() //everyone can register //post
                .requestMatchers(HttpMethod.GET,"/workshops" ).permitAll() //everyone can see the workshop calendar //get
                .requestMatchers(HttpMethod.GET,"/workshops/{workshopId}" ).permitAll() //everyone can see the workshop calendar //get
                .requestMatchers(HttpMethod.GET,"/workshops/workshopowner/{workshopOwnerId}" ).permitAll() //everyone can see the workshop calendar //get


                //customer
                .requestMatchers("/users/customer/**").authenticated() //get, put
                .requestMatchers(HttpMethod.PUT,"/workshops/favourite/{userId}/{workshopId}").authenticated() //put


                //owner
                .requestMatchers("/users/workshopowner/**").hasAnyRole("WORKSHOPOWNER", "ADMIN") //get, put
                .requestMatchers("/workshops/workshopowner/**" ).hasAnyRole("WORKSHOPOWNER", "ADMIN") //get, get, post, put, put, delete


                //admin
                .requestMatchers("/users/admin/**").hasRole("ADMIN") //get, get, put, post, delete, delete
                .requestMatchers("/workshops/admin/**" ).hasRole("ADMIN") //get, get, get, put



                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}