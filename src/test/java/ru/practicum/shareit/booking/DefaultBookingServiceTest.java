package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class DefaultBookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    private final Pageable defaultPage = PageRequest.of(0, 100);

    @Test
    void createOk() {
        Long bookerId = 6L;
        Long itemId = 1L;
        BookingDto bookingToCreate = BookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto created = bookingService.create(bookerId, bookingToCreate);
        assertNotNull(created);
        BookingDto stored = bookingService.findById(created.getId(), bookerId);
        assertNotNull(stored);
        assertEquals(stored.getBookerId(), bookerId);
        assertEquals(stored.getItem().getId(), itemId);
    }

    @Test
    void createFail() {
        Long bookerId = 6L;
        Long itemId = 1L;
        Long ownerId = 1L;
        BookingDto bookingToCreate = BookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThrows(EntityNotFoundException.class, () -> bookingService.create(99L, bookingToCreate));
        bookingToCreate.setItemId(99L);
        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookerId, bookingToCreate));
        bookingToCreate.setItemId(itemId);
        assertThrows(NotFoundException.class, () -> bookingService.create(ownerId, bookingToCreate));
        bookingToCreate.setEnd(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingService.create(bookerId, bookingToCreate));
    }

    @Test
    void approve() {
        Long bookerId = 6L;
        Long itemId = 1L;
        Long ownerId = 1L;
        BookingDto bookingToCreate = BookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto created = bookingService.create(bookerId, bookingToCreate);
        assertNotNull(created);
        bookingService.approve(created.getId(), ownerId, true);

        BookingDto stored = bookingService.findById(created.getId(), bookerId);
        assertNotNull(stored);
        assertEquals(stored.getStatus(), Status.APPROVED);
    }

    @Test
    void findAllByBooker() {
        Long bookerId = 1L;
        List<BookingDto> bookingList = bookingService.findAllByBooker(bookerId, "ALL", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 5);

        bookingList = bookingService.findAllByBooker(bookerId, "", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 5);

        bookingList = bookingService.findAllByBooker(bookerId, "REJECTED", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 1);

        bookingList = bookingService.findAllByBooker(bookerId, "WAITING", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 0);

        bookingList = bookingService.findAllByBooker(bookerId, "PAST", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 5);

        bookingList = bookingService.findAllByBooker(bookerId, "FUTURE", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 0);

        bookingList = bookingService.findAllByBooker(bookerId, "CURRENT", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 0);
    }

    @Test
    void findAllByOwner() {
        Long ownerId = 1L;
        List<BookingDto> bookingList = bookingService.findAllByOwner(ownerId, "ALL", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 2);

        bookingList = bookingService.findAllByOwner(ownerId, "", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 2);

        bookingList = bookingService.findAllByOwner(ownerId, "REJECTED", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 1);

        bookingList = bookingService.findAllByOwner(ownerId, "WAITING", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 0);

        bookingList = bookingService.findAllByOwner(ownerId, "PAST", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 1);

        bookingList = bookingService.findAllByOwner(ownerId, "FUTURE", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 1);

        bookingList = bookingService.findAllByOwner(ownerId, "CURRENT", defaultPage);
        assertNotNull(bookingList);
        assertEquals(bookingList.size(), 0);
    }

    @Test
    void findById() {
        Long bookerId = 6L;
        Long itemId = 1L;
        Long ownerId = 1L;
        BookingDto bookingToCreate = BookingDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto created = bookingService.create(bookerId, bookingToCreate);
        assertNotNull(created);

        BookingDto stored = bookingService.findById(created.getId(), bookerId);
        assertNotNull(stored);
        stored = bookingService.findById(created.getId(), ownerId);
        assertNotNull(stored);
    }
}