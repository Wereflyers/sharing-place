package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String name;
    private final String message;

    public ErrorResponse(String message) {
        name = "Error message";
        this.message = message;
    }
}
