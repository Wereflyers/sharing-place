package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @InjectMocks
    private BookingController bookingController;
    @Mock
    private BookingService bookingService;
    private final long userId = 1L;
    private BookingDto booking1;

    @BeforeEach
    void create() {
        booking1 = new BookingDto(null, 1L, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.WAITING, 1L);
    }

    @Test
    void save() {
        BookingForResponse bookingForResponse = new BookingForResponse();

        when(bookingService.create(anyLong(), any())).thenReturn(bookingForResponse);

        BookingForResponse response = bookingController.save(userId, booking1);

        verify(bookingService).create(userId, booking1);
        assertEquals(bookingForResponse, response);
    }

    @Test
    void approveOrReject() {
        BookingForResponse bookingForResponse = new BookingForResponse();

        when(bookingService.approveOrReject(userId, 1L, true)).thenReturn(bookingForResponse);

        BookingForResponse response = bookingController.approveOrReject(userId, 1L, true);

        verify(bookingService).approveOrReject(userId, 1L, true);
        assertEquals(bookingForResponse, response);
    }

    @Test
    void get() {
        BookingForResponse expectedResponse = new BookingForResponse();

        when(bookingService.get(anyLong(), anyLong())).thenReturn(expectedResponse);

        BookingForResponse response = bookingController.get(userId, 1L);

        verify(bookingService).get(userId, 1L);
        assertEquals(expectedResponse, response);
    }

    @Test
    void getAll_whenRightState_thenReturnList() {
        BookingForResponse currentBooking = new BookingForResponse();

        when(bookingService.getAll(userId, State.CURRENT, 0, 1)).thenReturn(List.of(currentBooking));

        List<BookingForResponse> current = bookingController.getAll(userId, "CURRENT", 0, 1);

        assertEquals(current, List.of(currentBooking));
        verify(bookingService, times(1)).getAll(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void getAll_whenWrongState_thenThrowException() {
        verify(bookingService, never()).getAll(anyLong(), any(), anyInt(), anyInt());
        assertThrows(IllegalArgumentException.class, () -> bookingController.getAll(userId, "IOI", 1, 1), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    void getAllForItems() {
        List<BookingForResponse> bookingsForResponse = List.of(new BookingForResponse());

        when(bookingService.getAllForItems(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookingsForResponse);

        List<BookingForResponse> response = bookingController.getAllForItems(userId, "ALL", 0, 1);

        assertEquals(bookingsForResponse, response);
        verify(bookingService).getAllForItems(userId, State.ALL, 0, 1);
    }

    @Test
    void getAllForItems_whenWrongState_thenThrowException() {
        verify(bookingService, never()).getAllForItems(anyLong(), any(), anyInt(), anyInt());
        assertThrows(IllegalArgumentException.class, () -> bookingController.getAllForItems(userId, "IOI", 1, 1));
    }
}