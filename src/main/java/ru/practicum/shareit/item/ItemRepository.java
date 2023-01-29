package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    /**
     * Вывод списка всех вещей, которые принадлежат конкретному пользователю.
     *
     * @param userId - идентификатор пользователя
     * @return List
     */
    List<Item> findAllByOwnerId(long userId);
    List<Item> findByNameContainsIgnoreCase(String req);

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
     * Обновление информации о вещи (доступно только хозяину вещи).
     *
     * @param itemDto - описание вещи
     * @return Item
     */
    Item update(ItemDto itemDto);
}
