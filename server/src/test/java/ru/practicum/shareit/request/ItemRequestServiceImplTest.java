package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestForResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("request")
                .userId(1L)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void add_whenOK_thenReturnRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findAllByRequestId(1L)).thenReturn(new ArrayList<>());
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        ItemRequestForResponse result = itemRequestService.add(1L, ItemRequestMapper.toItemRequestDto(itemRequest));

        assertEquals(result.getId(), 1L);
        assertEquals(result.getUserId(), 1L);
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertFalse(result.getAdded());
    }

    @Test
    void add_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemRequestService.add(1L, ItemRequestMapper.toItemRequestDto(itemRequest)));
        verify(itemRequestRepository, never()).save(any());
    }

    @Test
    void get_whenOK_thanReturnRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findAllByRequestId(1L)).thenReturn(List.of(new Item()));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestForResponse result = itemRequestService.get(1L, 1L);

        assertEquals(result.getId(), 1L);
        assertEquals(result.getUserId(), 1L);
        assertEquals(result.getDescription(), itemRequest.getDescription());
        assertTrue(result.getAdded());
    }

    @Test
    void get_whenUserNorFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemRequestService.get(1L, 1L));
    }

    @Test
    void get_whenRequestNotFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemRequestService.get(1L, 1L));
    }

    @Test
    void getRequests_whenEmpty_thanReturnEmptyList() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAllByUserIdOrderByCreated(1L)).thenReturn(new ArrayList<>());

        List<ItemRequestForResponse> result = itemRequestService.getRequests(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getRequests_whenOK_thanReturnList() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAllByUserIdOrderByCreated(1L)).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(new ArrayList<>());

        List<ItemRequestForResponse> result = itemRequestService.getRequests(1L);

        assertFalse(result.isEmpty());
    }

    @Test
    void getRequests_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemRequestService.getRequests(1L));
    }

    @Test
    void getAll_whenOK_thenReturnRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findAll(PageRequest.of(1, 1))).thenReturn(Page.empty());

        List<ItemRequestForResponse> result = itemRequestService.getAll(1L, 1, 1);

        assertTrue(result.isEmpty());
        verify(itemRequestRepository).findAll(PageRequest.of(1, 1));
    }

    @Test
    void getAll_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemRequestService.getAll(1L, 0, 10));
    }

    @Test
    void getAll_whenWrongParameters_thenThrowException() {
        assertThrows(NullPointerException.class, () -> itemRequestService.getAll(1L, -1, 10));
        assertThrows(NullPointerException.class, () -> itemRequestService.getAll(1L, 1, 0));
    }
}