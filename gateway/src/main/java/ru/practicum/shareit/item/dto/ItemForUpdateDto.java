package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ItemForUpdateDto {
    String name;
    String description;
    Boolean available;
    Long requestId;
}
