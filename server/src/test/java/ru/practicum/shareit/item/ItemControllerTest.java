package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Comment;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemService itemService;
    private ItemForResponse item1;
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        item1 = ItemForResponse.builder()
                .name("ball")
                .description("new")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void getAll() {
        List<ItemForResponse> expectedItems = List.of(item1);
        when(itemService.getAllForUser(userId, 0, 1)).thenReturn(expectedItems);

        List<ItemForResponse> response = itemController.getAll(userId, 0, 1);

        assertEquals(response, expectedItems);
        verify(itemService).getAllForUser(userId, 0, 1);
    }

    @Test
    void get() {
        when(itemService.get(1L, userId)).thenReturn(item1);

        ItemForResponse actualItem = itemController.get(userId, 1L);

        assertEquals(item1, actualItem);
        verify(itemService).get(1L, userId);
    }

    @Test
    void add() {
        when(itemService.add(anyLong(), any())).thenReturn(item1);

        ItemForResponse response = itemController.add(userId, new ItemDto());

        assertEquals(response, item1);
        verify(itemService).add(anyLong(), any());
    }

    @Test
    void update() {
        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(item1);

        ItemForResponse response = itemController.update(userId, 1L, new ItemDto());

        assertEquals(response, item1);
        verify(itemService).update(anyLong(), anyLong(), any());
    }

    @Test
    void delete() {
        itemController.delete(userId, 1L);

        verify(itemService).delete(userId, 1L);
    }

    @Test
    void search() {
        when(itemService.search("s", 0, 1)).thenReturn(List.of(item1));

        List<ItemForResponse> responses = itemController.search(userId, "s", 0, 1);

        assertEquals(responses.size(), 1);
        assertEquals(responses.get(0), item1);
        verify(itemService).search("s", 0, 1);
    }

    @Test
    void addComment() {
        Comment comment = Comment.builder()
                .author(1L)
                .text("good")
                .build();

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(CommentMapper.toCommentForResponse(comment, "name"));

        CommentForResponse response = itemController.addComment(userId, 1L, new CommentDto());

        assertEquals(response.getText(), "good");
        assertEquals(response.getAuthorName(), "name");
        verify(itemService).addComment(anyLong(), anyLong(), any());
    }
}