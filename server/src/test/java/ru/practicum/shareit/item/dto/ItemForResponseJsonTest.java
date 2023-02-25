package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShort;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemForResponseJsonTest {
    @Autowired
    private JacksonTester<ItemForResponse> json;

    @SneakyThrows
    @Test
    void testItemForResponse() {
        ItemForResponse itemForResponse = ItemForResponse.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .lastBooking(new BookingShort(1L, 1L))
                .nextBooking(new BookingShort(2L, 2L))
                .build();

        JsonContent<ItemForResponse> result = json.write(itemForResponse);

        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(2);
    }
}