package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    @Positive
    Long bookingId;
    @Positive
    Long userId;
    @NonNull
    @Positive
    Long itemId;
    Long ownerId;
    LocalDate fromDate;
    LocalDate tillDate;
    BookingStatus status;
}
