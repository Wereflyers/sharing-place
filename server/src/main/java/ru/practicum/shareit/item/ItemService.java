package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;

import java.util.List;

public interface ItemService {

    List<ItemForResponse> search(String req, int from, int size);

    List<ItemForResponse> getAllForUser(long userId, int from, int size);

    ItemForResponse add(long userId, ItemDto itemDto);

    ItemForResponse get(long id, long userId);

    ItemForResponse update(long userId, long id, ItemDto itemDto);

    ItemForResponse delete(long userId, long id);

    CommentForResponse addComment(long userId, long itemId, CommentDto commentDto);
}
