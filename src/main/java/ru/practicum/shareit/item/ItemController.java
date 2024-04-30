package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(value="X-Sharer-User-Id") int ownerId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(ownerId, itemDto);

    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Integer itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findAll() {
        return itemService.findAll();
    }


    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value="X-Sharer-User-Id") int ownerId,
                          @RequestBody Item item,
                          @PathVariable Integer itemId) {
        return itemService.update(ownerId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int itemId) {
        itemService.remove(itemId);
    }
}
