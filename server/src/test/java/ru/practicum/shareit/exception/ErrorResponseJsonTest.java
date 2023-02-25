package ru.practicum.shareit.exception;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ErrorResponseJsonTest {
    @Autowired
    private JacksonTester<ErrorResponse> json;

    @SneakyThrows
    @Test
    void testErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse("message");

        JsonContent<ErrorResponse> result = json.write(errorResponse);

        assertThat(result).hasJsonPathStringValue("$.error", "message");
    }
}