package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {
    @NonNull
    @NotBlank
    private String text;
}
