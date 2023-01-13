package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> getAll();

    UserDto get(long id);

    UserDto update(long id, UserDto userDto);

    boolean delete(long id);
}
