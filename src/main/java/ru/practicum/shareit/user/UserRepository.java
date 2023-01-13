package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {

    List<UserDto> getAll();

    User add(UserDto userDto);

    User get(long id);

    User update(UserDto userDto);

    boolean delete(long id);
}
