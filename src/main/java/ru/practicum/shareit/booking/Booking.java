package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "booking_id")
    Long bookingId;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "item_id")
    Long itemId;
    @Column(name = "owner_id")
    Long ownerId;
    @Column(name = "from_date")
    LocalDate fromDate;
    @Column(name = "till_date")
    LocalDate tillDate;
    @Enumerated
    BookingStatus status;
}
