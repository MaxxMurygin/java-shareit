package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.ValidationException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class DefaultUserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createOk() {
        UserDto userToCreate = UserDto.builder()
                .name("testUser")
                .email("testUser@user.com")
                .build();
        UserDto created = userService.create(userToCreate);
        assertNotNull(created);
        assertEquals(created.getName(), userToCreate.getName());
        assertEquals(created.getEmail(), userToCreate.getEmail());
    }

    @Test
    void createFail() {
        UserDto userToCreate = UserDto.builder()
                .name("testUser")
                .email("test@user.com")
                .build();

        userToCreate.setName(null);
        assertThrows(ValidationException.class, () -> userService.create(userToCreate));
        userToCreate.setName("testUser");
        userToCreate.setEmail(null);
        assertThrows(ValidationException.class, () -> userService.create(userToCreate));
//        userToCreate.setEmail("wrong.com");
//        assertThrows(ConstraintViolationException.class, () -> userService.create(userToCreate));
    }

    @Test
    void remove() {
        UserDto userDto = userService.findById(1L);
        assertNotNull(userDto);
        userService.remove(1L);
        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void update() {
        UserDto userToUpdate = UserDto.builder()
                .name("updated from test User")
                .email("test@user.com")
                .build();
        UserDto userDto = userService.findById(6L);
        assertEquals(userDto.getName(), "user6");

        userService.update(6L, userToUpdate);
        UserDto updated = userService.findById(6L);
        assertEquals(updated.getName(), userToUpdate.getName());
    }

    @Test
    void findAll() {
        List<UserDto> userList = userService.findAll();
        assertEquals(userList.size(), 6);
    }

    @Test
    void findById() {
        UserDto userDto = userService.findById(1L);
        assertNotNull(userDto);
        assertEquals(userDto.getName(), "updateName");
        assertEquals(userDto.getEmail(), "updateName@user.com");

        assertThrows(EntityNotFoundException.class, () -> userService.findById(99L));
    }
}