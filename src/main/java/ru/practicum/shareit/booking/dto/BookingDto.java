package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @NonNull
    Long itemId;
    @FutureOrPresent
    LocalDateTime start;
    @Future
    LocalDateTime end;
    BookingStatus status;
    Long bookerId;
}
