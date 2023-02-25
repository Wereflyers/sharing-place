package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private final long userId = 1L;
    private BookingForResponse response;
    private final BookingDto bookingDto = new BookingDto();

    @BeforeEach
    public void setUp() {
        response = BookingForResponse.builder()
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.now())
                .build();
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setBookerId(1L);
        bookingDto.setStart(LocalDateTime.of(2023,5,5,5,5));
        bookingDto.setEnd(LocalDateTime.MAX);
        bookingDto.setItemId(1L);
    }

    @SneakyThrows
    @Test
    void save() {
        when(bookingService.create(anyLong(), any())).thenReturn(response);
        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    @Test
    void approveOrReject() {
        when(bookingService.approveOrReject(userId, 1L, true)).thenReturn(response);

        String result = mockMvc.perform(patch("/bookings/{id}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    @Test
    void get_Booking() {
        when(bookingService.get(userId, 1L)).thenReturn(response);

        String result = mockMvc.perform(get("/bookings/{id}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(response));
    }

    @SneakyThrows
    @Test
    void getAll() {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingService).getAll(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllForItems() {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(bookingService).getAllForItems(anyLong(), any(), anyInt(), anyInt());
    }
}