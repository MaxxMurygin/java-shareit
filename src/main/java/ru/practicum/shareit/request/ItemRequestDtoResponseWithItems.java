package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.ItemDtoShort;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    public class ItemRequestDtoResponseWithItems {
        private Long id;
        private String description;
        private LocalDateTime created;
        private List<ItemDtoShort> items;
}
