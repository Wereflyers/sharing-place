package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingShortJsonTest {
    @Autowired
    private JacksonTester<BookingShort> json;

    @SneakyThrows
    @Test
    void testBookingShort() {
        BookingShort bookingShort = new BookingShort();
        bookingShort.setId(1L);
        bookingShort.setBookerId(1L);

        JsonContent<BookingShort> result = json.write(bookingShort);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}