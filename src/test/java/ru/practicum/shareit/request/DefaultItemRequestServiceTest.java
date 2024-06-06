package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class DefaultItemRequestServiceTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private final Pageable defaultPage = PageRequest.of(0, 100);

    @Test
    void createOk() {
        Long requesterId = 6L;
        ItemRequestDtoRequest request = new ItemRequestDtoRequest("wanna something");

        ItemRequestDtoResponseSimple created = itemRequestService.create(requesterId, request);
        assertNotNull(created);
        assertEquals(created.getDescription(), request.getDescription());
    }

    @Test
    void createFail() {
        Long requesterId = 99L;
        ItemRequestDtoRequest request = new ItemRequestDtoRequest("wanna something");

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.create(requesterId, request));
    }

    @Test
    void getAllByRequesterId() {
        Long requesterId = 6L;
        ItemRequestDtoRequest request = new ItemRequestDtoRequest("wanna something");

        ItemRequestDtoResponseSimple created = itemRequestService.create(requesterId, request);
        assertNotNull(created);

        List<ItemRequestDtoResponseWithItems> stored = itemRequestService.getAllByRequesterId(requesterId, defaultPage);
        assertNotNull(stored);
        assertEquals(stored.size(), 1);
    }

    @Test
    void getAllByRequestId() {
        Long requesterId = 6L;
        ItemRequestDtoRequest request = new ItemRequestDtoRequest("wanna something");

        ItemRequestDtoResponseSimple created = itemRequestService.create(requesterId, request);
        assertNotNull(created);

        ItemRequestDtoResponseWithItems stored = itemRequestService.getAllByRequestId(created.getId(), requesterId);
        assertNotNull(stored);
        assertEquals(stored.getDescription(), request.getDescription());
    }

    @Test
    void getAll() {
        Long requesterId = 6L;
        Long lookerId = 5L;
        ItemRequestDtoRequest request = new ItemRequestDtoRequest("wanna something");

        ItemRequestDtoResponseSimple created = itemRequestService.create(requesterId, request);
        assertNotNull(created);

        List<ItemRequestDtoResponseWithItems> stored = itemRequestService.getAll(lookerId, defaultPage);
        assertNotNull(stored);
        assertEquals(stored.size(), 2);

        stored = itemRequestService.getAll(requesterId, defaultPage);
        assertNotNull(stored);
        assertEquals(stored.size(), 1);
    }
}