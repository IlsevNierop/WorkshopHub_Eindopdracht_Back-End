package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.NoAvailableSpotsException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.BookingRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;

    //not sure if it's good practice / bad practice to inject the user service here, but using it to convert customer to customerdto (transfer method)
    private final UserService userService;


    public BookingService(BookingRepository bookingRepository, WorkshopRepository workshopRepository, UserRepository userRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<BookingOutputDto> getAllBookingsFromUser(Long userId) throws RecordNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        List<Booking> userBookings = user.getBookings();
        List<BookingOutputDto> userBookingOutputDtos = new ArrayList<>();
        for (Booking b : userBookings) {
            BookingOutputDto userBookingOutputDto = transferBookingToBookingOutputDto(b);
            userBookingOutputDtos.add(userBookingOutputDto);
        }
        return userBookingOutputDtos;
    }

    public List<BookingOutputDto> getAllBookingsFromWorkshop(Long workshopId) throws RecordNotFoundException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        List<Booking> workshopBookings = workshop.getWorkshopBookings();
        List<BookingOutputDto> workshopBookingOutputDtos = new ArrayList<>();
        for (Booking b : workshopBookings) {
            BookingOutputDto workshopBookingOutputDto = transferBookingToBookingOutputDto(b);
            workshopBookingOutputDtos.add(workshopBookingOutputDto);
        }
        return workshopBookingOutputDtos;
    }

    public BookingOutputDto getOneBookingById(Long bookingId) throws RecordNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("The booking with ID  " + bookingId + " doesn't exist."));

        return transferBookingToBookingOutputDto(booking);
    }

    public BookingOutputDto createBooking(Long customerId, Long workshopId, BookingInputDto bookingInputDto) throws RecordNotFoundException, NoAvailableSpotsException {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        if (workshop.getDate().isBefore(LocalDate.now())){
            throw new BadRequestException("This workshop takes place in the past, you can't book a workshop that has already taken place.");
        }
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));
        if (workshop.getAvailableSpotsWorkshop() < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Only " + (workshop.getAvailableSpotsWorkshop() + " spots are available for this workshop on this date and you're trying to book " + bookingInputDto.amount + " spots."));
        }
        Booking booking = transferBookingInputDtoToBooking(bookingInputDto);
        booking.setWorkshop(workshop);
        booking.setCustomer(customer);
        bookingRepository.save(booking);
        return transferBookingToBookingOutputDto(booking);
    }

    public BookingOutputDto updateBooking(Long bookingId, BookingInputDto bookingInputDto) throws RecordNotFoundException, NoAvailableSpotsException, BadRequestException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("The booking with ID  " + bookingId + " doesn't exist."));
        if (bookingInputDto.workshopId == null) {
            throw new BadRequestException("You can't update a booking without connecting it to a workshop.");
        }
        Workshop workshop = workshopRepository.findById(bookingInputDto.workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + bookingInputDto.workshopId + " doesn't exist."));
        if (workshop.getAvailableSpotsWorkshop() < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Only " + (workshop.getAvailableSpotsWorkshop() + " spots are available for this workshop on this date and you're trying to book " + bookingInputDto.amount + " spots."));
        }
        booking.setAmount(bookingInputDto.amount);
        if (bookingInputDto.commentsCustomer != null) {
            booking.setCommentsCustomer(bookingInputDto.commentsCustomer);
        }
        booking.setWorkshop(workshop);
        //customer can't be updated, customer is always leading
        bookingRepository.save(booking);
        return transferBookingToBookingOutputDto(booking);
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("The booking with ID  " + bookingId + " doesn't exist."));
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

        // calling a service from another service might nog be best practice
        bookingOutputDto.customerOutputDto = userService.transferUserToCustomerOutputDto(booking.getCustomer());

        return bookingOutputDto;
    }


    public Booking transferBookingInputDtoToBooking(BookingInputDto bookingInputDto) {
        Booking booking = new Booking();
        booking.setDateOrder(LocalDate.now());
        booking.setCommentsCustomer(bookingInputDto.commentsCustomer);
        booking.setAmount(bookingInputDto.amount);

        return booking;
    }

}
