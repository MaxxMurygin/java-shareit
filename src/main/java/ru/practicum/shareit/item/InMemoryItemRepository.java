package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        Boolean isAvailable = item.getAvailable();

        if (name != null && !name.isBlank()) {
            stored.setName(name);
        }
        if (description != null && !description.isBlank()) {
            stored.setDescription(description);
        }
        if (isAvailable != null) {
            stored.setAvailable(isAvailable);
        }
        return stored;
    }

    @Override
    public List<Item> findAll(int ownerId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findByNameOrDescription(String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.contains("отвер")) {
            System.out.println();
        }

        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerText) ||
                        item.getDescription().toLowerCase().contains(lowerText)) &&
                        item.getAvailable())
                .collect(Collectors.toList());
    }

    private int generateId() {
        return ++id;
    }
}
