package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    ItemDto create(Long ownerId, ItemDto itemDto, Pageable page);

    void remove(Long itemId, Pageable page);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto, Pageable page);

    List<ItemDto> findAll(Long ownerId, Pageable page);

    ItemDto findById(Long ownerId, Long itemId, Pageable page);

    List<ItemDto> findByText(String text, Pageable page);

    CommentDto createComment(Long authorId, CommentDto commentDto, Pageable page);

    List<CommentDto> findAllComments(Long ownerId, Pageable page);

    List<CommentDto> findCommentsByItemId(Long ownerId, Long itemId, Pageable page);
}
