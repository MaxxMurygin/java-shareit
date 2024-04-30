package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    void remove(int itemId);

    Item update(int itemId, Item item);

    List<Item> findAll(int ownerId);

    Item findById(Integer itemId);

    List<Item> findByNameOrDescription(String text);

}
