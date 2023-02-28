package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
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
        bookingDto.setStatus(BookingStatus.WAITING);
        Long ownerId = itemRepository.findById(bookingDto.getItemId()).get().getOwnerId();
        return createResponse(bookingRepository.save(BookingMapper.toBookingWithoutId(bookingDto, userId, ownerId)));
    }

    @Override
    @Transactional
    public BookingForResponse approveOrReject(Long userId, Long id, Boolean approved) {
        validateBooking(id, userId);
        Booking booking = bookingRepository.findById(id).get();
        if (!Objects.equals(booking.getOwnerId(), userId)) {
            throw new NullPointerException("You don't have rights for this.");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Already approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else
            booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
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
    public List<BookingForResponse> getAll(Long userId, State state, int from, int size) {
        PageRequest page = validateForFindAll(userId, from, size);
        if (bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page) == null) {
            return new ArrayList<>();
        }
        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED, page);
                break;
        }
        return bookings.stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingForResponse> getAllForItems(Long userId, State state, int from, int size) {
        PageRequest page = validateForFindAll(userId, from, size);
        if (bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page) == null) {
            return new ArrayList<>();
        }
        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page).stream()
                        .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(userId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED, page);
                break;
        }
        return bookings.stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private PageRequest validateForFindAll(Long userId, int from, int size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NullPointerException("User " + userId + " is not found");
        }
        return PageRequest.of(from / size, size);
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
