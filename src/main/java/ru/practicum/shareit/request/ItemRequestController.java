package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.PageMaker;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String REQUESTER_ID = "X-Sharer-User-Id";
    Pageable defaultPage = PageRequest.of(0, 100);
    Pageable userPage;
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ItemRequestDtoResponseSimple create(@RequestHeader(value = REQUESTER_ID) Long requesterId,
                                        @RequestBody ItemRequestDtoRequest itemRequestDtoRequest) {
        return itemRequestService.create(requesterId, itemRequestDtoRequest);
    }

    @GetMapping
    List<ItemRequestDtoResponseWithItems> getByRequesterId(@RequestHeader(value = REQUESTER_ID) Long requesterId) {
        return itemRequestService.getAllByRequesterId(requesterId, defaultPage);
    }

    @GetMapping("/{requestId}")
    ItemRequestDtoResponseWithItems getByRequestId(@PathVariable Long requestId) {
        return itemRequestService.getAllByRequestId(requestId);
    }

    @GetMapping("/all")
    List<ItemRequestDtoResponseSimple> getAll(@RequestParam(required = false) Integer from,
                                              @RequestParam(required = false) Integer size) {
        userPage = PageMaker.make(from, size, Sort.by("Created").descending());
        return itemRequestService.getAll(userPage);
    }
}
