package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private UserDto user1;
    private UserDto user2;

    @BeforeEach
    public void create() {
        user1 = UserDto.builder()
                .email("john@mail.ru")
                .name("john")
                .build();
        user2 = UserDto.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build();
    }

    @Test
    void getAll_whenInvoked_thenOKWithUsersCollection() {
        List<UserDto> expectedUsers = List.of(user1, user2);
        when(userService.getAll()).thenReturn(expectedUsers);

        List<UserDto> response = userController.getAll();

        assertEquals(response, expectedUsers);
        verify(userService).getAll();
    }

    @Test
    void get_whenOK_thenReturnUser() {
        long userId = 1L;
        when(userService.get(userId)).thenReturn(user1);

        UserDto actualUser = userController.get(userId);

        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
        verify(userService).get(userId);
    }

    @Test
    void add_whenOK_thenReturnUser() {
        when(userService.add(user1)).thenReturn(user1);

        UserDto actualUser = userController.add(user1);

        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
        verify(userService).add(user1);
    }

    @Test
    void update_whenOK_thenReturnUpdated() {
        when(userService.update(1L, user1)).thenReturn(user1);

        UserDto actualUser = userController.update(1L, user1);

        assertEquals(user1.getName(), actualUser.getName());
        assertEquals(user1.getEmail(), actualUser.getEmail());
        verify(userService).update(1L, user1);
    }

    @Test
    void delete_whenOK() {
        userController.delete(1L);

        verify(userService).delete(1L);
    }
}