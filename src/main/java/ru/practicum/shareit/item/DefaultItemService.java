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
    public ItemDto create(Long ownerId, ItemDto itemDto) {
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

        userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(User.class, String.format("Id = %s", ownerId)));
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public void remove(Long itemId) {
        itemRepository.deleteById(itemId);

    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Boolean isAvailable = itemDto.getAvailable();
        String updatedName = itemDto.getName();
        String updatedDescription = itemDto.getDescription();

        Item stored = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(Item.class, String.format("Id = %s", itemId)));
        if (!stored.getOwner().equals(ownerId)) {
            throw new ForbiddenException("Update item available only for owner");
        }

        if (isAvailable != null) {
            stored.setAvailable(isAvailable);
        }
        if (updatedName != null) {
            stored.setName(updatedName);
        }
        if (updatedDescription != null) {
            stored.setDescription(updatedDescription);
        }
        return ItemMapper.toItemDto(itemRepository.save(stored));
    }

    @Override
    public List<ItemDto> findAll(Long ownerId) {
        return itemRepository.findByOwner(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(Item.class, String.format("Id = %s", itemId))));
    }

    @Override
    public List<ItemDto> findByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
