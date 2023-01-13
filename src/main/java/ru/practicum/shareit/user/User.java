package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @NonNull
    @Positive
    Long id;
    String name;
    @Email
    String email;

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .build();
    }
}
