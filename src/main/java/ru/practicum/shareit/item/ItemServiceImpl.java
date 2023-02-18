package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<ItemForResponse> getAllForUser(long userId, int from, int size) {
        if (from < 0 || size <= 0)
            throw new ValidationException("Wrong parameters");
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size)).stream()
                .map(i -> createResponse(i, userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemForResponse get(long id, long userId) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        return createResponse(itemRepository.findById(id).get(), userId);
    }

    @Override
    @Transactional
    public ItemForResponse add(long userId, ItemDto itemDto) {
        validateItem(itemDto);
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return createResponse(itemRepository.save(ItemMapper.toItem(itemDto, userId)), userId);
    }

    @Override
    @Transactional
    public ItemForResponse update(long userId, long id, ItemDto itemDto) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.findById(id).get().getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemDto.setId(id);
        Item newItem = itemRepository.findById(itemDto.getId()).get();
        if (itemDto.getName() != null)
            newItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            newItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            newItem.setAvailable(itemDto.getAvailable());
        return createResponse(itemRepository.save(newItem), userId);
    }

    @Override
    @Transactional
    public void delete(long userId, long id) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.findById(id).get().getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemForResponse> search(String req, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Wrong parameters");
        }
        if (req.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(req, PageRequest.of(from / size, size)).stream()
                .map(i -> createResponse(i, i.getOwnerId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentForResponse addComment(long userId, long itemId, CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().isBlank()) {
            throw new ValidationException("Text is empty");
        }
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerId(itemId, userId).stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (bookings.size() == 0) {
            throw new ValidationException("Not proper rights");
        }
        return createCommentForResponse(commentRepository.save(CommentMapper.toComment(commentDto, userId, itemId)));
    }

    private void validateItem(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank())
            throw new ValidationException("Name is null");
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank())
            throw new ValidationException("Description is null");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("Available is null");
    }

    private ItemForResponse createResponse(Item item, long userId) {
        List<CommentForResponse> comments = commentRepository.findAllByItem(item.getId()).stream()
                .map(this::createCommentForResponse)
                .collect(Collectors.toList());
        BookingShort lastBooking = null;
        BookingShort nextBooking = null;
        if (userId == item.getOwnerId()) {
            if (getLastItemBooking(item.getId()) != null) {
                Booking last = getLastItemBooking(item.getId());
                lastBooking = new BookingShort(last.getId(), last.getBookerId());
            }
            if (getNextItemBooking(item.getId()) != null) {
                Booking next = getNextItemBooking(item.getId());
                nextBooking = new BookingShort(next.getId(), next.getBookerId());
            }
        }
        return ItemMapper.toItemForResponse(item, lastBooking, nextBooking, comments);
    }

    private CommentForResponse createCommentForResponse(Comment comment) {
        return CommentMapper.toCommentForResponse(comment, userRepository.findById(comment.getAuthor()).get().getName());
    }

    public Booking getNextItemBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(
                        itemId).stream()
                .filter(b -> !b.getStart().isEqual(LocalDateTime.now()))
                .filter(b -> !b.getEnd().isEqual(LocalDateTime.now()))
                .filter(b -> (b.getStart().isAfter(LocalDateTime.now())))
                .filter(b -> (b.getStatus() == BookingStatus.APPROVED))
                .collect(Collectors.toList());
        if (bookings.size() == 0) {
            return null;
        } else {
            return bookings.get(0);
        }
    }

    public Booking getLastItemBooking(Long itemId) {
        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStart(
                        itemId).stream()
                .filter(b -> !b.getStart().isEqual(LocalDateTime.now()))
                .filter(b -> !b.getEnd().isEqual(LocalDateTime.now()))
                .filter(b -> (b.getStart().isBefore(LocalDateTime.now())))
                .filter(b -> (b.getStatus() == BookingStatus.APPROVED))
                .filter(b -> (b.getEnd().isBefore(LocalDateTime.now())))
                .collect(Collectors.toList());
        if (bookings.size() == 0) {
            return null;
        } else {
            return bookings.get(0);
        }
    }
}
