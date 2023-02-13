package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingForResponse {
    Long id;
    @FutureOrPresent
    LocalDateTime start;
    @Future
    LocalDateTime end;
    BookingStatus status;
    ItemShort item;
    UserShort booker;
}
