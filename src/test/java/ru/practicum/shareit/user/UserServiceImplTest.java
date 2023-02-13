package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user1;
    private User user2;
    private final long userId = 1L;

    @BeforeEach
    public void create() {
        user1 = User.builder()
                .email("john@mail.ru")
                .name("john")
                .build();
        user2 = User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build();
    }

    @Test
    void get_whenFound_thenReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        UserDto actualUser = userService.get(userId);

        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
    }

    @Test
    void get_whenNotFound_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userService.get(userId));
        verify(userRepository, atMostOnce()).findById(userId);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> actualUsers = userService.getAll();

        assertEquals(actualUsers.size(), 2);
        assertEquals(actualUsers.get(0).getName(), user1.getName());
        assertEquals(actualUsers.get(0).getEmail(), user1.getEmail());
        assertEquals(actualUsers.get(1).getName(), user2.getName());
        assertEquals(actualUsers.get(1).getEmail(), user2.getEmail());
    }

    @Test
    void add_whenOK_thenReturnCreated() {
        when(userRepository.save(any())).thenReturn(user1);

        UserDto actualUser = userService.add(UserMapper.toUserDto(user1));

        verify(userRepository).save(any());
        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
    }

    @Test
    void add_whenDuplicatedEmail_thenThrowException() {
        when(userRepository.save(any())).thenThrow(DuplicateException.class);

        assertThrows(DuplicateException.class, () -> userService.add(UserMapper.toUserDto(user2)));
        verify(userRepository).save(any());
    }

    @Test
    void add_whenEmailNull_thenThrowException() {
        assertThrows(ValidationException.class, () -> userService.add(new UserDto()));

        verify(userRepository, never()).save(any());
    }

    @Test
    void update_whenOK_thenUpdateFields() {
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("Mike");
        oldUser.setEmail("Mike@mail.ru");

        UserDto newUser = new UserDto();
        newUser.setName("Sara");
        newUser.setEmail("sara@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.findAllByEmail(anyString())).thenReturn(Collections.emptyList());
        when(userRepository.save(any())).thenReturn(UserMapper.toUser(newUser,userId));

        UserDto actualUser = userService.update(userId, newUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(userId, savedUser.getId());
        assertEquals("Sara", savedUser.getName());
        assertEquals("sara@mail.ru", savedUser.getEmail());
    }

    @Test
    void update_whenNotFound_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userService.update(userId, new UserDto()));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_whenDuplicateEmail_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        when(userRepository.findAllByEmail(anyString())).thenReturn(List.of(user2.builder()
                .id(2L)
                .build()));

        assertThrows(DuplicateException.class, () -> userService.update(userId, UserMapper.toUserDto(user2)));
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_whenOK() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_whenNotFound_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userService.delete(userId));
    }
}