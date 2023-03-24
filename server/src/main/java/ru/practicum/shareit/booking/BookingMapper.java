package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShort;

@UtilityClass
public class BookingMapper {

    public static BookingForResponse toBookingForResponse(Booking booking, Item item) {
        return BookingForResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .booker(new UserShort(booking.getBookerId()))
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemShort(item.getId(), item.getName()))
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
}
