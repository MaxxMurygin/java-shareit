package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;

@Data
@Builder
public class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private int owner;
        private String request;
        private Booking lastBooking;
        private Booking nextBooking;
}
