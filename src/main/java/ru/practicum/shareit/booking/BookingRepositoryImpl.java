package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
            default:
                throw new IllegalArgumentException("State doesn't exist.");
        }
    }

    @Override
    public List<Booking> getBookingsForItems(Long userId, State state) {
        switch (state) {
            case ALL:
                return bookingRepository.findAllByOwnerIdOrderByStartDesc(userId);
            case PAST:
                return bookingRepository.findAllByOwnerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByOwnerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByOwnerIdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByOwnerIdAndStatusOrderByStart(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByOwnerIdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
            default:
                throw new IllegalArgumentException("State doesn't exist.");
        }
    }

    @Override
    public Booking getLastItemBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStartIsBeforeOrderByStart(
                itemId, LocalDateTime.now());
        if (bookings.size() == 0) {
            return null;
        } else {
            return bookings.get(0);
        }
    }

    @Override
    public Booking getNextItemBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStartIsAfterOrderByStart(
                itemId, LocalDateTime.now());
        if (bookings.size() == 0) {
            return null;
        } else {
            return bookings.get(0);
        }
    }


}
