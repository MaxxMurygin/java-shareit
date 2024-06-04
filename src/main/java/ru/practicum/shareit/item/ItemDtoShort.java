package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
public class ItemDtoShort {
        @Positive
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
}
