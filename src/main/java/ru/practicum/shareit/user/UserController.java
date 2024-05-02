package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);

    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Integer userId) {
        return userService.findById(userId);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }


    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody User user,
                          @PathVariable Integer userId) {
        return userService.update(userId, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) {
        userService.remove(id);
    }
}
