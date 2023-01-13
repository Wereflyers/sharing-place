package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto get(long id) {
        if (userRepository.get(id) == null)
            throw new NullPointerException("User " + id + "is not found.");
        return userRepository.get(id).toUserDto();
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll();
    }

    @Override
    public UserDto add(UserDto user) {
        if (user.getEmail() == null)
            throw new ValidationException("Email is null");
        return userRepository.add(user).toUserDto();
    }

    @Override
    public UserDto update(long id, UserDto user) {
        if (userRepository.get(id) == null)
            throw new NullPointerException("User " + id + " is not found.");
        user.setId(id);
        return userRepository.update(user).toUserDto();
    }

    @Override
    public boolean delete(long id) {
        if (userRepository.get(id) == null)
            throw new NullPointerException("User " + id + "is not found.");
        return userRepository.delete(id);
    }
}
