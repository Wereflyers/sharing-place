package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl {
/*
    @Override
    public Item add(ItemDto itemDto) {
        itemDto.setId(id);
        Item item = ItemMapper.toItem(itemDto);
        items.put(id, item);
        id = id + 1;
        return item;
    }

    @Override
    public Item update(ItemDto itemDto) {
        Item item = items.get(itemDto.getId());
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        items.remove(itemDto.getId());
        items.put(itemDto.getId(), item);
        return item;
    }

    @Override
    public List<ItemDto> search(String req) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(req.toLowerCase())
                || item.getDescription().toLowerCase().contains(req.toLowerCase()))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }*/
}
