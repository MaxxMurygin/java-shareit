package ru.practicum.shareit.request;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository repository;
    ItemRequest itemRequest;
    Pageable page = PageRequest.of(0, 100);

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest();
        itemRequest.setRequesterId(3L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("test item request");
    }

    @Test
    void createWithNonExistingUser() {
        itemRequest.setRequesterId(999L);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(itemRequest));
    }

    @Test
    void findAllByRequesterId() {
        repository.save(itemRequest);
        List<ItemRequest> saved = repository.findAllByRequesterId(3L, page);
        assertEquals(1, saved.size());
    }

    @Test
    void findAllByRequesterIdEmptyResult() {
        repository.save(itemRequest);
        List<ItemRequest> saved = repository.findAllByRequesterId(99L, page);
        assertEquals(0, saved.size());
    }

    @Test
    void findAllByRequesterIdNot() {
        List<ItemRequest> saved = repository.findAllByRequesterIdNot(6L, page);
        assertEquals(1, saved.size());
        repository.save(itemRequest);
        saved = repository.findAllByRequesterIdNot(6L, page);
        assertEquals(2, saved.size());
    }
}