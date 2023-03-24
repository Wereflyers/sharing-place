package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    private final long itemId = 1L;
    private final long userId = 1L;
    private final ItemDto itemDto = new ItemDto();
    private final Item item = new Item();

    @BeforeEach
    public void setUp() {
        itemDto.setName("coat");
        itemDto.setDescription("black");
        itemDto.setAvailable(false);
        item.setId(itemId);
        item.setName("ball");
        item.setDescription("big");
        item.setOwnerId(userId);
        item.setAvailable(true);
    }

    @Test
    void getAllForUser_whenOK_returnList() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemIdOrderByStart(itemId)).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItem(itemId)).thenReturn(new ArrayList<>());

        List<ItemForResponse> result = itemService.getAllForUser(userId, 0, 10);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), itemId);
        assertEquals(result.get(0).getName(), item.getName());
    }

    @Test
    void getAllForUser_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemService.getAllForUser(userId, 0, 1));
    }

    @Test
    void get_whenOK_thenReturnItem() {
        Booking lastBooking = Booking.builder()
                .start(LocalDateTime.of(2022, 1, 1, 1, 1))
                .end(LocalDateTime.of(2022, 3, 3, 3, 3))
                .status(BookingStatus.APPROVED)
                .itemId(itemId)
                .bookerId(1L)
                .build();
        Booking nextBooking = Booking.builder()
                .start(LocalDateTime.of(2024, 1, 1, 1, 1))
                .end(LocalDateTime.of(2024, 3, 3, 3, 3))
                .status(BookingStatus.APPROVED)
                .itemId(itemId)
                .bookerId(2L)
                .build();

        when(bookingRepository.findAllByItemIdOrderByStart(itemId)).thenReturn(List.of(nextBooking, lastBooking));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItem(itemId)).thenReturn(new ArrayList<>());

        ItemForResponse result = itemService.get(itemId, userId);

        assertEquals(result.getId(), itemId);
        assertEquals(result.getName(), item.getName());
        assertEquals(result.getLastBooking().getBookerId(), 1L);
        assertEquals(result.getNextBooking().getBookerId(), 2L);
    }

    @Test
    void get_whenItemNotFound_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemService.get(itemId, userId));
    }

    @Test
    void add_whenOK_thenReturnItem() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByItemIdOrderByStart(itemId)).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItem(itemId)).thenReturn(new ArrayList<>());
        when(itemRepository.save(any())).thenReturn(item);

        ItemForResponse result = itemService.add(userId, ItemMapper.toItemDto(item));

        assertEquals(result.getId(), itemId);
        assertEquals(result.getName(), item.getName());
    }

    @Test
    void add_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemService.add(userId, itemDto));
    }

    @Test
    void update_whenOK_thenReturnItem() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdOrderByStart(itemId)).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItem(itemId)).thenReturn(new ArrayList<>());
        when(itemRepository.save(any())).thenReturn(item);

        itemService.update(userId, itemId, itemDto);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(savedItem.getId(), itemId);
        assertEquals(savedItem.getName(), itemDto.getName());
        assertEquals(savedItem.getDescription(), itemDto.getDescription());
        assertEquals(savedItem.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void update_whenItemNotFound_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemService.update(userId, itemId, itemDto));
    }

    @Test
    void update_whenWrongUser_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(NullPointerException.class, () -> itemService.update(2L, itemId, itemDto));
    }

    @Test
    void delete_whenOK() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.delete(userId, itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    void delete_whenItemNotFound_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> itemService.delete(userId, itemId));
    }

    @Test
    void delete_whenWrongUser_thenThrowException() {
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(NullPointerException.class, () -> itemService.delete(2L, itemId));
    }

    @Test
    void search_whenOK_thenReturnItem() {
        when(itemRepository.search(anyString(), any())).thenReturn(new ArrayList<>());

        itemService.search("r", 1, 10);

        verify(itemRepository).search("r", PageRequest.of(1 / 10, 10));
    }

    @Test
    void search_whenEmpty_thenReturnEmptyList() {
        List<ItemForResponse> result = itemService.search(" ", 1, 10);

        verify(itemRepository, never()).search(anyString(), any());
        assertTrue(result.isEmpty());
    }

    @Test
    void addComment_whenOK_thenReturnComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");
        Booking booking = new Booking();
        booking.setBookerId(userId);
        booking.setItemId(itemId);
        booking.setId(1L);
        booking.setStart(LocalDateTime.of(2021,1,1,1,1));
        booking.setEnd(LocalDateTime.of(2022,1,1,1,1));

        when(bookingRepository.findByItemIdAndBookerId(itemId, userId)).thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenReturn(CommentMapper.toComment(commentDto, userId, itemId));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        CommentForResponse result = itemService.addComment(userId, itemId, commentDto);

        assertEquals(result.getText(), CommentMapper.toComment(commentDto, userId, itemId).getText());
    }

    @Test
    void addComment_whenEmpty_thenReturnException() {
        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, new CommentDto()));

        CommentDto emptyComment = new CommentDto();
        emptyComment.setText(" ");
        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, emptyComment));
    }

    @Test
    void addComment_whenNoRights_thanThrowException() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");

        when(bookingRepository.findByItemIdAndBookerId(itemId, userId)).thenReturn(new ArrayList<>());

        assertThrows(ValidationException.class, () -> itemService.addComment(userId, itemId, commentDto));
    }
}