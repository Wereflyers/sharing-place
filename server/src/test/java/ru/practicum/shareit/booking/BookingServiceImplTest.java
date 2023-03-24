package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private final long userId = 1L;
    private final BookingDto bookingDto = new BookingDto();
    private final Item itemForBooking = new Item();
    private final Booking booking = new Booking();

    @BeforeEach
    public void createBookings() {
        bookingDto.setBookerId(2L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.of(2024,1,1,1,1));
        bookingDto.setItemId(1L);
        itemForBooking.setAvailable(true);
        booking.setBookerId(2L);
        booking.setItemId(1L);
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.of(2024,1,1,1,1));
    }

    @Test
    void create_whenOK_thenReturnBooking() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L));

        BookingForResponse actualBooking = bookingService.create(userId, bookingDto);

        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
    }

    @Test
    void create_whenUserNotFound_thenThrowException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> bookingService.create(userId, bookingDto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenItemNotFound_thenThrowException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> bookingService.create(userId, bookingDto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenUserIsItemOwner_thenThrowException() {
        itemForBooking.setOwnerId(1L);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(NullPointerException.class, () -> bookingService.create(userId, bookingDto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenItemIsUnavailable_thenThrowException() {
        itemForBooking.setAvailable(false);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> bookingService.create(userId, bookingDto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void create_whenEndIsAfterStart_thenThrowException() {
        bookingDto.setEnd(LocalDateTime.of(2020,2,2,2,2,2));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(ValidationException.class, () -> bookingService.create(userId, bookingDto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approveOrReject_whenApprove_thenReturnBooking() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L));

        BookingForResponse actualBooking = bookingService.approveOrReject(userId, 1L, true);

        assertEquals(actualBooking.getStatus(), BookingStatus.APPROVED);
        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void approveOrReject_whenReject_thenReturnBooking() {
        when(bookingRepository.findById(any())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.save(any())).thenReturn(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L));

        BookingForResponse actualBooking = bookingService.approveOrReject(userId, 1L, false);

        assertEquals(actualBooking.getStatus(), BookingStatus.REJECTED);
        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void approveOrReject_whenNotOwner_thenThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 1L, 2L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(NullPointerException.class, () -> bookingService.approveOrReject(userId, 1L, true));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approveOrReject_whenUserNotFound_thenThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(NullPointerException.class, () -> bookingService.approveOrReject(userId, 1L, true));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approveOrReject_whenNotFound_thenThrowException() {
        assertThrows(NullPointerException.class, () -> bookingService.approveOrReject(userId, 1L, true));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void approveOrReject_whenAlreadyApproved_thenThrowException() {
        bookingDto.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 2L, 1L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> bookingService.approveOrReject(userId, 1L, true));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void get_whenOk_thenReturnBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 1L, 2L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));

        BookingForResponse actualBooking = bookingService.get(userId, 1L);

        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
    }

    @Test
    void get_whenNotOwnerOrBooker_thenThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(BookingMapper.toBookingWithoutId(bookingDto, 3L, 2L)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(NullPointerException.class, () -> bookingService.get(userId, 1L));
    }


    @Test
    void getAllPast_whenOK_returnAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.PAST, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFuture_whenOK_returnAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.FUTURE, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCurrent_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.CURRENT, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAll_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.ALL, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAllWithStatusWaiting_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStart(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.WAITING, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAllWithStatusRejected_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStart(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAll(userId, State.REJECTED, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAll_whenEmpty_returnEmptyList() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        List<BookingForResponse> result = bookingService.getAll(userId, State.REJECTED, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllForItems_whenEmpty_returnEmptyList() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        List<BookingForResponse> result = bookingService.getAll(userId, State.REJECTED, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllForItems_whenOK_returnAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any())).thenReturn(List.of(booking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.ALL, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
    }

    @Test
    void getAllForItemsPast_whenOK_returnAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.PAST, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllForItemsFuture_whenOK_returnAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.FUTURE, 0, 20);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllForItemsCurrent_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        //в списке только одно настоящее бронирование
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.CURRENT, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAllForItemsWithStatusWaiting_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByOwnerIdAndStatusOrderByStart(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.WAITING, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void getAllForItemsWithStatusRejected_whenOK_returnAll() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemForBooking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByOwnerIdAndStatusOrderByStart(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(booking));

        List<BookingForResponse> result = bookingService.getAllForItems(userId, State.REJECTED, 0, 20);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), 1);
        assertEquals(booking.getId(), result.get(0).getId());
    }

    @Test
    void validateForFindAll_whenUserNotFound_throwException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> bookingService.getAll(userId, State.ALL, 1, 2));
    }
}