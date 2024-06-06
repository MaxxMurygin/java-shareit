package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultItemRequestService implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDtoResponseSimple create(Long requesterId, ItemRequestDtoRequest itemRequestDtoRequest) {
        ItemRequest itemRequest = new ItemRequest();

        userRepository.findById(requesterId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", requesterId)));
        itemRequest.setRequesterId(requesterId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription(itemRequestDtoRequest.getDescription());
        return ItemRequestMapper.toRequestDtoSimple(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoResponseWithItems> getAllByRequesterId(Long requesterId, Pageable page) {
        userRepository.findById(requesterId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", requesterId)));

        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterId(requesterId, page);

        return requests
                .stream()
                .map(r -> ItemRequestMapper.toRequestDtoWithItems(r, itemRepository.findByRequestId(r.getId())
                        .stream()
                        .map(ItemMapper::toItemDtoShort)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoResponseWithItems getAllByRequestId(Long requestId, Long requesterId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException(ItemRequest.class, String.format("Id = %s", requestId)));
        userRepository.findById(requesterId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", requesterId)));

        List<ItemDtoShort> items = itemRepository.findByRequestId(requestId)
                .stream()
                .map(ItemMapper::toItemDtoShort)
                .collect(Collectors.toList());

        return ItemRequestMapper.toRequestDtoWithItems(itemRequest, items);
    }

    @Override
    public List<ItemRequestDtoResponseWithItems> getAll(Long requesterId, Pageable page) {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdNot(requesterId, page);
        return requests
                .stream()
                .map(r -> ItemRequestMapper.toRequestDtoWithItems(r, itemRepository.findByRequestId(r.getId())
                        .stream()
                        .map(ItemMapper::toItemDtoShort)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
