package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {
    @NotEmpty
    @NotBlank
    private String text;
}
