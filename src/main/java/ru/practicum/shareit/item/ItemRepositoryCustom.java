package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepositoryCustom {

    /**
     * Обновление информации о вещи (доступно только хозяину вещи).
     *
     * @param item - описание вещи
     * @return Item
     */
    Item update(Item item);

}
