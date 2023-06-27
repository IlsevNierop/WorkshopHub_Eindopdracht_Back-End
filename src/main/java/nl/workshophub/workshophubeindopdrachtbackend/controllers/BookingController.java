package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import jakarta.validation.Valid;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.WorkshopOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.util.FieldErrorHandling;
import nl.workshophub.workshophubeindopdrachtbackend.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingOutputDto>> getAllBookingsFromUser(@PathVariable Long userId) {
        return new ResponseEntity<>(bookingService.getAllBookingsFromUser(userId), HttpStatus.OK);
    }

    //nog checken of workshop van de specifieke owner is die ingelogd is / alleen door eerst naar workshop te gaan - dat is al de check / bookings ophalen - na authenticatie toevoeging
    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<BookingOutputDto>> getAllBookingsFromWorkshop(@PathVariable Long workshopId) {

        return new ResponseEntity<>(bookingService.getAllBookingsFromWorkshop(workshopId), HttpStatus.OK);

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutputDto> getOneBookingById(@PathVariable Long bookingId) {

        return new ResponseEntity<>(bookingService.getOneBookingById(bookingId), HttpStatus.OK);

    }

    @PostMapping("{customerId}/{workshopId}")
    public ResponseEntity<Object> createBooking(@PathVariable("customerId") Long customerId, @PathVariable("workshopId") Long workshopId, @Valid @RequestBody BookingInputDto bookingInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        BookingOutputDto bookingOutputDto = bookingService.createBooking(customerId, workshopId, bookingInputDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + bookingOutputDto.id).toUriString());
        return ResponseEntity.created(uri).body(bookingOutputDto);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable Long bookingId, @Valid @RequestBody BookingInputDto bookingInputDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(FieldErrorHandling.getErrorToStringHandling(bindingResult));
        }
        return new ResponseEntity<>(bookingService.updateBooking(bookingId, bookingInputDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/admin/{bookingId}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
