package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto create(int ownerId, ItemDto itemDto);

    void remove(int itemId);

    ItemDto update(int ownerId, int itemId, Item item);

    List<ItemDto> findAll(int ownerId);

    ItemDto findById(Integer itemId);

    List<ItemDto> findByText(String text);
}
