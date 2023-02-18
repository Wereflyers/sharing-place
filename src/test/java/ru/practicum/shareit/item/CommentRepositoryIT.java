package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryIT {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private Item item1;
    private Comment comment;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(User.builder()
                .email("john@mail.ru")
                .name("john")
                .build());
        item1 = itemRepository.save(Item.builder()
                .name("ball")
                .description("big")
                .available(true)
                .ownerId(user1.getId())
                .build());
        comment = commentRepository.save(Comment.builder()
                .author(user1.getId())
                .text("text")
                .created(LocalDateTime.now())
                .item(item1.getId())
                .build());
    }

    @Test
    void findAllByItem() {
        List<Comment> commentList = commentRepository.findAllByItem(item1.getId());

        assertEquals(commentList.size(), 1);
        assertEquals(commentList.get(0), comment);
    }
}