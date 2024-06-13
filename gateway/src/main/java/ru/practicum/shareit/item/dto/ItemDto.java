package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @Positive
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}