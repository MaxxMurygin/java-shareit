package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoResponse {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

}
