package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserRepositoryImpl {
/*
    @Override
    public User add(UserDto userDto) {
        userDto.setId(id);
        User user = UserMapper.toUser(userDto);
        for (User u:users.values()) {
            if (user.getEmail().equals(u.getEmail()))
                throw new DuplicateException("Email is not valid");
        }
        users.put(id, user);
        id = id + 1;
        return user;
    }

    @Override
    public User update(UserDto userDto) {
        User user = users.get(userDto.getId());
        if (userDto.getName() != null)
            user.setName(userDto.getName());
        if (userDto.getEmail() != null) {
            for (User u:users.values()) {
                if (userDto.getEmail().equals(u.getEmail()))
                    throw new DuplicateException("Email is not valid");
            }
            user.setEmail(userDto.getEmail());
        }
        users.remove(user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }*/
}
