package nl.workshophub.workshophubeindopdrachtbackend.services;

import nl.workshophub.workshophubeindopdrachtbackend.dtos.inputdtos.BookingInputDto;
import nl.workshophub.workshophubeindopdrachtbackend.dtos.outputdtos.BookingOutputDto;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.BadRequestException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.ForbiddenException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.NoAvailableSpotsException;
import nl.workshophub.workshophubeindopdrachtbackend.exceptions.RecordNotFoundException;
import nl.workshophub.workshophubeindopdrachtbackend.models.User;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.UserRepository;
import nl.workshophub.workshophubeindopdrachtbackend.models.Booking;
import nl.workshophub.workshophubeindopdrachtbackend.models.Workshop;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.BookingRepository;
import nl.workshophub.workshophubeindopdrachtbackend.repositories.WorkshopRepository;
import nl.workshophub.workshophubeindopdrachtbackend.util.CheckAuthorization;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;


    public BookingService(BookingRepository bookingRepository, WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
    }

    public List<BookingOutputDto> getAllBookingsFromUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + userId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view bookings from this user.");
        }
        List<Booking> userBookings = user.getBookings();
        List<BookingOutputDto> userBookingOutputDtos = new ArrayList<>();
        for (Booking b : userBookings) {
            BookingOutputDto userBookingOutputDto = transferBookingToBookingOutputDto(b);
            userBookingOutputDtos.add(userBookingOutputDto);
        }
        return userBookingOutputDtos;
    }

    public List<BookingOutputDto> getAllBookingsFromWorkshop(Long workshopId) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(workshop.getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view bookings from this workshop, since you're not the owner.");
        }
        List<Booking> workshopBookings = workshop.getWorkshopBookings();
        List<BookingOutputDto> workshopBookingOutputDtos = new ArrayList<>();
        for (Booking b : workshopBookings) {
            BookingOutputDto workshopBookingOutputDto = transferBookingToBookingOutputDto(b);
            workshopBookingOutputDtos.add(workshopBookingOutputDto);
        }
        return workshopBookingOutputDtos;
    }

    public List<BookingOutputDto> getAllBookingsFromWorkshopsFromWorkshopOwner(Long workshopOwnerId) {
        User user = userRepository.findById(workshopOwnerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + workshopOwnerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!CheckAuthorization.isAuthorized(user, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to view bookings from this user.");
        }
        List<Booking> workshopOwnerBookings = new ArrayList<>();
        List<Workshop> workshopOwnerWorkshops = workshopRepository.findByWorkshopOwnerId(workshopOwnerId);
        for (Workshop w : workshopOwnerWorkshops) {
            List<Booking> workshopBookings = w.getWorkshopBookings();
            for (Booking b : workshopBookings) {
                workshopOwnerBookings.add(b);
            }
        }
        List<BookingOutputDto> workshopOwnerBookingOutputDtos = new ArrayList<>();
        for (Booking b : workshopOwnerBookings) {
            BookingOutputDto userBookingOutputDto = transferBookingToBookingOutputDto(b);
            workshopOwnerBookingOutputDtos.add(userBookingOutputDto);
        }
        return workshopOwnerBookingOutputDtos;
    }

    public List<BookingOutputDto> getAllBookings() {
        List<Booking> allBookings = bookingRepository.findAll();

        List<BookingOutputDto> allBookingOutputDtos = new ArrayList<>();
        for (Booking b : allBookings) {
            BookingOutputDto workshopBookingOutputDto = transferBookingToBookingOutputDto(b);
            allBookingOutputDtos.add(workshopBookingOutputDto);
        }
        return allBookingOutputDtos;
    }

    public BookingOutputDto getOneBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("The booking with ID  " + bookingId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (CheckAuthorization.isAuthorized(booking.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName()) || CheckAuthorization.isAuthorized(booking.getWorkshop().getWorkshopOwner(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            return transferBookingToBookingOutputDto(booking);
        }
        throw new ForbiddenException("You're not allowed to view bookings from this user.");
    }

    public ByteArrayResource generateAndDownloadCsv() {
        List<Booking> bookings = bookingRepository.findAll();
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Booking ID,Date booking, Amount, First name customer, Last name customer, Email customer, Comments customer, Total Price, Workshop ID, Title workshop, Workshop date");

        for (Booking booking : bookings) {
            csvContent.append(System.lineSeparator())
                    .append(booking.getId()).append(",")
                    .append(booking.getDateOrder()).append(",")
                    .append(booking.getAmount()).append(",")
                    .append(booking.getCustomer().getFirstName()).append(",")
                    .append(booking.getCustomer().getLastName()).append(",")
                    .append(booking.getCustomer().getEmail()).append(",")
                    .append(booking.getCommentsCustomer()).append(",")
                    .append(booking.getTotalPrice()).append(",")
                    .append(booking.getWorkshop().getId()).append(",")
                    .append(booking.getWorkshop().getTitle()).append(",")
                    .append(booking.getWorkshop().getDate()).append(",");

        }

        try {
            FileWriter writer = new FileWriter("bookings.csv");
            writer.write(csvContent.toString());
            writer.close();

            byte[] content = csvContent.toString().getBytes();
            return new ByteArrayResource(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public BookingOutputDto createBooking(Long customerId, Long workshopId, BookingInputDto bookingInputDto) {
        Workshop workshop = workshopRepository.findById(workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + workshopId + " doesn't exist."));
        if (workshop.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("This workshop takes place in the past, you can't book a workshop that has already taken place.");
        }
        User customer = userRepository.findById(customerId).orElseThrow(() -> new RecordNotFoundException("The user with ID " + customerId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(customer, (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to create a booking from this user account.");
        }
        if (workshop.getAvailableSpotsWorkshop() < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Only " + (workshop.getAvailableSpotsWorkshop() + " spots are available for this workshop on this date and you're trying to book " + bookingInputDto.amount + " spots."));
        }
        Booking booking = transferBookingInputDtoToBooking(bookingInputDto);
        booking.setWorkshop(workshop);
        booking.setCustomer(customer);
        booking.setTotalPrice(bookingInputDto.amount * workshop.getPrice());
        bookingRepository.save(booking);
        return transferBookingToBookingOutputDto(booking);
    }

    public BookingOutputDto updateBooking(Long bookingId, BookingInputDto bookingInputDto) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RecordNotFoundException("The booking with ID  " + bookingId + " doesn't exist."));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(booking.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to update this booking.");
        }
        if (bookingInputDto.workshopId == null) {
            throw new BadRequestException("You can't update a booking without connecting it to a workshop.");
        }
        Workshop workshop = workshopRepository.findById(bookingInputDto.workshopId).orElseThrow(() -> new RecordNotFoundException("The workshop with ID " + bookingInputDto.workshopId + " doesn't exist."));
        if ((workshop.getAvailableSpotsWorkshop() + booking.getAmount()) < bookingInputDto.amount) {
            throw new NoAvailableSpotsException("Only " + (workshop.getAvailableSpotsWorkshop() + " spots are available for this workshop on this date and you're trying to book " + bookingInputDto.amount + " spots."));
        }
        booking.setAmount(bookingInputDto.amount);
        booking.setTotalPrice(bookingInputDto.amount * workshop.getPrice());
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!CheckAuthorization.isAuthorized(booking.getCustomer(), (Collection<GrantedAuthority>) authentication.getAuthorities(), authentication.getName())) {
            throw new ForbiddenException("You're not allowed to delete a booking from this user account.");
        }
        bookingRepository.delete(booking);
    }


    public BookingOutputDto transferBookingToBookingOutputDto(Booking booking) {
        BookingOutputDto bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.id = booking.getId();
        bookingOutputDto.dateOrder = booking.getDateOrder();
        bookingOutputDto.commentsCustomer = booking.getCommentsCustomer();
        bookingOutputDto.amount = booking.getAmount();
        bookingOutputDto.totalPrice = booking.getTotalPrice();
        bookingOutputDto.workshopId = booking.getWorkshop().getId();
        bookingOutputDto.workshopTitle = booking.getWorkshop().getTitle();
        bookingOutputDto.sppotsAvailableWorkshop = booking.getWorkshop().getAvailableSpotsWorkshop();

        bookingOutputDto.customerId = booking.getCustomer().getId();
        bookingOutputDto.firstNameCustomer = booking.getCustomer().getFirstName();
        bookingOutputDto.lastNameCustomer = booking.getCustomer().getLastName();
        bookingOutputDto.emailCustomer = booking.getCustomer().getEmail();


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
