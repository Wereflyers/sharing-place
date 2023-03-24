package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.util.List;

public interface BookingService {

    BookingForResponse create(Long userId, BookingDto bookingDto);

    BookingForResponse approveOrReject(Long userId, Long id, Boolean approved);

    BookingForResponse get(Long userId, Long id);

    List<BookingForResponse> getAll(Long userId, State state, int from, int size);

    List<BookingForResponse> getAllForItems(Long userId, State state, int from, int size);

}
