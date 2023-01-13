package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemRequest {
    Long id;
    Long userId;
    String name;
    Boolean added;
}
