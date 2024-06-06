package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("testUser");
        user.setEmail("test@user.com");
    }

    @Test
    void createWithExistingEmail() {
        user.setEmail("user@user.com");
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(user));
    }

    @Test
    void createOk() {
        User saved = repository.save(user);
        assertNotNull(saved.getId());
        assertEquals(user.getName(), saved.getName());
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void findByEmail() {
        repository.save(user);
        User saved = repository.findByEmail(user.getEmail());
        assertNotNull(saved);
        assertEquals(user.getName(), saved.getName());
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void findByName() {
        repository.save(user);
        User saved = repository.findByName(user.getName());
        assertNotNull(saved);
        assertEquals(user.getName(), saved.getName());
        assertEquals(user.getEmail(), saved.getEmail());
    }
}