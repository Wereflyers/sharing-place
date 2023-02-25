package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, Long userId, Long itemId) {
        return Comment.builder()
                .text(commentDto.getText())
                .item(itemId)
                .author(userId)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentForResponse toCommentForResponse(Comment comment, String authorName) {
        return CommentForResponse.builder()
                .id(comment.getId())
                .authorName(authorName)
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
