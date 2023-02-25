package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
}
