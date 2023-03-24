package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryIntegrationTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void addBookings() {
        user1 = userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        user2 = userRepository.save(User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build());
        item1 = itemRepository.save(Item.builder()
                .ownerId(user1.getId())
                .name("ball")
                .available(true)
                .description("red ball")
                .build());
        item2 = itemRepository.save(Item.builder()
                .ownerId(user2.getId())
                .name("coat")
                .available(true)
                .description("blue coat")
                .build());
        bookingRepository.save(Booking.builder()
                .bookerId(user2.getId())
                .ownerId(user1.getId())
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .itemId(item1.getId())
                .build());
        bookingRepository.save(Booking.builder()
                .bookerId(user2.getId())
                .ownerId(user2.getId())
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.of(2024, 12, 1, 1, 1))
                .end(LocalDateTime.of(2024, 12, 2, 1, 1))
                .itemId(item2.getId())
                .build());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user2.getId(), PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 2);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(1).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item2.getId());
    }

    @Test
    void findAllByBookerIdOrderByStartDesc_whenPageable() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user2.getId(), PageRequest.of(1, 1));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item1.getId());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStart(user2.getId(), BookingStatus.APPROVED, PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item2.getId());
    }

    @Test
    void findAllByOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(user1.getId(), PageRequest.ofSize(20));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item1.getId());
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(user1.getId(), BookingStatus.CANCELED, PageRequest.ofSize(10));

        assertTrue(bookings.isEmpty());

        bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStart(user1.getId(), BookingStatus.WAITING, PageRequest.ofSize(10));

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item1.getId());
    }

    @Test
    void findAllByItemIdOrderByStart() {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(item1.getId());

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item1.getId());
    }

    @Test
    void findByItemIdAndBookerId() {
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerId(item2.getId(), user2.getId());

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBookerId(), user2.getId());
        assertEquals(bookings.get(0).getItemId(), item2.getId());
    }
}