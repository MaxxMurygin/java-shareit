package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

@Data
@Builder
public class ItemDto {
        private int id;
        private String name;
        private String description;
        private boolean isAvailable;
        private int owner;
        private String request;

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .request(item.getRequest())
                .build();
    }

}
