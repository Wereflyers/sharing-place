package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> search(String req);

    List<ItemDto> getAllForUser(long userId);

    ItemDto add(long userId, ItemDto itemDto);

    ItemDto get(long id);

    ItemDto update(long userId, long id, ItemDto itemDto);

    void delete(long userId, long id);
}
