package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @InjectMocks
    private ItemRequestController itemRequestController;
    @Mock
    private ItemRequestService itemRequestService;
    private ItemRequestForResponse itemRequestForResponse;

    @BeforeEach
    void setUp() {
        itemRequestForResponse = ItemRequestForResponse.builder()
                .description("big")
                .added(false)
                .id(1L)
                .build();
    }

    @Test
    void addRequest() {
        when(itemRequestService.add(anyLong(), any())).thenReturn(itemRequestForResponse);

        ItemRequestForResponse response = itemRequestController.addRequest(1L, new ItemRequestDto());

        assertEquals(response, itemRequestForResponse);
        verify(itemRequestService).add(anyLong(), any());
    }

    @Test
    void getRequests() {
        when(itemRequestService.getRequests(1L)).thenReturn(List.of(itemRequestForResponse));

        List<ItemRequestForResponse> response = itemRequestController.getRequests(1L);

        assertEquals(response.size(), 1);
        assertEquals(response.get(0), itemRequestForResponse);
        verify(itemRequestService).getRequests(1L);
    }

    @Test
    void getAll() {
        when(itemRequestService.getAll(1L, 0, 1)).thenReturn(List.of(itemRequestForResponse));

        List<ItemRequestForResponse> response = itemRequestController.getAll(1L, 0, 1);

        assertEquals(response.size(), 1);
        assertEquals(response.get(0), itemRequestForResponse);
        verify(itemRequestService).getAll(1L, 0, 1);
    }

    @Test
    void get() {
        when(itemRequestService.get(1L, 1L)).thenReturn(itemRequestForResponse);

        ItemRequestForResponse response = itemRequestController.get(1L, 1L);

        assertEquals(response, itemRequestForResponse);
        verify(itemRequestService).get(1L, 1L);
    }
}