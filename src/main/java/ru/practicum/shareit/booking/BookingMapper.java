package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .bookingId(booking.getBookingId())
                .itemId(booking.getItemId())
                .userId(booking.getUserId())
                .fromDate(booking.getFromDate())
                .tillDate(booking.getTillDate())
                .ownerId(booking.getOwnerId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .bookingId(bookingDto.getBookingId())
                .itemId(bookingDto.getItemId())
                .userId(bookingDto.getUserId())
                .fromDate(bookingDto.getFromDate())
                .tillDate(bookingDto.getTillDate())
                .ownerId(bookingDto.getOwnerId())
                .status(bookingDto.getStatus())
                .build();
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
