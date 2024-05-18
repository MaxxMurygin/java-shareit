package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingDto;

import java.util.List;

public class ItemMapper {
    public static ItemDtoResponse toItemDtoResponse(Item item,
                                                    BookingDto lastBooking,
                                                    BookingDto nextBooking,
                                                    List<CommentDtoResponse> comments) {
        return ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .ownerId(item.getOwner())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static ItemDtoRequest toItemDtoShort(Item item) {
        return ItemDtoRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item fromItemDtoRequest(ItemDtoRequest itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}
