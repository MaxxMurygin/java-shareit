package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private final Pageable testPage = PageRequest.of(0, 10);
    private final BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1L,
            new Item(1L, "name", "desc", true, 1L, null),
            1L,
            new User(1L, "user", "user@user.com"),
            Status.WAITING);
    private static final String BOOKER_ID = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void createOk() {
        Long bookerId = bookingDto.getBookerId();
        when(bookingService.create(bookerId, bookingDto)).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header(BOOKER_ID, 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).create(bookerId, bookingDto);
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void createWithoutHeader() {
        Long bookerId = bookingDto.getBookerId();
        when(bookingService.create(bookerId, bookingDto)).thenReturn(bookingDto);

         mockMvc.perform(post("/bookings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).create(bookerId, bookingDto);
    }

    @SneakyThrows
    @Test
    void findByBookerOk() {
        Long bookerId = bookingDto.getBookerId();
        String state = "ALL";
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);

        when(bookingService.findAllByBooker(bookerId, state, testPage)).thenReturn(bookingDtoList);

        String result = mockMvc.perform(get("/bookings")
                        .header(BOOKER_ID, 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).findAllByBooker(bookerId, state, testPage);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

//    @SneakyThrows
//    @Test
//    void findByBookerWithWrongParams() {
//        Long bookerId = bookingDto.getBookerId();
//        String state = "ALL";
//        List<BookingDto> bookingDtoList = new ArrayList<>();
//        bookingDtoList.add(bookingDto);
//
//        when(bookingService.findAllByBooker(bookerId, state, testPage)).thenReturn(bookingDtoList);
//
//        mockMvc.perform(get("/bookings")
//                        .header(BOOKER_ID, 1)
//                        .param("state", "ALL")
//                        .param("from", "-1")
//                        .param("size", "10"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/bookings")
//                        .header(BOOKER_ID, 1)
//                        .param("state", "ALL")
//                        .param("from", "2")
//                        .param("size", "0"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/bookings")
//                        .param("from", "-1")
//                        .param("size", "10"))
//                .andExpect(status().isBadRequest());
//
//        verify(bookingService, never()).findAllByBooker(bookerId, state, testPage);
//    }

    @SneakyThrows
    @Test
    void findByOwnerOk() {
        Long ownerId = bookingDto.getItem().getOwner();
        String state = "ALL";
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);

        when(bookingService.findAllByOwner(ownerId, state, testPage)).thenReturn(bookingDtoList);

        String result = mockMvc.perform(get("/bookings/owner")
                        .header(BOOKER_ID, 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).findAllByOwner(ownerId, state, testPage);
        assertEquals(objectMapper.writeValueAsString(bookingDtoList), result);
    }

//    @SneakyThrows
//    @Test
//    void findByOwnerWithWrongParams() {
//        Long ownerId = bookingDto.getItem().getOwner();
//        String state = "ALL";
//        List<BookingDto> bookingDtoList = new ArrayList<>();
//        bookingDtoList.add(bookingDto);
//
//        when(bookingService.findAllByBooker(ownerId, state, testPage)).thenReturn(bookingDtoList);
//
//        mockMvc.perform(get("/bookings")
//                        .header(BOOKER_ID, 1)
//                        .param("state", "ALL")
//                        .param("from", "-1")
//                        .param("size", "10"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/bookings")
//                        .header(BOOKER_ID, 1)
//                        .param("state", "ALL")
//                        .param("from", "2")
//                        .param("size", "0"))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(get("/bookings")
//                        .param("from", "-1")
//                        .param("size", "10"))
//                .andExpect(status().isBadRequest());
//
//        verify(bookingService, never()).findAllByOwner(ownerId, state, testPage);
//    }

    @SneakyThrows
    @Test
    void findById() {
        Long bookingId = bookingDto.getId();
        Long userId = 1L;

        when(bookingService.findById(bookingId, userId)).thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(BOOKER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).findById(bookingId, userId);
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approveOk() {
        Long bookingId = bookingDto.getId();
        Long userId = 1L;

        when(bookingService.approve(bookingId, userId, true)).thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(BOOKER_ID, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(bookingService).approve(bookingId, userId, true);
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approveWithoutParam() {
        Long bookingId = bookingDto.getId();
        Long userId = 1L;

        when(bookingService.approve(bookingId, userId, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(BOOKER_ID, 1))
                .andExpect(status().isBadRequest());
        verify(bookingService, never()).approve(bookingId, userId, true);
    }
}