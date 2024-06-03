package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private final UserDto userDto = new UserDto(1L, "user", "user@user.com");
    private final UserDto userDto1 = new UserDto(2L, "user2", "user2@user2.com");

    @SneakyThrows
    @Test
    void createOK() {
        when(userService.create(userDto)).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).create(userDto);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void createWithWrongEmail() {
        userDto.setEmail("user.com");
        when(userService.create(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(userDto);

        userDto.setEmail("");
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(userDto);
    }

    @SneakyThrows
    @Test
    void createWithWrongUserName() {
        userDto.setName("");
        when(userService.create(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(userDto);

        userDto.setName("More50SymbolsNameMore50SymbolsNameMore50SymbolsName");
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(userDto);
    }

    @SneakyThrows
    @Test
    void findByIdExistingUser() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).findById(userId);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void findByIdNonExistingUser() {
        Long userId = 99L;
        when(userService.findById(userId)).thenThrow(new EntityNotFoundException(User.class, "User not found"));

        mockMvc.perform(get("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
        verify(userService).findById(userId);
    }


    @SneakyThrows
    @Test
    void findAll() {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        users.add(userDto1);
        when(userService.findAll()).thenReturn(users);

        String result = mockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).findAll();
        assertEquals(objectMapper.writeValueAsString(users), result);
    }

    @SneakyThrows
    @Test
    void updateOk() {
        Long userId = userDto.getId();
        userDto.setName("updated");
        when(userService.update(userId, userDto)).thenReturn(userDto);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).update(userId, userDto);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @SneakyThrows
    @Test
    void updateWithWrongEmail() {
        Long userId = userDto.getId();
        userDto.setEmail("user@u ser.com");
        when(userService.update(userId, userDto)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(userId, userDto);
    }

    @SneakyThrows
    @Test
    void updateWithWrongName() {
        Long userId = userDto.getId();
        userDto.setName("");
        when(userService.update(userId, userDto)).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).update(userId, userDto);
    }
    @SneakyThrows
    @Test
    void remove() {
        Long userId = userDto.getId();
        doNothing().when(userService).remove(userId);

        mockMvc.perform(delete("/users/{userId}", userId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());
        verify(userService).remove(userId);
    }
}