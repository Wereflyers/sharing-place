package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class BookingControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    @Test
    void save() {
    }

    @Test
    void approveOrReject() {
    }

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllForItems() {
    }
}