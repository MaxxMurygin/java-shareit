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
                .requestId(item.getRequestId())
                .ownerId(item.getOwner())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static ItemDtoShort toItemDtoShort(Item item) {
        return ItemDtoShort.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

//    public static ItemDtoForRequests toItemDtoForRequest(Item item) {
//        return ItemDtoForRequests.builder()
//                .id(item.getId())
//                .name(item.getName())
//                .description(item.getDescription())
//                .available(item.getAvailable())
//                .requestId(item.getRequestId())
//                .build();
//    }

    public static Item fromItemDtoRequest(ItemDtoShort itemDto) {
        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        return item;
    }
}
