package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDtoResponseSimple {
    private Long id;
    private String description;
    private LocalDateTime created;
}
