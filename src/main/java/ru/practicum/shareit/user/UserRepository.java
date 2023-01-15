package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {

    /**
     * Вывод списка пользователей.
     *
     * @return List
     */
    List<UserDto> getAll();

    /**
     * Добавление пользователя.
     *
     * @param userDto - описание профиля
     * @return User
     */
    User add(UserDto userDto);

    /**
     * Получение информации о пользователе по идентификатору.
     *
     * @param id - идентификатор пользователя
     * @return User
     */
    User get(long id);

    /**
     * Обновление информации о пользователе.
     *
     * @param userDto - описание профиля
     * @return User
     */
    User update(UserDto userDto);

    /**
     * Удаление пользователя.
     *
     * @param id - идентификатор пользователя
     * @return boolean
     */
    boolean delete(long id);
}
