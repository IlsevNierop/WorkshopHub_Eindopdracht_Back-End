package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.BookingRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final WorkshopRepository workshopRepository;

    public BookingService(BookingRepository bookingRepository, WorkshopRepository workshopRepository) {
        this.bookingRepository = bookingRepository;
        this.workshopRepository = workshopRepository;
    }

    public List<BookingOutputDto> getAllBookingsFromWorkshop(Long workshopId) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + workshopId + " bestaat niet"));
        List<Booking> workshopBookings = workshop.getWorkshopBookings();
        if (workshopBookings.isEmpty()) {
            throw new RecordNotFoundException("Er zijn momenteel geen boekingen van de workshop met ID nummer " + workshopId);
        }
        List<BookingOutputDto> workshopBookingsOutputDto = new ArrayList<>();
        for (Booking b : workshopBookings) {
            BookingOutputDto workshopBookingOutputDto = transferBookingToBookingOutputDto(b);
            workshopBookingsOutputDto.add(workshopBookingOutputDto);
        }
        return workshopBookingsOutputDto;
    }

    public BookingOutputDto getOneBookingById(Long bookingId) throws RecordNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("De boeking met ID nummer " + bookingId + " bestaat niet"));

        return transferBookingToBookingOutputDto(booking);
    }




    public BookingOutputDto transferBookingToBookingOutputDto(Booking booking) {
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.id = booking.getId();
        bookingOutputDto.dateOrder = booking.getDateOrder();
        bookingOutputDto.commentsCustomer = booking.getCommentsCustomer();
        bookingOutputDto.amount = booking.getAmount();
        bookingOutputDto.workshopId = booking.getWorkshop().getId();
        bookingOutputDto.workshopTitle = booking.getWorkshop().getTitle();

        return bookingOutputDto;
    }


    public Booking transferBookingInputDtoToBooking(BookingInputDto bookingInputDto) {
        Booking booking = new Booking();
        booking.setDateOrder(bookingInputDto.dateOrder);
        booking.setCommentsCustomer(bookingInputDto.commentsCustomer);
        booking.setAmount(bookingInputDto.amount);

        //als je in de body van reviewinputdto de workshopId meegeeft, kun je hier, via de workshoprepository, ook nog de workshop setten.


        return booking;
    }

}
