package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    private static final String REQUESTER_ID = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(value = REQUESTER_ID) Long requesterId,
                                         @Valid @RequestBody RequestDto requestDto) {
        return requestClient.createRequest(requesterId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByRequesterId(@RequestHeader(value = REQUESTER_ID) Long requesterId) {
        return requestClient.getByRequesterId(requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId(@RequestHeader(value = REQUESTER_ID) Long requesterId,
                                                @Positive @PathVariable Long requestId) {
        return requestClient.getByRequestId(requestId, requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(value = REQUESTER_ID) Long requesterId,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return requestClient.getRequests(requesterId, from, size);
    }
}