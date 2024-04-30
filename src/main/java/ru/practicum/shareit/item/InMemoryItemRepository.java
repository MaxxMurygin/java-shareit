package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {
    private final HashMap<Integer, Item> items = new HashMap<>();
    private int id;
    @Override
    public Item create(Item item) {
        int itemId = generateId();

        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public void remove(int itemId) {
        items.remove(itemId);

    }

    @Override
    public Item update(int itemId, Item item) {
        Item stored = items.get(itemId);
        String name = item.getName();
        String description = item.getDescription();
        boolean isAvailable = item.getAvailable();
        String request = item.getRequest();

        if (name != null && !name.isBlank()) {
            stored.setName(name);
        }
        if (description != null && !description.isBlank()) {
            stored.setDescription(description);
        }
        stored.setAvailable(isAvailable);
        return stored;
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item findById(Integer itemId) {
        return items.get(itemId);
    }

    private int generateId() {
        return ++id;
    }
}
