package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDto save(Long userId, BookingDto bookingDto) {
        bookingDto.setOwnerId(userId);
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto)));
    }

    @Override
    public BookingDto approveOrReject(Long userId, Long bookingId, Boolean approved) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NullPointerException("Booking " + bookingId + " is not exist.");
        }
        if (!Objects.equals(bookingRepository.findById(bookingId).get().getUserId(), userId)) {
            throw new NullPointerException("You don't have rights for this.");
        }
        return null;
    }

    @Override
    public BookingDto get(Long userId, Long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NullPointerException("Booking " + bookingId + " is not exist.");
        }
        if (!Objects.equals(bookingRepository.findById(bookingId).get().getUserId(), userId)) {
            throw new NullPointerException("You don't have rights for this.");
        }
        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
    }

    @Override
    public List<BookingDto> getAll(Long userId, State state) {
        if (bookingRepository.findAllByOwnerIdOrderByFromDate(userId) == null) {
            return new ArrayList<>();
        }
        switch (state) {
            case ALL:
                return BookingMapper.toBookingDtoList(bookingRepository.findAllByOwnerIdOrderByFromDate(userId));
            case PAST:
                return BookingMapper.toBookingDtoList
                        (bookingRepository.findAllByOwnerIdAndTillDateIsBeforeOrderByFromDate(userId, LocalDate.now()));
            case FUTURE:
                return BookingMapper.toBookingDtoList
                        (bookingRepository.findAllByOwnerIdAndFromDateIsAfterOrderByFromDate(userId, LocalDate.now()));
            case CURRENT:
                return BookingMapper.toBookingDtoList(
                        bookingRepository.findAllByOwnerIdAndFromDateIsBeforeAndTillDateIsAfterOrderByFromDate
                                (userId, LocalDate.now(), LocalDate.now()));
            case WAITING:
                return BookingMapper.toBookingDtoList(
                        bookingRepository.findAllByOwnerIdAndStatusOrderByFromDate(userId, BookingStatus.WAITING));
            case REJECTED:
                return BookingMapper.toBookingDtoList(
                        bookingRepository.findAllByOwnerIdAndStatusOrderByFromDate(userId, BookingStatus.REJECTED));
            default:
                throw new IllegalArgumentException("State doesn't exist.");
        }
    }

    @Override
    public List<BookingDto> getAllForItems(Long userId, State state) {
        return null;
    }
}
