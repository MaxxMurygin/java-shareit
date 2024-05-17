package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDto;

@Data
@Builder
public class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long ownerId;
        private String request;
        private BookingDto lastBooking;
        private BookingDto nextBooking;
}
