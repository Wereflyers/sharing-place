package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingRepositoryCustom {

    List<Booking> getBookings(Long userId, State state);

    List<Booking> getBookingsForItems(Long userId, State state);

    Booking getLastItemBooking(Long itemId);

    Booking getNextItemBooking(Long itemId);
}
