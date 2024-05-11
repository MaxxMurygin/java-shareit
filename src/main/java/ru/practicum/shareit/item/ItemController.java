package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(value = OWNER_ID) Long ownerId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(value = OWNER_ID) Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = OWNER_ID) Long ownerId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        return itemService.update(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long itemId) {
        itemService.remove(itemId);
    }
}
