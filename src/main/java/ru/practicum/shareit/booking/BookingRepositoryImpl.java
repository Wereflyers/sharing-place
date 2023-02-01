package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    private final BookingRepository bookingRepository;

    public BookingRepositoryImpl(@Lazy BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> getBookings(Long userId, State state) {
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStart(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStart(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStart(userId, LocalDateTime.now());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(
                        userId, LocalDateTime.now(), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
            default:
                throw new IllegalArgumentException("State doesn't exist.");
        }
    }
}
