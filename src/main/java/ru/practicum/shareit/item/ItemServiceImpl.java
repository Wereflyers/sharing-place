package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
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
    public List<ItemForResponse> getAllForUser(long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemForResponse get(long id) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        return createResponse(itemRepository.findById(id).get());
    }

    @Override
    @Transactional
    public ItemForResponse add(long userId, ItemDto itemDto) {
        validateItem(itemDto);
        if (userRepository.findById(userId).isEmpty())
            throw new NullPointerException("User " + userId + " is not found");
        return createResponse(itemRepository.save(ItemMapper.toItem(itemDto, userId)));
    }

    @Override
    @Transactional
    public ItemForResponse update(long userId, long id, ItemDto itemDto) {
        if (itemRepository.findById(id).isEmpty())
            throw new NullPointerException("Item " + id + " is not found.");
        if (itemRepository.findById(id).get().getOwnerId() != userId)
            throw new NullPointerException("You don't have proper rights.");
        itemDto.setId(id);
        return createResponse(itemRepository.update(ItemMapper.toItem(itemDto, userId)));
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
    public List<ItemForResponse> search(String req) {
        if (req.isBlank())
            return new ArrayList<>();
        return itemRepository.search(req).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
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

    private ItemForResponse createResponse(Item item) {
        BookingShort lastBooking = null;
        BookingShort nextBooking = null;
        if (bookingRepository.getLastItemBooking(item.getId()) != null) {
            lastBooking = new BookingShort(bookingRepository.getLastItemBooking(item.getId()));
        }
        if (bookingRepository.getNextItemBooking(item.getId()) != null) {
            nextBooking = new BookingShort(bookingRepository.getNextItemBooking(item.getId()));
        }
        List<CommentForResponse> comments = commentRepository.findAllByItem(item.getId()).stream()
                .map(this::createCommentForResponse)
                .collect(Collectors.toList());
       return ItemMapper.toItemForResponse(item, lastBooking, nextBooking, comments);
    }

    private CommentForResponse createCommentForResponse(Comment comment) {
        return CommentMapper.toCommentForResponse(comment, userRepository.findById(comment.getAuthor()).get().getName());
    }
}
