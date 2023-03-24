package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;


    @SneakyThrows
    @Test
    void getAll() {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService).getAll();
    }

    @SneakyThrows
    @Test
    void get_UserById() {
        long userId = 1L;
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).get(userId);
    }

    @SneakyThrows
    @Test
    void add() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("boris");
        userToCreate.setEmail("user@user.com");

        when(userService.add(any())). thenReturn(userToCreate);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToCreate), result);
    }

    /*@SneakyThrows
    @Test
    void add_whenUserNotValid_thenReturnBadRequest() {
        long userId = 1L;
        UserDto userToUpdate = new UserDto();
        userToUpdate.setEmail("sara");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).update(userId, userToUpdate);
    }*/

    @SneakyThrows
    @Test
    void update() {
        long userId = 1L;
        UserDto userToCreate = new UserDto();
        userToCreate.setName("boris");
        userToCreate.setEmail("user@user.com");

        when(userService.update(anyLong(), any())). thenReturn(userToCreate);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToCreate), result);
        verify(userService).update(anyLong(), any());
    }

    //@SneakyThrows
    /*@Test
    void update_whenUserNotValid_thenReturnBadRequest() {
        long userId = 1L;
        UserDto userToUpdate = new UserDto();
        userToUpdate.setEmail("sara");

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).update(userId, userToUpdate);
    }*/
}