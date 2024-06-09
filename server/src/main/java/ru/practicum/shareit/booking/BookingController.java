package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;
    private static final String BOOKER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);
    Pageable userPage;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(value = BOOKER_ID) Long bookerId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookerId, bookingDto);
    }

    @GetMapping
    public List<BookingDto> findByBooker(@RequestHeader(value = BOOKER_ID) Long bookerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        userPage = PageRequest.of(from / size, size);
        return bookingService.findAllByBooker(bookerId, state, userPage);
    }

    @GetMapping("/owner")
    public List<BookingDto> findByOwner(@RequestHeader(value = BOOKER_ID) Long ownerId,
                                        @RequestParam(name = "state", defaultValue = "ALL") String state,
                                        @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") Integer size) {
        userPage = PageRequest.of(from / size, size);
        return bookingService.findAllByOwner(ownerId, state, userPage);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(value = BOOKER_ID) Long userId,
                               @PathVariable Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(value = BOOKER_ID) Long bookerId,
                          @RequestParam Boolean approved,
                             @PathVariable Long bookingId) {
        return bookingService.approve(bookingId, bookerId, approved);
    }
}
