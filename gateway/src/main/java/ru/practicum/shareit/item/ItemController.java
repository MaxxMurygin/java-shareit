package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String OWNER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(value = OWNER_ID) Long ownerId,
                                         @Valid @RequestBody ItemDto itemDto) {
        return itemClient.createItem(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Positive @RequestHeader(value = OWNER_ID) Long ownerId,
                                    @PathVariable Long itemId) {
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@Positive @RequestHeader(value = OWNER_ID) Long ownerId,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByText(@RequestParam(name = "text") String text,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.searchItems(text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Positive @RequestHeader(value = OWNER_ID) Long ownerId,
                               @Valid @RequestBody ItemDto itemDto,
                               @PathVariable Long itemId) {
        return itemClient.patchItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@Positive @RequestHeader(value = OWNER_ID) Long ownerId,
                       @PathVariable Long itemId) {
        itemClient.deleteItem(ownerId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Positive @RequestHeader(value = OWNER_ID) Long authorId,
                                            @Valid @RequestBody CommentDto commentDto,
                                            @PathVariable Long itemId) {
        return itemClient.createComment(authorId, itemId, commentDto);

    }
}
