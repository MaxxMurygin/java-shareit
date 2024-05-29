package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoResponseSimple create(Long requesterId, ItemRequestDtoRequest itemRequestDtoRequest);
    List<ItemRequestDtoResponseWithItems> getAllByRequesterId(Long requesterId, Pageable page);
    ItemRequestDtoResponseWithItems getAllByRequestId(Long requestId);
    List<ItemRequestDtoResponseSimple> getAll(Pageable page);


}
