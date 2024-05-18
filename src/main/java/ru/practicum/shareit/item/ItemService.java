package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    ItemDtoRequest create(Long ownerId, ItemDtoRequest itemDto, Pageable page);

    void remove(Long itemId, Pageable page);

    ItemDtoRequest update(Long ownerId, Long itemId, ItemDtoRequest itemDto, Pageable page);

    List<ItemDtoResponse> findAll(Long ownerId, Pageable page);

    ItemDtoResponse findById(Long ownerId, Long itemId, Pageable page);

    List<ItemDtoRequest> findByText(String text, Pageable page);

    CommentDtoResponse createComment(Long authorId, Long itemId, CommentDtoRequest commentDtoRequest, Pageable page);
}
