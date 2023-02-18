package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingForResponseJsonTest {
    @Autowired
    private JacksonTester<BookingForResponse> json;

    @SneakyThrows
    @Test
    void testBookingForResponse() {
        BookingForResponse bookingForResponse = new BookingForResponse();
        bookingForResponse.setItem(new ItemShort(1L, "item"));
        bookingForResponse.setStart(LocalDateTime.of(2020, 2, 2, 2, 2, 2));
        bookingForResponse.setEnd(LocalDateTime.of(2022, 2, 2, 2, 2, 2));
        bookingForResponse.setStatus(BookingStatus.APPROVED);
        bookingForResponse.setBooker(new UserShort(1L));

        JsonContent<BookingForResponse> result = json.write(bookingForResponse);

        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(LocalDateTime.of(2020, 2, 2, 2, 2, 2).toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(LocalDateTime.of(2022, 2, 2, 2, 2, 2).toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
    }
}