package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    void remove(int userId);

    UserDto update(int userId, User user);

    List<UserDto> findAll();

    UserDto findById(Integer userId);

}
