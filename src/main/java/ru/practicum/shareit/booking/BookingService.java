package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.ItemDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long bookerId, BookingDto bookingDto);

    void remove(Long bookingId);

    BookingDto update(Long bookerId, Long bookingId, BookingDto bookingDto);

    BookingDto approve(Long itemId, Long bookerId, Boolean isApproved);

    List<BookingDto> findAllByBooker(Long bookerId, State state);

    List<BookingDto> findAllByOwner(Long bookerId, State state);

    BookingDto findById(Long bookingId, Long userId);
}
