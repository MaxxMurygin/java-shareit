package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ItemService itemService;
    private final Item item = new Item(1L, "name", "desc", true, 1L, null);
    ItemDtoShort itemDto = ItemMapper.toItemDtoShort(item);
    ItemDtoResponse itemDtoResponse = ItemMapper.toItemDtoResponse(item,
            new BookingDto(),
            new BookingDto(),
            new ArrayList<>());
    private static final String OWNER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 10);

    @SneakyThrows
    @Test
    void createOk() {
        Long ownerId = item.getOwner();
        ItemDtoShort itemDto = ItemMapper.toItemDtoShort(item);
        when(itemService.create(ownerId, itemDto)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(OWNER_ID, 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).create(ownerId, itemDto);
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void findById() {
        Long itemId = item.getId();
        Long ownerId = item.getOwner();
        when(itemService.findById(ownerId, itemId)).thenReturn(itemDtoResponse);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(OWNER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).findById(ownerId, itemId);
        assertEquals(objectMapper.writeValueAsString(itemDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void findAll() {
        Long ownerId = item.getOwner();
        List<ItemDtoResponse> itemList = new ArrayList<>();
        itemList.add(itemDtoResponse);
        when(itemService.findAll(ownerId, defaultPage)).thenReturn(itemList);

        String result = mockMvc.perform(get("/items")
                        .header(OWNER_ID, 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).findAll(ownerId, defaultPage);
        assertEquals(objectMapper.writeValueAsString(itemList), result);
    }

    @SneakyThrows
    @Test
    void findByText() {
        List<ItemDtoShort> itemList = new ArrayList<>();
        itemList.add(itemDto);
        String search = "findMe";
        when(itemService.findByText(search, defaultPage)).thenReturn(itemList);

        String result = mockMvc.perform(get("/items/search")
                        .header(OWNER_ID, 1)
                        .param("text", search)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).findByText(search, defaultPage);
        assertEquals(objectMapper.writeValueAsString(itemList), result);
    }

    @SneakyThrows
    @Test
    void update() {
        Long itemId = item.getId();
        Long ownerId = item.getOwner();
        ItemDtoShort itemDto = ItemMapper.toItemDtoShort(item);
        when(itemService.update(ownerId, itemId, itemDto)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(OWNER_ID, 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).update(ownerId, itemId, itemDto);
        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void remove() {
        Long itemId = item.getId();
        doNothing().when(itemService).remove(itemId);

        mockMvc.perform(delete("/items/{itemId}", itemId)
                        .header(OWNER_ID, 1))
                .andExpect(status().isNoContent());
        verify(itemService).remove(itemId);
    }

    @SneakyThrows
    @Test
    void createComment() {
        Long authorId = item.getOwner();
        Long itemId = item.getId();
        CommentDtoRequest commentDtoRequest = new CommentDtoRequest();
        commentDtoRequest.setText("comment text");
        CommentDtoResponse commentDtoResponse = new CommentDtoResponse(authorId,
                commentDtoRequest.getText(),
                "Name",
                LocalDateTime.now());

        when(itemService.createComment(authorId, itemId, commentDtoRequest, defaultPage)).thenReturn(commentDtoResponse);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(OWNER_ID, 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDtoRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(itemService).createComment(authorId, itemId, commentDtoRequest, defaultPage);
        assertEquals(objectMapper.writeValueAsString(commentDtoResponse), result);
    }
}