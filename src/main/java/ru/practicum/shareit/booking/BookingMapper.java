package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .itemId(booking.getItemId())
                .userId(booking.getUserId())
                .fromDate(booking.getFromDate())
                .tillDate(booking.getTillDate())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .itemId(bookingDto.getItemId())
                .userId(bookingDto.getUserId())
                .fromDate(bookingDto.getFromDate())
                .tillDate(bookingDto.getTillDate())
                .status(bookingDto.getStatus())
                .build();
    }
}
