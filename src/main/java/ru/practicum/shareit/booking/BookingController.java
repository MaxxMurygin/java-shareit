package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String BOOKER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(value = BOOKER_ID) Long bookerId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookerId, bookingDto);
    }
}
