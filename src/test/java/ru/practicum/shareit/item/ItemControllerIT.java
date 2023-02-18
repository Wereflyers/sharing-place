package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private final long userId = 1L;
    private ItemForResponse item;
    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        item = ItemForResponse.builder()
                .name("item")
                .id(itemId)
                .description("new")
                .lastBooking(new BookingShort(1L, 1L))
                .nextBooking(new BookingShort(2L, 2L))
                .build();
        itemDto = ItemDto.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(itemService.getAllForUser(userId, 0, 100)).thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                        .andExpect(status().isOk());

        verify(itemService).getAllForUser(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void get_Item() {
        when(itemService.get(itemId, userId)).thenReturn(item);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(item));
        verify(itemService).get(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void add() {
        item = ItemMapper.toItemForResponse(ItemMapper.toItem(itemDto, userId),null, null, new ArrayList<>());

        when(itemService.add(anyLong(), any())).thenReturn(item);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(item));
        verify(itemService).add(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update() {
        item = ItemMapper.toItemForResponse(ItemMapper.toItem(itemDto, userId),null, null, new ArrayList<>());

        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(item);

        String result = mockMvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(item));
        verify(itemService).update(anyLong(), anyLong(), any());
    }

    @SneakyThrows
    @Test
    void deleteItem() {
        mockMvc.perform(delete("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService).delete(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void search() {
        when(itemService.search("req", 0, 100)).thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", "req"))
                .andExpect(status().isOk());

        verify(itemService).search("req", 0, 100);
    }

    @SneakyThrows
    @Test
    void addComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        CommentForResponse commentForResponse = CommentForResponse.builder()
                .text("text")
                .build();

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(commentForResponse);

        String result = mockMvc.perform(post("/items/{id}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(result, objectMapper.writeValueAsString(commentForResponse));
    }
}