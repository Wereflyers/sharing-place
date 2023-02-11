package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequestResponse {
    private Long itemId;
    private String name;
    private Long ownerId;
}
