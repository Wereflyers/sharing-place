package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryIT {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void addBookings() {
        userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        userRepository.save(User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build());
        itemRepository.save(Item.builder()
                .ownerId(1L)
                .name("ball")
                .available(true)
                .description("red ball")
                .build());
        itemRepository.save(Item.builder()
                .ownerId(2L)
                .name("coat")
                .available(true)
                .description("blue coat")
                .build());
        bookingRepository.save(Booking.builder()
                .bookerId(2L)
                .ownerId(1L)
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .itemId(1L)
                .build());
        bookingRepository.save(Booking.builder()
                .bookerId(2L)
                .ownerId(2L)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.of(2024, 12, 1, 1, 1))
                .end(LocalDateTime.of(2024, 12, 2, 1, 1))
                .itemId(2L)
                .build());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L, PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 2);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(1).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 2L);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc_whenPageable() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L, PageRequest.of(1, 1));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 2L);
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(2L, BookingStatus.APPROVED, PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 2L);
    }

    @Test
    void findAllByOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(1L, PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 1L);
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(1L, BookingStatus.CANCELED, PageRequest.ofSize(10));

        assertTrue(bookings.isEmpty());

        bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(1L, BookingStatus.WAITING, PageRequest.ofSize(10));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 1L);
    }

    @Test
    void findAllByItemIdOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(1L);

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 1L);
    }

    @Test
    void findByItemIdAndBookerId() {
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerId(2L, 2L);

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), 2L);
        assertEquals(bookings.get(0).getItemId(), 2L);
    }

    @AfterEach
    public void delete() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}