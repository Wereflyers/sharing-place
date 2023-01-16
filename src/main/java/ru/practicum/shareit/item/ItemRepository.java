package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    /**
     * Вывод списка всех вещей, которые принадлежат конкретному пользователю.
     *
     * @param userId - идентификатор пользователя
     * @return List
     */
    List<ItemDto> getAllForUser(long userId);

    /**
     * Поиск вещей, доступных для бронирования, по названию и описанию.
     *
     * @param req - текст поиска
     * @return List
     */
    List<ItemDto> search(String req);

    /**
     * Добавление вещи на сайт.
     *
     * @param itemDto - описание вещи
     * @return Item
     */
    Item add(ItemDto itemDto);

    /**
     * Вывод вещи по идентификатору.
     *
     * @param id - индентификатор вещи
     * @return Item
     */
    Item get(long id);

    /**
     * Обновление информации о вещи (доступно только хозяину вещи).
     *
     * @param itemDto - описание вещи
     * @return Item
     */
    Item update(ItemDto itemDto);

    /**
     * Удаление вещи с сайта (доступно только хозяину вещи).
     *
     * @param id - идентификатор вещи
     * @return boolean
     */
    boolean delete(long id);
}
