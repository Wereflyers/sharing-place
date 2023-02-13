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
        BookingForResponse pastBooking = new BookingForResponse();
        BookingForResponse futureBooking = new BookingForResponse();
        BookingForResponse waitingBooking = new BookingForResponse();
        BookingForResponse rejectedBooking = new BookingForResponse();
        List<BookingForResponse> allBookings = List.of(currentBooking, pastBooking, futureBooking, waitingBooking, rejectedBooking);

        when(bookingService.getAll(userId, State.ALL, 0, 1)).thenReturn(allBookings);
        when(bookingService.getAll(userId, State.CURRENT, 0, 1)).thenReturn(List.of(currentBooking));
        when(bookingService.getAll(userId, State.PAST, 0, 1)).thenReturn(List.of(pastBooking));
        when(bookingService.getAll(userId, State.FUTURE, 0, 1)).thenReturn(List.of(futureBooking));
        when(bookingService.getAll(userId, State.WAITING, 0, 1)).thenReturn(List.of(waitingBooking));
        when(bookingService.getAll(userId, State.REJECTED, 0, 1)).thenReturn(List.of(rejectedBooking));

        List<BookingForResponse> all = bookingController.getAll(userId, "ALL", 0, 1);
        List<BookingForResponse> current = bookingController.getAll(userId, "CURRENT", 0, 1);
        List<BookingForResponse> past = bookingController.getAll(userId, "PAST", 0, 1);
        List<BookingForResponse> future = bookingController.getAll(userId, "FUTURE", 0, 1);
        List<BookingForResponse> waiting = bookingController.getAll(userId, "WAITING", 0, 1);
        List<BookingForResponse> rejected = bookingController.getAll(userId, "REJECTED", 0, 1);

        assertEquals(all, allBookings);
        assertEquals(current, List.of(currentBooking));
        assertEquals(past, List.of(pastBooking));
        assertEquals(future, List.of(futureBooking));
        assertEquals(waiting, List.of(waitingBooking));
        assertEquals(rejected, List.of(rejectedBooking));
        verify(bookingService, times(6)).getAll(anyLong(), any(), anyInt(), anyInt());

        //TODO separately is better
    }

    @Test
    void getAllForItems() {
        List<BookingForResponse> bookingsForResponse = List.of(new BookingForResponse());

        when(bookingService.getAllForItems(anyLong(), any(), anyInt(), anyInt())).thenReturn(bookingsForResponse);

        List<BookingForResponse> response = bookingController.getAllForItems(userId, "ALL", 0, 1);

        assertEquals(bookingsForResponse, response);
        verify(bookingService).getAllForItems(userId, State.ALL, 0, 1);
    }
}