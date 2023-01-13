package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long ownerId;
    Long rentTimes;

    public ItemDto toItemDto() {
        return ItemDto.builder()
                .id(getId())
                .ownerId(getOwnerId())
                .name(getName())
                .description(getDescription())
                .available(getAvailable())
                .rentTimes(getRentTimes())
                .build();
    }
}
