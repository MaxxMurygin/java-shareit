package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.ForbiddenException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultBookingService implements BookingService{
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(Long bookerId, BookingDto bookingDto) {
        Long itemId = bookingDto.getItemId();
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException(User.class, String.format("Id = %s", bookerId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(Item.class, String.format("Id = %s", itemId)));

        if (!item.getAvailable()) {
            throw new RuntimeException(String.format("Item id = %s unavailable", itemId));
        }
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new ValidationException("End time must come after the start time");
        }
        Booking booking = BookingMapper.fromBookingDto(bookingDto);
        booking.setBookerId(bookerId);
        booking.setStatus(Status.WAITING);


        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public void remove(Long bookingId) {

    }

    @Override
    public BookingDto update(Long bookerId, Long bookingId, BookingDto bookingDto) {
        return null;
    }

    @Override
    public List<BookingDto> findAllByBooker(Long bookerId, State state) {
        return null;
    }

    @Override
    public List<BookingDto> findAllByOwner(Long bookerId, State state) {
        return null;
    }

    @Override
    public BookingDto findById(Long bookingId) {
        return null;
    }
}
