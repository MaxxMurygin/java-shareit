package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.AlreadyExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id;

    @Override
    public User create(User user) {
        int userId = generateId();

        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public void remove(int userId) {
        users.remove(userId);
    }

    @Override
    public User update(User user) {
        User stored = users.get(user.getId());
        String name = user.getName();
        String email = user.getEmail();
        if (name != null) {
            stored.setName(name);
        }
        if (email != null) {
            User duplicateEmail = users.values().stream()
                    .filter(u -> u.getEmail().equals(email) && u.getId() != stored.getId())
                    .findFirst().orElse(null);
            if (duplicateEmail != null) {
                throw new AlreadyExistException(User.class, String.format("Email = %s", email));
            }
            stored.setEmail(email);
        }
        return stored;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Integer userId) {
        return users.get(userId);
    }

    @Override
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    private int generateId() {
        return ++id;
    }
}
