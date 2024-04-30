package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface ItemService {
    ItemDto create(int ownerId, ItemDto itemDto);

    void remove(int itemId);

    ItemDto update(int itemId, Item item);

    List<ItemDto> findAll();

    ItemDto findById(Integer itemId);
}
