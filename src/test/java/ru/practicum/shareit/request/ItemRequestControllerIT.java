package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequest;
    private ItemRequestForResponse response;
    private final long userId = 1L;

    @BeforeEach
    public void setUp() {
        itemRequest = ItemRequestDto.builder()
                .description("text")
                .build();
        response = ItemRequestForResponse.builder()
                .id(1L)
                .userId(userId)
                .description("some text")
                .added(false)
                .build();
    }

    @SneakyThrows
    @Test
    void addRequest() {
        response = ItemRequestMapper.toResponse(ItemRequestMapper.addItemRequest(itemRequest, userId), false, null);

        when(itemRequestService.add(anyLong(), any())).thenReturn(response);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(response), result);
    }

    @SneakyThrows
    @Test
    void getRequests() {
        mockMvc.perform(get("/requests", userId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemRequestService).getRequests(userId);
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(itemRequestService.getAll(userId, 0, 10)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(itemRequestService).getAll(userId, 0, 10);
    }

    @SneakyThrows
    @Test
    void get_ItemRequest() {
        when(itemRequestService.get(userId, 1L)).thenReturn(response);

        String result = mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService).get(userId, 1L);
        assertEquals(result, objectMapper.writeValueAsString(response));
    }
}