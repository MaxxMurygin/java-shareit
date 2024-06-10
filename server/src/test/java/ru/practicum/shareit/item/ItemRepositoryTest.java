package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository repository;
    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setName("test item");
        item.setDescription("test description");
        item.setOwner(1L);
        item.setAvailable(true);
        item.setRequestId(1L);
    }

    @Test
    void createWithEmptyName() {
        item.setName(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(item));
    }

    @Test
    void createWithEmptyDescription() {
        item.setDescription(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(item));
    }

    @Test
    void createWithEmptyAvailable() {
        item.setAvailable(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(item));
    }

    @Test
    void createWithNonExistingUser() {
        item.setOwner(99L);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(item));
    }

    @Test
    void search() {
        repository.save(item);
        List<Item> searched = repository.search("item");
        assertEquals(1, searched.size());
        assertEquals(item.getName(), searched.get(0).getName());
        searched = repository.search("description");
        assertEquals(item.getDescription(), searched.get(0).getDescription());
    }

    @Test
    void findByOwnerOrderById() {
    }

    @Test
    void findByRequestId() {
        repository.save(item);
        List<Item> searched = repository.findByRequestId(1L);

        assertEquals(2, searched.size());
        assertEquals(item.getName(), searched.get(1).getName());
    }
}