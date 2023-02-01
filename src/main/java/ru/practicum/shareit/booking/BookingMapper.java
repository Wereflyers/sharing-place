package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItemId())
                .start(booking.getStart())
                .bookerId(booking.getBookerId())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static BookingForResponse toBookingForResponse(Booking booking, ItemDto itemDto) {
        return BookingForResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .booker(new UserShort(booking.getBookerId()))
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemShort(itemDto.getId(), itemDto.getName()))
                .build();
    }
    public static Booking toBooking(BookingDto bookingDto, Long id, Long userId, Long ownerId) {
        return Booking.builder()
                .id(id)
                .itemId(bookingDto.getItemId())
                .bookerId(userId)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .ownerId(ownerId)
                .status(bookingDto.getStatus())
                .build();
    }

    public static Booking toBookingWithoutId(BookingDto bookingDto, Long userId, Long ownerId) {
        return Booking.builder()
                .itemId(bookingDto.getItemId())
                .bookerId(userId)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .ownerId(ownerId)
                .status(bookingDto.getStatus())
                .build();
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
