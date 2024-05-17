package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.CommentRepository;
import ru.practicum.shareit.common.ForbiddenException;
import ru.practicum.shareit.common.EntityNotFoundException;
import ru.practicum.shareit.common.NotFoundException;
import ru.practicum.shareit.common.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    Pageable oneStringPage = PageRequest.of(0, 1);

    @Override
    @Transactional
    public ItemDto create(Long ownerId, ItemDto itemDto, Pageable page) {
        Boolean isAvailable = itemDto.getAvailable();
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        if (isAvailable == null) {
            throw new ValidationException("Available must be");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("Wrong name");
        }
        if (description == null || description.isBlank()) {
            throw new ValidationException("Wrong description");
        }

        userRepository.findById(ownerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", ownerId)));
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwner(ownerId);
        return ItemMapper.toItemDtoShort(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void remove(Long itemId, Pageable page) {
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto, Pageable page) {
        Boolean isAvailable = itemDto.getAvailable();
        String updatedName = itemDto.getName();
        String updatedDescription = itemDto.getDescription();

        userRepository.findById(ownerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", ownerId)));
        Item stored = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(Item.class, String.format("Id = %s", itemId)));
        if (!stored.getOwner().equals(ownerId)) {
            throw new ForbiddenException("Update item available only for owner");
        }

        if (isAvailable != null) {
            stored.setAvailable(isAvailable);
        }
        if (updatedName != null) {
            stored.setName(updatedName);
        }
        if (updatedDescription != null) {
            stored.setDescription(updatedDescription);
        }
        return ItemMapper.toItemDtoShort(itemRepository.save(stored));
    }

    @Override
    public List<ItemDto> findAll(Long ownerId, Pageable page) {
        userRepository.findById(ownerId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", ownerId)));

        return itemRepository.findByOwnerOrderById(ownerId).stream()
                .map(item ->ItemMapper.toItemDtoWithBooking(item,
                        BookingMapper.toBookingDto(bookingRepository
                                .findAllByItemIdAndStartBeforeOrderByEndDesc(item.getId(),
                                        LocalDateTime.now(),
                                        oneStringPage)
                                .stream()
                                .filter(b -> b.getItem().getOwner().equals(ownerId))
                                .findFirst()
                                .orElse(null)),
                        BookingMapper.toBookingDto(bookingRepository
                                .findAllByItemIdAndStartAfterOrderByStartAsc(item.getId(),
                                        LocalDateTime.now(),
                                        oneStringPage)
                                .stream()
                                .filter(b -> b.getItem().getOwner().equals(ownerId))
                                .findFirst()
                                .orElse(null))))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long ownerId, Long itemId, Pageable page) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(Item.class, String.format("Id = %s", itemId)));

        Booking lastBooking = bookingRepository
                .findAllByItemIdAndStartBeforeOrderByEndDesc(itemId, LocalDateTime.now(), oneStringPage)
                .stream()
                .filter(b -> b.getItem().getOwner().equals(ownerId))
                .findFirst()
                .orElse(null);
        Booking nextBooking = bookingRepository
                .findAllByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now(), oneStringPage)
                .stream()
                .filter(b -> b.getItem().getOwner().equals(ownerId))
                .findFirst()
                .orElse(null);
//        ItemDto itemDto = ;
//        log.info("{} \n {} \n {}\n {}", LocalDateTime.now(), lastBooking, nextBooking, itemDto);

        return ItemMapper.toItemDtoWithBooking(item,
                BookingMapper.toBookingDto(lastBooking),
                BookingMapper.toBookingDto(nextBooking));
    }

    @Override
    public List<ItemDto> findByText(String text, Pageable page) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(Long authorId, CommentDto commentDto, Pageable page) {
        Long itemId = commentDto.getItem().getId();
        String text = commentDto.getText();

        if (text == null || text.isBlank()) {
            throw new ValidationException("Wrong text");
        }
        if (commentDto.getItem().getId() == null) {
            throw new ValidationException("Available must be");
        }

        itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(Item.class, String.format("Id = %s", itemId)));
        userRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundException(User.class, String.format("Id = %s", authorId)));
        bookingRepository
                .findAllByItemId(itemId, page)
                .stream()
                .filter(b -> b.getBooker().getId().equals(authorId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Author did not rent this item"));
        Comment comment = CommentMapper.fromCommentDto(commentDto);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> findAllComments(Long ownerId, Pageable page) {
        return null;
    }

    @Override
    public List<CommentDto> findCommentsByItemId(Long ownerId, Long itemId, Pageable page) {
        return null;
    }

}
