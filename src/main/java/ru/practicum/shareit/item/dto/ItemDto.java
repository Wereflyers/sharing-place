package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @Positive
    Long id;
    String name;
    String description;
    Boolean available;
    @Positive
    Long ownerId;
    Long rentTimes;
}
