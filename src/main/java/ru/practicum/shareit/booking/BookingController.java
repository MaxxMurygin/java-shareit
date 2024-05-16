package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String BOOKER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(value = BOOKER_ID) Long bookerId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookerId, bookingDto, defaultPage);
    }

    @GetMapping
    public List<BookingDto> findByBooker(@RequestHeader(value = BOOKER_ID) Long bookerId,
                                         @RequestParam(required = false) String state) {
        return bookingService.findAllByBooker(bookerId, state, defaultPage);
    }

    @GetMapping("/owner")
    public List<BookingDto> findByOwner(@RequestHeader(value = BOOKER_ID) Long ownerId,
                                         @RequestParam(required = false) String state) {
        return bookingService.findAllByOwner(ownerId, state, defaultPage);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(value = BOOKER_ID) Long userId,
                               @PathVariable Long bookingId) {
        return bookingService.findById(bookingId, userId, defaultPage);
    }


    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(value = BOOKER_ID) Long bookerId,
                          @RequestParam Boolean approved,
                             @PathVariable Long bookingId) {
        return bookingService.approve(bookingId, bookerId, approved, defaultPage);
    }
}
