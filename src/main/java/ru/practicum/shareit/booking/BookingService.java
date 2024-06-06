package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookingService {
    BookingDto create(Long bookerId, BookingDto bookingDto);

    BookingDto approve(Long itemId, Long bookerId, Boolean isApproved);

    List<BookingDto> findAllByBooker(Long bookerId, String state, Pageable page);

    List<BookingDto> findAllByOwner(Long bookerId, String state, Pageable page);

    BookingDto findById(Long bookingId, Long userId);
}
