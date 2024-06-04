package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDtoShort;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ItemRequestDtoResponseWithItems {
        private Long id;
        private String description;
        private LocalDateTime created;
        private List<ItemDtoShort> items;
}
