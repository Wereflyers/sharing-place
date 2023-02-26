package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemShortJsonTest {
    @Autowired
    private JacksonTester<ItemShort> json;

    /*@SneakyThrows
    @Test
    void testItemShort() {
        ItemShort itemShort = new ItemShort(1L, "name");

        JsonContent<ItemShort> result = json.write(itemShort);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
    }*/
}