package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto get(long id) {
        if (userRepository.findById(id).isEmpty())
            throw new NullPointerException("User " + id + "is not found.");
        return UserMapper.toUserDto(userRepository.findById(id).get());
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto add(UserDto userDto) {
        if (userDto.getEmail() == null)
            throw new ValidationException("Email is null");
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        if (userRepository.findById(id).isEmpty())
            throw new NullPointerException("User " + id + " is not found.");
        userDto.setId(id);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void delete(long id) {
        if (userRepository.findById(id).isEmpty())
            throw new NullPointerException("User " + id + "is not found.");
        userRepository.deleteById(id);
    }
}
