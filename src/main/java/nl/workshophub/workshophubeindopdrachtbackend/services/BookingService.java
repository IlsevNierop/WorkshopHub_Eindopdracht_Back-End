package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.NoAvailableSpotsException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.AvailableSpotsCalculation;
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
    private final UserRepository userRepository;

    private final AvailableSpotsCalculation availableSpotsCalculation;

    public BookingService(BookingRepository bookingRepository, WorkshopRepository workshopRepository, UserRepository userRepository, AvailableSpotsCalculation availableSpotsCalculation) {
        this.bookingRepository = bookingRepository;
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.availableSpotsCalculation = availableSpotsCalculation;
    }

    public List<BookingOutputDto> getAllBookingsFromUser(Long userId) throws RecordNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID nummer " + userId + " bestaat niet"));
        List<Booking> userBookings = user.getBookings();
        List<BookingOutputDto> userBookingOutputDtos = new ArrayList<>();
        for (Booking b : userBookings) {
            BookingOutputDto userBookingOutputDto = transferBookingToBookingOutputDto(b);
            userBookingOutputDtos.add(userBookingOutputDto);
        }
        return userBookingOutputDtos;
    }

    public List<BookingOutputDto> getAllBookingsFromWorkshop(Long workshopId) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + workshopId + " bestaat niet"));
        List<Booking> workshopBookings = workshop.getWorkshopBookings();
        List<BookingOutputDto> workshopBookingOutputDtos = new ArrayList<>();
        for (Booking b : workshopBookings) {
            BookingOutputDto workshopBookingOutputDto = transferBookingToBookingOutputDto(b);
            workshopBookingOutputDtos.add(workshopBookingOutputDto);
        }
        return workshopBookingOutputDtos;
    }

    public BookingOutputDto getOneBookingById(Long bookingId) throws RecordNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("De boeking met ID nummer " + bookingId + " bestaat niet"));

        return transferBookingToBookingOutputDto(booking);
    }

    public BookingOutputDto createBooking(Long customerId, Long workshopId, BookingInputDto bookingInputDto) throws RecordNotFoundException, NoAvailableSpotsException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + workshopId + " bestaat niet"));
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("De gebruiker met ID nummer " + customerId + " bestaat niet"));
        if (availableSpotsCalculation.getAvailableSpotsWorkshop(workshop) < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Er zijn nog maar " + (availableSpotsCalculation.getAvailableSpotsWorkshop(workshop) + " plekjes beschikbaar, en je probeert " + bookingInputDto.amount + " plekjes te boeken"));
        }
        Booking booking = transferBookingInputDtoToBooking(bookingInputDto);
        booking.setWorkshop(workshop);
        booking.setCustomer(customer);
        bookingRepository.save(booking);
        return transferBookingToBookingOutputDto(booking);
    }

    public BookingOutputDto updateBooking(Long bookingId, BookingInputDto bookingInputDto) throws RecordNotFoundException, NoAvailableSpotsException, BadRequestException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("De boeking met ID nummer " + bookingId + " bestaat niet"));
        if (bookingInputDto.workshopId == null) {
            throw new BadRequestException("Je kunt geen boeking wijzigen zonder aan deze aan een workshop te koppelen");
        }
        Workshop workshop = workshopRepository.findById(bookingInputDto.workshopId).orElseThrow(() -> new RecordNotFoundException("De workshop met ID nummer " + bookingInputDto.workshopId + " bestaat niet"));
        if (availableSpotsCalculation.getAvailableSpotsWorkshop(workshop) < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Er zijn nog maar " + (availableSpotsCalculation.getAvailableSpotsWorkshop(workshop) + " plekjes beschikbaar, en je probeert " + bookingInputDto.amount + " plekjes te boeken"));
        }

        booking.setDateOrder(bookingInputDto.dateOrder);
        booking.setAmount(bookingInputDto.amount);
        if (bookingInputDto.commentsCustomer != null) {
            booking.setCommentsCustomer(bookingInputDto.commentsCustomer);
        }
        booking.setWorkshop(workshop);
        //customer wordt niet gewijzigd bij boeking - die is altijd leidend
        bookingRepository.save(booking);
        return transferBookingToBookingOutputDto(booking);
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("De boeking met ID nummer " + bookingId + " bestaat niet"));
        bookingRepository.delete(booking);
    }


    public BookingOutputDto transferBookingToBookingOutputDto(Booking booking) {
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.id = booking.getId();
        bookingOutputDto.dateOrder = booking.getDateOrder();
        bookingOutputDto.commentsCustomer = booking.getCommentsCustomer();
        bookingOutputDto.amount = booking.getAmount();
        bookingOutputDto.workshopId = booking.getWorkshop().getId();
        bookingOutputDto.workshopTitle = booking.getWorkshop().getTitle();
        bookingOutputDto.customer = booking.getCustomer();

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
