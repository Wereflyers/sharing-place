package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    private final long itemId = 1L;

    @SneakyThrows
    @Test
    void getAll() {
        mockMvc.perform(get("/items"))
                        .andExpect(status().isOk());

        verify(itemService).getAllForUser(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItem() {
        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk());

        verify(itemService).get(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void add() {
    }

    @SneakyThrows
    @Test
    void update() {
    }

    @SneakyThrows
    @Test
    void delete() {
    }

    @SneakyThrows
    @Test
    void search() {
    }

    @SneakyThrows
    @Test
    void addComment() {
    }
}