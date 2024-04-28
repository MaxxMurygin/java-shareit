package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User create(User user);

    void remove(int userId);

    User update(User user);

    List<User> findAll();

    User findById(Integer userId);
}
