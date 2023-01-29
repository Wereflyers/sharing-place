package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @Min(1)
    @Positive
    Long id;

    String name;
    String description;
    Boolean available;
    @Positive
    @NonNull
    Long ownerId;
    Long rentTimes;
}
