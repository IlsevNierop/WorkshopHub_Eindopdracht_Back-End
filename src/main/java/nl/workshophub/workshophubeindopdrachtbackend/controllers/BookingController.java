package nl.workshophub.workshophubeindopdrachtbackend.controllers;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.ReviewOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //owner and customer
//    @GetMapping ("/{userId}")
//    public ResponseEntity<List<BookingOutputDto>> getAllBookingsFromOwner(@PathVariable Long userId){
//        return new ResponseEntity<>(, HttpStatus.OK);
//
//    }

    //nog checken of workshop van de specifieke owner is die ingelogd is / alleen door eerst naar workshop te gaan - dat is al de check / bookings ophalen
    @GetMapping ("/workshop/{workshopId}")
    public ResponseEntity<List<BookingOutputDto>> getAllBookingsFromWorkshop(@PathVariable Long workshopId){

        return new ResponseEntity<>(bookingService.getAllBookingsFromWorkshop(workshopId), HttpStatus.OK);

    }
    @GetMapping ("/booking/{bookingId}")
    public ResponseEntity<BookingOutputDto> getOneBookingById(@PathVariable Long bookingId){

        return new ResponseEntity<>(bookingService.getOneBookingById(bookingId), HttpStatus.OK);

    }





}
