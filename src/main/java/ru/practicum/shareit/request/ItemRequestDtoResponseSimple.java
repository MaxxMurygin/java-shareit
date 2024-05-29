package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoForRequests;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoResponseSimple {
    private Long id;
    private String description;
    private LocalDateTime created;
}
