package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    ItemDtoShort create(Long ownerId, ItemDtoShort itemDto);

    void remove(Long itemId);

    ItemDtoShort update(Long ownerId, Long itemId, ItemDtoShort itemDto);

    List<ItemDtoResponse> findAll(Long ownerId, Pageable page);

    ItemDtoResponse findById(Long ownerId, Long itemId);

    List<ItemDtoShort> findByText(String text, Pageable page);

    CommentDtoResponse createComment(Long authorId, Long itemId, CommentDtoRequest commentDtoRequest, Pageable page);
}
