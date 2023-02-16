package ru.practicum.shareit.request;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .userId(itemRequest.getUserId())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest addItemRequest(ItemRequestDto itemRequestDto, long userId) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .userId(userId)
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestForResponse toResponse(ItemRequest itemRequest, boolean added, List<ItemDto> items) {
        return ItemRequestForResponse.builder()
                .id(itemRequest.getId())
                .userId(itemRequest.getUserId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .added(added)
                .items(items)
                .build();
    }
}
