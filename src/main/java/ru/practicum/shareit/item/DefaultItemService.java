package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.ForbiddenException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(int ownerId, ItemDto itemDto) {
        Boolean isAvailable = itemDto.getAvailable();
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        if (isAvailable == null) {
            throw new ValidationException("Available must be");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Wrong name");
        }
        if (description == null || description.isBlank()) {
            throw new ValidationException("Wrong description");
        }

        User owner = userRepository.findById(ownerId);
        if (owner == null) {
            throw new NotFoundException(User.class, String.format("Id = %s", ownerId));
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public void remove(int itemId) {
        itemRepository.remove(itemId);

    }

    @Override
    public ItemDto update(int ownerId, int itemId, Item item) {
        Item stored = itemRepository.findById(itemId);
        if (stored.getOwner() != ownerId) {
            throw new ForbiddenException("Update item available only for owner");
        }
        return ItemMapper.toItemDto(itemRepository.update(itemId, item));
    }

    @Override
    public List<ItemDto> findAll(int ownerId) {
        return itemRepository.findAll(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Integer itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId));
    }

    @Override
    public List<ItemDto> findByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
