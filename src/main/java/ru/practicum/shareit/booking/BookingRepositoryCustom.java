package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingRepositoryCustom {
    List<Booking> getBookings(Long userId, State state);
}
