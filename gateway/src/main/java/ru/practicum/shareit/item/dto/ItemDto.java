package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NotEmpty
    @NotBlank
    String name;
    @NotEmpty
    @NotBlank
    String description;
    @NotNull
    Boolean available;
    Long requestId;
}
