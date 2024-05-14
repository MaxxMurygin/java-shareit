package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        log.info("booking " + booking + " item " + item + " booker " + booker);


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
    public BookingDto approve(Long bookingId, Long bookerId, Boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(Booking.class, String.format("Id = %s", bookingId)));
        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException(User.class, String.format("Id = %s", bookerId)));
        Item item = booking.getItem();

        if (!booker.getId().equals(item.getOwner())) {
            throw new ValidationException("Approve available only for owner");
        }
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> findAllByBooker(Long bookerId, State state) {
        List<Booking> result = new ArrayList<>();
        userRepository.findById(bookerId).orElseThrow(() ->
                new NotFoundException(User.class, String.format("Id = %s", bookerId)));

        if (state == null) {
            state = State.ALL;
        }

        switch (state) {
            case PAST:

                break;
            case FUTURE:

                break;
            case CURRENT:

                break;
            case REJECTED:

                break;
            case WAITING:

                break;

            default:
                result = bookingRepository.findAllByBookerId(bookerId);
        }

        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllByOwner(Long bookerId, State state) {
        return null;
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(Booking.class, String.format("Id = %s", bookingId)));
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(User.class, String.format("Id = %s", userId)));
        Long ownerId = booking.getItem().getOwner();
        Long bookerId = booking.getBooker().getId();

        if (userId.equals(ownerId) || userId.equals(bookerId)) {
            return BookingMapper.toBookingDto(booking);
        }
        throw new ValidationException("Info for booking is available only for booker or item owner");
    }
}
