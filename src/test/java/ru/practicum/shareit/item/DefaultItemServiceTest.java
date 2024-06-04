package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.ForbiddenException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class DefaultItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    private final Pageable defaultPage = PageRequest.of(0, 100);

    @Test
    void createOk() {
        ItemDtoShort itemToCreate = ItemDtoShort.builder()
                .available(true)
                .name("test item")
                .description("test item description")
                .build();
        ItemDtoShort created = itemService.create(1L, itemToCreate);
        assertNotNull(created);
        assertEquals(created.getId(), 6);
        assertEquals(created.getName(), itemToCreate.getName());
        assertEquals(created.getDescription(), itemToCreate.getDescription());
        assertEquals(created.getAvailable(), itemToCreate.getAvailable());
    }

    @Test
    void createFail() {
        ItemDtoShort itemToCreate = ItemDtoShort.builder()
                .available(true)
                .name("test item")
                .description("test item description")
                .build();
        itemToCreate.setAvailable(null);
        assertThrows(ValidationException.class, () -> itemService.create(1L, itemToCreate));
        itemToCreate.setAvailable(true);
        itemToCreate.setName("");
        assertThrows(ValidationException.class, () -> itemService.create(1L, itemToCreate));
        itemToCreate.setName("test item");
        itemToCreate.setDescription("");
        assertThrows(ValidationException.class, () -> itemService.create(1L, itemToCreate));
        itemToCreate.setDescription("test item description");
        assertThrows(EntityNotFoundException.class, () -> itemService.create(99L, itemToCreate));
    }

    @Test
    void remove() {
        ItemDtoResponse existing = itemService.findById(1L, 1L);
        assertNotNull(existing);
        itemService.remove(1L);

        assertThrows(EntityNotFoundException.class, () -> itemService.findById(1L, 1L));
    }

    @Test
    void updateOk() {
        ItemDtoShort itemToUpdate = ItemDtoShort.builder()
                .available(false)
                .name("updated item")
                .description("updated item description")
                .build();
        ItemDtoShort updated = itemService.update(1L, 1L, itemToUpdate);
        assertNotNull(updated);
        assertEquals(updated.getName(), itemToUpdate.getName());
        assertEquals(updated.getDescription(), itemToUpdate.getDescription());
        assertEquals(updated.getAvailable(), itemToUpdate.getAvailable());
    }

    @Test
    void updateFail() {
        Long itemId = 1L;
        Long ownerId = 1L;
        ItemDtoShort itemToUpdate = ItemDtoShort.builder()
                .available(false)
                .name("updated item")
                .description("updated item description")
                .build();
        ItemDtoResponse stored = itemService.findById(ownerId, itemId);
        assertNotNull(stored);

        assertThrows(EntityNotFoundException.class, () -> itemService.update(99L, itemId, itemToUpdate));
        assertThrows(EntityNotFoundException.class, () -> itemService.update(ownerId, 99L, itemToUpdate));
        assertThrows(ForbiddenException.class, () -> itemService.update(2L, itemId, itemToUpdate));
    }

    @Test
    void findAll() {
        Long ownerId = 4L;
        List<ItemDtoResponse> items = itemService.findAll(ownerId, defaultPage);
        assertEquals(items.size(), 3);
    }

    @Test
    void findById() {
        Long itemId = 1L;
        Long ownerId = 1L;
        ItemDtoResponse stored = itemService.findById(ownerId, itemId);
        assertNotNull(stored);

        assertThrows(EntityNotFoundException.class, () -> itemService.findById(99L, itemId));
        assertThrows(EntityNotFoundException.class, () -> itemService.findById(ownerId, 99L));
    }

    @Test
    void findByText() {
        String searchText = "";
        List<ItemDtoShort> items = itemService.findByText(searchText, defaultPage);
        assertEquals(items.size(), 0);

        searchText = "АккумулЯторнАя";
        items = itemService.findByText(searchText, defaultPage);
        assertEquals(items.size(), 2);
    }

    @Test
    void createCommentOk() {
        Long authorId = 5L;
        Long itemId = 1L;
        Long ownerId = 1L;
        CommentDtoRequest commentDtoRequest = new CommentDtoRequest("test comment");

        itemService.createComment(authorId, itemId, commentDtoRequest, defaultPage);
        ItemDtoResponse stored = itemService.findById(ownerId, itemId);
        assertEquals(stored.getComments().size(), 1);
    }

    @Test
    void createCommentFail() {
        Long authorId = 6L;
        Long itemId = 2L;
        CommentDtoRequest commentDtoRequest = new CommentDtoRequest("test comment");

        assertThrows(ValidationException.class, () -> itemService.createComment(authorId, itemId,
                commentDtoRequest, defaultPage));

        assertThrows(EntityNotFoundException.class, () -> itemService.createComment(99L, itemId,
                commentDtoRequest, defaultPage));
        assertThrows(EntityNotFoundException.class, () -> itemService.createComment(authorId, 99L,
                commentDtoRequest, defaultPage));
    }
}