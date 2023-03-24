package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user1;
    private User user2;
    private ItemRequest itemRequest;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        user2 = userRepository.save(User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build());
        itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .userId(user1.getId())
                .description("d")
                .created(LocalDateTime.now())
                .build());
        item1 = itemRepository.save(Item.builder()
                .name("ball")
                .description("big")
                .available(true)
                .ownerId(user1.getId())
                .requestId(itemRequest.getId())
                .build());
        item2 = itemRepository.save(Item.builder()
                .name("bag")
                .description("big")
                .available(true)
                .ownerId(user2.getId())
                .build());
    }

    @Test
    void findAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerId(user1.getId(), Pageable.ofSize(100));

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "ball");
        assertEquals(items.get(0).getDescription(), "big");
        assertEquals(items.get(0).getOwnerId(), user1.getId());
        assertEquals(items.get(0).getAvailable(), true);
    }

    @Test
    void search() {
        List<Item> items = itemRepository.search("big", Pageable.unpaged());

        assertEquals(items.size(), 2);
        assertEquals(items.get(0).getName(), "ball");
        assertEquals(items.get(0).getDescription(), "big");
        assertEquals(items.get(0).getOwnerId(), user1.getId());
        assertEquals(items.get(0).getAvailable(), true);
        assertEquals(items.get(1).getName(), "bag");
        assertEquals(items.get(1).getDescription(), "big");
        assertEquals(items.get(1).getOwnerId(), user2.getId());
        assertEquals(items.get(1).getAvailable(), true);
    }

    @Test
    void findAllByRequestId() {
        List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "ball");
        assertEquals(items.get(0).getDescription(), "big");
        assertEquals(items.get(0).getOwnerId(), user1.getId());
        assertEquals(items.get(0).getAvailable(), true);
        assertEquals(items.get(0).getRequestId(), itemRequest.getId());
    }
}