package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoRequest create(@RequestHeader(value = OWNER_ID) Long ownerId,
                                 @RequestBody ItemDtoRequest itemDto) {
        return itemService.create(ownerId, itemDto, defaultPage);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse findById(@RequestHeader(value = OWNER_ID) Long ownerId,
                                   @PathVariable Long itemId) {
        return itemService.findById(ownerId, itemId, defaultPage);
    }

    @GetMapping
    public Collection<ItemDtoResponse> findAll(@RequestHeader(value = OWNER_ID) Long ownerId) {
        return itemService.findAll(ownerId, defaultPage);
    }

    @GetMapping("/search")
    public Collection<ItemDtoRequest> findByText(@RequestParam String text) {
        return itemService.findByText(text, defaultPage);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoRequest update(@RequestHeader(value = OWNER_ID) Long ownerId,
                                 @RequestBody ItemDtoRequest itemDto,
                                 @PathVariable Long itemId) {
        return itemService.update(ownerId, itemId, itemDto, defaultPage);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long itemId) {
        itemService.remove(itemId, defaultPage);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestHeader(value = OWNER_ID) Long authorId,
                                           @RequestBody CommentDtoRequest commentDtoRequest,
                                           @PathVariable Long itemId) {
        return itemService.createComment(authorId, itemId, commentDtoRequest, defaultPage);

    }

}
