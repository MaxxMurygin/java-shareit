package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public void remove(int userId) {

    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(Integer userId) {
        return null;
    }
}
