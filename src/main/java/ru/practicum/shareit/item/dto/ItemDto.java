package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
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
