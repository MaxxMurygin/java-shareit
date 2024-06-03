package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository repository;
    private Booking booking;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "user", "user@domain.com");
        Item item = new Item(1L, "item", "item description", true, 5L, null);
        booking = new Booking();

        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createWithWrongUser() {
        booking.getBooker().setId(99L);
        assertThrows(DataIntegrityViolationException.class,
                () ->repository.save(booking));
    }

    @Test
    void createWithWrongItem() {
        booking.getItem().setId(99L);
        assertThrows(DataIntegrityViolationException.class,
                () ->repository.save(booking));
    }

    @Test
    void createWithWrongStatus() {
        booking.setStatus(null);
        assertThrows(DataIntegrityViolationException.class,
                () ->repository.save(booking));
    }

    @Test
    void createOk() {
        Booking saved = repository.save(booking);
        assertNotNull(saved);
        assertEquals(1L, saved.getItem().getId());
        assertEquals(1L, saved.getBooker().getId());
        assertEquals(saved.getStatus(), booking.getStatus());
    }
}