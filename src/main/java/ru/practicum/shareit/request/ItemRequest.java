package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    Long id;
    Long userId;
    String name;
    LocalDateTime created;
    Boolean added;
}
