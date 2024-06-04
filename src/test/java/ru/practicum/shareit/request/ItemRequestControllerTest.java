package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemRequestService itemRequestService;
    private final ItemRequestDtoRequest itemRequestDtoRequest = new ItemRequestDtoRequest("request");
    private final ItemRequestDtoResponseSimple itemRequestDtoResponseSimple = new ItemRequestDtoResponseSimple(1L,
            "request response",
            LocalDateTime.now());
    private final ItemRequestDtoResponseWithItems itemRequestDtoResponseWithItems =
            new ItemRequestDtoResponseWithItems(1L,
                    "description",
                    LocalDateTime.now(),
                    new ArrayList<>());
    private static final String REQUESTER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);

    @SneakyThrows
    @Test
    void create() {
        Long requestId = 1L;
        when(itemRequestService.create(requestId, itemRequestDtoRequest)).thenReturn(itemRequestDtoResponseSimple);

        String result = mockMvc.perform(post("/requests")
                        .header(REQUESTER_ID, 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDtoRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).create(requestId, itemRequestDtoRequest);
        assertEquals(objectMapper.writeValueAsString(itemRequestDtoResponseSimple), result);
    }

    @SneakyThrows
    @Test
    void getByRequesterId() {
        Long requesterId = 1L;
        List<ItemRequestDtoResponseWithItems> requestList = new ArrayList<>();
        requestList.add(itemRequestDtoResponseWithItems);
        when(itemRequestService.getAllByRequesterId(requesterId, defaultPage)).thenReturn(requestList);

        String result = mockMvc.perform(get("/requests")
                        .header(REQUESTER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getAllByRequesterId(requesterId, defaultPage);
        assertEquals(objectMapper.writeValueAsString(requestList), result);
    }

    @SneakyThrows
    @Test
    void getByRequestId() {
        Long requestId = 1L;
        Long requesterId = 1L;
        when(itemRequestService.getAllByRequestId(requestId, requesterId)).thenReturn(itemRequestDtoResponseWithItems);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(REQUESTER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getAllByRequestId(requesterId, requesterId);
        assertEquals(objectMapper.writeValueAsString(itemRequestDtoResponseWithItems), result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        Long requesterId = 1L;
        Pageable page = PageRequest.of(0, 100, Sort.by("Created").descending());
        List<ItemRequestDtoResponseWithItems> requestList = new ArrayList<>();
        requestList.add(itemRequestDtoResponseWithItems);
        when(itemRequestService.getAll(requesterId, page)).thenReturn(requestList);


        String result = mockMvc.perform(get("/requests/all")
                        .header(REQUESTER_ID, 1)
                        .param("from", "0")
                        .param("size", "100"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemRequestService).getAll(requesterId, page);
        assertEquals(objectMapper.writeValueAsString(requestList), result);
    }
}