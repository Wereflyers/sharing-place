package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingForResponse create(Long userId, BookingDto bookingDto) {
        if (itemRepository.findById(bookingDto.getItemId()).isEmpty())
            throw new NullPointerException("Item " + bookingDto.getItemId() + " is not found");
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User " + userId + " is not found");
        }
        if (Objects.equals(itemRepository.findById(bookingDto.getItemId()).get().getOwnerId(), userId)) {
            throw new NullPointerException("Incorrect request");
        }
        if (!itemRepository.findById(bookingDto.getItemId()).get().getAvailable()) {
            throw new ValidationException("Item is unavailable.");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Date is incorrect");
        }
        if (bookingDto.getStatus() == null) {
            bookingDto.setStatus(BookingStatus.WAITING);
        }
        if (bookingRepository.getBookings(userId, State.CURRENT).size() > 0) {
            throw new NullPointerException("User is not available");
        }
        Long ownerId = itemRepository.findById(bookingDto.getItemId()).get().getOwnerId();
        return createResponse(bookingRepository.save(BookingMapper.toBookingWithoutId(bookingDto, userId, ownerId)));
    }

    @Override
    @Transactional
    public BookingForResponse approveOrReject(Long userId, Long id, Boolean approved) {
        validateBooking(id, userId);
        if (!Objects.equals(bookingRepository.findById(id).get().getOwnerId(), userId)) {
            throw new NullPointerException("You don't have rights for this.");
        }
        Booking booking = bookingRepository.findById(id).get();
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Already approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else
            booking.setStatus(BookingStatus.REJECTED);
        return createResponse(booking);
    }

    @Override
    public BookingForResponse get(Long userId, Long id) {
        validateBooking(id, userId);
        if (Objects.equals(bookingRepository.findById(id).get().getOwnerId(), userId) ||
                Objects.equals(bookingRepository.findById(id).get().getBookerId(), userId)) {
            return createResponse(bookingRepository.findById(id).get());
        } else {
            throw new NullPointerException("You don't have rights for this.");
        }
    }

    @Override
    public List<BookingForResponse> getAll(Long userId, State state) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User " + userId + " is not found");
        }
        if (bookingRepository.findAllByBookerIdOrderByStartDesc(userId) == null) {
            return new ArrayList<>();
        }
        List<Booking> bookings = bookingRepository.getBookings(userId, state);
        return bookings.stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingForResponse> getAllForItems(Long userId, State state) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User " + userId + " is not found");
        }
        if (bookingRepository.findAllByOwnerIdOrderByStartDesc(userId) == null) {
            return new ArrayList<>();
        }
        List<Booking> bookings = bookingRepository.getBookingsForItems(userId, state);
        return bookings.stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private void validateBooking(Long id, Long userId) {
        if (bookingRepository.findById(id).isEmpty()) {
            throw new NullPointerException("Booking " + id + " is not exist.");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User " + userId + " is not found");
        }
    }

    private BookingForResponse createResponse(Booking booking) {
        return BookingMapper.toBookingForResponse(booking,
                itemRepository.findById(booking.getItemId()).get());
    }
}
