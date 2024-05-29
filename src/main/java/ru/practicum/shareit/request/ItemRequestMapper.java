package ru.practicum.shareit.request;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoForRequests;
import ru.practicum.shareit.item.ItemDtoShort;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDtoResponseSimple toRequestDtoSimple(ItemRequest itemRequest) {
        ItemRequestDtoResponseSimple shortResponse = new ItemRequestDtoResponseSimple();
        shortResponse.setId(itemRequest.getId());
        shortResponse.setDescription(itemRequest.getDescription());
        shortResponse.setCreated(itemRequest.getCreated());
        return shortResponse;
    }

    public static ItemRequestDtoResponseWithItems toRequestDtoWithItems(ItemRequest itemRequest,
                                                                        List<ItemDtoShort> items) {
        ItemRequestDtoResponseWithItems fullResponse = new ItemRequestDtoResponseWithItems();
        fullResponse.setId(itemRequest.getId());
        fullResponse.setDescription(itemRequest.getDescription());
        fullResponse.setCreated(itemRequest.getCreated());
        fullResponse.setItems(items);
        return fullResponse;
    }
}
