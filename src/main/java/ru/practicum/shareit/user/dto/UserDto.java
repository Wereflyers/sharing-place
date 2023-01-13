package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    @Positive
    Long id;
    String name;
    @Email
    String email;
}
