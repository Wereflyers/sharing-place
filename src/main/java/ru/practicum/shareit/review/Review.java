package ru.practicum.shareit.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Review {
    Long userId;
    String description;
}
