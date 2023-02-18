package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestForResponseJsonTest {
    @Autowired
    private JacksonTester<ItemRequestForResponse> json;

    @SneakyThrows
    @Test
    void testItemRequest() {
        ItemRequestForResponse itemRequest = ItemRequestForResponse.builder()
                .added(true)
                .id(1L)
                .description("itemRequest")
                .userId(1L)
                .created(LocalDateTime.of(2022,2,2,2,1, 2))
                .build();

        JsonContent<ItemRequestForResponse> result = json.write(itemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.added").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("itemRequest");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(LocalDateTime.of(2022,2,2,2,1,2).toString());
    }
}