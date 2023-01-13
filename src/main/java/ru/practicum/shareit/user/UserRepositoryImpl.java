package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private static long id = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<UserDto> getAll() {
        return users.values().stream()
                .map(User::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User add(UserDto userDto) {
        User user = User.builder()
                .id(id)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        for (User u:users.values()) {
            if(user.getEmail().equals(u.getEmail()))
                throw new DuplicateException("Email is not valid");
        }
        users.put(id, user);
        id = id + 1;
        return user;
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public User update(UserDto userDto) {
        User user = users.get(userDto.getId());
        if (userDto.getName() != null)
            user.setName(userDto.getName());
        if (userDto.getEmail() != null) {
            for (User u:users.values()) {
                if(userDto.getEmail().equals(u.getEmail()))
                    throw new DuplicateException("Email is not valid");
            }
            user.setEmail(userDto.getEmail());
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public boolean delete(long id) {
        return users.remove(id) != null;
    }
}
