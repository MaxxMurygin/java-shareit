package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.common.EntityNotFoundException;
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
@Transactional(readOnly = true)
public class DefaultBookingService implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(Long bookerId, BookingDto bookingDto, Pageable page) {
        Long itemId = bookingDto.getItemId();
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", bookerId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(Item.class, String.format("Id = %s", itemId)));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException(String.format("Item id = %s unavailable", itemId));
        }
        if (end.isBefore(start) || end.isEqual(start)) {
            throw new ValidationException("End time must come after the start time");
        }
        if (item.getOwner().equals(bookerId)) {
            throw new NotFoundException("Booker cannot be owner");
        }
        Booking booking = BookingMapper.fromBookingDto(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }


    @Override
    @Transactional
    public BookingDto approve(Long bookingId, Long bookerId, Boolean isApproved, Pageable page) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(Booking.class, String.format("Id = %s", bookingId)));
        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", bookerId)));
        Item item = booking.getItem();

        if (!booker.getId().equals(item.getOwner())) {
            throw new NotFoundException("Approve available only for owner");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Cannot change approved booking");
        }
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> findAllByBooker(Long bookerId, String stateString, Pageable page) {
        List<Booking> result;
        State state;

        userRepository.findById(bookerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", bookerId)));
        if (stateString == null || stateString.isBlank()) {
            state = State.ALL;
        } else {
            try {
                state = State.valueOf(stateString);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Unknown state: " + stateString);
            }
        }

        switch (state) {
            case ALL:
                result = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, page);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId,
                        LocalDateTime.now(), page);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId,
                        LocalDateTime.now(), page);
                break;
            case CURRENT:
                result = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId,
                        Status.REJECTED, page);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId,
                        Status.WAITING, page);
                break;
            default:
                result = new ArrayList<>();
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllByOwner(Long ownerId, String stateString, Pageable page) {
        List<Booking> result;

        userRepository.findById(ownerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", ownerId)));
        State state;

        if (stateString == null || stateString.isBlank()) {
            state = State.ALL;
        } else {
            try {
                state = State.valueOf(stateString);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Unknown state: " + stateString);
            }
        }
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByOwnerId(ownerId, page);
                break;
            case PAST:
                result = bookingRepository.findByOwnerIdAndPastState(ownerId, LocalDateTime.now(), page);
                break;
            case FUTURE:
                result = bookingRepository.findByOwnerIdAndFutureState(ownerId, LocalDateTime.now(), page);
                break;
            case CURRENT:
                result = bookingRepository.findByOwnerIdAndCurrentState(ownerId, LocalDateTime.now(), page);
                break;
            case REJECTED:
                result = bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED, page);
                break;
            case WAITING:
                result = bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING, page);
                break;
            default:
                result = new ArrayList<>();
        }

        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId, Pageable page) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(Booking.class, String.format("Id = %s", bookingId)));
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", userId)));
        Long ownerId = booking.getItem().getOwner();
        Long bookerId = booking.getBooker().getId();

        if (userId.equals(ownerId) || userId.equals(bookerId)) {
            return BookingMapper.toBookingDto(booking);
        }
        throw new EntityNotFoundException(User.class, "Info for booking is available only for booker or item owner");
    }
}
