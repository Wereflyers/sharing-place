package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;

@Getter
@Setter
@AllArgsConstructor
public class BookingShort {
    private Long id;
    private Long bookerId;

    public BookingShort(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBookerId();
    }

    public BookingShort() {

    }
}
