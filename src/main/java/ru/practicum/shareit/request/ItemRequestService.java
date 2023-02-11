package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;

import java.util.List;

public interface ItemRequestService {
    /**
     * Создать запрос.
     * @param userId - пользователь
     * @param itemRequestDto - тело запроса
     * @return ItemRequestForResponse
     */
    ItemRequestForResponse add(long userId, ItemRequestDto itemRequestDto);

    /**
     * Получить запрос по идентификатору, доступно всем пользователям.
     * @param userId - пользователь
     * @param id - номер запроса
     * @return ItemRequestForResponse
     */
    ItemRequestForResponse get(long userId, long id);

    /**
     * Получить список своих запросов.
     * @param userId - хозяин запросов
     * @return List
     */
    List<ItemRequestForResponse> getRequests(long userId);

    /**
     * Получить список запросов, созданных другими пользователями, от более новых к более старым.
     * @param userId - пользователь
     * @param from - индекс первого элемента
     * @param size - количество элементов для отображения
     * @return List
     */
    List<ItemRequestForResponse> getAll(long userId, int from, int size);
}
