package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "user_id")
    Long bookerId;
    @Column(name = "item_id")
    Long itemId;
    @Column(name = "owner_id")
    Long ownerId;
    @Column(name = "start_time")
    @DateTimeFormat
    LocalDateTime start;
    @Column(name = "end_time")
    @DateTimeFormat
    LocalDateTime end;
    @Enumerated
    BookingStatus status;
}
