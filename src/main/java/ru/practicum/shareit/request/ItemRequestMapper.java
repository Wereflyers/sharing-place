package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .name(itemRequest.getName())
                .userId(itemRequest.getUserId())
                .created(itemRequest.getCreated())
                .added(itemRequest.getAdded())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .name(itemRequestDto.getName())
                .userId(itemRequestDto.getUserId())
                .created(itemRequestDto.getCreated())
                .added(itemRequestDto.getAdded())
                .build();
    }
}
