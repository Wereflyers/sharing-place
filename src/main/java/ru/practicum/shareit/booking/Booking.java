package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @NonNull
    @Positive
    Long userId;
    @NonNull
    @Positive
    Long itemId;
    LocalDate fromDate;
    LocalDate tillDate;
    BookingStatus status;
}
