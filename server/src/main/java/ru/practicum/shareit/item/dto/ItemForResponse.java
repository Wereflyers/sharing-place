package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemForResponse {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
    BookingShort lastBooking;
    BookingShort nextBooking;
    List<CommentForResponse> comments;
}
