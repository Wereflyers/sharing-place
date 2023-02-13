package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class ItemControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    @Test
    void getAll() {
    }

    @Test
    void get() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void search() {
    }

    @Test
    void addComment() {
    }
}