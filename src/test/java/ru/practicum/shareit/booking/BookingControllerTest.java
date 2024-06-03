package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    private BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1L,
            new Item(1L, "name", "desc", true, 1L, null),
            1L,
            new User(1L, "user", "user@user.com"),
            Status.WAITING);

    @Test
    void create() {
        Long bookerId = bookingDto.getBookerId();
        when(bookingService.create(bookerId, bookingDto)).thenReturn(bookingDto);


    }

    @Test
    void findByBooker() {
    }

    @Test
    void findByOwner() {
    }

    @Test
    void findById() {
    }

    @Test
    void approve() {
    }
}