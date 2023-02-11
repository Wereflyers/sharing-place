package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Entity
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "requestor_id")
    Long userId;
    String description;
    @DateTimeFormat
    LocalDateTime created;
    Boolean added;
}
