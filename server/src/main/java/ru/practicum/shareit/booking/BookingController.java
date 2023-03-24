package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingForResponse save(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingForResponse approveOrReject(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        return bookingService.approveOrReject(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingForResponse get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingForResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String state,
                                           @RequestParam int from, @RequestParam int size) {
        State st = State.fromString(state);
        return bookingService.getAll(userId, st, from, size);
    }

    @GetMapping("/owner")
    public List<BookingForResponse> getAllForItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String state,
                                                   @RequestParam int from, @RequestParam int size) {
        State st = State.fromString(state);
        return bookingService.getAllForItems(userId, st, from, size);
    }
}
