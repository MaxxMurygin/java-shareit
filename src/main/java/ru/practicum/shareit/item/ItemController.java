package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.PageMaker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String OWNER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);
    Pageable userPage;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoShort create(@RequestHeader(value = OWNER_ID) Long ownerId,
                               @RequestBody ItemDtoShort itemDto) {
        return itemService.create(ownerId, itemDto, defaultPage);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse findById(@RequestHeader(value = OWNER_ID) Long ownerId,
                                   @PathVariable Long itemId) {
        return itemService.findById(ownerId, itemId, defaultPage);
    }

    @GetMapping
    public Collection<ItemDtoResponse> findAll(@RequestHeader(value = OWNER_ID) Long ownerId,
                                   @PositiveOrZero  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        userPage = PageMaker.make(from, size);
        return itemService.findAll(ownerId, userPage);
    }

    @GetMapping("/search")
    public Collection<ItemDtoShort> findByText(@RequestParam(name = "text") String text,
                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        userPage = PageMaker.make(from, size);
        return itemService.findByText(text, userPage);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoShort update(@RequestHeader(value = OWNER_ID) Long ownerId,
                               @RequestBody ItemDtoShort itemDto,
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
