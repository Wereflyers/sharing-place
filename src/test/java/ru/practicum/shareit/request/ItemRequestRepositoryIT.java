package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        User user2 = userRepository.save(User.builder()
                .name("jane")
                .email("jane@mail.ru")
                .build());
        itemRequestRepository.save(ItemRequest.builder()
                .userId(user1.getId())
                .created(LocalDateTime.MIN)
                .description("test")
                .build());
        itemRequestRepository.save(ItemRequest.builder()
                .userId(user2.getId())
                .created(LocalDateTime.now())
                .description("newTest")
                .build());
    }

    @Test
    void findAllByUserIdOrderByCreated() {
        List<ItemRequest> requestList = itemRequestRepository.findAllByUserIdOrderByCreated(user1.getId());

        assertEquals(requestList.size(), 1);
        assertEquals(requestList.get(0).getUserId(), 1L);
        assertEquals(requestList.get(0).getCreated(), LocalDateTime.MIN);
    }
}