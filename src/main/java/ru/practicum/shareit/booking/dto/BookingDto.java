package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@AllArgsConstructor
@Getter
@Setter
public class BookingDto {
    @NonNull
    @Positive
    Long userId;
    @NonNull
    @Positive
    Long itemId;
    LocalDate fromDate;
    LocalDate tillDate;
    Boolean approved;
}
