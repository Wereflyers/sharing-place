package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingForResponse save(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingForResponse approveOrReject(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long id,
                                      @RequestParam(name = "approved") Boolean approved) {
        return bookingService.approveOrReject(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingForResponse get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long id) {
        return bookingService.get(userId, id);
    }

    @GetMapping
    public List<BookingForResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestParam(defaultValue = "ALL") String state) {
        State st = State.fromString(state);
        return bookingService.getAll(userId, st);
    }

    @GetMapping("/owner")
    public List<BookingForResponse> getAllForItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        State st = State.fromString(state);
        return bookingService.getAllForItems(userId, st);
    }
}
