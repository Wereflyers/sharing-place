package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentForResponseJsonTest {
    @Autowired
    private JacksonTester<CommentForResponse> json;

    @SneakyThrows
    @Test
    void testCommentForResponse() {
        CommentForResponse commentForResponse = new CommentForResponse(1L, "text", "author",
                LocalDateTime.of(2022, 1, 1, 1, 1, 1));

        JsonContent<CommentForResponse> result = json.write(commentForResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(
                LocalDateTime.of(2022, 1, 1, 1, 1, 1).toString());
    }
}