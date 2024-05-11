package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto itemDto);

    void remove(Long itemId);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    List<ItemDto> findAll(Long ownerId);

    ItemDto findById(Long itemId);

    List<ItemDto> findByText(String text);
}
