package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> getAllForUser(long userId);
    List<ItemDto> search(String req);
    Item add(ItemDto itemDto);
    Item get(long id);
    Item update(ItemDto itemDto);
    boolean delete(long id);
}
