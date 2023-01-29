package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto save(Long userId, BookingDto bookingDto);

    BookingDto approveOrReject(Long userId, Long bookingId, Boolean approved);

    BookingDto get(Long userId, Long bookingId);

    List<BookingDto> getAll(Long userId, State state);

    List<BookingDto> getAllForItems(Long userId, State state);

}
